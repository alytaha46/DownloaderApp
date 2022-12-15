package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.log
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText: String = ""
    private var customBackgroundColor = 0
    private var customTextColor = 0
    private val valueAnimator = ValueAnimator.ofInt(0, 360)
    private val rectAnimator = ValueAnimator.ofInt(0, 970)
    private var angle = 0
    private var width_loading = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Completed -> {
                this.isClickable = true
                buttonText = "Download"
                valueAnimator.cancel()
                rectAnimator.cancel()
                angle = 0
                width_loading = 0
            }
            ButtonState.Clicked -> {
                this.isClickable = false
            }
            ButtonState.Loading -> {
                this.isClickable = false
                buttonText = "We are loading"
                valueAnimator.start()
                rectAnimator.start()
            }
        }
        invalidate()
    }


    init {
        buttonState = ButtonState.Completed
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            customTextColor = getColor(R.styleable.LoadingButton_textColor, 0)
            customBackgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
        }
        valueAnimator.apply {
            valueAnimator.duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                angle = it.animatedValue as Int
                invalidate()
            }
        }
        rectAnimator.apply {
            rectAnimator.duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                width_loading = it.animatedValue as Int
                invalidate()
            }
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = customBackgroundColor
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        paint.color = Color.BLACK
        canvas?.drawRect(0f, 0f, width_loading.toFloat(), heightSize.toFloat(), paint)

        paint.color = customTextColor
        canvas?.drawText(
            buttonText,
            (widthSize / 2).toFloat(),
            (heightSize / 2).toFloat() + 10f,
            paint
        )

        paint.color = Color.RED
        canvas?.drawArc(
            0f,
            0f,
            heightSize.toFloat(),
            heightSize.toFloat(),
            0f,
            angle.toFloat(),
            true,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}