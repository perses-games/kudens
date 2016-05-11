package com.persesgames

import com.persesgames.math.Matrix4
import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.VertextAttributeInfo
import com.persesgames.texture.Textures
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext
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
    varying vec2 v_textCoord;

    void main(void) {
        v_color = a_color;
        v_textCoord = a_position.xy;

        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);
    }
"""

val fragmentShaderSource = """
    precision mediump float;

    uniform sampler2D u_sampler;

    varying vec3 v_color;
    varying vec2 v_textCoord;

    void main(void) {
        gl_FragColor = texture2D(u_sampler, v_textCoord) * vec4(v_color, 1.0);
    }
"""

class Test(val webgl: WebGLRenderingContext) {
    var red: Float = 1f
    var green: Float = 1f;
    var blue: Float = 0f;
    var rotX: Float = 0f;
    var rotY: Float = 0f;
    var rotZ: Float = 0f;
    var z = -1f;

    var mMatrix = Matrix4()
    var vMatrix = Matrix4()
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

        Textures.load("SHIP", "images/ship2.png")
    }

    fun update(time: Double) {
        if (!Textures.ready()) {
            return
        }

        red = Math.abs(Math.sin(time*0.5)).toFloat()
        green = Math.abs(Math.cos(time*0.3)).toFloat()
        blue = Math.abs(Math.cos(time*0.7)).toFloat()

        rotX = time.toFloat() / 5f
        rotY = time.toFloat() / 3f
        z = -2f + Math.sin(time).toFloat() * 1f
        //rotZ = time.toFloat()
    }

    fun render() {
        resize()

        if (!Textures.ready()) {
            return
        }

        webgl.clearColor(red, green, blue, 0.9f)
        webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

        webgl.enable(WebGLRenderingContext.BLEND);
        webgl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE_MINUS_SRC_ALPHA); //ONE_MINUS_DST_ALPHA);

        mMatrix.setToIdentity()
        mMatrix.translate(-0.5f, -0.5f,0f);
        mMatrix.scale(2f, 2f, 1f)
        mMatrix.rotateX(rotX);
        mMatrix.rotateY(rotY);
        mMatrix.rotateZ(rotX + rotY);
        mMatrix.translate(0f, 0f, z);
        //triangle.set(8, red)
        //triangle.set(8, green)
        //triangle.set(14, blue)

        pMatrix.setPerspectiveProjection(60f, (window.innerWidth/window.innerHeight).toFloat(), 0.1f, 100f)
//        pMatrix.mul(vMatrix)
//        pMatrix.mul(mMatrix)
        mMatrix.mul(vMatrix);
        mMatrix.mul(pMatrix);

        program.begin()

        Textures.get("SHIP").bind()

        program.setUniform1i("u_sampler", 0)
        program.setUniformMatrix4fv("u_projectionView", mMatrix.getFloat32Array())
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

/*
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
*/
