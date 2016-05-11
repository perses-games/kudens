package com.persesgames.sprite

import com.persesgames.game.Game
import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.VertextAttributeInfo
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

class SpriteBatch {
    val program: ShaderProgram;
    val vainfo = arrayOf(
      VertextAttributeInfo("a_position", 2),
      VertextAttributeInfo("a_color", 3)
    )
    // TODO: replace with Float32Array when it supports [] or set
    private val spriteArray = Array(6, { 0f })

    init {
        program = ShaderProgram(Game.webgl, WebGLRenderingContext.TRIANGLES, vertexShaderSource, fragmentShaderSource, vainfo)
    }

    fun begin() {
        program.begin()
    }

    fun draw(sprite: Sprite, x: Float, y: Float) {
        spriteArray[0] = 1f

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
