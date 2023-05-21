package ktor.plugins

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import domain.AlreadyExistsException
import domain.NotFoundException
import domain.entity.ValueClass
import domain.entity.user.AuthException
import domain.entity.user.LoginException
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.metadata.MethodInfo
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.core.routes.redoc
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import usecases.model.UserModel
import usecases.usecase.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

fun Application.rest(usecases: Collection<UsecaseType<*>>) {
    routing {
        redoc(pageTitle = "EventAggregator API Docs")
//        install(ContentNegotiation) {
//            this.gson {
//                registerTypeAdapterFactory(ValueClassAdapterFactory())
//                registerTypeAdapter(Instant::class.java, InstantAdapter())
//                registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
//                registerTypeAdapter(BigDecimal::class.java, BigDecimalAdapter())
//            }
//        }
        authenticate(optional = true) {
            usecases
                .filter { it::class.hasAnnotation<Query>() || it::class.hasAnnotation<Mutation>() }
                .forEach {
                    usecase(it)
                }
        }
    }
}

private fun Route.usecase(usecase: UsecaseType<*>) {
    val name = usecase::class.simpleName!!
    route(usecase.path) {
        install(NotarizedRoute()) {
            configureNotarizedRoute(usecase, name)
        }
        configureRoute(usecase)
    }
}

private fun NotarizedRoute.Config.configureNotarizedRoute(
    usecase: UsecaseType<*>,
    name: String
) {
    if (usecase is UsecaseA0<*> && usecase::class.hasAnnotation<Query>()) {
        get = GetInfo.builder {
            configure(usecase)
        }
    } else {
        post = PostInfo.builder {
            configure(usecase)
            request {
                description(name)
                when (usecase) {
                    is UsecaseA1<*, *> -> RestA1::class.createType(usecase.args.map { KTypeProjection.invariant(it) })
                    else -> null
                }.let {
                    requestType(it ?: typeOf<Unit>())
                }
            }
        }
    }
}

private fun <T : MethodInfo> MethodInfo.Builder<T>.configure(usecase: UsecaseType<*>) {
    val name = usecase::class.simpleName!!
    summary(name)
    description(name)
    response {
        description(name)
        responseCode(HttpStatusCode.OK)
        responseType(usecase.result)
    }
}

