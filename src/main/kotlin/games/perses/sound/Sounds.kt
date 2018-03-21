package games.perses.sound

import org.w3c.dom.HTMLAudioElement
import kotlin.browser.document

/**
 * User: rnentjes
 * Date: 18-5-16
 * Time: 12:34
 */

class Sound(
    val name: String,
    val url: String,
    val defaultVolume: Double = 0.75,
    val numberOfChannels: Int
) {
  var channels: Array<HTMLAudioElement?> =
      Array(numberOfChannels, { document.createElement("audio") as HTMLAudioElement })
  var nextChannel: Int = 0

  init {
    //println("CREATING: $name")

    for (audio in channels) {
      if (audio != null) {
        audio.src = url
        audio.pause()
        audio.load()
        audio.volume = defaultVolume
      }
    }
  }

  fun play(volume: Double = defaultVolume) {
    //println("PLAYING: $name - $nextChannel")
    channels[nextChannel]?.volume = volume
    channels[nextChannel]?.currentTime = 0.0
    channels[nextChannel]?.play()

    nextChannel = (nextChannel + 1) % channels.size
  }

  fun pause() {
    for (audio in channels) {
      audio?.pause()
    }
  }

  fun dispose() {
    for (audio in channels) {
      audio?.dispose()
    }
  }
}

object Sounds {
  val sounds: MutableMap<String, Sound> = HashMap()

  fun load(name: String, url: String, volume: Double = 0.75, channels: Int = 1) {
    sounds.put(name, Sound(name, url, volume, channels))
  }

  fun play(name: String, volume: Double? = null) {
    val sound: Sound = sounds[name] ?: throw IllegalArgumentException("Sound '$name' not found, load it first!")

    if (volume != null) {
      sound.play(volume)
    } else {
      sound.play()
    }
  }

  fun pause(name: String) {
    val sound: Sound = sounds[name] ?: throw IllegalArgumentException("Sound '$name' not found, load it first!")

    sound.pause()
  }

  fun dispose(name: String) {
    val sound: Sound? = sounds[name]

    if (sound != null) {
      sounds.remove(name)
      sound.dispose()
    }
  }

  fun disposeAll() {
    for (sound in sounds.values) {
      sound.dispose()
    }

    sounds.clear()
  }
}
