package com.persesgames

import com.persesgames.math.Matrix4
import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.VertextAttributeInfo
import com.persesgames.texture.Textures
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 13:17
 */

val vertexShaderSource = """
    attribute vec2 a_position;
    attribute vec3 a_color;

    uniform mat4 u_projectionView;

    varying vec3 v_color;

    void main(void) {
        v_color = a_color;
        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);
    }
"""

val fragmentShaderSource = """
    precision mediump float;

    varying vec3 v_color;

    void main(void) {
        gl_FragColor = vec4(v_color, 1.0);
    }
"""

class Test(val context3d: WebGLRenderingContext) {
    var red: Float = 1f
    var green: Float = 1f;
    var blue: Float = 0f;

    var pMatrix = Matrix4()
    var program: ShaderProgram
    var triangle: Float32Array

    init {
        var vainfo = arrayOf(
            VertextAttributeInfo("a_position", 2),
            VertextAttributeInfo("a_color", 3)
          )

        program = ShaderProgram(context3d, WebGLRenderingContext.TRIANGLES, vertexShaderSource, fragmentShaderSource, vainfo)
        triangle = Float32Array(arrayOf(
          0f, 0f, 1f, 0f, 0f,
          1f, 0f, 0f, 1f, 0f,
          1f, 1f, 0f, 0f, 1f
          ))
    }

    fun update(time: Double) {
        red = Math.abs(Math.sin(time*0.5)).toFloat()
        green = Math.abs(Math.cos(time*0.3)).toFloat()
    }

    fun render() {
        context3d.clearColor(red, green, blue, 1f)
        context3d.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

        program.begin()
        program.setUniformMatrix4fv("u_projectionView", pMatrix.get())
        program.queueVertices(triangle)
        program.end()
    }

}

var game: Test? = null
var start: Int = Date().getTime()
var time: Int = Date().getTime()
fun loop() {
    var testInstance = game
    if (testInstance != null) {
        time = Date().getTime()
        testInstance.update((time - start) / 1000.0)
        testInstance.render()
    }

    window.requestAnimationFrame {
        loop()
    }
}

fun main(args: Array<String>) {
    println("Hello!")

    var webGlElement = document.getElementById("canvas")!! as HTMLCanvasElement
    var context3d = webGlElement.getContext("webgl") as WebGLRenderingContext

    Textures.load(context3d, "SHIP", "images/ship2.png")

    game = Test(context3d)

    loop()
}
