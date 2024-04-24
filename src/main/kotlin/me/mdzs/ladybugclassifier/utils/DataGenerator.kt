package me.mdzs.ladybugclassifier.utils

import kotlin.math.roundToLong

object DataGenerator {

    fun generateData(size: Int): List<Insect> {
        val data = mutableListOf<Insect>()
        for (i in 1..size) {
            if (Math.random() < 0.5) {
                // Божья коровка
                var length = 2 + Math.random() * 3
                length = String.format("%.2f", length).toDouble()
                val width = length
                data.add(Insect(length, width, 0))
            } else {
                // Гусеница
                var length = 7 + Math.random() * 8
                length = String.format("%.2f", length).toDouble()
                var width = 0.5 + Math.random() * 1
                width = String.format("%.2f", width).toDouble()
                data.add(Insect(length, width, 1))
            }
        }
        return data
    }
}