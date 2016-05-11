package com.persesgames.game

import com.persesgames.math.Matrix4
import com.persesgames.texture.Textures
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

    override fun render() {
        // show loading  message?
        Game.webgl.clearColor(1f, 1f, 0f, 1f)
        Game.webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
    }
}

enum class ViewType {
    PROJECTION,
    WIDTH,
    HEIGHT,
    ABSOLUTE
}

class View(
  var width: Float = 1024f,
  var height: Float = 1024f,
  var angle: Float = 60f,
  var minAspectRatio: Float = 1f,
  var maxAspectRatio: Float = 1f,
  var viewType: ViewType = ViewType.WIDTH) {
    var vMatrix = Matrix4()
    var aspectRatio = 1f

    init {
        updateView()
    }

    fun updateView() {
        when(viewType) {
            ViewType.ABSOLUTE -> {
                vMatrix.setOrthographicProjection(0f, 0f, width, height, 0.1f, 10f)
            }
            ViewType.WIDTH -> {

            }
            ViewType.HEIGHT -> {

            }
            ViewType.PROJECTION -> {
                vMatrix.setPerspectiveProjection(angle, aspectRatio, 1f, 1f);
            }
            else -> {
                throw IllegalStateException("ViewType $viewType not implemented!")
            }
        }
    }

    fun setToWidth(fl: Float) {
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

object Game {
    var started = false
    val view: View = View()
    val webgl: WebGLRenderingContext by lazy {
        var canvas = document.createElement("canvas") as HTMLCanvasElement
        document.body!!.appendChild(canvas)
        canvas.getContext("webgl") as WebGLRenderingContext
    }

    var currentScreen: Screen = DefaultScreen()
    var start = Date().getTime()
    var currentTime = start
    var currentDelta = 0f

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
            Game.webgl.clearColor(1f, 0f, 0f, 1f)
            Game.webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
        } else {
            resize();

            var time = Date().getTime()
            currentDelta = (currentTime - time) / 1000f
            currentTime = time

            currentScreen.update(currentDelta);
            currentScreen.render();
        }

        window.requestAnimationFrame {
            gameLoop()
        }
    }


}
