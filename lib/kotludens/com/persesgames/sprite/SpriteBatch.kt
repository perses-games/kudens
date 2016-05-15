package com.persesgames.sprite

import com.persesgames.texture.Texture
import com.persesgames.texture.Textures

/**
 * User: rnentjes
 * Date: 20-4-16
 * Time: 13:48
 */

class Sprite(val textureName: String) {
    val texture: Texture by lazy { Textures.get(textureName) }
}

class SpriteBatch {

    fun draw(sprite: Sprite, x: Float, y: Float) {
        sprite.texture.queueDraw(x, y)
    }

    fun render() {
        Textures.render()
    }

}
