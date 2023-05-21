@file:Suppress("unused")
package repo.utils

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*

infix fun <T : Comparable<T>, S : T?> ExpressionWithColumnType<in S>.less(t: T?): Op<Boolean> =
    SqlExpressionBuilder.run {
        if (t == null) Op.TRUE else LessOp(this@less, wrap(t))
    }

infix fun <T : Comparable<T>, S : T?> ExpressionWithColumnType<in S>.greater(t: T?): Op<Boolean> =
    SqlExpressionBuilder.run {
        if (t == null) Op.TRUE else GreaterOp(this@greater, wrap(t))
    }

infix fun <T : Comparable<T>, S : T?> ExpressionWithColumnType<in S>.lessEq(t: T?): Op<Boolean> =
    SqlExpressionBuilder.run {
        if (t == null) Op.TRUE else LessEqOp(this@lessEq, wrap(t))
    }

infix fun <T : Comparable<T>, S : T?> ExpressionWithColumnType<in S>.greaterEq(t: T?): Op<Boolean> =
    SqlExpressionBuilder.run {
        if (t == null) Op.TRUE else GreaterEqOp(this@greaterEq, wrap(t))
    }

infix fun <T : Comparable<T>, E : EntityID<T>?, V : T?> ExpressionWithColumnType<E>.eqn(t: V?): Op<Boolean> =
    SqlExpressionBuilder.run {
        if (t == null) Op.TRUE else EqOp(this@eqn, wrap(t))
    }

infix fun <T> ExpressionWithColumnType<T>.notEqn(t: T?): Op<Boolean> =
    SqlExpressionBuilder.run {
        if (t == null) Op.TRUE else NeqOp(this@notEqn, wrap(t))
    }

infix fun <T : String?> Expression<T>.likeInside(pattern: String?): Op<Boolean> =
    SqlExpressionBuilder.run {
        if (pattern == null) Op.TRUE else (this@likeInside.lowerCase() like "%$pattern%".lowercase())
    }