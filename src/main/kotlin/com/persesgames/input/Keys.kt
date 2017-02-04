package com.persesgames.input

import com.persesgames.game.Game
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.dom.on
import kotlin.js.Date

/**
 * User: rnentjes
 * Date: 18-5-16
 * Time: 12:18
 */

enum class KeyCode(val keyCode: Int) {
    LEFT(37),
    UP(38),
    DOWN(40),
    RIGHT(39),
    SPACE(32),
    MINUS(109),
    PLUS(107),
}

interface InputProcessor {

    fun keyPressed(charCode: Int)

    fun keyDown(keyCode: Int)

    fun keyUp(keyCode: Int)

    fun pointerClick(pointer: Int, x: Float, y: Float)

}

open class EmptyInputProcessor : InputProcessor {

    override fun pointerClick(pointer: Int, x: Float, y: Float) { }

    override fun keyDown(keyCode: Int) { }

    override fun keyPressed(charCode: Int) { }

    override fun keyUp(keyCode: Int) { }
}

object Keys {

    private val keys: MutableMap<Int, Int> = HashMap()
    private var inputProcesser: InputProcessor = EmptyInputProcessor()

    init {
        val body = document.body
        if (body != null) {
            body.on("keydown", true) {
                Keys.keyDown(it)
            }

            body.on("keyup", true) {
                Keys.keyUp(it)
            }

            body.on("keypress", true) {
                Keys.keyPress(it)
            }

            body.on("click", true) {
                Keys.mouseClick(it)
            }

            body.on("mousedown", true) {
                Keys.mouseMove(it)
            }

            body.on("mouseup", true) {
                Keys.mouseMove(it)
            }

            body.on("mousemove", true) {
                Keys.mouseMove(it)
            }
        }
    }

    fun setInputProcessor(processor: InputProcessor) {
        this.inputProcesser = processor
    }

    private fun keyDown(key: Event) {
        if (key is KeyboardEvent) {
            keys.put(key.keyCode, Date().getTime().toInt())

            inputProcesser.keyDown(key.keyCode)
        }
    }

    private fun keyUp(key: Event) {
        if (key is KeyboardEvent) {
            inputProcesser.keyUp(key.keyCode)

            keys.remove(key.keyCode)
        }
    }

    private fun keyPress(key: Event) {
        if (key is KeyboardEvent) {
            inputProcesser.keyPressed(key.charCode)
        }
    }

    private fun mouseClick(event: Event) {
        if (event is MouseEvent) {
            val vx: Float = Game.view.screenToGameCoordX(event.clientX.toFloat())
            val vy: Float = Game.view.screenToGameCoordY(event.clientY.toFloat())

            inputProcesser.pointerClick(event.button.toInt(), vx, vy)
        }
    }

    private fun mouseMove(event: Event) {
        if (event is MouseEvent) {
            val vx: Float = Game.view.screenToGameCoordX(event.clientX.toFloat())
            val vy: Float = Game.view.screenToGameCoordY(event.clientY.toFloat())


        }
    }

    fun isDown(keyCode: Int) = keys.containsKey(keyCode)

    fun isDown(keyCode: KeyCode) = keys.containsKey(keyCode.keyCode)

}
