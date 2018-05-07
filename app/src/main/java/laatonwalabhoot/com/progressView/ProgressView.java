package laatonwalabhoot.com.progressView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Aishwarya on 5/4/2018.
 */

public class ProgressView extends View {

    private RectF mRectF;
    private int mHeight;
    private int mWidth;
    private Paint mPaintOut;
    private Paint mPaintIn;
    private float mProgress;
    private int mProgressColor;
    private int mProgressBackgroundColor;
    private float mRadius;
    private int mAnimationDelay;

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paintProgressBar();
        if(mRadius>0) {
            mRectF.set(0,0,mWidth,mHeight);
            canvas.drawRoundRect(mRectF,mRadius,mRadius,mPaintOut);
            mRectF.set(0,0,(mProgress*mWidth)/100,mHeight);
            canvas.drawRoundRect(mRectF,mRadius,mRadius,mPaintIn);
        }
        else {
            mRectF.set(0,0,mWidth,mHeight);
            canvas.drawRect(mRectF,mPaintOut);
            mRectF.set(0,0,(mProgress*mWidth)/100,mHeight);
            canvas.drawRect(mRectF,mPaintIn);
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animateFill();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if(attrs!=null) {

            //Density
            float density = getResources().getDisplayMetrics().density;

            //Defaults
            mProgress = 0;
            mRadius = 0;
            mAnimationDelay = 0;
            mProgressColor = ContextCompat.getColor(context,android.R.color.black);
            mProgressBackgroundColor = ContextCompat.getColor(context,android.R.color.darker_gray);

            //Setting property values
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ProgressView,0,0);
            setProgress(typedArray.getFloat(R.styleable.ProgressView_progress,mProgress));
            setRadius(typedArray.getDimension(R.styleable.ProgressView_radius,mRadius));
            setAnimationDelay(typedArray.getInt(R.styleable.ProgressView_animation_delay,mAnimationDelay));
            setProgressColor(typedArray.getColor(R.styleable.ProgressView_progress_color,mProgressColor));
            setProgressBackgroundColor(typedArray.getColor(R.styleable.ProgressView_progress_background_color,mProgressBackgroundColor));
            typedArray.recycle();

            //Initializing before onDraw
            mRectF = new RectF();
            mPaintOut = new Paint();
            mPaintIn = new Paint();
        }
    }

    private void animateFill() {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,mProgress);
        valueAnimator.setDuration(mAnimationDelay*1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (float)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private void paintProgressBar() {
        mPaintOut.setAntiAlias(true);
        mPaintOut.setDither(true);
        mPaintOut.setColor(mProgressBackgroundColor);

        mPaintIn.setDither(true);
        mPaintIn.setAntiAlias(true);
        mPaintIn.setColor(mProgressColor);
    }

    public void setProgress(float progress) {
        mProgress = progress;
        requestLayout();
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
        requestLayout();
        invalidate();
    }

    public void setRadius(float radius) {
        mRadius = radius;
        requestLayout();
        invalidate();
    }

    public void setAnimationDelay(int animationDelay) {
        mAnimationDelay = animationDelay;
        requestLayout();
        invalidate();
    }

    public void setProgressBackgroundColor(int progressBackgroundColor) {
        mProgressBackgroundColor = progressBackgroundColor;
        requestLayout();
        invalidate();
    }
}
