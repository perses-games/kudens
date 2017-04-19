package games.perses.shader

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext

/**
 * User: rnentjes
 * Date: 14-5-16
 * Time: 11:57
 */

class VertextAttributeInfo(val locationName: String, val numElements: Int) {
    var location = 0
    var offset = 0
}

class ShaderProgramMesh<T>(
  val shaderProgram: ShaderProgram<T>
) {
    val webgl = shaderProgram.webgl
    val data: Float32Array = Float32Array(20000 - (20000 % shaderProgram.drawLength))
    var currentIndex: Int = 0
    val attribBuffer: WebGLBuffer
    var counter = 0

    init {

        attribBuffer = webgl.createBuffer() ?: throw IllegalStateException("Unable to create webgl buffer!")
        webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer)
    }

    fun queue(vararg vertices: Float) {
        queueArray(vertices as Array<Float>)
    }

    fun queueArray(vertices: Array<Float>) {
        data.set(vertices, currentIndex)
        currentIndex += vertices.size

        if (bufferFull()) {
            //println("Skipped draw call, to many values!")
            currentIndex = 0
        }
    }

    fun remaining() = data.length - currentIndex

    fun bufferFull() = currentIndex == data.length

    fun render(userdata: T) {
        counter++
        if (currentIndex > 0) {
            if (currentIndex % shaderProgram.verticesBlockSize != 0) {
                throw IllegalStateException("Number of vertices not a multiple of the attribute block size!")
            }

            shaderProgram.begin(attribBuffer, userdata)

            webgl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, data, WebGLRenderingContext.DYNAMIC_DRAW)
            webgl.drawArrays(shaderProgram.drawType, 0, (currentIndex / shaderProgram.verticesBlockSize).toInt())
            currentIndex = 0

            shaderProgram.end()
        }
    }
}
