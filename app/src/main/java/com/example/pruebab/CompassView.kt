package com.example.pruebab

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.math.cos
import kotlin.math.sin


/**
 * https://gamedev.stackexchange.com/questions/9607/moving-an-object-in-a-circular-path
 */
class CompassView(context: Context?) : View(context) {
    private var paint: Paint? = null
    private var position = 0f
    private val pi = 3.143
    private fun init() {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = 2f
        paint!!.textSize = 25f
        paint!!.style = Paint.Style.STROKE
        paint!!.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas) {
        val xPoint: Float = (measuredWidth / 2).toFloat()
        val yPoint: Float = (measuredHeight / 2).toFloat()
        val radius = (xPoint.coerceAtLeast(yPoint) * 0.5).toFloat()
        canvas.drawCircle(xPoint, yPoint, radius, paint!!)
        canvas.drawLine(
            xPoint,
            yPoint,
            (xPoint + radius
                    * sin((-position).toDouble() * pi / 180)).toFloat(),
            (yPoint - radius
                    * cos((-position).toDouble() * pi / 180)).toFloat(),
            paint!!
        )
        canvas.drawText(position.toString(), xPoint, yPoint, paint!!)
    }

    fun setDirection(position: Float) {
        this.position = position
        invalidate()
    }

    init {
        init()
    }
}