package app.study.nick.com.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

import app.study.nick.com.demo.R;
import app.study.nick.com.demo.utils.ConvertUtil;

/**
 * Created by yangjun1 on 2016/8/10.
 */
public class LabelView extends View {
    private LabelDataManager mLabelDataManager;
    private ValueAnimator mAnimator;
    private boolean isAnimation;

    public LabelView(Context context) {
        this(context,null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLabelDataManager = new LabelDataManager(context);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.LabelView_labelText1:
                    mLabelDataManager.setLabelText1(a.getString(attr));
                    break;
                case R.styleable.LabelView_labelText2:
                    mLabelDataManager.setLabelText2(a.getString(attr));
                    break;
                case R.styleable.LabelView_labelTextColor:
                    mLabelDataManager.mLabelTextColor =a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.LabelView_labelTextSize:
                    // 默认设置为16sp
                    int labelTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    mLabelDataManager.mLabelTextSize = labelTextSize;
                    break;
                case R.styleable.LabelView_labelLineColor:
                    mLabelDataManager.mLabelLineColor = a.getColor(attr,Color.WHITE);
                    break;
                case R.styleable.LabelView_labelLineSize:
                    mLabelDataManager.mLabelLineSize = a.getDimensionPixelSize(attr,2);
                    break;
                case R.styleable.LabelView_labelHeaderColor:
                    mLabelDataManager.mLabelHeaderColor = a.getColor(attr,Color.WHITE);
                    break;
                case R.styleable.LabelView_labelHeaderRadius:
                    mLabelDataManager.mLabelHeaderRadius = a.getDimensionPixelSize(attr,7);
                    mLabelDataManager.setLabelHeaderShaderRadius(mLabelDataManager.mLabelHeaderRadius +ConvertUtil.dip2px(context,10));
                    break;
                case R.styleable.LabelView_labelDirect:
                    mLabelDataManager.setDirection(a.getInt(attr,0));
                    break;
                case R.styleable.LabelView_labelPositionX:
                    mLabelDataManager.getLabelData().setPositionX(a.getFloat(attr,0.5f));
                    break;
                case R.styleable.LabelView_labelPositionY:
                    mLabelDataManager.getLabelData().setPositionY(a.getFloat(attr,0.5f));
                    break;
                case R.styleable.LabelView_labelHeaderShaderStartColor:
                    mLabelDataManager.setLabelHeaderShaderStartColor(a.getColor(attr,0x70000000));
                    break;
                case R.styleable.LabelView_labelHeaderShaderEndColor:
                    mLabelDataManager.setLabelHeaderShaderEndColor(a.getColor(attr,0x30000000));
                    break;
            }

        }
        a.recycle();
        mLabelDataManager.build();
    }

    public void setLabelData(LabelData labelData){
        mLabelDataManager.setLabelData(labelData);
    }
    public LabelData getLabelData(){
        return mLabelDataManager.getLabelData();
    }

    public enum LabelDirect{
        RIGHT,
        LEFT
    }
    public void setDisplayDirection(LabelDirect labelDirect){
        if(labelDirect == LabelDirect.LEFT){
            mLabelDataManager.setDirection(1);
        }else {
            mLabelDataManager.setDirection(0);
        }
    }

    //EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
    //AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
    //UNSPECIFIED：表示子布局想要多大就多大，很少使用
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int reallyWidth;
        int reallyHeight;

        if (widthMode == MeasureSpec.EXACTLY)
        {
            reallyWidth = widthSize;
        } else {
            reallyWidth = getPaddingLeft() + mLabelDataManager.getLabelTotalWidth() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            reallyHeight = heightSize;
        } else {
            reallyHeight = getPaddingTop() + mLabelDataManager.getLabelTotalHeight() + getPaddingBottom();
        }
        setMeasuredDimension(reallyWidth,reallyHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLabelDataManager.onDraw(canvas);
        if(isAnimation){
            startAnimation();
        }
    }

    public void startAnimation() {
        if(mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0.4f, 1);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (float) animation.getAnimatedValue();
                    //改变标签圆半径
                    mLabelDataManager.changeLabelHeaderRaduis(scale);
                    //请求重绘
                    postInvalidate();
                }
            });
            //设置动画持续时间
            mAnimator.setDuration(1000);
            //设置动画的动作，先加速后减速
            mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            // 重复次数 -1表示无限循环
            mAnimator.setRepeatCount(-1);
            // 重复模式, RESTART: 重新开始 REVERSE:恢复初始状态再开始
            mAnimator.setRepeatMode(ValueAnimator.REVERSE);
            //启动动画
            mAnimator.start();
        }else if(!mAnimator.isRunning()){
            mAnimator.start();
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == GONE || visibility == INVISIBLE){
            if(mAnimator != null && mAnimator.isRunning()){
                mAnimator.cancel();
                mAnimator = null;
            }
        }else if(isAnimation){
            startAnimation();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAnimation = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAnimation = false;
        if(mAnimator != null && mAnimator.isRunning()){
            mAnimator.cancel();
            mAnimator = null;
        }
    }
}
