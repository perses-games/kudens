package com.persesgames.texture

import com.persesgames.game.Game
import com.persesgames.map.tiled.MapTileset
import com.persesgames.math.Matrix4
import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.ShaderProgramMesh
import com.persesgames.shader.VertextAttributeInfo
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.HTMLImageElement
import org.w3c.fetch.Request
import java.util.*
import kotlin.browser.document

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 14:52
 */

private val vertexShaderSource = """
    attribute vec2 a_position;
    attribute vec2 a_boundingBox;
    attribute vec2 a_texCoord;
    attribute float a_scale;
    attribute float a_rotation;

    uniform mat4 u_projectionView;

    varying vec2 v_textCoord;

    mat4 scale(float scale) {
        return mat4(
            vec4(scale, 0.0,   0.0,   0.0),
            vec4(0.0,   scale, 0.0,   0.0),
            vec4(0.0,   0.0,   scale, 0.0),
            vec4(0.0,   0.0,   0.0,   1.0)
        );
    }

    mat4 rotateZ(float angle) {
        return mat4(
            vec4(cos(angle),   sin(angle),  0.0,  0.0),
            vec4(-sin(angle),  cos(angle),  0.0,  0.0),
            vec4(0.0,          0.0,         1.0,  0.0),
            vec4(0.0,          0.0,         0.0,  1.0)
        );
    }

    void main(void) {
        v_textCoord = a_texCoord;

        vec4 scaledBox = vec4(a_boundingBox, 1.0, 1.0) * scale(a_scale) * rotateZ(a_rotation);

        gl_Position = u_projectionView * vec4(a_position + scaledBox.xy, -1, 1.0);
    }
"""

private val fragmentShaderSource = """
    precision mediump float;

    uniform sampler2D u_sampler;

    varying vec2 v_textCoord;

    void main(void) {
        gl_FragColor = texture2D(u_sampler, v_textCoord);
    }
"""

class TextureData(
  val vMatrix: Matrix4,
  val texture: WebGLTexture
)

class Texture(
  val glTexture: WebGLTexture,
  val shaderProgram: ShaderProgram<TextureData>,
  val width: Int,
  val height: Int
) {
    val shaderProgramMesh: ShaderProgramMesh<TextureData>
    val left = -width / 2f
    val right = width / 2f
    val bottom = -height / 2f
    val top = height / 2f

    init {
        shaderProgramMesh = ShaderProgramMesh(shaderProgram)
    }

    fun queueDraw(x: Float, y: Float, scale: Float, rotation: Float) {
        shaderProgramMesh.queue( x, y, left,  bottom,  0f, 0f, scale, rotation)
        shaderProgramMesh.queue( x, y, left,  top,     0f, 1f, scale, rotation)
        shaderProgramMesh.queue( x, y, right, top,     1f, 1f, scale, rotation)
        shaderProgramMesh.queue( x, y, right, top,     1f, 1f, scale, rotation)
        shaderProgramMesh.queue( x, y, right, bottom,  1f, 0f, scale, rotation)
        shaderProgramMesh.queue( x, y, left,  bottom,  0f, 0f, scale, rotation)

        if (shaderProgramMesh.remaining() < 36) {
            render()
        }
    }

    fun queueTileDraw(x: Float, y: Float, tcLeft: Float, tcTop: Float, tcRight: Float, tcBottom: Float) {
        shaderProgramMesh.queue( x, y, left,  bottom,  tcLeft,  tcBottom, 1f/8f, 0f)
        shaderProgramMesh.queue( x, y, left,  top,     tcLeft,  tcTop,    1f/8f, 0f)
        shaderProgramMesh.queue( x, y, right, top,     tcRight, tcTop,    1f/8f, 0f)
        shaderProgramMesh.queue( x, y, right, top,     tcRight, tcTop,    1f/8f, 0f)
        shaderProgramMesh.queue( x, y, right, bottom,  tcRight, tcBottom, 1f/8f, 0f)
        shaderProgramMesh.queue( x, y, left,  bottom,  tcLeft,  tcBottom, 1f/8f, 0f)

        if (shaderProgramMesh.remaining() < 36) {
            render()
        }
    }

    fun render() {
        Game.gl().activeTexture(WebGLRenderingContext.TEXTURE0)
        Game.gl().bindTexture(WebGLRenderingContext.TEXTURE_2D, glTexture)

        shaderProgramMesh.render(TextureData(Game.view.vMatrix, glTexture))
    }
}

