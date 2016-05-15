package com.persesgames.shader

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
    //val data: Array<Float> = Array(4096, { 0f })
    val data: Float32Array = Float32Array(4096)
    var currentIndex: Int = 0
    val attribBuffer: WebGLBuffer

    init {
        attribBuffer = webgl.createBuffer() ?: throw IllegalStateException("Unable to create webgl buffer!")
        webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer);
    }

    fun queue(vararg vertices: Float) {
        data.set(vertices.toTypedArray(), currentIndex)
        currentIndex += vertices.size
    }

    fun queue(vertices: Array<Float>) {
        data.set(vertices, currentIndex)
        currentIndex += vertices.size
    }

    fun render(userdata: T) {
        if (currentIndex > 0) {
            //println("currentIndex=$currentIndex blockSize=${shaderProgram.verticesBlockSize} drawLength=${shaderProgram.drawLength} drawing=${(currentIndex / shaderProgram.drawLength).toInt()}")
            if (currentIndex % shaderProgram.verticesBlockSize != 0) {
                throw IllegalStateException("Number of vertices not a multiple of the attribute block size!")
            }

            shaderProgram.begin(attribBuffer, userdata)

            webgl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, data, WebGLRenderingContext.DYNAMIC_DRAW);
            webgl.drawArrays(shaderProgram.drawType, 0, (currentIndex / shaderProgram.verticesBlockSize).toInt())
            currentIndex = 0;

            shaderProgram.end()
        }
    }
}
