package com.persesgames.game

import com.persesgames.game
import com.persesgames.texture.Textures
import com.persesgames.time
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

/**
 * Created by rnentjes on 19-4-16.
 */

class DefaultScreen: Screen() {
    override fun update(time: Float) {
    }

    override fun render(webgl: WebGLRenderingContext) {
        // show loading  message?
    }
}

open class Game(var currentScreen: Screen) {
    val webgl: WebGLRenderingContext
    var start = Date().getTime()
    var currentTime = start
    var currentDelta = 0f

    init {
        var canvas = document.createElement("canvas") as HTMLCanvasElement
        document.body!!.appendChild(canvas)
        webgl = canvas.getContext("webgl") as WebGLRenderingContext

        resize()

        currentScreen.loadResources()
    }

    fun resize() {
        var canvas = webgl.canvas
        // Check if the canvas is not the same size.
        if (canvas.width != window.innerWidth.toInt() ||
          canvas.height != window.innerHeight.toInt()) {

            // Make the canvas the same size
            canvas.width = window.innerWidth.toInt()
            canvas.height = window.innerHeight.toInt()

            webgl.viewport(0, 0, canvas.width, canvas.height)
        }
    }

    fun start() {
        // start game loop
        gameLoop()
    }

    open fun setScreen(screen: Screen) {
        currentScreen.closeResources()

        currentScreen = screen

        currentScreen.loadResources()
    }

    fun gameLoop() {
        if (!Textures.ready()) {
            return;
        }

        var time = Date().getTime()
        currentDelta = (currentTime - time) / 1000f
        currentTime = time

        currentScreen.update(currentDelta);
        currentScreen.render(webgl);

        var testInstance = game
        if (testInstance != null) {
            time = Date().getTime()
            testInstance.update((time - com.persesgames.start) / 1000.0)
            testInstance.render()
        }

        window.requestAnimationFrame {
            gameLoop()
        }
    }


}
