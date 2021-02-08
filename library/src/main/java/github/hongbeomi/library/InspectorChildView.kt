package github.hongbeomi.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

class InspectorChildView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mBitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }
    private val mPaint = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.darker_gray)
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimensionPixelSize(R.dimen.dp_2).toFloat()
        isAntiAlias = true
    }

    fun setInfo(info: InspectorInfo) {
        layoutParams = ViewGroup.LayoutParams(
            info.width,
            info.height
        ).apply {
            x = info.offsetX.toFloat()
            y = info.offSetY.toFloat()
        }
        mBitmap = info.bitmap
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mBitmap?.let {
            canvas?.drawBitmap(it, 0f, 0f, null)
        }
        // draw background line
        canvas?.drawRoundRect(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            0f,
            0f,
            mPaint
        )
    }

}