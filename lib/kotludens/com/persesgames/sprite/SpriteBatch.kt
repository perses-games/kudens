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

class SpriteBatch {

    fun draw(sprite: Sprite, x: Float, y: Float) {
        var texture = Textures.get(sprite.textureName)

        texture.queueDraw(x, y)

    }

    fun render() {
        Textures.render()
    }

}
