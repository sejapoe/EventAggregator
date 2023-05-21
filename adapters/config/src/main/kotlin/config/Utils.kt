@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package config

import domain.repo.Repo
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.definition.KoinDefinition
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.module.Module
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.java.KoinJavaComponent
import org.reflections.Reflections
import usecases.usecase.UsecaseType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

private const val usecasePackage = "usecases.usecase"
private const val repoPackage = "repo"

private val usecases get() = Reflections(usecasePackage).getSubTypesOf(UsecaseType::class.java)
private val repos get() = Reflections(repoPackage).getSubTypesOf(Repo::class.java)

inline fun <reified T : Any> getAll(): Collection<T> = KoinJavaComponent.getKoin().let { koin ->
    koin.instanceRegistry.instances.values.map { it.beanDefinition }.filter { it.kind == Kind.Singleton }
        .filter { it.primaryType.isSubclassOf(T::class) }
        .map { koin.get(clazz = it.primaryType, qualifier = null, parameters = null) }
}

fun Module.usecasesAndRepos(domain: String) {
    usecases(domain)
    repos(domain)
}

fun Module.usecases(domain: String) {
    usecases
        .map { it.kotlin }
        .filter { it.qualifiedName!!.startsWith("$usecasePackage.$domain") }
        .forEach {
            usecase(it)
        }
}

fun Module.usecase(usecase: KClass<out UsecaseType<*>>) =
    KoinDefinition(this, createFactory(usecase))

fun Module.repos(domain: String) {
    repos
        .map { it.kotlin }
        .filter { it.qualifiedName!!.startsWith("$repoPackage.$domain") }
        .forEach {
            repo(it)
        }

}

@Suppress("UNCHECKED_CAST")
fun Module.repo(repo: KClass<out Repo<*, *>>) {
    val domain = repo.supertypes.find { it.isSubtypeOf(typeOf<Repo<* ,*>>()) }!!.jvmErasure as KClass<in Repo<*, *>>
    val koinDef= KoinDefinition(this, createFactory(repo))
    koinDef.bind(domain)
}

private fun <T : Any> Module.createFactory(clazz: KClass<T>): SingleInstanceFactory<T> {
    val def = BeanDefinition(
        _q("_root_"),
        clazz,
        null,
        {
            val constructor = clazz.constructors.first()
            val params = constructor.parameters.map {
                this.getOrNull<Any>(
                    it.type.jvmErasure,
                    null
                ) { emptyParametersHolder() }!!
            }
            constructor.call(*params.toTypedArray())
        },
        Kind.Singleton,
        secondaryTypes = emptyList()
    )
    val factory = SingleInstanceFactory(def)
    indexPrimaryType(factory)
    return factory
}