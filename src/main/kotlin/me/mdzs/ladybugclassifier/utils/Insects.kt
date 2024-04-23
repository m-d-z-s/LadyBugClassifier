package me.mdzs.ladybugclassifier.utils

data class Insects(
    val length: Int,
    val width: Int,
    val type: Int
){
    constructor(length: Int, width: Int) : this(length, width, -1)
}
