package com.orichalque

import java.awt.Color
import java.util.*

class WeirdSplitter1() {

    enum class Section {
        TOP,
        DOWN,
        RIGHT,
        LEFT
    }

    fun split(matrix : ArrayList<ArrayList<Color>>, factor: Int): ArrayList<ArrayList<Color>> {
        val width = matrix.size
        val height = matrix[0].size
        val xChunk = width / factor
        val yChunk = height / factor

        val squares = ArrayList<Square>()
        for (x in 0..width step xChunk)
            for (y in 0..height step yChunk)
                squares.add(Square(x, y, xChunk, yChunk, HashMap()))

        matrix.forEachIndexed { i, arrayList ->
            arrayList.forEachIndexed { j, color ->
                squares.forEach { t ->
                    if (i >= t.startX && i < t.startX + xChunk)
                        if (j >= t.startY && j < t.startY + yChunk)
                            t.subMap[Pair(i,j)] = color
                }
            }
        }

        squares.forEach { t -> t.computeAverageColorsPerSection().forEach { entry ->
                matrix[entry.key.first][entry.key.second] = entry.value
            }
        }

        return matrix
    }


    class Square(var startX: Int, var startY: Int, var width: Int, var height: Int, var subMap: MutableMap<Pair<Int, Int>, Color>) {
        var quarterMap = HashMap<Section, ArrayList<Map.Entry<Pair<Int, Int>, Color>>>()

        init {

        }

        fun computeAverageColorsPerSection() : MutableMap<Pair<Int, Int>, Color> {

            quarterMap[Section.TOP] = ArrayList()
            quarterMap[Section.DOWN] = ArrayList()
            quarterMap[Section.LEFT] = ArrayList()
            quarterMap[Section.RIGHT] = ArrayList()

            subMap.forEach { entry: Map.Entry<Pair<Int, Int>, Color> ->
                val top1 : Boolean = entry.key.first > entry.key.second
                val top2 : Boolean = (entry.key.first + entry.key.second - startX - startY) > (width + height) / 2

                if (top1 && top2) {
                    //right
                    quarterMap[Section.RIGHT]?.add(entry)
                }

                if (top1 && !top2) {
                    //top
                    quarterMap[Section.TOP]?.add(entry)
                }
                if (!top1 && top2) {
                    //down
                    quarterMap[Section.DOWN]?.add(entry)
                }
                if (!top1 && !top2) {
                    //left
                    quarterMap[Section.LEFT]?.add(entry)
                }
            }

            var subMap : MutableMap<Pair<Int, Int>, Color> = mutableMapOf()

            var averageColor = averageColor(quarterMap[Section.TOP]?.map { entry -> entry.value } as ArrayList<Color>?)
            quarterMap[Section.TOP]?.forEach { entry: Map.Entry<Pair<Int, Int>, Color> ->  subMap[entry.key] = averageColor }

            averageColor = averageColor(quarterMap[Section.DOWN]?.map { entry -> entry.value } as ArrayList<Color>?)
            quarterMap[Section.DOWN]?.forEach { entry: Map.Entry<Pair<Int, Int>, Color> ->  subMap[entry.key] = averageColor }

            averageColor = averageColor(quarterMap[Section.RIGHT]?.map { entry -> entry.value } as ArrayList<Color>?)
            quarterMap[Section.RIGHT]?.forEach { entry: Map.Entry<Pair<Int, Int>, Color> ->  subMap[entry.key] = averageColor }

            averageColor = averageColor(quarterMap[Section.LEFT]?.map { entry -> entry.value } as ArrayList<Color>?)
            quarterMap[Section.LEFT]?.forEach { entry: Map.Entry<Pair<Int, Int>, Color> ->  subMap[entry.key] = averageColor }

            return subMap
        }

        fun averageColor(colors: ArrayList<Color>?): Color {

            var r = 0
            var g = 0
            var b = 0
            var a = 0

            if (colors != null && colors.size > 0) {
                colors.forEach{ c -> g+=c.green; r+=c.red; b+=c.blue; a+=c.alpha}
                return Color(r/colors.size, g/colors.size, b/colors.size, a/colors.size)
            } else {
                return Color.WHITE
            }
        }
    }

}