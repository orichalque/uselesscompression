package com.orichalque

import org.junit.Test
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

}