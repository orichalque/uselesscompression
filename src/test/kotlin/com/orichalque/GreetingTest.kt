package com.orichalque

import org.junit.Test
import java.awt.Color
import java.io.File


class GreetingTest {

    @Test
    fun testLoadImage() {
        var image = File("src/test/resources/download.jpg")
        var main = Main()
        var matrix = main.loadFile(image)
        //main.displayMatrix(matrix)
        var reduced = main.reduceMatrix(matrix, 10)
        main.displayMatrix(reduced)
        main.toImage(reduced, File("src/test/resources/output.jpg"), main.readMetadata(image))
    }

    @Test
    fun testLoadImagePng() {
        var image = File("src/test/resources/download.png")
        var main = Main()
        var matrix = main.loadFile(image)
        //main.displayMatrix(matrix)
        var reduced = main.reduceMatrix(matrix, 50)

        main.toImage(reduced, File("src/test/resources/output.jpg"), null)
    }

    @Test
    fun checkCompareImage() {
        var color1 = Color.BLUE
        var color2 = Color(0, 1, 255)

        println(EuclidianDistanceColors.euclidianDistance(color1, color2))
    }

    @Test
    fun checkWeirdSplitter() {
        var image = File("src/test/resources/octopus.jpg")
        var matrix = WeirdSplitter1().split(Main().loadFile(image), 25)
        Main().toImage(matrix, File("src/test/resources/downloadWeird.jpg"), null)
    }


}