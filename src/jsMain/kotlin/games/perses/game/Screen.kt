package games.perses.game

import org.khronos.webgl.WebGLRenderingContext

/**
 * Created by rnentjes on 19-4-16.
 */

abstract class Screen {

  open fun loadResources() {}

  @Deprecated(
      message = "replace with unloadResources",
      replaceWith = ReplaceWith("unloadResources()")
  )
  open fun closeResources() {
    unloadResources()
  }

  open fun unloadResources() {}

  abstract fun update(time: Float, delta: Float)

  abstract fun render()

}

class DefaultScreen : Screen() {
  override fun update(time: Float, delta: Float) {
  }

  override fun render() {
    // show loading  message?
    Game.gl().clearColor(1f, 1f, 0f, 1f)
    Game.gl().clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
  }
}
