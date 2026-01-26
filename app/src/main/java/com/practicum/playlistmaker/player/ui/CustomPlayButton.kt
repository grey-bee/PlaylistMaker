package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.practicum.playlistmaker.R

class CustomPlayButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val playDrawable: Drawable?
    private val pauseDrawable: Drawable?
    private var isPlaying = false

    fun setButtonChange(status: Boolean) {
        if (isPlaying != status) {
            isPlaying = status
            invalidate()
        }
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomPlayButton,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playDrawable = getDrawable(R.styleable.CustomPlayButton_playImg)
                pauseDrawable = getDrawable(R.styleable.CustomPlayButton_pauseImg)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = playDrawable?.intrinsicWidth ?: MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = playDrawable?.intrinsicHeight ?: MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pauseDrawable?.setBounds(0, 0, w, h)
        playDrawable?.setBounds(0, 0, w, h)
    }

    override fun onDraw(canvas: Canvas) {
        if (isPlaying) {
            pauseDrawable?.draw(canvas)
        } else {
            playDrawable?.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                setButtonChange(true)
                performClick()
                return true
            }
        }
        return false
    }
}