package com.persesgames.shader

import com.persesgames.game.Game
import java.util.*

/**
 * User: rnentjes
 * Date: 14-5-16
 * Time: 11:57
 */

class VertextAttributeInfo(val locationName: String, val numElements: Int) {
    var location = 0
    var offset = 0
}

class ShaderProgramMesh<T>(
  val shaderProgram: ShaderProgram,
  val meshSetter: (ShaderProgram, T) -> Unit,
  val type: Int,
  val vainfo: Array<VertextAttributeInfo>) {
    val data: Array<Float> = Array(32796, { 0f });
    var count: Int = 0
    var blockLength: Int = 0

    init {
        for (info in vainfo.iterator()) {
            info.location = shaderProgram.getAttribLocation(info.locationName)
            info.offset = blockLength;

            blockLength += info.numElements;
            println("attrib: ${info.locationName}, info.location: ${info.location}, info.offset: ${info.offset}");
        }

        println("verticesBlockSize $blockLength");
    }

}