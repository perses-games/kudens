package games.perses.kudens.color

/**
 * User: rnentjes
 * Date: 8-2-17
 * Time: 12:28
 *
 * Taken from (java version): http://stackoverflow.com/questions/2353211/hsl-to-rgb-color-conversion
 */

object Color {

  /**
   * Converts an HSL color value to RGB. Conversion formula
   * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
   * Assumes h, s, and l are contained in the set [0, 1] and
   * returns r, g, and b in the set [0, 255].

   * @param h       The hue
   * *
   * @param s       The saturation
   * *
   * @param l       The lightness
   * *
   * @return int array, the RGB representation
   */
  fun hslToRgb(h: Float, s: Float, l: Float): IntArray {
    val r: Float
    val g: Float
    val b: Float

    if (s == 0f) {
      b = l
      g = b
      r = g // achromatic
    } else {
      val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
      val p = 2 * l - q
      r = hueToRgb(p, q, h + 1f / 3f)
      g = hueToRgb(p, q, h)
      b = hueToRgb(p, q, h - 1f / 3f)
    }
    val rgb = intArrayOf((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())

    return rgb
  }

  /** Helper method that converts hue to rgb  */
  fun hueToRgb(p: Float, q: Float, t: Float): Float {
    var lt = t
    if (lt < 0f) {
      lt += 1f
    }
    if (lt > 1f) {
      lt -= 1f
    }
    if (lt < 1f / 6f) {
      return p + (q - p) * 6f * lt
    }
    if (lt < 1f / 2f) {
      return q
    }
    if (lt < 2f / 3f) {
      return p + (q - p) * (2f / 3f - lt) * 6f
    }
    return p
  }

  /**
   * Converts an RGB color value to HSL. Conversion formula
   * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
   * Assumes pR, pG, and bpBare contained in the set [0, 255] and
   * returns h, s, and l in the set [0, 1].

   * @param pR       The red color value
   * *
   * @param pG       The green color value
   * *
   * @param pB       The blue color value
   * *
   * @return float array, the HSL representation
   */
  fun rgbToHsl(pR: Int, pG: Int, pB: Int): FloatArray {
    val r = pR / 255f
    val g = pG / 255f
    val b = pB / 255f

    val max = if (r > g && r > b) r else if (g > b) g else b
    val min = if (r < g && r < b) r else if (g < b) g else b

    var h: Float
    val s: Float
    val l: Float
    l = (max + min) / 2.0f

    if (max == min) {
      s = 0.0f
      h = s
    } else {
      val d = max - min
      s = if (l > 0.5f) d / (2.0f - max - min) else d / (max + min)

      if (r > g && r > b)
        h = (g - b) / d + (if (g < b) 6.0f else 0.0f)
      else if (g > b)
        h = (b - r) / d + 2.0f
      else
        h = (r - g) / d + 4.0f

      h /= 6.0f
    }

    val hsl = floatArrayOf(h, s, l)
    return hsl
  }
}