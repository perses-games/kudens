package games.perses.kudens.game

import games.perses.kudens.texture.Textures
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.js.Date

/**
 * Created by rnentjes on 19-4-16.
 */

enum class DrawMode {
  LINEAR,
  NEAREST
}

class HTMLElements {
  var container: HTMLElement = document.createElement("div") as HTMLElement
  var webgl: WebGLRenderingContext
  var canvas2d: CanvasRenderingContext2D

  init {
    val webGlCanvas = document.createElement("canvas") as HTMLCanvasElement
    val canvas = document.createElement("canvas") as HTMLCanvasElement

    container.setAttribute("style", "position: absolute; left: 0px; top: 0px;")
    webGlCanvas.setAttribute("style", "position: absolute; left: 0px; top: 0px;")
    canvas.setAttribute("style", "position: absolute; left: 0px; top: 0px; z-index: 10; width: 1000px; height: 500px;")

    document.body!!.appendChild(container)
    container.appendChild(webGlCanvas)
    container.appendChild(canvas)

    val canvas2dcanvas = canvas.getContext("2d")

    var webglcanvas = webGlCanvas.getContext("webgl")
    if (webglcanvas == null) {
      console.log("webgl context not found, trying experimental-webgl.")
      webglcanvas = webGlCanvas.getContext("experimental-webgl")
    }

    if (webglcanvas != null) {
      webgl = webglcanvas as WebGLRenderingContext
    } else {
      console.log("webgl?", webglcanvas)
      window.alert("Your browser doesn't seem to support webgl!")

      throw IllegalStateException("Your browser doesn't seem to support webgl!")
    }

    if (canvas2dcanvas != null) {
      canvas2d = canvas2dcanvas as CanvasRenderingContext2D
    } else {
      console.log("canvas2d?", canvas2dcanvas)
      window.alert("Your browser doesn't seem to support 2d canvas!")

      throw IllegalStateException("Your browser doesn't seem to support webgl!")
    }
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
  var clearScreenEveryFrame = true

  var clearRed = 0f
  var clearGreen = 0f
  var clearBlue = 0f
  var clearAlpha = 1f

  var fps = 0
  var fpsCount = 0
  var fpsCountTime = 0f

  var borderLeft = 0
  var borderTop = 0

  var focus = true
  var pauseOnNoFocus = true

  init {
    window.onfocus = {
      //println("GOT FOCUS!")

      focus = true
      ""
    }
    window.onblur = {
      //println("LOST FOCUS!")

      focus = false
      ""
    }

  }

  fun gl() = html.webgl
  fun htmlCanvas() = html.canvas2d

  fun resize() {
    val canvas = gl().canvas

    // Check if the canvas is not the same size.
    val windowWidth = window.innerWidth
    val windowHeight = window.innerHeight

    if (view.lastWindowWidth != windowWidth ||
        view.lastWindowHeight != windowHeight) {
      view.lastWindowWidth = windowWidth
      view.lastWindowHeight = windowHeight
      view.windowWidth = windowWidth
      view.windowHeight = windowHeight

      view.updateView()

      val htmlCanvas = htmlCanvas().canvas

      // Make the canvas the same size
      canvas.width = view.width.toInt()
      canvas.height = view.height.toInt()

      htmlCanvas.width = view.width.toInt()
      htmlCanvas.height = view.height.toInt()

      gl().viewport(0, 0, view.width.toInt(), view.height.toInt())

      borderLeft = (windowWidth - view.windowWidth) / 2
      borderTop = (windowHeight - view.windowHeight) / 2

      canvas.setAttribute(
          "style",
          "position: absolute; left: ${borderLeft}px; top: ${borderTop}px; z-index: 5; " +
              "width: ${view.windowWidth}px; height: ${view.windowHeight}px;"
      )
      htmlCanvas.setAttribute(
          "style",
          "position: absolute; left: ${borderLeft}px; top: ${borderTop}px; z-index: 10; " +
              "width: ${view.windowWidth}px; height: ${view.windowHeight}px;"
      )
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
    currentScreen.unloadResources()

    currentScreen = screen

    screen.loadResources()
  }

  fun setClearColor(r: Float, g: Float, b: Float, a: Float) {
    clearRed = r
    clearGreen = g
    clearBlue = b
    clearAlpha = a
  }

  fun gameLoop() {
    try {
      if (!Textures.ready()) {
        gl().clearColor(1f, 1f, 1f, 1f)
        gl().clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
      } else {
        resize()

        if (!pause) {
          if (clearScreenEveryFrame) {
            htmlCanvas().clearRect(0.0, 0.0, view.width.toDouble(), view.height.toDouble())

            gl().clearColor(clearRed, clearGreen, clearBlue, clearAlpha)
            gl().clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
          }

          gl().enable(WebGLRenderingContext.BLEND)
          gl().blendFunc(
              WebGLRenderingContext.SRC_ALPHA,
              WebGLRenderingContext.ONE_MINUS_SRC_ALPHA
          ) //ONE_MINUS_DST_ALPHA);

          val time = Date().getTime()
          currentDelta = ((time - currentTime) / 1000f).toFloat()
          currentTime = time

          val timeInSeconds = (currentTime - start) / 1000f

          fpsCountTime += currentDelta
          fpsCount++
          while (fpsCountTime > 1f) {
            fps = fpsCount
            fpsCountTime -= 1f
            fpsCount = 0
          }

          if (focus || !pauseOnNoFocus) {
            currentScreen.update(timeInSeconds.toFloat(), currentDelta)
          }

          currentScreen.render()
        }
      }
    } catch (e: Exception) {
      println("Some error...")
      console.log(e)
    }

    window.requestAnimationFrame {
      gameLoop()
    }
  }

}
