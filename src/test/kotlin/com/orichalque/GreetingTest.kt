package com.orichalque

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals


class GreetingTest {

    @Test
    fun testLoadImage() {
        var image = File("src/test/resources/download.jpeg")
        var main = Main()
        var matrix = main.loadFile(image)
        //main.displayMatrix(matrix)
        var reduced = main.reduceMatrix(matrix, 10)
        //main.displayMatrix(reduced)
        main.toImage(reduced, File("src/test/resources/output.jpeg"))
    }

}