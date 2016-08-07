package com.persesgames.shooter

import com.persesgames.game.DrawMode
import com.persesgames.game.Game
import com.persesgames.game.Screen
import com.persesgames.input.EmptyInputProcessor
import com.persesgames.input.KeyCode
import com.persesgames.input.Keys
import com.persesgames.map.tiled.TiledMap
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

class GameInputProcessor : EmptyInputProcessor() {

    override fun keyPressed(charCode: Int) {
        println("charCode: $charCode")
        if (charCode == 32) {
            Sounds.play("EXPLOSION", 0.5f)
        } else if (charCode == 'x'.toInt()) {
            Sounds.play("DROP", 0.75f)
        }
    }

    override fun pointerClick(pointer: Int, x: Float, y: Float) {
        println("POINTER $pointer -> ($x, $y)")
    }
}

var music: HTMLAudioElement? = null
var showFPS: Boolean = true

class WelcomeScreen : Screen() {

    override fun loadResources() {
        println("loading resource!")

        //music = Music.play("music/DST-TechnoBasic.mp3", 1.0, looping = true)

        Textures.loadSpriteSheet("images/data-0.json")

        Keys.setInputProcessor(GameInputProcessor())
    }

    override fun update(time: Float, delta: Float) { }

    override fun render() {

        if (showFPS) {
            Texts.drawText(20f, 100f, "Hello! FPS ${Game.fps}", font = "bold 72pt Arial")
        }
    }
}

class GameScreen : Screen() {
    val map = TiledMap("maps", "level_1_01.json")

    var sprites = SpriteBatch()
    var x = -640f + 64f
    var y = 15500f
    var sprite = Sprite("SHIP")
    var numberOfSprites: Int = 5000
    var time: Float = 0f

    override fun loadResources() {
        Textures.load("SHIP", "images/ship2.png")

        Sounds.load("EXPLOSION", "sounds/Explosion7.ogg", channels = 2)
        Sounds.load("DROP", "sounds/Bomb_Drop.ogg", channels = 4)

        music = Music.play("music/DST-TechnoBasic.mp3", 0.5, looping = true)

        Keys.setInputProcessor(GameInputProcessor())

        println("width: ${map.data.width}")
        println("height: ${map.data.height}")
        println("layers: ${map.data.layers?.size}")

        val layers = map.data.layers
        if (layers != null) {
            println("layer0: ${layers[0].name}")
        }
        val tilesets = map.data.tilesets
        if (tilesets != null) {
            println("tilesets ${tilesets.size}")
            println("tileset0: ${tilesets[0].name}")
        }
    }

    override fun update(time: Float, delta: Float) {
        this.time = time
        val speed = 500f // units per second

        if (Keys.isDown(KeyCode.LEFT)) {
            x -= delta * speed
            println("x=$x")
        }

        if (Keys.isDown(KeyCode.RIGHT)) {
            x += delta * speed
            println("x=$x")
        }

        if (Keys.isDown(KeyCode.UP)) {
            y += delta * speed
            println("y=$y")
        }

        if (Keys.isDown(KeyCode.DOWN)) {
            y -= delta * speed
            println("y=$y")
        }

        if (Keys.isDown(KeyCode.MINUS)) {
            if (numberOfSprites > 25) {
                numberOfSprites = (numberOfSprites * 0.9f).toInt()
            }
        }

        if (Keys.isDown(KeyCode.PLUS)) {
            numberOfSprites = (numberOfSprites * 1.1f).toInt()
        }
    }

    override fun render() {
        var r = 0f
        var d = 0f
        var x = 0f
        var y = 0f

        map.drawLayer(1, this.x, this.y)
        map.drawLayer(2, this.x, this.y)

        val time = this.time / 10f
        for (index in 0..numberOfSprites) {
            r = index * 0.05f
            d = index * 2.13f
            x = (Math.sin((time + d).toDouble()) * r).toFloat()
            y = (Math.cos((time + d).toDouble()) * r).toFloat()

            sprites.draw(sprite, x.toFloat(), y.toFloat(), scale = 0.4f + Math.sin(time.toDouble() + r).toFloat(), rotation = r * 10f)
        }

        sprites.render()

        Texts.drawText(20f, 150f, "Drawing $numberOfSprites sprites per frame.")

        if (showFPS) {
            Texts.drawText(20f, 100f, "FPS ${Game.fps}", font = "bold 72pt Arial", fillStyle = "red")
            Texts.drawText(15f, -20f, "Music by DST", font = "bold 28pt Arial", fillStyle = "green")
        }
    }

}

fun main(args: Array<String>) {
    Game.view.setToWidth(2000f)
    Game.view.drawMode = DrawMode.LINEAR

    Game.start(WelcomeScreen())
}

fun changeMusic(it: HTMLInputElement) {
    val mus = music

    if (mus != null) {
        if (it.checked) {
            mus.volume = 0.5
        } else {
            mus.volume = 0.0
        }
    }
}

fun showFPS(it: HTMLInputElement) {
    showFPS = it.checked
}

fun pause(it: HTMLInputElement) {
    Game.pause = it.checked
}

fun playGame() {
    document.getElementById("menu")?.setAttribute("style", "display: none;")

    Game.setScreen(GameScreen())
}

fun fullscreen() {
    Game.view.requestFullscreen()
}
