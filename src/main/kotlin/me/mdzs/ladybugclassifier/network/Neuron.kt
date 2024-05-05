package me.mdzs.ladybugclassifier.network

data class Neuron(
    var value: Double,
    var weights: MutableList<Double>,
    var bias: Double,
    var gradient: Double = 0.0,
    var inputSum: Double = 0.0
)