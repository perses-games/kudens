package games.perses.kudens.sprite

import games.perses.kudens.texture.Texture
import games.perses.kudens.texture.Textures

/**
 * User: rnentjes
 * Date: 20-4-16
 * Time: 13:48
 */

class Sprite(val textureName: String) {
  val texture: Texture by lazy { Textures[textureName] }
}

class SpriteBatch {

  fun draw(sprite: Sprite, x: Float, y: Float, scale: Float = 1f, rotation: Float = 0f) {
    sprite.texture.queueDraw(x, y, scale, rotation)
  }

  fun render() {
    Textures.render()
  }

}
