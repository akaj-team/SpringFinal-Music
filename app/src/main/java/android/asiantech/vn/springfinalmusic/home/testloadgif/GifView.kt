package android.asiantech.vn.springfinalmusic.home.testloadgif

import android.asiantech.vn.springfinalmusic.R
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import java.io.InputStream

class GifView(context: Context, attributes: AttributeSet?) : View(context, attributes) {

    private var mMovie: Movie? = null
    private var mMovieStart: Int = 0
    private val DEFAULT_DURATION = 1000
    private val mTimeDelay: Long = 33
    private lateinit var mRunnablePlayGif: Runnable
    private var mHandler = Handler()

    init {
        val inputStream: InputStream
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        inputStream = context.resources.openRawResource(R.raw.default_bg)
        mMovie = Movie.decodeStream(inputStream)
        mRunnablePlayGif = Runnable {
            invalidate()
            mHandler.postDelayed(mRunnablePlayGif, mTimeDelay)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.TRANSPARENT)
        val now = android.os.SystemClock.uptimeMillis()
        if (mMovieStart == 0) {
            mMovieStart = now.toInt()
        }

        if (mMovie != null) {
            val scaleX = width.toFloat() / mMovie!!.width()
            val scaleY = height.toFloat() / mMovie!!.height()
            var duration = mMovie?.duration()
            if (duration == 0)
                duration = DEFAULT_DURATION
            val realTime = ((now - mMovieStart) % duration!!).toInt()
            canvas?.scale(scaleX, scaleY, width.toFloat() / 2, height.toFloat() / 2)
            mMovie?.setTime(realTime)
            mMovie?.draw(canvas, (width / 2 - mMovie?.width()!! / 2).toFloat(),
                    (height / 2 - mMovie?.height()!! / 2).toFloat())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = View.getDefaultSize(suggestedMinimumHeight, widthMeasureSpec)
        setMeasuredDimension(width, height)
    }

    fun play() {
        mRunnablePlayGif.run()
    }

    fun stop() {
        mHandler.removeCallbacks(mRunnablePlayGif)
    }
}
