package me.mdzs.ladybugclassifier.network

import me.mdzs.ladybugclassifier.utils.Insect
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.random.Random

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
            val errors = mutableListOf<Double>()
            var gradientSum = 0.0

            val mixedData = data.shuffled()

            mixedData.forEach { insect ->
                captureData(insect)
                val y = insect.type

                feedForward()

                val error = cE(y, outputs[0].value)
                val currentGradient = error * sigmoidDerivative(outputs[0].inputSum)
                gradientSum += currentGradient
                errors.add((error))
            }

            propagateBack(gradientSum)

            val losses = errors.average()
            averageLosses.add(losses)
        }
    }

    public fun predict(data: List<Insect>): List<Int> {
        val predictions = mutableListOf<Int>()

        data.forEach { insect ->
            captureData(insect)
            feedForward()
            predictions.add(if (outputs[0].value > 0.5) 1 else 0)
        }

        return predictions
    }

    private fun captureData(insect: Insect) {
        inputs[0].value = insect.length
        inputs[1].value = insect.width
    }

    private fun propagateBack(gradientSum: Double) {
        outputs[0].gradient = gradientSum // gradient
        outputs[0].bias -= learningRate * outputs[0].gradient * outputs[0].value // o[0] bias update

        hiddens.forEach { hiddenNeuron ->
            // weights
            hiddenNeuron.weights[0] += learningRate * outputs[0].gradient * hiddenNeuron.value // wi = wi-1 - lr * gradient * hi
            hiddenNeuron.gradient =
                outputs[0].gradient * hiddenNeuron.weights[0] * reluDerivative(hiddenNeuron.inputSum) // gradient * updated_wi * f'(hi)
            // biases
            hiddenNeuron.bias -= learningRate * hiddenNeuron.gradient * hiddenNeuron.value
        }

        inputs.forEach { inputNeuron ->
            hiddens.forEachIndexed { j, hiddenNeuron ->
                inputNeuron.weights[j] += learningRate * hiddenNeuron.gradient * inputNeuron.value
            }
        }
    }

    private fun cE(y: Int, p: Double): Double = - y * ln(p) - (1 - y) * ln(1 - p)

    private fun feedForward() {
        hiddens.forEachIndexed { index, hiddenNeuron ->
            hiddenNeuron.inputSum = dotProduct(inputs, index) + hiddenNeuron.bias
            hiddenNeuron.value = relu(hiddenNeuron.inputSum)
        }
        outputs.forEachIndexed { index, outputNeuron ->
            outputNeuron.inputSum = dotProduct(hiddens, index) + outputNeuron.bias
            outputNeuron.value = sigmoid(outputNeuron.inputSum)
        }
    }


    private fun initLayer(layerSize: Int, nextLayerSize: Int): List<Neuron> {
        val randomizer = Random(37)

        val listNeurons = mutableListOf<Neuron>()
        repeat(layerSize) {
            listNeurons.add(
                Neuron(
                    value = 0.0,
                    weights = initWeights(nextLayerSize),
                    bias = randomizer.nextDouble(-1.0, 1.0)
                )
            )
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
    private fun sigmoidDerivative(x: Double): Double = sigmoid(x) * (1 - sigmoid(x))

    private fun relu(x: Double): Double = max(0.0, x)
    private fun reluDerivative(x: Double): Double = when {
        x > 0.0 -> 1.0
        else -> 0.0
    }


    private fun initWeights(sizeOut: Int): MutableList<Double> {
        val randomizer = Random(37)

        val weights = mutableListOf<Double>()
        repeat(sizeOut) {
            weights.add(randomizer.nextDouble(0.0, 0.5))
        }

        return weights
    }
}

data class NetworkShape(
    val inputLayerSize: Int,
    val hiddenLayersSize: Int,
    val outputLayerSize: Int
)