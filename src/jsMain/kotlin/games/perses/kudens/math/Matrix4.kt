package games.perses.kudens.math

import org.khronos.webgl.Float32Array

fun Matrix4.getFloat32Array() = Float32Array(get().toTypedArray())
