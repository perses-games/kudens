package com.persesgames.texture

import com.persesgames.game.Game
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.HTMLImageElement
import java.util.*
import kotlin.browser.document

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 14:52
 */

class Texture(val glTexture: WebGLTexture) {

}

object Textures {
    var textures = HashMap<String, WebGLTexture>();
    var startedLoading = 0
    var loaded = 0

    fun loadSpriteSheet(name: String, filename: String) {

    }

    fun load(name: String, filename: String) {
        var gl = Game.webgl

        startedLoading++

        var webGlTexture = gl.createTexture()
        if (webGlTexture != null) {
            var image = document.createElement("img") as HTMLImageElement
            image.onload = {
                textureLoaded(webGlTexture, image)
                textures.put(name, webGlTexture)
                loaded++
                println("loaded texture $loaded/$startedLoading ${ready()}")
            }
            image.src = filename
        } else {
            throw IllegalStateException("Couldn't create webgl texture!")
        }
    }

    fun textureLoaded(texture: WebGLTexture, image: HTMLImageElement) {
        var gl = Game.webgl

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
        gl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1); // second argument must be an int
        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image);
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST);
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST);
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
    }

    fun ready() = loaded == startedLoading

    fun get(name: String) = textures.get(name) ?: throw IllegalArgumentException("Texture with name $name is not loaded!")

    fun clear() {
        // delete and unbind all textures...
    }

}
