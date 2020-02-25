package com.example.pruebab

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView


class Rose(context: Context?) : ImageView(context) {
    var paint: Paint = Paint()
    private var direction = 0f
    override fun onDraw(canvas: Canvas) {
        val height: Float = height.toFloat()
        val width: Float = width.toFloat()
        canvas.rotate(direction, width / 2, height / 2)
        super.onDraw(canvas)
    }

    fun setDirection(direction: Float) {
        this.direction = direction
        this.invalidate()
    }

    init {
        paint.color = Color.WHITE
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        this.setImageResource(R.drawable.simple_compass)
    }
}