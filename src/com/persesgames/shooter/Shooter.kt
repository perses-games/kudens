package com.persesgames.shooter

import com.persesgames.game.Game
import com.persesgames.game.Screen
import org.khronos.webgl.WebGLRenderingContext

/**
 * Created by rnentjes on 19-4-16.
 */

class WelcomeScreen: Screen() {

    override fun update(time: Float) {
    }

    override fun render(webgl: WebGLRenderingContext) {
    }

}

class GameScreen: Screen() {

    override fun update(time: Float) {
    }

    override fun render(webgl: WebGLRenderingContext) {
    }

}

fun main(args: Array<String>) {
    var game = Game(WelcomeScreen())

    game.start()
}