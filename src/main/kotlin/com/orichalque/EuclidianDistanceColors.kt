package com.orichalque

import java.awt.Color

class EuclidianDistanceColors {
    companion object {
        fun euclidianDistance(color1 : Color, color2: Color) : Double {
            val r = (color1.red - color2.red)*(color1.red - color2.red)
            var g = (color1.green - color2.green)*(color1.green - color2.green)
            var b = (color1.blue - color2.blue)*(color1.blue- color2.blue)

            return Math.sqrt((r+g+b).toDouble())
        }
    }
}