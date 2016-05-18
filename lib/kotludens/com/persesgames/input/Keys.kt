package com.persesgames.input

import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import java.util.*
import kotlin.browser.document
import kotlin.dom.on

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
}

interface InputProcessor {

    fun keyPressed(charCode: Int)

    fun keyDown(keyCode: Int)

    fun keyUp(keyCode: Int)

}

class DefaultProcessor: InputProcessor {
    override fun keyDown(keyCode: Int) {
    }

    override fun keyPressed(charCode: Int) {
    }

    override fun keyUp(keyCode: Int) {
    }
}

object Keys {

    private val keys: MutableMap<Int, Int> = HashMap();
    private var inputProcesser: InputProcessor = DefaultProcessor()

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

    fun isDown(keyCode: Int) = keys.containsKey(keyCode)

    fun isDown(keyCode: KeyCode) = keys.containsKey(keyCode.keyCode)

}
