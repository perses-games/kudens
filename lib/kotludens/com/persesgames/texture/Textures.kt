package com.persesgames.texture

import com.persesgames.game.Game
import com.persesgames.math.Matrix4
import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.ShaderProgramMesh
import com.persesgames.shader.VertextAttributeInfo
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.HTMLImageElement
import java.util.*
import kotlin.browser.document

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 14:52
 */

private val vertexShaderSource = """
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

private val fragmentShaderSource = """
    precision mediump float;

    uniform sampler2D u_sampler;

    varying vec3 v_color;
    varying vec2 v_textCoord;

    void main(void) {
        gl_FragColor = texture2D(u_sampler, v_textCoord) * vec4(v_color, 1.0);
    }
"""

class TextureData(
  val vMatrix: Matrix4,
  val texture: WebGLTexture
)

class Texture(val glTexture: WebGLTexture, val shaderProgram: ShaderProgram<TextureData>) {
    val shaderProgramMesh: ShaderProgramMesh<TextureData>

    init {
        shaderProgramMesh = ShaderProgramMesh(shaderProgram)
    }

    fun queueDraw(x: Float, y: Float) {
        shaderProgramMesh.queue( 0f, 0f, 1f, 1f, 1f);
        shaderProgramMesh.queue( 0f, 1f, 1f, 1f, 1f);
        shaderProgramMesh.queue( 1f, 1f, 1f, 1f, 1f);

        shaderProgramMesh.queue( 1f, 1f, 1f, 1f, 1f);
        shaderProgramMesh.queue( 1f, 0f, 1f, 1f, 1f);
        shaderProgramMesh.queue( 0f, 0f, 1f, 1f, 1f);
    }

    fun render(userdata: TextureData) {
        Game.gl().activeTexture(WebGLRenderingContext.TEXTURE0)
        Game.gl().bindTexture(WebGLRenderingContext.TEXTURE_2D, glTexture);

        shaderProgramMesh.render(userdata)
    }
}

object Textures {
    var textures = HashMap<String, Texture>();
    var startedLoading = 0
    var loaded = 0
    val shaderProgram: ShaderProgram<TextureData>

    init {
        val setter = { program: ShaderProgram<TextureData>, data: TextureData ->
            program.webgl.activeTexture(WebGLRenderingContext.TEXTURE0);
            program.webgl.bindTexture(WebGLRenderingContext.TEXTURE_2D, data.texture);

            program.setUniform1i("u_sampler", 0)
            var matrix = Matrix4()
            matrix.setToIdentity()
            program.setUniformMatrix4fv("u_projectionView", matrix.getFloat32Array())
        }

        val vainfo = arrayOf(
          VertextAttributeInfo("a_position", 2),
          VertextAttributeInfo("a_color", 3)
        )

        shaderProgram = ShaderProgram(Game.gl(), WebGLRenderingContext.TRIANGLES, vertexShaderSource, fragmentShaderSource, vainfo, setter)
    }

    fun loadSpriteSheet(name: String, filename: String) {

    }

    fun load(name: String, filename: String) {
        var gl = Game.gl()

        startedLoading++

        var webGlTexture = gl.createTexture()
        if (webGlTexture != null) {
            var image = document.createElement("img") as HTMLImageElement
            image.onload = {
                textureLoaded(webGlTexture, image)
                textures.put(name, Texture(webGlTexture, shaderProgram))
                loaded++
                println("loaded texture $loaded/$startedLoading ${ready()}")
            }
            image.src = filename
        } else {
            throw IllegalStateException("Couldn't create webgl texture!")
        }
    }

    fun textureLoaded(texture: WebGLTexture, image: HTMLImageElement) {
        var gl = Game.gl()

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
        gl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1); // second argument must be an int
        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image);
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST);
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST);
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
    }

    fun ready() = loaded == startedLoading

    fun get(name: String) = textures.get(name) ?: throw IllegalArgumentException("Texture with name $name is not loaded!")

    fun clear() {
        // delete and unbind all textures...
    }

    fun render() {

        for ((key, value) in textures) {
            val textureData = TextureData(Game.view.vMatrix, value.glTexture)

            value.render(textureData)
        }
    }

}
