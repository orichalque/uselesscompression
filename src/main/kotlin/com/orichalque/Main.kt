package com.orichalque

import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel


class Main {

    lateinit var matrix : ArrayList<ArrayList<Color>>

    fun loadFile(file : File) : ArrayList<ArrayList<Color>> {
        if (file.exists()) {
            var image = ImageIO.read(file)

            matrix = ArrayList()
            for (i in 0 until image.width) {
                var line = ArrayList<Color>(image.width)
                for (j in 0 until image.height) {
                    line.add(Color(image.getRGB(i,j), true))
                }
                matrix.add(line)
            }
        }

        return matrix
    }

    fun displayMatrix(matrix : ArrayList<ArrayList<Color>>) {
        var image = BufferedImage(matrix.size, matrix[0].size, BufferedImage.TYPE_INT_ARGB)

        for (i in 0 until matrix.size)
            for (j in 0 until matrix[i].size)
                image.setRGB(i, j, matrix[i][j].rgb)


        val picLabel = JLabel(ImageIcon(image))
        val jPanel = JPanel()
        jPanel.add(picLabel)
        val f = JFrame()
        f.size = Dimension(matrix.size, matrix[0].size)
        f.add(jPanel)
        f.isVisible = true

        Scanner(System.`in`).next()
    }

    fun reduceMatrix(matrix: ArrayList<ArrayList<Color>>, factor : Int) : ArrayList<ArrayList<Color>> {

        var map = HashMap<Pair<Int, Int>, ArrayList<Color>>()
        matrix.mapIndexed { i, line ->  line.mapIndexed {j, column -> Pair(i / factor , j / factor)}
                .mapIndexed { j, pair ->
                    if (map[pair] == null) {
                        map[pair] = ArrayList()
                    }
                    map[pair]!!.add(matrix[i][j])
                    pair
                }
                .forEachIndexed {j, pair -> matrix[i][j] = averageColor(map.get(key = Pair(i / factor , j / factor))!!)}
        }


        return matrix

    }

    private fun averageColor(colors: ArrayList<Color>): Color {
        var r = 0
        var g = 0
        var b = 0
        var a = 0

        colors.forEach{ c -> g+=c.green; r+=c.red; b+=c.blue; a+=c.alpha}
        return Color(r/colors.size, g/colors.size, b/colors.size, a/colors.size)
    }

    fun toImage(reduced: ArrayList<ArrayList<Color>>, f : File) {
        var image = BufferedImage(reduced.size, reduced[0].size, BufferedImage.TYPE_INT_ARGB)
        reduced.forEachIndexed{i, arrayList ->  arrayList.forEachIndexed { j, color -> image.setRGB(i, j, color.rgb) }}
        if (!f.exists())
            f.createNewFile()

        ImageIO.write(image, "jpg", f)
    }

}