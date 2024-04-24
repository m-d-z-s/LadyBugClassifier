package me.mdzs.ladybugclassifier.network

import me.mdzs.ladybugclassifier.utils.DataGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class NeuralNetworkTest {

    @Test // H7, S20, E100
    fun test1() {
        val neuralNetwork = NeuralNetwork(
            shape = NetworkShape(2, 10, 1),
            learningRate = 0.01
        )
        val insects = DataGenerator.generateData(50)
        val testInsects = DataGenerator.generateData(10)

        neuralNetwork.train(insects,30)
        println(neuralNetwork.averageLosses.min())
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