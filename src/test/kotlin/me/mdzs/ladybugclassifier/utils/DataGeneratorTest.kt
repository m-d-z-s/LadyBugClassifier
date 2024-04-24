package me.mdzs.ladybugclassifier.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DataGeneratorTest{
    @Test
    fun test1(){
        val insects = DataGenerator.generateData(5)
        println(insects)
    }
}