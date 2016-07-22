package com.persesgames.sound

import org.w3c.dom.HTMLAudioElement
import java.util.*
import kotlin.browser.document

/**
 * User: rnentjes
 * Date: 18-5-16
 * Time: 12:34
 */

class Sound(val name:String, val url: String, val volume: Double = 0.75) {
    var audio: HTMLAudioElement

    init {
        println("CREATING: $name")
        audio = document.createElement("audio") as HTMLAudioElement


        audio.src = url
        audio.pause()
        audio.load()
        audio.volume = volume
    }

    fun play() {
        println("PLAYING: $name")
        audio.currentTime = 0.0
        audio.play()
    }

    fun pause() {
        audio.pause()
    }
}

object Sounds {
    val sounds: MutableMap<String, Sound> = HashMap()

    fun load(name: String, url: String, volume: Double = 0.75 ) {
        sounds.put(name, Sound(name, url, volume))
    }

    fun play(name: String, volume: Float = 0.75f) {
        val sound: Sound = sounds[name] ?: throw IllegalArgumentException("Sound '$name' not found, load it first!")

        sound.play()
    }

    fun pause(name: String) {
        val sound: Sound = sounds[name] ?: throw IllegalArgumentException("Sound '$name' not found, load it first!")

        sound.pause()
    }
}
