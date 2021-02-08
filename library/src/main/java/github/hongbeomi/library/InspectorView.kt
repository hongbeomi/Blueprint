package github.hongbeomi.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import java.util.*
import kotlin.collections.HashMap

class InspectorView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val statusBarResourcesId = resources.getIdentifier(
        "status_bar_height",
        "dimen",
        "android"
    )
    private var statusBarHeight = resources.getDimensionPixelSize(statusBarResourcesId)

    private var mScaleFactor = 1f
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = mScaleFactor.coerceIn(0.1f, 5.0f)
            scaleX = mScaleFactor
            scaleY = mScaleFactor
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?) = true
        override fun onScaleEnd(detector: ScaleGestureDetector?) {}
    }
    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)
    private var dx = 0f
    private var dy = 0f

    init {
        scaleX = 0.4f
        scaleY = 0.4f
        rotationY = 10f
    }

    private val mHashMap: HashMap<Int, FrameLayout> = hashMapOf()
    private val mQueue: Queue<Pair<Int, View>> = LinkedList()
    private val mStack: Stack<Pair<Int, MutableList<View>>> = Stack()

    override fun setScaleX(scaleX: Float) {
        post { mHashMap.values.forEach { it.scaleX = scaleX } }
    }

    override fun setScaleY(scaleY: Float) {
        post { mHashMap.values.forEach { it.scaleY = scaleY } }
    }

    override fun setRotationY(rotationY: Float) {
        post {
            mHashMap.entries.forEachIndexed { index, entry ->
                val newRotationY = rotationY.coerceIn(-85f, 85f)
                entry.value.rotationY = newRotationY
                val densityFactor = 10f / mHashMap.keys.size
                val xRotation = newRotationY * 0.01f
                val newOffsetX =
                    (100 * (index % mHashMap.keys.size) * scaleX * densityFactor * xRotation)
                entry.value.x = newOffsetX
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                dx = x - ev.rawX
                dy = y - ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                // TODO: rotationX..?
                rotationY += (dx + ev.rawX) / 5f
            }
            MotionEvent.ACTION_UP -> {
                dx = 0f
                dy = 0f
            }
            else -> return false
        }
        return true
    }

    fun offerViewInQueue(value: View, depth: Int) {
        mQueue.offer(depth to value)
    }

    fun moveFromQueueToStack() {
        mQueue.forEach loop@{ q ->
                if (mStack.isEmpty()) {
                    mStack.add(q.first to mutableListOf(q.second))
                    return@loop
                }
                if (mStack.peek().first != q.first) {
                    mStack.add(q.first to mutableListOf(q.second))
                } else {
                    if (!isExistLayoutIntersect(mStack.peek().second, q.second)) {
                        mStack.peek().second.add(q.second)
                    } else {
                        mStack.add(q.first to mutableListOf(q.second))
                    }
                }
            }
        drawContainerView()
    }

    private fun isExistLayoutIntersect(source: List<View>, target: View): Boolean {
        val targetRect = Rect(target.left, target.top, target.right, target.bottom)
        return source.any {
            val sourceRect = Rect(it.left, it.top, it.right, it.bottom)
            sourceRect.intersect(targetRect)
        }
    }

    private fun drawContainerView() {
        mStack.forEachIndexed { index, pair ->
            // container
            val containerView = FrameLayout(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
                alpha = 0.7f
            }
            addView(containerView)

            // save hash map
            mHashMap[index] = containerView

            // addView
            pair.second.forEach { value ->
                val info = InspectorInfo(
                    value.measuredWidth,
                    value.measuredHeight,
                    getLocationByChildView(value)[0],
                    getLocationByChildView(value)[1] - statusBarHeight,
                    getBitmap(value)
                )
                val inspectorChildView = InspectorChildView(context).apply {
                    setInfo(info)
                }
                containerView.addView(inspectorChildView)
            }
            postInvalidate()
        }
    }

    private fun getBitmap(value: View): Bitmap {
        if (value.measuredWidth == 0 || value.measuredHeight == 0) {
            throw IllegalArgumentException("You must call after all the views have been drawn! hint :: doOnLayout")
        }
        if (value is ViewGroup) {
            return value.background?.toBitmap(
                value.measuredWidth,
                value.measuredHeight
            ) ?: Bitmap.createBitmap(
                value.measuredWidth,
                value.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        return value.drawToBitmap()
    }

    private fun getLocationByChildView(value: View): IntArray {
        val location = IntArray(2)
        value.getLocationOnScreen(location)
        return location
    }

}