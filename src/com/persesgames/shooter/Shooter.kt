package com.persesgames.shooter

import com.persesgames.game.Game
import com.persesgames.game.Screen
import com.persesgames.input.EmptyInputProcessor
import com.persesgames.input.InputProcessor
import com.persesgames.input.KeyCode
import com.persesgames.input.Keys
import com.persesgames.sound.Music
import com.persesgames.sound.Sounds
import com.persesgames.sprite.Sprite
import com.persesgames.sprite.SpriteBatch
import com.persesgames.text.Texts
import com.persesgames.texture.Textures

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

class WelcomeScreen: Screen() {
    var sprites = SpriteBatch()
    var x = 0f
    var y = 0f
    var sprite = Sprite("SHIP")

    override fun loadResources() {
        Textures.load("SHIP", "images/ship2.png")
        Sounds.load("EXPLOSION", "sounds/Explosion7.ogg")

        Music.play("music/DST-TechnoBasic.mp3", 1.0, looping = true)

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
    }

    override fun render() {
        for (index in 0..100) {
            val x = Math.random() * 2000f - 1000f
            val y = Math.random() * 2000f - 1000f

            sprites.draw(sprite, x.toFloat(), y.toFloat());
        }

        sprites.draw(sprite, x, y);

        sprites.render()

        Texts.drawText(20f, 80f, "Hello! FPS ${Game.fps}", font = "bold 72pt Arial")
    }
}

class GameScreen: Screen() {

    override fun update(time: Float, delta: Float) {
    }

    override fun render() {
    }

}

fun main(args: Array<String>) {
    Game.view.setToWidth(2000f);

    Game.start(WelcomeScreen())
}
