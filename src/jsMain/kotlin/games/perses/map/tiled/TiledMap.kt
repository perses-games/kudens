package games.perses.map.tiled

import games.perses.net.getUrlAsString
import games.perses.texture.Texture
import games.perses.texture.Textures
import kotlin.browser.window
import kotlin.math.max

/**
 * Created by rnentjes on 22-7-16.
 */

class MapData {
  var version: Int = 1
  var properties: MutableMap<String, String> = HashMap()
  var layers: Array<MapLayer>? = null
  var tilesets: Array<MapTileset>? = null

  var height: Int = 0
  var width: Int = 0

  var nextobjectid: Int = 0
  var orientation: String = "orthogonal"
  var renderorder: String = "right-down"
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
  var draworder: String = ""
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

class TilesetIndex(
    val texture: Texture?,
    val tcLeft: Float,
    val tcTop: Float,
    val tcRight: Float,
    val tcBottom: Float,
    val scale: Float
) {
  constructor() : this(null, 0f, 0f, 0f, 0f, 0f)

  fun render(x: Float, y: Float) {
    texture?.queueTileDraw(x, y, tcLeft, tcTop, tcRight, tcBottom, scale)
  }
}

class TiledMap(
    dir: String = "",
    url: String
) {
  val properties: Map<String, String> = HashMap()
  val data: MapData
  val tileset: Array<String>
  val tiles: Array<TilesetIndex>
  var first = true
  //var tilesetIndex: Array<TilesetIndex> = Array(0, { TilesetIndex() })

  init {
    var tileDir = dir
    if (!tileDir.isEmpty() && !tileDir.endsWith("/")) {
      tileDir = tileDir + "/"
    }

    data = JSON.parse<MapData>(getUrlAsString(tileDir + url))
    //println("map data is loaded")
    val tilesets = data.tilesets
    if (tilesets != null) {
      tileset = Array(tilesets.size, { "" })
      var maxGid = 0
      for (index in 0..tilesets.size - 1) {
        tileset[index] = tilesets[index].name
        Textures.load(tilesets[index].name, tileDir + tilesets[index].image)
        maxGid = max(maxGid, tilesets[index].firstgid + tilesets[index].tilecount)
      }

      tiles = Array(maxGid, { TilesetIndex() })
    } else {
      tileset = Array(0, { "" })
      tiles = Array(0, { TilesetIndex() })
    }

    cacheTiles()
  }

  fun cacheTiles() {
    if (!Textures.ready()) {
      window.setTimeout({ cacheTiles() }, 10)
    } else {
      val tilesets = data.tilesets
      var tcLeft = 0f
      var tcTop = 0f
      var tcRight = 0f
      var tcBottom = 0f

      if (tilesets != null) {
        for (tileset in tilesets) {
          val tilesHor = tileset.imagewidth / tileset.tilewidth
          val tilesVer = tileset.imageheight / tileset.tileheight
          val scale = (tileset.tilewidth / tileset.imagewidth.toFloat())

          for (index in tileset.firstgid..tileset.firstgid + tileset.tilecount) {
            val texture = Textures.get(tileset.name)

            val gid = index - tileset.firstgid

            val xi = gid % tilesHor
            var yi = gid / tilesHor
            yi = tilesVer - yi - 1
            val tw = 1f / tilesHor.toFloat()
            val th = 1f / tilesVer.toFloat()

            val pixelW = 0.1f / tileset.tilewidth
            val pixelH = 0.1f / tileset.tileheight

            tcLeft = xi * tw
            tcRight = tcLeft + tw

            // switch up/down because of texture coord 0,0 in left bottom corner
            tcBottom = yi * th
            tcTop = tcBottom + th

            tcLeft += pixelW
            tcRight -= pixelW

            tcBottom += pixelH
            tcTop -= pixelH

            tiles[index] = TilesetIndex(texture, tcLeft, tcTop, tcRight, tcBottom, scale)
          }
        }
      }
    }
  }

  fun drawTile(
      tile: Int,
      x: Float,
      y: Float
  ) {
    tiles[tile].render(x, y)
  }

  fun drawLayer(
      layerIndex: Int,
      xo: Float,
      yo: Float
  ) {
    var x = 0f
    var y = 0f
    val layers = data.layers ?: throw IllegalArgumentException("MapData has no layers ($data)")
    val layer = layers[layerIndex]

    val layerData = layer.data
    if (layerData != null) {
      for (index in layerData.indices) {
        // todo: determine if in view
        // todo: determine tilewidth
        //if (xo+x*128f < Game.view.width && yo + y * 128 < Game.view.height) {
        drawTile(layerData[index], xo + x * 128f, yo + y * 128f)

        when (data.renderorder) {
          "right-down" -> {
            x++
            if (x >= layer.width) {
              x = 0f
              y--
            }
          }
          else -> {
            throw IllegalStateException("Renderorder ${data.renderorder} not supported in $this")
          }
        }
        //}
      }
    }

    val tilesets = data.tilesets
    if (tilesets != null) {
      for (tileset in tilesets) {
        if (Textures.has(tileset.name)) {
          val tx = Textures.get(tileset.name)

          tx.render()
        }
      }
    }


    first = false
  }
}
