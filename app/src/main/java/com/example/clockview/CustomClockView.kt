package com.example.clockview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomClockView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)  {

    companion object {
        private const val STROKE_WIDTH = 5f

        private const val DEFAULT_MAIN_COLOR = Color.BLACK
        private const val DEFAULT_CENTER_COLOR = Color.WHITE
        private const val DEFAULT_CONTAINER_COLOR = Color.GRAY
    }
    private var radius = 0f

    private var mainColor = DEFAULT_MAIN_COLOR
    private var centerColor = DEFAULT_CENTER_COLOR
    private var containerColor = DEFAULT_CONTAINER_COLOR


    private val paintClock = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
        color = mainColor
    }

    init {
        setupAttrs(attrs)
    }

    private fun setupAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomClockView)

        with(typedArray) {
            mainColor = getColor(R.styleable.CustomClockView_mainColor, DEFAULT_MAIN_COLOR)
            paintClock.color = mainColor

            centerColor = getColor(R.styleable.CustomClockView_centerColor, DEFAULT_CENTER_COLOR)
            containerColor = getColor(R.styleable.CustomClockView_containerColor, DEFAULT_CONTAINER_COLOR)
        }

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            setBackgroundColor(centerColor)
            drawClockRound()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = Integer.min(width, height)

        radius = size / 2f

        setMeasuredDimension(size, size)
    }

    private fun Canvas.drawClockRound() {
        paintClock.style = Paint.Style.STROKE
        drawCircle(
            radius,
            radius,
            radius * 0.9f,
            paintClock
        )
    }

}