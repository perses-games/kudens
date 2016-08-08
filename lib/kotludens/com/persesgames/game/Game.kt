package com.persesgames.game

import com.persesgames.texture.Textures
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window

/**
 * Created by rnentjes on 19-4-16.
 */

enum class DrawMode {
    LINEAR,
    NEAREST
}

class HTMLElements {
    var container: HTMLElement
    var webgl: WebGLRenderingContext
    var canvas2d: CanvasRenderingContext2D

    init {
        container = document.createElement("div") as HTMLElement

        val webGlCanvas = document.createElement("canvas") as HTMLCanvasElement
        val canvas = document.createElement("canvas") as HTMLCanvasElement

        container.setAttribute("style", "position: absolute; left: 0px; top: 0px;")
        webGlCanvas.setAttribute("style", "position: absolute; left: 0px; top: 0px;" )
        canvas.setAttribute("style", "position: absolute; left: 0px; top: 0px; z-index: 10; width: 1000px; height: 500px;" )

        document.body!!.appendChild(container)
        container.appendChild(webGlCanvas)
        container.appendChild(canvas)

        webgl = webGlCanvas.getContext("webgl") as WebGLRenderingContext
        canvas2d = canvas.getContext("2d") as CanvasRenderingContext2D
    }
}

object Game {
    var started = false
    val view: View = View()
    val html: HTMLElements by lazy { HTMLElements() }
    var currentScreen: Screen = DefaultScreen()
    var start = Date().getTime()
    var currentTime = start
    var currentDelta = 0f
    var pause: Boolean = false

    var fps = 0
    var fpsCount = 0
    var fpsCountTime = 0f

    fun gl() = html.webgl

    fun resize() {
        val canvas = gl().canvas

        // Check if the canvas is not the same size.
        val windowWidth = window.innerWidth.toInt()
        val windowHeight = window.innerHeight.toInt()

        if (view.windowWidth != windowWidth ||
            view.windowHeight != windowHeight) {
            view.windowWidth = windowWidth
            view.windowHeight = windowHeight

            view.updateView()

            val textCanvas = html.canvas2d.canvas

            // Make the canvas the same size
            canvas.width = view.width.toInt()
            canvas.height = view.height.toInt()

            textCanvas.width = view.width.toInt()
            textCanvas.height = view.height.toInt()

            gl().viewport(0, 0, view.width.toInt(), view.height.toInt())

            val left = 0 //(windowWidth - view.windowWidth) / 2
            val top = 0 //(windowHeight - view.windowHeight) / 2

            canvas.setAttribute("style", "position: absolute; left: ${left}px; top: ${top}px; z-index: 5; width: ${view.windowWidth}px; height: ${view.windowHeight}px;" )
            textCanvas.setAttribute("style", "position: absolute; left: ${left}px; top: ${top}px; z-index: 10; width: ${view.windowWidth}px; height: ${view.windowHeight}px;" )
        }
    }

    fun start(startScreen: Screen) {
        if (started) {
            throw IllegalStateException("You can only start a game once!")
        }

        setScreen(startScreen)

        // start game loop
        started = true
        gameLoop()
    }

    fun setScreen(screen: Screen) {
        currentScreen.closeResources()

        currentScreen = screen

        currentScreen.loadResources()
    }

    fun gameLoop() {
        if (!Textures.ready()) {
            Game.gl().clearColor(1f, 1f, 1f, 1f)
            Game.gl().clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
        } else {
            resize()

            if (!pause) {
                html.canvas2d.clearRect(0.0, 0.0, view.width.toDouble(), view.height.toDouble());

                Game.gl().clearColor(0f, 0f, 0f, 1f)
                Game.gl().clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

                Game.gl().enable(WebGLRenderingContext.BLEND);
                Game.gl().blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE_MINUS_SRC_ALPHA); //ONE_MINUS_DST_ALPHA);

                val time = Date().getTime()
                currentDelta = (time - currentTime) / 1000f
                currentTime = time

                val timeInSeconds = (currentTime - start) / 1000f

                fpsCountTime += currentDelta
                fpsCount++
                while (fpsCountTime > 1f) {
                    fps = fpsCount
                    fpsCountTime -= 1f
                    fpsCount = 0
                }

                currentScreen.update(timeInSeconds, currentDelta);
                currentScreen.render()
            }
        }

        window.requestAnimationFrame {
            gameLoop()
        }
    }

}
