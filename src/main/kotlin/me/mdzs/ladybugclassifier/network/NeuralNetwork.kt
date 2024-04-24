package me.mdzs.ladybugclassifier.network

import me.mdzs.ladybugclassifier.utils.Insect
import kotlin.math.abs
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
    val shape: NetworkShape,
    val learningRate: Double
) {
    var averageLosses: MutableList<Double> = mutableListOf()

    private val inputs: List<Neuron> = initLayer(shape.inputLayerSize, shape.hiddenLayersSize)
    private val hiddens: List<Neuron> = initLayer(shape.hiddenLayersSize, shape.outputLayerSize)
    private val outputs: List<Neuron> = initLayer(shape.outputLayerSize, 0)

    public fun train(data: List<Insect>, epochs: Int) {
        averageLosses = emptyList<Double>().toMutableList()
        repeat(epochs) {

            print("Epoch $it began. ")

            val losses = mutableListOf<Double>()
            data.forEach { insect ->
                captureData(insect)

                val y = insect.type

                feedForward()

                losses.add(propagateBack(y))

                updateWeights()

            }

            val averageLoss = losses.average()
            println("Current average loss = $averageLoss")
            averageLosses.add(averageLoss)
        }
    }

    public fun predict(data: List<Insect>): List<Int> {
        val predictions = mutableListOf<Int>()

        data.forEach { insect ->
            captureData(insect)
            feedForward()
            predictions.add(if (outputs[0].value >= 0.5) 1 else 0)
        }

        return predictions
    }

    private fun captureData(insect: Insect) {
        inputs[0].value = insect.length
        inputs[1].value = insect.width
    }

    private fun updateWeights() {
        hiddens.forEachIndexed { i, hidden ->
            inputs.forEachIndexed { j, input ->
                input.weights[i] += input.value * hidden.error * learningRate
            }
        }
        outputs.forEachIndexed { i, output ->
            hiddens.forEachIndexed { j, hidden ->
                hidden.weights[i] += hidden.value * output.error * learningRate
            }
        }

    }

    private fun propagateBack(y: Int):Double {
        outputs[0].error = abs(y.toDouble() - outputs[0].value)

        hiddens.forEachIndexed { index, neuron ->
            neuron.error = sigmoidDerivative(neuron.value) * neuron.weights[0] * outputs[0].error
        }

        return outputs[0].error
    }

    private fun feedForward() {
        hiddens.forEachIndexed { index, neuron ->
            neuron.value = relu(dotProduct(inputs, index))
        }
        outputs.forEachIndexed { index, neuron ->
            neuron.value = sigmoid(dotProduct(hiddens, index))
        }
    }


    private fun initLayer(layerSize: Int, nextLayerSize: Int): List<Neuron> {
        val listNeurons = mutableListOf<Neuron>()
        repeat(layerSize) {
            listNeurons.add(Neuron(0.0, initWeights(nextLayerSize)))
        }
        return listNeurons
    }

    private fun dotProduct(neurons: List<Neuron>, index: Int): Double {
        var sum = 0.0
        neurons.forEach { neuron ->
            sum += neuron.value * neuron.weights[index]
        }
        return sum
    }


    private fun sigmoid(x: Double): Double = 1 / (1 + exp(-x))
    private fun sigmoidDerivative(x: Double): Double = x * (1 - x)

    private fun relu(x: Double): Double = max(0.0, x)
    private fun reluDerivative(x: Double): Double = when {
        x > 0.0 -> 1.0
        else -> 0.0
    }


    private fun initWeights(sizeOut: Int): MutableList<Double> {
        val weights = mutableListOf<Double>()
        repeat(sizeOut) {
            weights.add(Math.random())
        }

        return weights
    }


}

data class NetworkShape(
    val inputLayerSize: Int,
    val hiddenLayersSize: Int,
    val outputLayerSize: Int
)
