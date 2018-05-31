package android.asiantech.vn.springfinalmusic.playmusic

import android.asiantech.vn.springfinalmusic.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

class CircleProgressBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val mStrokeWidth = 9f
    private val mStrokeWidthBackground = 9f
    private var mProgress = 0f
    private var min = 0
    private val max = 100
    private val mColor = Color.DKGRAY
    private val mStartAngle = -90
    private val mOffsetCircle = 5
    private lateinit var mRectF: RectF
    private lateinit var mBackgroundPaint: Paint
    private lateinit var mForegroundPaint: Paint
    private lateinit var mThumbnail: Paint
    private var mBitmap: Bitmap? = null

    init {
        init()
    }

    private fun init() {
        mRectF = RectF()
        mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackgroundPaint.color = adjustColor(mColor, 0.3f)
        mBackgroundPaint.style = Paint.Style.STROKE
        mBackgroundPaint.strokeWidth = mStrokeWidthBackground

        mForegroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mForegroundPaint.color = ContextCompat.getColor(getContext(), R.color.colorOrange)
        mForegroundPaint.style = Paint.Style.STROKE
        mForegroundPaint.strokeWidth = mStrokeWidth

        mThumbnail = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private fun adjustColor(color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val blue = Color.blue(color)
        val green = Color.green(color)
        return Color.argb(alpha, red, green, blue)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = View.getDefaultSize(suggestedMinimumHeight, widthMeasureSpec)
        min = Math.min(height, width)
        setMeasuredDimension(min, min)
        mRectF.set(0 + mStrokeWidth / 2, 0 + mStrokeWidth / 2, min - mStrokeWidth / 2, min - mStrokeWidth / 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(mRectF, mBackgroundPaint)
        val angle = 360 * mProgress / max
        canvas.drawArc(mRectF, mStartAngle.toFloat(), angle, false, mForegroundPaint)
        if (mBitmap != null) {
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, false)
            val bitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            mThumbnail.shader = bitmapShader
            val radius = (min / 2).toFloat() - mStrokeWidthBackground - mOffsetCircle.toFloat()
            canvas.drawCircle(pivotX, pivotY, radius, mThumbnail)
        }
    }

    fun setProgress(progress: Float) {
        this.mProgress = progress
        invalidate()
    }

    fun setBitMap(bitMap: Bitmap) {
        mBitmap = bitMap
        invalidate()
    }
}
