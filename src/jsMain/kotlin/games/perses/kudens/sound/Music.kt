package games.perses.kudens.sound

import org.w3c.dom.HTMLAudioElement
import kotlinx.browser.document

/**
 * User: rnentjes
 * Date: 18-5-16
 * Time: 13:02
 */

fun HTMLAudioElement.dispose() {
  this.pause()
  this.parentNode?.removeChild(this)
}

object Music {
  val playing: MutableSet<HTMLAudioElement> = HashSet()

  fun load(url: String): HTMLAudioElement {
    val audio = document.createElement("audio") as HTMLAudioElement

    audio.src = url

    return audio
  }

  fun play(url: String, volume: Double = 0.75, looping: Boolean = false): HTMLAudioElement {
    val audio = document.createElement("audio") as HTMLAudioElement

    audio.autoplay = true

    audio.onended = {
      if (looping) {
        audio.currentTime = 0.0
        audio.play()
      } else {
        //println("REMOVING: $audio")
        audio.parentNode?.removeChild(audio)
        playing.remove(audio)
      }

    }

    audio.src = url
    audio.volume = volume

    document.body?.appendChild(audio)

    return audio
  }

  fun stopAll() {

  }
}
