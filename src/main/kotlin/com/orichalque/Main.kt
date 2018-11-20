package com.orichalque

import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.imageio.ImageWriteParam
import javax.imageio.metadata.IIOMetadata
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.FileImageOutputStream
import com.sun.deploy.uitoolkit.ToolkitStore.dispose
import java.io.FileOutputStream
import java.lang.System.out
import javax.imageio.stream.MemoryCacheImageOutputStream
import javax.imageio.stream.ImageOutputStream
import javax.imageio.ImageWriter




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

    }

    fun reduceMatrix(matrix: ArrayList<ArrayList<Color>>, factor : Int) : ArrayList<ArrayList<Color>> {

        var map = HashMap<Pair<Int, Int>, ArrayList<Color>>()
        matrix.mapIndexed { i, line ->
            line.mapIndexed {j, column -> Pair(i / factor , j / factor)}
                .mapIndexed { j, pair ->
                    if (map[pair] == null) {
                        map[pair] = ArrayList()
                    }
                    map[pair]!!.add(matrix[i][j])
                    pair
                }
                .forEachIndexed {j, pair -> matrix[i][j] = averageColor(map.get(key = Pair(i / factor , j / factor))!!)}
        }

        separatePixelsWithLines(matrix, map, factor, Color.BLACK)
        return matrix
    }

    private fun separatePixelsWithLines(matrix: ArrayList<ArrayList<Color>>, map: HashMap<Pair<Int, Int>, ArrayList<Color>>, factor: Int, color: Color) {

        map.forEach { t, u ->
            if (t.second -1 >= 0 && almostDifferentColorsWithDelta(averageColor(u),averageColor(map[Pair(t.first, t.second-1)]))) {
                matrix[t.first*factor][(t.second)*factor] = color
                //nok
                matrix[(1+t.first)*factor][(1+t.second)*factor] = color

            }
            if (t.first -1 >= 0 && almostDifferentColorsWithDelta(averageColor(u), averageColor(map[Pair(t.first -1, t.second)]))) {
                matrix[t.first*factor][t.second*factor] = color

                matrix[(1+t.first)*factor][(1+t.second)*factor] = color
            }
        }

    }

    private fun almostDifferentColorsWithDelta(color1: Color, color2: Color): Boolean {
        return EuclidianDistanceColors.euclidianDistance(color1, color2) > 40.0
    }

    fun averageColor(colors: ArrayList<Color>?): Color {

        var r = 0
        var g = 0
        var b = 0
        var a = 0

        if (colors != null) {
            colors.forEach{ c -> g+=c.green; r+=c.red; b+=c.blue; a+=c.alpha}
            return Color(r/colors.size, g/colors.size, b/colors.size, a/colors.size)
        } else {
            return Color.WHITE
        }
    }

    fun toImage(reduced: ArrayList<ArrayList<Color>>, f: File, metadata: IIOMetadata? = null) {
        var image = BufferedImage(reduced.size, reduced[0].size, BufferedImage.TYPE_INT_RGB)
        reduced.forEachIndexed{i, arrayList ->  arrayList.forEachIndexed { j, color -> image.setRGB(i, j, color.rgb) }}

        if (!f.exists())
            f.createNewFile()


        if ("png".equals(f.extension)) {
            savePng(image, f)
        } else if ("jpg".equals(f.extension)) {
            saveJpg(image, f, metadata)
        }

    }

    private fun saveJpg(image: BufferedImage, f: File, metadata: IIOMetadata?) {
        var imageWriter = ImageIO.getImageWritersByFormatName("jpg").next()
        val iwp = imageWriter.defaultWriteParam
        iwp.compressionMode = ImageWriteParam.MODE_EXPLICIT
        iwp.compressionQuality = 1f
        val imgOut = MemoryCacheImageOutputStream(FileOutputStream(f))
        imageWriter.output = imgOut

        val iioimage = IIOImage(image, null, metadata)
        imageWriter.write(null, iioimage, iwp)
        imageWriter.dispose()
    }

    private fun savePng(image: BufferedImage, f: File) {
        ImageIO.write(image, "png", f)
    }

    fun readMetadata(image: File): IIOMetadata? {
        var input = ImageIO.createImageInputStream(image)
        var reader = ImageIO.getImageReaders(input).next()
        reader.setInput(input, true)
        return reader.streamMetadata
    }

    fun enhancePixels(reduced: ArrayList<ArrayList<Color>>, factor: Int) {
        var width = reduced.size
        var length = reduced[0].size

        for (i in 0 until width) {
            for (j in 0 until length) {
               if (i % factor == 0 || j % factor == 0)
                   reduced[i][j] = Color.BLACK
            }
        }
    }

}