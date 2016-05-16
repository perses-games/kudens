package com.persesgames.shooter

import com.persesgames.game.Game
import com.persesgames.game.Screen
import com.persesgames.sprite.Sprite
import com.persesgames.sprite.SpriteBatch
import com.persesgames.text.Texts
import com.persesgames.texture.Textures
import org.khronos.webgl.WebGLRenderingContext

/**
 * Created by rnentjes on 19-4-16.
 */

class WelcomeScreen: Screen() {
    var sprites = SpriteBatch()
    var x = 1f
    var y = 1f
    var sprite = Sprite("SHIP")
    val random = Math.random()

    override fun loadResources() {
        Textures.load("SHIP", "images/ship2.png")
    }

    override fun update(time: Float, delta: Float) {
        x = Math.sin(time.toDouble()).toFloat() * 150f
        y = Math.cos(time.toDouble()).toFloat() * 150f
    }

    override fun render() {
        for (index in 0..2500) {
            val x = Math.random() * 1000f - 500f
            val y = Math.random() * 1000f - 500f

            sprites.draw(sprite, x.toFloat(), y.toFloat());
        }

        sprites.draw(sprite, x, y);
        sprites.draw(sprite, -x, y);
        sprites.draw(sprite, x, -y);
        sprites.draw(sprite, -x, -y);

        sprites.render()

        Texts.drawText(10f, 40f, "Hello! FPS ${Game.fps}", font = "bold 36pt Arial")
    }
}

class GameScreen: Screen() {

    override fun update(time: Float, delta: Float) {
    }

    override fun render() {
    }

}

fun main(args: Array<String>) {
    Game.view.setToWidth(1000f);

    Game.start(WelcomeScreen())
}
