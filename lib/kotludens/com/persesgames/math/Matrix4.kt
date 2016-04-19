package com.persesgames.math

import org.khronos.webgl.Float32Array

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 15:43
 */
class Matrix4 {

    internal var matrix = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f)

    internal var temp = FloatArray(16)

    private val translateMatrix = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f)

    private val scaleMatrix = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f)

    private val rotateXMatrix = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f)

    private val rotateYMatrix = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f)

    private val rotateZMatrix = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f)

    fun get(): FloatArray {
        return matrix
    }

    fun getFloat32Array() = Float32Array(get().toTypedArray())

    fun set(values: FloatArray) {
        if (values.size != 16) {
            throw IllegalArgumentException("Matrix size should be 16!")
        }

        matrix = values
    }

    fun setPerspectiveProjection(angle: Float, imageAspectRatio: Float, near: Float, far: Float) {
        val r = (angle / 180f * Math.PI).toFloat()
        val f = (1.0f / Math.tan((r / 2.0f).toDouble())).toFloat()

        matrix[0] = f / imageAspectRatio
        matrix[1] = 0.0f
        matrix[2] = 0.0f
        matrix[3] = 0.0f

        matrix[4] = 0.0f
        matrix[5] = f
        matrix[6] = 0.0f
        matrix[7] = 0.0f

        matrix[8] = 0.0f
        matrix[9] = 0.0f
        matrix[10] = -(far + near) / (far - near)
        matrix[11] = -1.0f

        matrix[12] = 0.0f
        matrix[13] = 0.0f
        matrix[14] = -(2.0f * far * near) / (far - near)
        matrix[15] = 0.0f
    }

    fun setToIdentity() {
        matrix[0] = 1.0f
        matrix[1] = 0.0f
        matrix[2] = 0.0f
        matrix[3] = 0.0f
        matrix[4] = 0.0f
        matrix[5] = 1.0f
        matrix[6] = 0.0f
        matrix[7] = 0.0f
        matrix[8] = 0.0f
        matrix[9] = 0.0f
        matrix[10] = 1.0f
        matrix[11] = 0.0f
        matrix[12] = 0.0f
        matrix[13] = 0.0f
        matrix[14] = 0.0f
        matrix[15] = 1.0f
    }

    fun mul(other: Matrix4) {
        mul(other.get())
    }

    protected fun mul(other: FloatArray) {
        if (other.size != 16) {
            throw IllegalArgumentException("Matrix size should be 16!")
        }

        temp[0] = matrix[0] * other[0] + matrix[1] * other[4] + matrix[2] * other[8] + matrix[3] * other[12]
        temp[1] = matrix[0] * other[1] + matrix[1] * other[5] + matrix[2] * other[9] + matrix[3] * other[13]
        temp[2] = matrix[0] * other[2] + matrix[1] * other[6] + matrix[2] * other[10] + matrix[3] * other[14]
        temp[3] = matrix[0] * other[3] + matrix[1] * other[7] + matrix[2] * other[11] + matrix[3] * other[15]
        temp[4] = matrix[4] * other[0] + matrix[5] * other[4] + matrix[6] * other[8] + matrix[7] * other[12]
        temp[5] = matrix[4] * other[1] + matrix[5] * other[5] + matrix[6] * other[9] + matrix[7] * other[13]
        temp[6] = matrix[4] * other[2] + matrix[5] * other[6] + matrix[6] * other[10] + matrix[7] * other[14]
        temp[7] = matrix[4] * other[3] + matrix[5] * other[7] + matrix[6] * other[11] + matrix[7] * other[15]
        temp[8] = matrix[8] * other[0] + matrix[9] * other[4] + matrix[10] * other[8] + matrix[11] * other[12]
        temp[9] = matrix[8] * other[1] + matrix[9] * other[5] + matrix[10] * other[9] + matrix[11] * other[13]
        temp[10] = matrix[8] * other[2] + matrix[9] * other[6] + matrix[10] * other[10] + matrix[11] * other[14]
        temp[11] = matrix[8] * other[3] + matrix[9] * other[7] + matrix[10] * other[11] + matrix[11] * other[15]
        temp[12] = matrix[12] * other[0] + matrix[13] * other[4] + matrix[14] * other[8] + matrix[15] * other[12]
        temp[13] = matrix[12] * other[1] + matrix[13] * other[5] + matrix[14] * other[9] + matrix[15] * other[13]
        temp[14] = matrix[12] * other[2] + matrix[13] * other[6] + matrix[14] * other[10] + matrix[15] * other[14]
        temp[15] = matrix[12] * other[3] + matrix[13] * other[7] + matrix[14] * other[11] + matrix[15] * other[15]

        matrix[0] = temp[0]
        matrix[1] = temp[1]
        matrix[2] = temp[2]
        matrix[3] = temp[3]
        matrix[4] = temp[4]
        matrix[5] = temp[5]
        matrix[6] = temp[6]
        matrix[7] = temp[7]
        matrix[8] = temp[8]
        matrix[9] = temp[9]
        matrix[10] = temp[10]
        matrix[11] = temp[11]
        matrix[12] = temp[12]
        matrix[13] = temp[13]
        matrix[14] = temp[14]
        matrix[15] = temp[15]
    }

    fun translate(x: Float, y: Float, z: Float) {
        translateMatrix[12] = x
        translateMatrix[13] = y
        translateMatrix[14] = z

        mul(translateMatrix)
    }

    fun scale(x: Float, y: Float, z: Float) {
        scaleMatrix[0] = x
        scaleMatrix[5] = y
        scaleMatrix[10] = z

        mul(scaleMatrix)
    }

    fun rotateX(angle: Float) {
        rotateXMatrix[5] = Math.cos(angle.toDouble()).toFloat()
        rotateXMatrix[6] = (-Math.sin(angle.toDouble())).toFloat()
        rotateXMatrix[9] = Math.sin(angle.toDouble()).toFloat()
        rotateXMatrix[10] = Math.cos(angle.toDouble()).toFloat()

        mul(rotateXMatrix)
    }

    fun rotateY(angle: Float) {
        rotateYMatrix[0] = Math.cos(angle.toDouble()).toFloat()
        rotateYMatrix[2] = Math.sin(angle.toDouble()).toFloat()
        rotateYMatrix[8] = (-Math.sin(angle.toDouble())).toFloat()
        rotateYMatrix[10] = Math.cos(angle.toDouble()).toFloat()

        mul(rotateYMatrix)
    }

    fun rotateZ(angle: Float) {
        rotateZMatrix[0] = Math.cos(angle.toDouble()).toFloat()
        rotateZMatrix[1] = Math.sin(angle.toDouble()).toFloat()
        rotateZMatrix[4] = (-Math.sin(angle.toDouble())).toFloat()
        rotateZMatrix[5] = Math.cos(angle.toDouble()).toFloat()

        mul(rotateZMatrix)
    }
}
