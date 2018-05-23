package android.asiantech.vn.springfinalmusic.play_music;

import android.asiantech.vn.springfinalmusic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {
    private final float mStrokeWidth = 9;
    private final float mStrokeWidthBackground = 9;
    private float mProgress = 0;
    private int min = 0;
    private int max = 100;
    private final int mColor = Color.DKGRAY;
    private final int mStartAngle = -90;
    private final int mOffsetCircle = 5;
    private RectF mRectF;
    private Paint mBackgroundPaint;
    private Paint mForegroundPaint;
    private Paint mThumbnail;
    private Bitmap mBitmap = null;


    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRectF = new RectF();
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(adjustColor(mColor, 0.3f));
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(mStrokeWidthBackground);

        mForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForegroundPaint.setColor(context.getResources().getColor(R.color.colorOrange));
        mForegroundPaint.setStyle(Paint.Style.STROKE);
        mForegroundPaint.setStrokeWidth(mStrokeWidth);

        mThumbnail = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private int adjustColor(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumHeight(), widthMeasureSpec);
        min = Math.min(height, width);
        setMeasuredDimension(min, min);
        mRectF.set(0 + mStrokeWidth / 2, 0 + mStrokeWidth / 2, min - mStrokeWidth / 2, min - mStrokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(mRectF, mBackgroundPaint);
        float angle = 360 * mProgress / max;
        canvas.drawArc(mRectF, mStartAngle, angle, false, mForegroundPaint);
        if (mBitmap != null) {
            mBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
            BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mThumbnail.setShader(bitmapShader);
            float radius = min / 2 - mStrokeWidthBackground - mOffsetCircle;
            canvas.drawCircle(getPivotX(), getPivotY(), radius, mThumbnail);
        }
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void setBitMap(Bitmap bitMap) {
        mBitmap = bitMap;
        invalidate();
    }
}
