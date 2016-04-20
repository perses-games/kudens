package com.persesgames.shooter

import com.persesgames.game.Game
import com.persesgames.game.Screen
import com.persesgames.sprite.SpriteBatch
import com.persesgames.texture.Textures
import org.khronos.webgl.WebGLRenderingContext

/**
 * Created by rnentjes on 19-4-16.
 */

class WelcomeScreen: Screen() {
    var sprites = SpriteBatch()

    override fun loadResources() {
        Textures.load("SHIP", "images/ship2.png")
    }

    override fun update(time: Float) {
    }

    override fun render() {
        Game.webgl.clearColor(0f, 1f, 1f, 1f)
        Game.webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
    }

}

class GameScreen: Screen() {

    override fun update(time: Float) {
    }

    override fun render() {
    }

}

fun main(args: Array<String>) {
    Game.start(WelcomeScreen())
}
