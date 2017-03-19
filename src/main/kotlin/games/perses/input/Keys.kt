package games.perses.input

import games.perses.game.Game
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
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

    private val keys: MutableMap<Int, Double> = HashMap()
    private var inputProcesser: InputProcessor = EmptyInputProcessor()

    init {
        val body = document.body
        if (body != null) {
            body.onkeydown = {
                keyDown(it)
            }

            body.onkeyup = {
                keyUp(it)
            }

            body.onkeypress = {
                keyPress(it)
            }

            body.onclick = {
                mouseClick(it)
            }

            body.onmousedown = {
                mouseMove(it)
            }

            body.onmouseup = {
                mouseMove(it)
            }

            body.onmousemove = {
                mouseMove(it)
            }
        } else {
            console.log("Can't register key events, document.body is null!?")
        }
    }

    fun setInputProcessor(processor: InputProcessor) {
        this.inputProcesser = processor
    }

    private fun keyDown(key: Event) {
        if (key is KeyboardEvent) {
            keys.put(key.keyCode, Date().getTime())

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

    fun wasPressed(keyCode: Int, delta: Double): Boolean {
        val time = keys[keyCode]

        return (time != null && time > (Date().getTime() - delta))
    }

}
