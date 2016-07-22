package com.persesgames.map.tiled

import com.persesgames.texture.Textures
import org.w3c.xhr.XMLHttpRequest
import java.util.*

/**
 * Created by rnentjes on 22-7-16.
 */

fun getUrlAsString(url: String): String {
    val req = XMLHttpRequest()

    req.open("GET", url, false)
    req.send(null)

    return req.responseText
}

fun drawLayer(layer: MapLayer) {

}

class MapData {
    var version: Int = 1
    var properties: MutableMap<String, String> = HashMap()
    var layers: Array<MapLayer>? = null
    var tilesets: Array<MapTileset>? = null

    var height: Int = 0
    var width: Int = 0

    var nextobjectid: Int = 0
    var orientation: String =  "orthogonal"
    var renderorder: String =  "right-down"
    var tileheight: Int = 0
    var tilewidth: Int = 0
}

class MapLayer {
    var properties: MutableMap<String, String> = HashMap()

    var data: Array<Int>? = null
    var encoding: String = ""
    var x: Int = 0
    var y: Int = 0
    var width: Int = 0
    var height: Int = 0
    var name: String = ""
    var opacity: Float = 1f
    var type: String = ""
    var visible: Boolean = true
}

class MapTileset {
    var properties: MutableMap<String, String> = HashMap()

    var firstgid: Int = 0
    var image: String = ""
    var imageheight: Int = 0
    var imagewidth: Int = 0
    var margin: Int = 0
    var name: String = ""
    var spacing: Int = 0
    var tilecount: Int = 0
    var tileheight: Int = 0
    var tilewidth: Int = 0
    var tileproperties: MutableMap<String, MutableMap<String, String>> = HashMap()
}

class TiledMap(dir: String = "", url: String) {
    val properties: Map<String, String> = HashMap()
    val data: MapData
    val tiles: Array<String>

    init {
        var tileDir = dir
        if (!tileDir.isEmpty() && !tileDir.endsWith("/")) {
            tileDir = tileDir + "/"
        }

        data = JSON.parse<MapData>(getUrlAsString(tileDir + url))
        println("map data is loaded")
        val tilesets = data.tilesets
        if (tilesets != null) {
            tiles = Array(tilesets.size, { "" })
            for (index in 0..tilesets.size - 1) {
                tiles[index] = tilesets[index].name
                Textures.load(tilesets[index].name, tileDir + tilesets[index].image)
            }
        } else {
            tiles = Array(0, { "" })
        }
    }
}
