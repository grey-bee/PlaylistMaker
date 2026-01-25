package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R

class CustomPlayButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val playBitmap: Bitmap?
    private val pauseBitmap: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlaying = false
    private val paint = Paint()

    fun setButtonChange() {
        isPlaying = !isPlaying
        invalidate()
    }
    fun setPlayingStop() {
        isPlaying = false
        invalidate()
    }
    fun setPlayingStart() {
        isPlaying = true
        invalidate()
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomPlayButton,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playBitmap = getDrawable(R.styleable.CustomPlayButton_playImg)?.toBitmap()
                pauseBitmap = getDrawable(R.styleable.CustomPlayButton_pauseImg)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = playBitmap?.width ?: MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = playBitmap?.height ?: MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (isPlaying) {
            pauseBitmap?.let { canvas.drawBitmap(pauseBitmap, null, imageRect, paint) }
        } else {
            playBitmap?.let { canvas.drawBitmap(playBitmap, null, imageRect, paint) }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                setButtonChange()
                performClick()
                return true
            }
        }
        return false
    }
}