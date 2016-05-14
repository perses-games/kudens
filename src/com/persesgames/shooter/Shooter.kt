package com.persesgames.shooter

import com.persesgames.game.Game
import com.persesgames.game.Screen
import com.persesgames.sprite.Sprite
import com.persesgames.sprite.SpriteBatch
import com.persesgames.texture.Textures
import org.khronos.webgl.WebGLRenderingContext

/**
 * Created by rnentjes on 19-4-16.
 */

class WelcomeScreen: Screen() {
    var sprites = SpriteBatch()
    var x = 100f
    var y = 100f
    var sprite = Sprite("SHIP")

    override fun loadResources() {
        Textures.load("SHIP", "images/ship2.png")
    }

    override fun update(time: Float) {
        x = 100f + Math.sin(time.toDouble()).toFloat() * 50f
        y = 100f + Math.cos(time.toDouble()).toFloat() * 50f
    }

    override fun render() {
        Game.gl().clearColor(0f, 1f, 1f, 1f)
        Game.gl().clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

        sprites.begin()
        sprites.draw(sprite, x, y);
        sprites.end();
    }

}

class GameScreen: Screen() {

    override fun update(time: Float) {
    }

    override fun render() {
    }

}

fun main(args: Array<String>) {
    Game.view.setToWidth(2000f);

    Game.start(WelcomeScreen())
}
