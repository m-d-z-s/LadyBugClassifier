package me.mdzs.ladybugclassifier.utils

data class Insect(
    val length: Double,
    val width: Double,
    val type: Int
){
    constructor(length: Double, width: Double) : this(length, width, -1)
}
