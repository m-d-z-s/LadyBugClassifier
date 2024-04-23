package me.mdzs.ladybugclassifier.network

import jdk.javadoc.internal.doclets.toolkit.taglets.InheritableTaglet.Output
import me.mdzs.ladybugclassifier.utils.Insects
import kotlin.math.exp
import kotlin.math.max

/**
 * NeuralNetwork is implementation of a classifier. It contains fields:
 *
 * 1. neurons
 * 2. weights
 * 3. thresholds
 */
class NeuralNetwork(
    val shape: NetworkShape
) {
    private val weightsToHidden: MutableList<MutableList<Double>> = initWeights(
        shape.inputLayerSize,
        shape.hiddenLayersSize
    )
    private val weightsToOutput: MutableList<MutableList<Double>> = initWeights(
        shape.hiddenLayersSize,
        shape.outputLayerSize
    )

    public fun train(data: List<Insects>, epochs: Int) {
        repeat(epochs) {
            data.forEach { insect ->
                val inputs = listOf(insect.length, insect.width)
                val y = insect.type
                val hiddens = MutableList(shape.hiddenLayersSize) { 0.0 }
                val outputs = MutableList(shape.outputLayerSize) { 0.0 }

                for (i in 0 until shape.hiddenLayersSize) {
                    hiddens[i] = relu(dotProduct(inputs, weightsToHidden[i]))
                }
            }
        }
    }

    private fun dotProduct(inputs: List<Int>, doubles: List<Double>): Double {
        TODO("Not yet implemented")
    }

    public fun predict(data: List<Insects>): List<Int> {
        TODO()
    }

    private fun sigmoid(x: Double): Double = 1 / (1 + exp(-x))
    private fun sigmoidDerivative(x: Double): Double = x * (1 - x)

    private fun relu(x: Double): Double = max(0.0, x)
    private fun reluDerivative(x: Double): Double = when {
        x > 0.0 -> 1.0
        else -> 0.0
    }


    private fun initWeights(sizeIn: Int, sizeOut: Int): MutableList<MutableList<Double>> {
        val weights = mutableListOf<MutableList<Double>>()
        repeat(sizeIn) {
            weights.add(mutableListOf())
        }
        weights.forEach { vector ->
            repeat(sizeOut) {
                vector.add(Math.random())
            }
        }
        return weights
    }


}

data class NetworkShape(
    val inputLayerSize: Int,
    val hiddenLayersSize: Int,
    val outputLayerSize: Int
)
