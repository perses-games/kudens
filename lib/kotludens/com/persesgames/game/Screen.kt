package com.persesgames.game

import org.khronos.webgl.WebGLRenderingContext

/**
 * Created by rnentjes on 19-4-16.
 */
abstract class Screen {

    open fun loadResources() {

    }

    open fun closeResources() {

    }

    abstract fun update(time: Float)

    abstract fun render()

}