private fun Route.configureRoute(usecase: UsecaseType<*>) {
    if (usecase is UsecaseA0<*> && usecase::class.hasAnnotation<Query>()) {
        get {
            handle {
                execute(usecase)
            }
        }
    } else {
        post {
            handle {
                when (usecase) {
                    is UsecaseA0<*> -> execute(usecase)
                    is UsecaseA1<*, *> -> execute(usecase)
                    else -> throw Exception("Invalid usecase")
                }
            }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.handle(execute: suspend () -> Any) {
    try {
        call.respond(execute())
    } catch (ex: LoginException) {
        call.respond(HttpStatusCode.Unauthorized, ex.message ?: "")
    } catch (ex: AuthException) {
        call.respond(HttpStatusCode.Forbidden, ex.message ?: "")
    } catch (ex: AlreadyExistsException) {
        call.respond(HttpStatusCode.Conflict, ex.message ?: "")
    } catch (ex: NotFoundException) {
        call.respond(HttpStatusCode.NotFound, ex.message ?: "")
    } catch (ex: Exception) {
        call.respond(HttpStatusCode.InternalServerError, ex.message ?: "")
    }
}

fun ApplicationCall.getAuthentication(): UserModel? {
    return authentication.principal<UserPrincipal>()?.user
}

suspend fun <R, U : UsecaseA0<R>> PipelineContext<Unit, ApplicationCall>.execute(usecase: U): R {
    return usecase(call.getAuthentication())
}

suspend fun <R, A0 : Any, U : UsecaseA1<A0, R>> PipelineContext<Unit, ApplicationCall>.execute(usecase: U): R {
    val args = call.deserialize<RestA1<A0>>(usecase)
    return usecase(call.getAuthentication(), args.a0)
}

private val gson = GsonBuilder().apply {
    registerTypeAdapterFactory(ValueClassAdapterFactory())
    registerTypeAdapter(Instant::class.java, InstantAdapter())
    registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
    registerTypeAdapter(BigDecimal::class.java, BigDecimalAdapter())
}.create()!!

private suspend inline fun <reified T : Any> ApplicationCall.deserialize(usecase: UsecaseType<*>): T {
    val type = TypeToken.getParameterized(
        T::class.java,
        *usecase.args.map { boxedClassOf(it.jvmErasure).java }.toTypedArray()
    ).type

    val receiveText = this.receiveText()
    return gson.fromJson(receiveText, type)
}

fun boxedClassOf(kClass: KClass<*>) = when (kClass) {
    Boolean::class -> java.lang.Boolean::class
    Byte::class -> java.lang.Byte::class
    Char::class -> java.lang.Character::class
    Float::class -> java.lang.Float::class
    Int::class -> java.lang.Integer::class
    Long::class -> java.lang.Long::class
    Short::class -> java.lang.Short::class
    Double::class -> java.lang.Double::class
    else -> kClass
}

data class RestA1<A0>(
    val a0: A0
)

class ValueClassAdapterFactory() : TypeAdapterFactory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
        val rawType = type!!.rawType
        val type1 = findInterface(rawType.kotlin, ValueClass::class)
        if (type1 != null) {
            val argType = (type1 as ParameterizedType).actualTypeArguments.first()
            return ValueClassAdapter(rawType, argType) as TypeAdapter<T>
        }
        if (rawType == RestA1::class.java) {
            val argType = (type.type as ParameterizedType).actualTypeArguments.first()
            return RestA1Adapter(rawType, argType) as TypeAdapter<T>
        }
        return gson!!.getDelegateAdapter(this, type)
    }
}

fun findInterface(kClass: KClass<*>, interfaceClass: KClass<*>): Type? {
    return kClass.java.genericInterfaces.firstOrNull() { it.typeName.startsWith(interfaceClass.qualifiedName!!) }
        ?: kClass.superclasses.firstOrNull { it != Any::class }?.let { findInterface(it, interfaceClass) }
}

class ValueClassAdapter(private val valueClass: Class<*>, private val argType: Type) :
    TypeAdapter<ValueClass<*>>() {
    override fun write(out: JsonWriter?, value: ValueClass<*>?) {
        if (value == null) {
            out!!.nullValue()
            return
        }

        out!!.jsonValue(gson.toJson(value.value))
    }

    override fun read(inp: JsonReader?): ValueClass<*>? {
        if (inp!!.peek() == JsonToken.NULL) {
            inp.nextNull()
            return null
        }
        val value = gson.fromJson<Any>(inp, argType)
        val constructor = valueClass.kotlin.constructors.first()
        return constructor.call(value) as ValueClass<*>
    }
}

class RestA1Adapter(private val valueClass: Class<*>, private val argType: Type) :
    TypeAdapter<RestA1<*>>() {
    override fun write(out: JsonWriter?, value: RestA1<*>?) {
        if (value == null) {
            out!!.nullValue()
            return
        }

        out!!.jsonValue(gson.toJson(value.a0))
    }

    override fun read(inp: JsonReader?): RestA1<*>? {
        if (inp!!.peek() == JsonToken.NULL) {
            inp.nextNull()
            return null
        }
        val value = gson.fromJson<Any>(inp, argType)
        val constructor = valueClass.kotlin.constructors.first()
        return constructor.call(value) as RestA1<*>
    }
}

class InstantAdapter : JsonSerializer<Instant>, JsonDeserializer<Instant> {
    override fun serialize(src: Instant, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Instant =
        Instant.parse((json as JsonPrimitive).asString)
}

class BigDecimalAdapter : JsonSerializer<BigDecimal> {
    override fun serialize(src: BigDecimal, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toPlainString())
    }
}

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime =
        LocalDateTime.parse((json as JsonPrimitive).asString)
}