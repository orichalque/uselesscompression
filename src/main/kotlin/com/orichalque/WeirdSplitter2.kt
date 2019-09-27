package com.orichalque

import java.awt.Color
import java.util.*

open class WeirdSplitter2 {


    fun split(matrix : ArrayList<ArrayList<Color>>, factor: Int): ArrayList<ArrayList<Color>> {
        val width = matrix.size
        val height = matrix[0].size
        val xChunk = width / factor
        val yChunk = height / factor

        val squares = ArrayList<Square>()

        for (x in 0 until width step xChunk)
            for (y in 0 until height step yChunk)
                squares.add(Square(x, y, xChunk, yChunk, HashMap()))

        squares.forEach { square ->
            run {
                build(matrix, square)
            }
        }

        return rebuild(matrix, squares)
    }

    protected fun build(matrix: ArrayList<ArrayList<Color>>, square: WeirdSplitter2.Square) {
        for (x in square.startX until square.startX+square.width) {
            for (y in square.startY until (square.startY+square.height - 1)) {

                if (x < matrix.size && y < matrix[x].size) {
                    val color = matrix[x][y]
                    square.subMap[Pair(x, y)] = color
                }

            }
        }

        transform(square)
    }

    class Square(var startX: Int, var startY: Int, var width: Int, var height: Int, var subMap: MutableMap<Pair<Int, Int>, Color>)

    protected fun rebuild(matrix: ArrayList<ArrayList<Color>>, squares: ArrayList<Square>): ArrayList<ArrayList<Color>> {
        squares.forEach { square ->
            run {
                square.subMap.forEach { entry: Map.Entry<Pair<Int, Int>, Color> ->
                    run {
                        matrix[entry.key.first][entry.key.second] = entry.value
                    }
                }
            }
        }

        return matrix
    }

    protected open fun transform(square: Square) {
        //var averageColor = Main.Utils.averageColor(square.subMap.values)
        var centerCoordinates = Pair(square.startX + square.width/2, square.startY + square.height/2)
        var averageColor = Main.Utils.averageColor(square.subMap.values)
        square.subMap.forEach { entry: Map.Entry<   Pair<Int, Int>, Color> ->
            run {
                if (Main.Utils.distance(entry.key, centerCoordinates) > (square.width / 2)) {
                    square.subMap[entry.key] = averageColor
                }
            }
        }

    }


}