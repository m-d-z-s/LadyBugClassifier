package me.mdzs.ladybugclassifier.network

data class Neuron(
    var value: Double,
    var weights: MutableList<Double>,
    var error: Double = 0.0,
    var threshold: Double = 0.0,
    var sum: Double = 0.0
)
