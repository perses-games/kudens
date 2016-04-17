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
import kotlin.dom.build.createElement
import kotlin.dom.on

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

class Test(val webgl: WebGLRenderingContext) {
    var red: Float = 1f
    var green: Float = 1f;
    var blue: Float = 0f;
    var rotX: Float = 0f;
    var rotY: Float = 0f;
    var rotZ: Float = 0f;

    var pMatrix = Matrix4()
    var program: ShaderProgram
    var triangle: Float32Array

    init {
        var vainfo = arrayOf(
            VertextAttributeInfo("a_position", 2),
            VertextAttributeInfo("a_color", 3)
          )

        program = ShaderProgram(webgl, WebGLRenderingContext.TRIANGLES, vertexShaderSource, fragmentShaderSource, vainfo)
        triangle = Float32Array(arrayOf(
          0f, 0f, 1f, 0f, 0f,
          1f, 0f, 0f, 1f, 0f,
          1f, 1f, 0f, 0f, 1f,

          1f, 1f, 0f, 0f, 1f,
          0f, 1f, 1f, 1f, 0f,
          0f, 0f, 1f, 0f, 0f
          ))
    }

    fun update(time: Double) {
        red = Math.abs(Math.sin(time*0.5)).toFloat()
        green = Math.abs(Math.cos(time*0.3)).toFloat()
        blue = Math.abs(Math.cos(time*0.7)).toFloat()

        rotX = time.toFloat() / 5f
        //rotZ = time.toFloat()
    }

    fun render() {
        resize()

        webgl.clearColor(red, green, blue, 1f)
        webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

        //triangle.set(8, red)
        //triangle.set(8, green)
        //triangle.set(14, blue)
        //pMatrix.setPerspectiveProjection(45f, 800f/600f, 0.1f, 10f)
        //pMatrix.rotateX(rotX)
        //pMatrix.rotateZ(rotZ)

        program.begin()
        program.setUniformMatrix4fv("u_projectionView", pMatrix.getFloat32Array())
        program.queueVertices(triangle)
        program.end()
    }

    fun resize() {
        var canvas = webgl.canvas
        // Check if the canvas is not the same size.
        if (canvas.width != window.innerWidth.toInt() ||
          canvas.height != window.innerHeight.toInt()) {

            // Make the canvas the same size
            canvas.width = window.innerWidth.toInt()
            canvas.height = window.innerHeight.toInt()

            webgl.viewport(0, 0, canvas.width, canvas.height)
        }
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

    var canvas = document.createElement("canvas") as HTMLCanvasElement
    document.body!!.appendChild(canvas)
    var webgl = canvas.getContext("webgl") as WebGLRenderingContext

    canvas.on("resize", true, {
        canvas.width = window.innerWidth.toInt();
        canvas.height = window.innerHeight.toInt();

        webgl.viewport(0, 0, canvas.width, canvas.height)
    })

    Textures.load(webgl, "SHIP", "images/ship2.png")

    game = Test(webgl)

    loop()
}
