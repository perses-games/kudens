package games.perses.game

import games.perses.math.Matrix4

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
        //println("Requesting fullscreen")
        js("""
        if (document.webkitFullscreenElement) {
          document.webkitCancelFullScreen();
        } else {
          document.documentElement.webkitRequestFullScreen();
        }
        """)
        //if (document.fullscreenEnabled) {
        //  println("fullscreen Enabled")
        //Game.html.container.requestFullscreen()
        //document.documentElement?.requestFullscreen()
        //}
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
            ViewType.WIDTH -> {
                result = (screenX / windowWidth * width) - width / 2
            }
            ViewType.HEIGHT -> {
                result = (screenX / windowWidth * width) - width / 2
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
            ViewType.WIDTH -> {
                result = -((screenY / windowHeight * height) - height / 2)
            }
            ViewType.HEIGHT -> {
                result = -((screenY / windowHeight * height) - height / 2)
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
            ViewType.WIDTH -> {
                result = (windowWidth / width * normalizedX)
            }
            ViewType.HEIGHT -> {
                result = (windowWidth / width * normalizedX)
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
            ViewType.WIDTH -> {
                result = windowHeight - (windowHeight / height * normalizedY)
            }
            ViewType.HEIGHT -> {
                result = windowHeight - (windowHeight / height * normalizedY)
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