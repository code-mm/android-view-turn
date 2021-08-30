package com.ms.module.view.turn;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.ms.view.ms.turn.R;


/**
 * 病毒扫描效果
 */
public class TurnView extends View {

    private static final String TAG = "FastChargeView";

    private Context context;

    // 圆弧半径  280
    private int exteralArcRadius;

    //圆弧画笔尺寸
    private int arcPaintStrokeSize;

    //圆弧画笔尺寸
    private int pointRadius;

    private int mViewWidth;

    private int mViewHeight;

    //旋转度数
    private int rotateArc = 0;

    //  旋转部分
    private Paint paintBlack;

    // 头部点的颜色
    private Paint lightPaint;

    // 时间长度
    private int duration = 1000;

    private SweepGradient sweepGradient;

    private int beforeColor = 0x00ffffff;
    private int afterColor = 0xffffffff;
    private int headPointColor = 0xffffffff;

    public TurnView(Context context) {
        this(context, null);
    }


    public TurnView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TurnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.context = context;

        if (attributeSet != null) {
            TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.FastChargeView);
            exteralArcRadius = (int) array.getDimension(R.styleable.FastChargeView_exteralArcRadius, 100);
            arcPaintStrokeSize = (int) array.getDimension(R.styleable.FastChargeView_arcPaintStrokeSize, 10);
            pointRadius = (int) array.getDimension(R.styleable.FastChargeView_pointRadius, 10);
            duration = (int) array.getInt(R.styleable.FastChargeView_duration, 1000);
            beforeColor = (int) array.getInt(R.styleable.FastChargeView_beforeColor, 0x00ffffff);
            afterColor = (int) array.getInt(R.styleable.FastChargeView_afterColor, 0xffffffff);
            headPointColor = (int) array.getInt(R.styleable.FastChargeView_headPointColor, 0xffffffff);
        }

        pointRadius = (int) (arcPaintStrokeSize * 1.2f);
        paintBlack = new Paint();
        paintBlack.setStrokeWidth(arcPaintStrokeSize);
        paintBlack.setAntiAlias(true);
        paintBlack.setColor(0xffffffff);

        //画笔是空心
        paintBlack.setStyle(Paint.Style.STROKE);
        // 前面颜色 后面颜色
        sweepGradient = new SweepGradient(0, 0, beforeColor, afterColor);
        paintBlack.setShader(sweepGradient);

        lightPaint = new Paint();
        lightPaint.setStrokeWidth(arcPaintStrokeSize);
        lightPaint.setAntiAlias(true);
        lightPaint.setColor(headPointColor);
        lightPaint.setStyle(Paint.Style.FILL);
        lightPaint.setMaskFilter(new BlurMaskFilter(pointRadius, BlurMaskFilter.Blur.SOLID));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            //绘制圆弧
            canvas.save();
            canvas.translate(mViewWidth / 2, mViewHeight / 2);
            //1、旋转画布
            canvas.rotate(rotateArc);
            //2、绘制圆弧
            RectF rectF = new RectF(-exteralArcRadius, -exteralArcRadius, exteralArcRadius, exteralArcRadius);
            canvas.drawArc(rectF, 10, 350, false, paintBlack);
            //3、绘制圆点
            canvas.drawCircle(exteralArcRadius, -pointRadius / 2, pointRadius, lightPaint); //发光
            canvas.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    AnimatorSet animationSet;
    ValueAnimator rotateAnimator;   //旋转动画

    public void startAnimation() {
        stopAnim();

        //旋转动画
        rotateAnimator = ObjectAnimator.ofInt(0, 360);
        rotateAnimator.setDuration(duration);
        rotateAnimator.setInterpolator(new LinearInterpolator()); //线性运动
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateArc = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        animationSet = new AnimatorSet();
        animationSet.play(rotateAnimator);
        animationSet.start();
    }

    private void stopAnim() {
        try {
            if (animationSet != null) {
                animationSet.cancel();
                animationSet = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestory() {
        try {
            if (animationSet != null) {
                animationSet.cancel();
                animationSet = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
