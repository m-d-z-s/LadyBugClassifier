package me.mdzs.ladybugclassifier.network

import me.mdzs.ladybugclassifier.utils.Insect
import org.junit.jupiter.api.Test
import java.io.File

class NeuralNetworkTest {
    @Test
    fun test1() {
        val neuralNetwork = NeuralNetwork(
            shape = NetworkShape(2, 20, 1),
            learningRate = 0.001
        )
        val testInsects = listOf(
            Insect(0.1111111111, 0.1041666667, 0),
            Insect(0.006944444444, 0.9583333333, 1),
            Insect(0.2152777778, 0.1041666667, 0),
            Insect(0.05555555556, 0.9583333333, 1),
        )


        neuralNetwork.train(INSECTS, 350)
        val minLoss = neuralNetwork.averageLosses.min()
        println(minLoss.toString() + " at epoch " + neuralNetwork.averageLosses.indexOf(minLoss))
        val losses = neuralNetwork.averageLosses
        val file = File("file.txt")
        file.bufferedWriter().use { out ->
            losses.forEach { value ->
                out.write("$value\n")
            }
        }
        println("Predicted: ${neuralNetwork.predict(testInsects)}")
        print("Actual:    [")
        testInsects.forEach {
            print("${it.type}, ")
        }
        println("]")

    }
}