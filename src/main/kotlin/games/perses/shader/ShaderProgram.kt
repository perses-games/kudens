package games.perses.shader

import org.khronos.webgl.*

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 15:15
 */

class ShaderProgram<T>(
  val webgl: WebGLRenderingContext,
  val drawType: Int,
  vertexShaderSource: String,
  fragmentShaderSource: String,
  val vainfo: Array<VertextAttributeInfo>,
  val setter: (program: ShaderProgram<T>, data: T) -> Unit) {

    var shaderProgram: WebGLProgram
    var vertex: WebGLShader
    var fragment: WebGLShader

    var verticesBlockSize = 0
    var drawLength = 0

    init {
        vertex = compileShader(vertexShaderSource, WebGLRenderingContext.VERTEX_SHADER)
        fragment = compileShader(fragmentShaderSource, WebGLRenderingContext.FRAGMENT_SHADER)

        shaderProgram = webgl.createProgram() ?: throw IllegalStateException("Unable to request shader program from webgl context!")
        webgl.attachShader(shaderProgram, vertex)
        webgl.attachShader(shaderProgram, fragment)
        webgl.linkProgram(shaderProgram)

        if (webgl.getProgramParameter(shaderProgram, WebGLRenderingContext.LINK_STATUS) == false) {
            //println(webgl.getProgramInfoLog(shaderProgram))
            throw IllegalStateException("Unable to compile shader program!")
        }

        webgl.useProgram(shaderProgram)

        this.verticesBlockSize = 0

        // set attribute locations...
        for (info in vainfo.iterator()) {
            info.location = webgl.getAttribLocation(shaderProgram, info.locationName)
            info.offset = verticesBlockSize

            verticesBlockSize += info.numElements
            //println("attrib: ${info.locationName}, info.location: ${info.location}, info.offset: ${info.offset}")
        }

        when(drawType) {
            WebGLRenderingContext.TRIANGLES -> {
                drawLength = verticesBlockSize * 3
            }
            else -> {
                drawLength = verticesBlockSize
            }
        }

        //println("verticesBlockSize $verticesBlockSize")

        //println("ShaderProgram constructor done")
    }

    private fun compileShader(source: String, type: Int): WebGLShader {
        val result: WebGLShader = webgl.createShader(type) ?: throw IllegalStateException("Unable to request shader from webgl context!")

        webgl.shaderSource(result, source)
        webgl.compileShader(result)

        if (webgl.getShaderParameter(result, WebGLRenderingContext.COMPILE_STATUS) == false) {
            throw IllegalStateException("Unable to compile shader!\n${source}\n\n${webgl.getShaderInfoLog(result)}")
        }

        return result
    }

    fun begin(attribBuffer: WebGLBuffer, userdata: T) {
        webgl.useProgram(shaderProgram);
        webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer);

        // set attribute locations...
        for (info in vainfo.iterator()) {
            webgl.enableVertexAttribArray(info.location)
            webgl.vertexAttribPointer(info.location, info.numElements, WebGLRenderingContext.FLOAT, false, verticesBlockSize * 4, info.offset * 4)
        }

        setter(this, userdata)
    }

    fun end() {
        for (info in vainfo.iterator()) {
            webgl.disableVertexAttribArray(info.location);
        }
        webgl.useProgram(null)
    }

    fun getAttribLocation(location: String) = webgl.getAttribLocation(shaderProgram, location);

    fun getUniformLocation(location: String) = webgl.getUniformLocation(shaderProgram, location);

    fun setUniform1f(location: String, value: Float) { webgl.uniform1f(getUniformLocation(location), value); }
    fun setUniform4f(location: String, v1: Float, v2: Float, v3: Float, v4: Float) { webgl.uniform4f(getUniformLocation(location), v1, v2, v3, v4); }
    fun setUniform1i(location: String, value: Int) { webgl.uniform1i(getUniformLocation(location), value); }
    fun setUniformMatrix4fv(location: String, value: Float32Array) { webgl.uniformMatrix4fv(getUniformLocation(location), false, value); }

}
