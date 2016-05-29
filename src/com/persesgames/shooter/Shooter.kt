package com.persesgames.shooter

import com.persesgames.game.Game
import com.persesgames.game.Screen
import com.persesgames.input.EmptyInputProcessor
import com.persesgames.input.KeyCode
import com.persesgames.input.Keys
import com.persesgames.sound.Music
import com.persesgames.sound.Sounds
import com.persesgames.sprite.Sprite
import com.persesgames.sprite.SpriteBatch
import com.persesgames.text.Texts
import com.persesgames.texture.Textures
import org.w3c.dom.HTMLAudioElement
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document

/**
 * Created by rnentjes on 19-4-16.
 */

class GameInputProcessor: EmptyInputProcessor() {

    override fun keyPressed(charCode: Int) {
        println("charCode: $charCode")
        if (charCode == 32) {
            Music.play("sounds/Explosion7.ogg", 0.5)
        }
    }

    override fun pointerClick(pointer: Int, x: Float, y: Float) {
        println("POINTER $pointer -> ($x, $y)")
    }
}

var music: HTMLAudioElement? = null
var showFPS: Boolean = true

class WelcomeScreen: Screen() {

    override fun loadResources() {
        println("loading resource!")
        music = Music.play("music/DST-TechnoBasic.ogg", 1.0, looping = true)

        Textures.loadSpriteSheet("images/data-0.json")

        Keys.setInputProcessor(GameInputProcessor())
    }

    override fun update(time: Float, delta: Float) {
    }

    override fun render() {

        if (showFPS) {
            Texts.drawText(20f, 100f, "Hello! FPS ${Game.fps}", font = "bold 72pt Arial")
        }
    }
}

class GameScreen: Screen() {
    var sprites = SpriteBatch()
    var x = 0f
    var y = 0f
    var sprite = Sprite("SHIP")

    override fun loadResources() {
        Textures.load("SHIP", "images/ship2.png")
        Sounds.load("EXPLOSION", "sounds/Explosion7.ogg")


        Keys.setInputProcessor(GameInputProcessor())
    }

    override fun update(time: Float, delta: Float) {
        val speed = 500f // pixels per second
        if (Keys.isDown(KeyCode.LEFT)) {
            x -= delta * speed;
        }

        if (Keys.isDown(KeyCode.RIGHT)) {
            x += delta * speed;
        }

        if (Keys.isDown(KeyCode.UP)) {
            y += delta * speed;
        }

        if (Keys.isDown(KeyCode.DOWN)) {
            y -= delta * speed;
        }

        if (Keys.isDown(KeyCode.SPACE)) {
            //Music.play("sounds/Explosion7.ogg", 0.5)
        }
    }

    override fun render() {

        for (index in 0..100) {
            val x = Math.random() * 2000f - 1000f
            val y = Math.random() * 2000f - 1000f

            sprites.draw(sprite, x.toFloat(), y.toFloat())
        }

        sprites.draw(sprite, x, y);

        sprites.render()
                Texts.drawText(150f, 400f, "Playing teh Game!", font = "bold 72pt Arial")

        if (showFPS) {
            Texts.drawText(20f, 100f, "Hello! FPS ${Game.fps}", font = "bold 72pt Arial")
        }
    }

}

fun main(args: Array<String>) {
    Game.view.setToWidth(2000f)

    Game.start(WelcomeScreen())
}

fun changeMusic(it: HTMLInputElement) {
    val mus = music

    if (mus != null) {
        if (it.checked) {
            mus.volume = 1.0
        } else {
            mus.volume = 0.0
        }
    }
}

fun showFPS(it: HTMLInputElement) {
    showFPS = it.checked
}

fun playGame() {
    document.getElementById("menu")?.setAttribute("style", "display: none;")

    Game.setScreen(GameScreen())
}
