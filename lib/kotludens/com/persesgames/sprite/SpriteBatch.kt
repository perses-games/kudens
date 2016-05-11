package com.persesgames.sprite

import com.persesgames.game.Game
import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.VertextAttributeInfo
import com.persesgames.texture.Textures
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext

/**
 * User: rnentjes
 * Date: 20-4-16
 * Time: 13:48
 */

class Sprite(val textureName: String) {

}

private val vertexShaderSource = """
    attribute vec2 a_position;
    attribute vec2 a_texCoord;

    uniform mat4 u_projectionView;

    varying vec2 v_textCoord;

    void main(void) {
        v_textCoord = a_texCoord;

        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);
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

class SpriteBatch {
    val program: ShaderProgram;
    val vainfo = arrayOf(
      VertextAttributeInfo("a_position", 2),
      VertextAttributeInfo("a_texCoord", 2)
    )
    // TODO: replace with Float32Array when it supports [] or set
    private val spriteArray = Array(6 * 4, { 0f })

    init {
        program = ShaderProgram(Game.gl(), WebGLRenderingContext.TRIANGLES, vertexShaderSource, fragmentShaderSource, vainfo)
    }

    fun begin() {
        program.begin()
    }

    fun draw(sprite: Sprite, x: Float, y: Float) {
        spriteArray[0] = 1f

        Textures.get(sprite.textureName).bind()

        program.queueVertices(Float32Array(spriteArray))
    }

    fun flush() {
        program.flush()
    }

    fun end() {
        flush()
        program.end()
    }
}
