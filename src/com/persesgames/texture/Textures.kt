package com.persesgames.texture

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

object Textures {
    var textures = HashMap<String, WebGLTexture>();
    var startedLoading = 0
    var loaded = 0

    fun load(gl: WebGLRenderingContext, name: String, filename: String) {
        startedLoading++

        var webGlTexture = gl.createTexture()
        if (webGlTexture != null) {
            var image = document.createElement("img") as HTMLImageElement
            image.onload = {
                textureLoaded(gl, webGlTexture, image)
                textures.put(name, webGlTexture)
                loaded++
            }
            image.src = filename
        } else {
            println("Couldn't create webgl texture!")
        }

    }

    fun textureLoaded(gl: WebGLRenderingContext, texture: WebGLTexture, image: HTMLImageElement) {
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