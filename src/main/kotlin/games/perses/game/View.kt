package games.perses.game

import games.perses.math.Matrix4
import kotlin.browser.document

enum class ViewType {
  PROJECTION,
  WIDTH,
  HEIGHT,
  ABSOLUTE
}

class View(
    var lastWindowWidth: Int = 2000,
    var lastWindowHeight: Int = 1000,
    var windowWidth: Int = 2000,
    var windowHeight: Int = 1000,
    var width: Float = 1024f,
    var height: Float = 1024f,
    var angle: Float = 60f,
    var near: Float = -0.1f,
    var far: Float = -100f,
    var minAspectRatio: Float = 1f,
    var maxAspectRatio: Float = 1f,
    var leftOffset: Int = 0,
    var bottomOffset: Int = 0,
    var viewType: ViewType = ViewType.WIDTH,
    var drawMode: DrawMode = DrawMode.LINEAR) {
  var vMatrix = Matrix4()
  var aspectRatio = 1f

  init {
    updateView()
  }

  fun requestFullscreen() {
    val element = document.body
    //language=javascript
    js("""
             if(element.requestFullscreen) {
                element.requestFullscreen();
              } else if(element.mozRequestFullScreen) {
                element.mozRequestFullScreen();
              } else if(element.webkitRequestFullscreen) {
                element.webkitRequestFullscreen();
              } else if(element.msRequestFullscreen) {
                element.msRequestFullscreen();
              }
        """)
  }

  fun exitFullscreen() {
    js("""
              if(document.exitFullscreen) {
                document.exitFullscreen();
              } else if(document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
              } else if(document.webkitExitFullscreen) {
                document.webkitExitFullscreen();
              }
        """)
  }

  fun switchFullscreen() {
    if (isFullscreen()) {
      exitFullscreen()
    } else {
      requestFullscreen()
    }
  }

  fun isFullscreen(): Boolean {
    val fse = js("document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement");

    return fse != undefined
  }

  fun updateView() {
    aspectRatio = windowWidth / windowHeight.toFloat()

    if (aspectRatio < minAspectRatio) {
      aspectRatio = minAspectRatio

      windowHeight = (windowWidth / aspectRatio).toInt()
    }

    if (aspectRatio > maxAspectRatio) {
      aspectRatio = maxAspectRatio

      windowWidth = (windowHeight * aspectRatio).toInt()
    }

    when (viewType) {
      ViewType.ABSOLUTE -> {
        vMatrix.setOrthographicProjection(0f, width, 0f, height, near, far)
      }
      ViewType.WIDTH -> {
        height = width / aspectRatio

        vMatrix.setOrthographicProjection(0f, width, 0f, height, near, far)
      }
      ViewType.HEIGHT -> {
        width = height * aspectRatio

        vMatrix.setOrthographicProjection(0f, width, 0f, height, near, far)
      }
      ViewType.PROJECTION -> {
        vMatrix.setPerspectiveProjection(angle, aspectRatio, near, far)
      }
      else -> {
        throw IllegalStateException("ViewType $viewType not implemented!")
      }
    }

    //println("width: $width, height: $height")
  }

  fun screenToGameCoordX(screenX: Float): Float {
    var result = screenX

    when (viewType) {
      ViewType.ABSOLUTE -> {
        // nop
      }
      ViewType.WIDTH, ViewType.HEIGHT -> {
        result = ((screenX - Game.borderLeft) * width / windowWidth)
      }
      ViewType.PROJECTION -> {
        // uhm
      }
      else -> {
        throw IllegalStateException("ViewType $viewType not implemented!")
      }
    }

    return result
  }

  fun screenToGameCoordY(screenY: Float): Float {
    var result = screenY

    when (viewType) {
      ViewType.ABSOLUTE -> {
        // nop
      }
      ViewType.WIDTH, ViewType.HEIGHT -> {
        result = height - ((screenY - Game.borderTop) * height / windowHeight)
      }
      ViewType.PROJECTION -> {
        // uhm
      }
      else -> {
        throw IllegalStateException("ViewType $viewType not implemented!")
      }
    }

    return result
  }

  fun gameToScreenCoordX(gameX: Float): Float {
    var result = gameX
    val normalizedX = gameX + (width / 2)

    when (viewType) {
      ViewType.ABSOLUTE -> {
        // nop
      }
      ViewType.WIDTH, ViewType.HEIGHT -> {
        result = (gameX / width * windowWidth) + Game.borderLeft
      }
      ViewType.PROJECTION -> {
        // uhm
      }
      else -> {
        throw IllegalStateException("ViewType $viewType not implemented!")
      }
    }

    return result
  }

  fun gameToScreenCoordY(gameY: Float): Float {
    var result = gameY
    val normalizedY = gameY + (height / 2)

    when (viewType) {
      ViewType.ABSOLUTE -> {
        // nop
      }
      ViewType.WIDTH, ViewType.HEIGHT -> {
        result = height - (gameY / height * windowHeight) + Game.borderTop
      }
      ViewType.PROJECTION -> {
        // uhm
      }
      else -> {
        throw IllegalStateException("ViewType $viewType not implemented!")
      }
    }

    return result
  }

  fun setToWidth(width: Float) {
    this.width = width
    this.viewType = ViewType.WIDTH

    updateView()
  }

  fun setToHeight(height: Float) {
    this.height = height
    this.viewType = ViewType.HEIGHT

    updateView()
  }

  fun setProjection(angle: Float) {
    this.angle = angle
    this.viewType = ViewType.PROJECTION

    updateView()
  }

  fun setNear(near: Float) {
    this.near = near

    updateView()
  }

  fun setFar(far: Float) {
    this.far = far

    updateView()
  }
}