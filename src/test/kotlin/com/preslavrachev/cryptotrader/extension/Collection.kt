package com.preslavrachev.cryptotrader.extension

fun <T> List<T>.toPairs(): List<Pair<T,T>> {
    val leftSide = this.dropLast(1)
    val rightSide = this.drop(1)
    return leftSide.zip(rightSide)
}