/*
{
    "frame": {"x":921,"y":1,"w":182,"h":103},
    "rotated": false,
    "trimmed": true,
    "spriteSourceSize": {"x":4,"y":4,"w":182,"h":103},
    "sourceSize": {"w":190,"h":110},
    "pivot": {"x":0.5,"y":0.5}
},
*/

class Rect(val x: Int, val y: Int, val w: Int, val h: Int)
class Size(val w: Int, val h: Int)
class Pivot(val x: Double, val y: Double)

class SpriteSheetData(
  val frame: Rect,
  val rotated: Boolean,
  val trimmed: Boolean,
  val spriteSourceSize: Rect,
  val sourceSize: Size,
  val pivot: Pivot
  )

class SpriteSheet(
    val glTexture: WebGLTexture,
    val shaderProgram: ShaderProgram<SpriteSheetData>,
    val data: Map<String, SpriteSheetData>
) {

}

object Textures {
    var textures = HashMap<String, Texture>()
    var startedLoading = 0
    var loaded = 0
    val shaderProgram: ShaderProgram<TextureData>

    init {
        val setter = { program: ShaderProgram<TextureData>, data: TextureData ->
            //program.webgl.activeTexture(WebGLRenderingContext.TEXTURE0);
            //program.webgl.bindTexture(WebGLRenderingContext.TEXTURE_2D, data.texture);

            program.setUniform1i("u_sampler", 0)
            program.setUniformMatrix4fv("u_projectionView", Game.view.vMatrix.getFloat32Array())
        }

        val vainfo = arrayOf(
          VertextAttributeInfo("a_position", 2),
          VertextAttributeInfo("a_boundingBox", 2),
          VertextAttributeInfo("a_texCoord", 2),
          VertextAttributeInfo("a_scale", 1),
          VertextAttributeInfo("a_rotation", 1)
        )

        shaderProgram = ShaderProgram(Game.gl(), WebGLRenderingContext.TRIANGLES, vertexShaderSource, fragmentShaderSource, vainfo, setter)
    }

    fun loadSpriteSheet(name: String) {
        //val data = Request(name).json()

        //println(data)
    }

    fun load(name: String, filename: String) {
        val gl = Game.gl()

        startedLoading++

        val webGlTexture = gl.createTexture()
        if (webGlTexture != null) {
            val image = document.createElement("img") as HTMLImageElement
            image.onload = {
                textureLoaded(webGlTexture, image)
                val texture = Texture(webGlTexture, shaderProgram, image.width, image.height)

                textures.put(name, texture)

                loaded++
                println("loaded texture $loaded/$startedLoading ${ready()}")
            }
            image.src = filename
        } else {
            throw IllegalStateException("Couldn't create webgl texture!")
        }
    }

    fun load(mapTileSet: MapTileset) {

    }

    fun textureLoaded(texture: WebGLTexture, image: HTMLImageElement) {
        val gl = Game.gl()

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)
        gl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1) // second argument must be an int
        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image)
        //gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST)
        //gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.LINEAR)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.LINEAR)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null)
    }

    fun ready() = loaded == startedLoading

    fun get(name: String) = textures[name] ?: throw IllegalArgumentException("Texture with name $name is not loaded!")

    fun clear() {
        // delete and unbind all textures...
    }

    fun render() {
        for ((key, value) in textures) {
            value.render()
        }
    }

}
