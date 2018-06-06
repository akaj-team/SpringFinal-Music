package android.asiantech.vn.springfinalmusic.playmusic

import android.asiantech.vn.springfinalmusic.R
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
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
    private lateinit var mBitmap: Bitmap
    private val mHandler = Handler()
    private val mTimeDelayRotate: Long = 16
    private lateinit var mRunnableRotateCircle: Runnable
    private var mCurrentAngleRotate = 0f
    private var mOffsetRotate = 2f
    private var mRadius: Float = 0f

    init {
        init()
    }

    private fun init() {
        mRectF = RectF()
        mBackgroundPaint = Paint()
        mBackgroundPaint.color = adjustColor(mColor, 0.3f)
        mBackgroundPaint.style = Paint.Style.STROKE
        mBackgroundPaint.strokeWidth = mStrokeWidthBackground

        mForegroundPaint = Paint()
        mForegroundPaint.color = ContextCompat.getColor(getContext(), R.color.colorOrange)
        mForegroundPaint.style = Paint.Style.STROKE
        mForegroundPaint.strokeWidth = mStrokeWidth

        mThumbnail = Paint(Paint.ANTI_ALIAS_FLAG)
        mRunnableRotateCircle = Runnable {
            rotateCircle()
            mHandler.postDelayed(
                    mRunnableRotateCircle
                    , mTimeDelayRotate)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initBitMap()
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
        mRadius = (min / 2).toFloat() - mStrokeWidthBackground - mOffsetCircle.toFloat()
        mRectF.set(0 + mStrokeWidth / 2, 0 + mStrokeWidth / 2, min - mStrokeWidth / 2, min - mStrokeWidth / 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawOval(mRectF, mBackgroundPaint)
//        val angle = 360 * mProgress / max
//        canvas.drawArc(mRectF, mStartAngle.toFloat(), angle, false, mForegroundPaint)
//        if (mBitmap != null) {
//            mCurrentAngleRotate += mOffsetRotate
//            mCurrentAngleRotate %= 360
//            Log.e("123", mCurrentAngleRotate.toString())
//            canvas.rotate(mCurrentAngleRotate, pivotX, pivotY)
//            canvas.drawCircle(pivotX, pivotY, mRadius, mThumbnail)
//        }
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawColor(Color.TRANSPARENT)
        canvas?.drawOval(mRectF, mBackgroundPaint)
        val angle = 360 * mProgress / max
        canvas?.drawArc(mRectF, mStartAngle.toFloat(), angle, false, mForegroundPaint)
        if (mBitmap != null) {
            mCurrentAngleRotate += mOffsetRotate
            mCurrentAngleRotate %= 360
            Log.e("123", mCurrentAngleRotate.toString())
            canvas?.rotate(mCurrentAngleRotate, pivotX, pivotY)
            canvas?.drawCircle(pivotX, pivotY, mRadius, mThumbnail)
        }
        invalidate()
        super.draw(canvas)
    }

    fun setProgress(progress: Float) {
        this.mProgress = progress
    }

    fun setBitMap(id: Int) {
        mBitmap = BitmapFactory.decodeResource(resources, id)
    }

    fun initBitMap() {
        val matrix = Matrix()
        val scaleX = width.toFloat() / mBitmap.width
        val scaleY = width.toFloat() / mBitmap.height
        matrix.postScale(scaleX, scaleY)
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.width, mBitmap.height, matrix, false)
        val bitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mThumbnail.shader = bitmapShader
    }

    fun startRotate() {
        mRunnableRotateCircle.run()
    }

    private fun rotateCircle() {
//        mCurrentAngleRotate += mOffsetRotate
        invalidate()
    }

    fun stopRotate() {
        if (mBitmap != null) {
            mHandler.removeCallbacks(mRunnableRotateCircle)
        }
    }

}
