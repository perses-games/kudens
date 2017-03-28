package games.perses.texture

import games.perses.game.DrawMode
import games.perses.game.Game
import games.perses.map.tiled.MapTileset
import games.perses.math.Matrix4
import games.perses.shader.ShaderProgram
import games.perses.shader.ShaderProgramMesh
import games.perses.shader.VertextAttributeInfo
import org.khronos.webgl.ArrayBufferView
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 14:52
 */
//language=GLSL
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

        gl_Position = u_projectionView * vec4(a_position + scaledBox.xy, 1.0, 1.0);
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
    val shaderProgramMesh: ShaderProgramMesh<TextureData> = ShaderProgramMesh(shaderProgram)
    val left = -width / 2f
    val right = width / 2f
    val bottom = -height / 2f
    val top = height / 2f

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

    fun queueTileDraw(x: Float, y: Float, tcLeft: Float, tcTop: Float, tcRight: Float, tcBottom: Float, scale: Float) {
        shaderProgramMesh.queue( x, y, left,  bottom,  tcLeft,  tcBottom, scale, 0f)
        shaderProgramMesh.queue( x, y, left,  top,     tcLeft,  tcTop,    scale, 0f)
        shaderProgramMesh.queue( x, y, right, top,     tcRight, tcTop,    scale, 0f)
        shaderProgramMesh.queue( x, y, right, top,     tcRight, tcTop,    scale, 0f)
        shaderProgramMesh.queue( x, y, right, bottom,  tcRight, tcBottom, scale, 0f)
        shaderProgramMesh.queue( x, y, left,  bottom,  tcLeft,  tcBottom, scale, 0f)

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

        shaderProgram = ShaderProgram(Game.gl(), WebGLRenderingContext.Companion.TRIANGLES, vertexShaderSource, fragmentShaderSource, vainfo, setter)
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
                //println("loaded texture $loaded/$startedLoading ${ready()}")
            }
            image.src = filename
        } else {
            throw IllegalStateException("Couldn't create webgl texture!")
        }
    }

    fun create(name: String, image: HTMLImageElement) {
        val gl = Game.gl()

        startedLoading++

        val webGlTexture = gl.createTexture()
        if (webGlTexture != null) {
            textureLoaded(webGlTexture, image)
            val texture = Texture(webGlTexture, shaderProgram, image.width, image.height)

            textures.put(name, texture)

            loaded++
        } else {
            throw IllegalStateException("Couldn't create webgl texture!")
        }
    }


    fun create(name: String, width: Int, height: Int, image: ArrayBufferView) {
        val gl = Game.gl()

        startedLoading++

        val webGlTexture = gl.createTexture()
        if (webGlTexture != null) {
            textureLoaded(webGlTexture, width, height, image)
            val texture = Texture(webGlTexture, shaderProgram, width, height)

            textures.put(name, texture)

            loaded++
        } else {
            throw IllegalStateException("Couldn't create webgl texture!")
        }
    }

    fun load(mapTileSet: MapTileset) {

    }

    private fun textureLoaded(texture: WebGLTexture, image: HTMLImageElement) {
        val gl = Game.gl()

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)
        gl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1) // second argument must be an int
        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image)
        setTextureParameters();
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null)
    }

    private fun textureLoaded(texture: WebGLTexture, width: Int, height: Int, image: ArrayBufferView) {
        val gl = Game.gl()

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)
        gl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1) // second argument must be an int
        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, width, height, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image)
        setTextureParameters();
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null)
    }

    private fun setTextureParameters() {
        val gl = Game.gl()

        if (Game.view.drawMode == DrawMode.NEAREST) {
            gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST)
            gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST)
        } else {
            gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.LINEAR)
            gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.LINEAR)
        }
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_T, WebGLRenderingContext.CLAMP_TO_EDGE)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_S, WebGLRenderingContext.CLAMP_TO_EDGE)
    }

    fun ready() = loaded == startedLoading

    fun has(name: String) = textures[name] != null
    fun get(name: String) = textures[name] ?: throw IllegalArgumentException("Texture with name $name is not loaded!")

    fun clear() {
        // todo: delete and unbind all textures...
    }

    fun render() {
        for ((key, value) in textures) {
            value.render()
        }
    }

}
