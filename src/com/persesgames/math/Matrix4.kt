package com.persesgames.math

import org.khronos.webgl.Float32Array

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 15:43
 */

class Matrix4 {

    internal var matrix = Float32Array(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f).toTypedArray())

    internal var temp = Float32Array(16)

    private val translateMatrix = Float32Array(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f).toTypedArray())

    private val scaleMatrix = Float32Array(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f).toTypedArray())

    private val rotateXMatrix = Float32Array(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f).toTypedArray())

    private val rotateYMatrix = Float32Array(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f).toTypedArray())

    private val rotateZMatrix = Float32Array(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f).toTypedArray())

    fun get(): Float32Array {
        return matrix
    }

    fun set(values: Float32Array) {
        if (values.length != 16) {
            throw IllegalArgumentException("FloatArray must hava 16 entries!")
        }
        //assert(values.size == 16)
        matrix = values
    }

    fun setPerspectiveProjection(angle: Float, imageAspectRatio: Float, near: Float, far: Float) {
        val r = (angle / 180f * Math.PI).toFloat()
        val f = (1.0f / Math.tan((r / 2.0f).toDouble())).toFloat()

        matrix.set(0,f / imageAspectRatio)
        matrix.set(1,0.0f)
        matrix.set(2,0.0f)
        matrix.set(3,0.0f)

        matrix.set(4, 0.0f)
        matrix.set(5, f)
        matrix.set(6, 0.0f)
        matrix.set(7, 0.0f)
        matrix.set(8, 0.0f)
        matrix.set(9, 0.0f)
        matrix.set(10, -(far + near) / (far - near))
        matrix.set(11, -1.0f)
        matrix.set(12, 0.0f)
        matrix.set(13, 0.0f)
        matrix.set(14, -(2.0f * far * near) / (far - near))
        matrix.set(15, 0.0f)
    }

    fun setToIdentity() {
        matrix.set(0, 1.0f)
        matrix.set(1, 0.0f)
        matrix.set(2, 0.0f)
        matrix.set(3, 0.0f)
        matrix.set(4, 0.0f)
        matrix.set(5, 1.0f)
        matrix.set(6, 0.0f)
        matrix.set(7, 0.0f)
        matrix.set(8, 0.0f)
        matrix.set(9, 0.0f)
        matrix.set(10, 1.0f)
        matrix.set(11, 0.0f)
        matrix.set(12, 0.0f)
        matrix.set(13, 0.0f)
        matrix.set(14, 0.0f)
        matrix.set(15, 1.0f)
    }

    fun mul(other: Matrix4) {
        mul(other.get())
    }

    protected fun mul(other: Float32Array) {
        if (other.length != 16) {
            throw IllegalArgumentException("FloatArray must hava 16 entries!")
        }

        temp.set(0, matrix.get(0) * other.get(0) + matrix.get(1) * other.get(4) + matrix.get(2) * other.get(8) + matrix.get(3) * other.get(12))
        temp.set(1, matrix.get(0) * other.get(1) + matrix.get(1) * other.get(5) + matrix.get(2) * other.get(9) + matrix.get(3) * other.get(13))
        temp.set(2, matrix.get(0) * other.get(2) + matrix.get(1) * other.get(6) + matrix.get(2) * other.get(10) + matrix.get(3) * other.get(14))
        temp.set(3, matrix.get(0) * other.get(3) + matrix.get(1) * other.get(7) + matrix.get(2) * other.get(11) + matrix.get(3) * other.get(15))
        temp.set(4, matrix.get(4) * other.get(0) + matrix.get(5) * other.get(4) + matrix.get(6) * other.get(8) + matrix.get(7) * other.get(12))
        temp.set(5, matrix.get(4) * other.get(1) + matrix.get(5) * other.get(5) + matrix.get(6) * other.get(9) + matrix.get(7) * other.get(13))
        temp.set(6, matrix.get(4) * other.get(2) + matrix.get(5) * other.get(6) + matrix.get(6) * other.get(10) + matrix.get(7) * other.get(14))
        temp.set(7, matrix.get(4) * other.get(3) + matrix.get(5) * other.get(7) + matrix.get(6) * other.get(11) + matrix.get(7) * other.get(15))
        temp.set(8, matrix.get(8) * other.get(0) + matrix.get(9) * other.get(4) + matrix.get(10) * other.get(8) + matrix.get(11) * other.get(12))
        temp.set(9, matrix.get(8) * other.get(1) + matrix.get(9) * other.get(5) + matrix.get(10) * other.get(9) + matrix.get(11) * other.get(13))
        temp.set(10, matrix.get(8)* other.get(2) + matrix.get(9) * other.get(6) + matrix.get(10) * other.get(10) + matrix.get(11) * other.get(14))
        temp.set(11, matrix.get(8)* other.get(3) + matrix.get(9) * other.get(7) + matrix.get(10) * other.get(11) + matrix.get(11) * other.get(15))
        temp.set(12, matrix.get(12) * other.get(0) + matrix.get(13) * other.get(4) + matrix.get(14) * other.get(8) + matrix.get(15) * other.get(12))
        temp.set(13, matrix.get(12) * other.get(1) + matrix.get(13) * other.get(5) + matrix.get(14) * other.get(9) + matrix.get(15) * other.get(13))
        temp.set(14, matrix.get(12) * other.get(2) + matrix.get(13) * other.get(6) + matrix.get(14) * other.get(10) + matrix.get(15) * other.get(14))
        temp.set(15, matrix.get(12) * other.get(3) + matrix.get(13) * other.get(7) + matrix.get(14) * other.get(11) + matrix.get(15) * other.get(15))

        matrix.set(0 , temp.get(0))
        matrix.set(1 , temp.get(1))
        matrix.set(2 , temp.get(2))
        matrix.set(3 , temp.get(3))
        matrix.set(4 , temp.get(4))
        matrix.set(5 , temp.get(5))
        matrix.set(6 , temp.get(6))
        matrix.set(7 , temp.get(7))
        matrix.set(8 , temp.get(8))
        matrix.set(9 , temp.get(9))
        matrix.set(10, temp.get(10))
        matrix.set(11, temp.get(11))
        matrix.set(12, temp.get(12))
        matrix.set(13, temp.get(13))
        matrix.set(14, temp.get(14))
        matrix.set(15, temp.get(15))
    }

    fun translate(x: Float, y: Float, z: Float) {
        translateMatrix.set(12, x)
        translateMatrix.set(13, y)
        translateMatrix.set(14, z)

        mul(translateMatrix)
    }

    fun scale(x: Float, y: Float, z: Float) {
        scaleMatrix.set(0, x)
        scaleMatrix.set(5, y)
        scaleMatrix.set(10, z)

        mul(scaleMatrix)
    }

    fun rotateX(angle: Float) {
        rotateXMatrix.set(5, Math.cos(angle.toDouble()).toFloat())
        rotateXMatrix.set(6, (-Math.sin(angle.toDouble())).toFloat())
        rotateXMatrix.set(9, Math.sin(angle.toDouble()).toFloat())
        rotateXMatrix.set(10, Math.cos(angle.toDouble()).toFloat())

        mul(rotateXMatrix)
    }

    fun rotateY(angle: Float) {
        rotateYMatrix.set(0, Math.cos(angle.toDouble()).toFloat())
        rotateYMatrix.set(2, Math.sin(angle.toDouble()).toFloat())
        rotateYMatrix.set(8, (-Math.sin(angle.toDouble())).toFloat())
        rotateYMatrix.set(10, Math.cos(angle.toDouble()).toFloat())

        mul(rotateYMatrix)
    }

    fun rotateZ(angle: Float) {
        rotateZMatrix.set(0, Math.cos(angle.toDouble()).toFloat())
        rotateZMatrix.set(1, Math.sin(angle.toDouble()).toFloat())
        rotateZMatrix.set(4, (-Math.sin(angle.toDouble())).toFloat())
        rotateZMatrix.set(5, Math.cos(angle.toDouble()).toFloat())

        mul(rotateZMatrix)
    }
}
