package app.study.nick.com.demo.view;

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

import java.util.ArrayList;

import app.study.nick.com.demo.R;
import app.study.nick.com.demo.utils.ConvertUtil;

/**
 * Created by yangjun1 on 2016/8/10.
 */
public class LabelView extends View {
    private final int RIGHT_LABEL = 0;  //标签的方向
    private final int LEFT_LABEL = 1;
    private int mLabelDirection = RIGHT_LABEL;     //标签的方向，0，文字在标签头右边，1文字在标签头左边
    private LabelDataManager mLabelDataManager;

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
                    mLabelDataManager.setLabelHeaderShaderRadius(mLabelDataManager.mLabelHeaderRadius +6);
                    break;
                case R.styleable.LabelView_labelDirect:
                    mLabelDirection = a.getInt(attr,RIGHT_LABEL);
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
        if(labelDirect == LabelDirect.LEFT){}else {}
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
        mLabelDataManager.drawLabelLine(canvas);
        mLabelDataManager.drawLabelText(canvas);
        mLabelDataManager.drawLabelHeaderShader(canvas);
        mLabelDataManager.drawLabelHeader(canvas);

    }

    private class LabelDataManager{
        private LabelData mLabelData;
        public Paint mTextPaint;
        public int mLabelTextColor = Color.WHITE;       //标签文字的颜色
        public int mLabelTextSize;  //标签文字的size
        public int text2lineGapsPx = 4;  //标签文字与下划线的间距
        public int mText1ToText2Gaps = 14; //dp  第一排文字下划线与第二排文字的间距
        public int mLabelLineColor = Color.WHITE;      //标签下划线的颜色
        public int mLabelLineSize = 2;      //标签下划线的size
        public int mLabelHeader2TxtGaps = 20; //文字与标签头中心的间距
        private int mLabelHeaderRadius;  //标签头半径
        private int mLabelHeaderShaderRadius;  //标签头阴影半径
        private int mLabelHeaderColor = Color.WHITE;  //标签头颜色
        private Paint mLinePaint;  //下划线的画笔
        private int mLabelWidth;   //Label 所占的宽度，除去padding
        private int mLabelHeight;  //Label 所占的高度, 除去padding
        private Paint mHeaderPaint; //头部画笔
        private Paint mHeaderShaderPaint; //头部画笔

        public Point mLabelHeaderCenterPoint = new Point();  //标签的头部中心坐标
        public Point mLabelText1StartPoint;    //标签的文字1的起点坐标，若只有一行，使用它
        public Point mLabelText2StartPoint;    //标签的文字1的起点坐标
        private Path mLabelLinePath1;  //标签下划线1的坐标，若只有一行，使用它

        public LabelDataManager(Context context){
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mLabelData = new LabelData();
            mLabelTextSize = ConvertUtil.dip2px(context,12);
            mText1ToText2Gaps = ConvertUtil.dip2px(context,mText1ToText2Gaps);
            mLabelHeaderRadius = ConvertUtil.dip2px(context,4);
            mLabelHeaderShaderRadius = mLabelHeaderRadius + ConvertUtil.dip2px(context,4);

            mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mHeaderPaint = new Paint();
            mHeaderPaint.setAntiAlias(true);
            mHeaderPaint.setStyle(Paint.Style.FILL);
            mHeaderShaderPaint = new Paint();
            mHeaderShaderPaint.setAntiAlias(true);
        }

        private void init(){
            mTextPaint.setTextSize(mLabelTextSize);
            mTextPaint.setColor(mLabelTextColor);
            mLinePaint.setColor(mLabelLineColor);
            mLinePaint.setStrokeWidth(mLabelLineSize);
            mLinePaint.setShadowLayer(4,1,0,0x7f000000);
            mHeaderPaint.setColor(mLabelHeaderColor);
        }

        public void setLabelData(LabelData data){
            mLabelData = data;
            build();
            invalidate();
        }

        public void setLabelHeaderShaderRadius(int radius){
            mLabelHeaderShaderRadius = ((radius < mLabelHeaderRadius)? mLabelHeaderRadius:radius);
        }

        public LabelData getLabelData(){
            return mLabelData;
        }

        public int getTextHeight(){
            return calcTextSize(getLabelText1()).height()+calcTextSize(getLabelText2()).height();
        }

        public int getTextLineNum(){
            int line = 0;
            if(!TextUtils.isEmpty(getLabelText1())){
                line ++;
            }
            if(!TextUtils.isEmpty(getLabelText2())){
                line ++;
            }
            return line;
        }
        public void setLabelText1(String text){
            mLabelData.describe = text;
        }
        public void setLabelText2(String text){
            mLabelData.price = text;
        }

        public String getLabelText1(){
            if(TextUtils.isEmpty(mLabelData.brand)&&TextUtils.isEmpty(mLabelData.describe)){
                return ellipsizeString(mLabelData.price);
            }else {
                if (TextUtils.isEmpty(mLabelData.brand)) {
                    return ellipsizeString(mLabelData.describe);
                } else if (TextUtils.isEmpty(mLabelData.describe)) {
                    return ellipsizeString(ellipsizeString(mLabelData.brand));
                } else {
                    return ellipsizeString(mLabelData.brand + " " + mLabelData.describe);
                }
            }
        }
        public String getLabelText2(){
            return ellipsizeString(mLabelData.price);
        }

        private String ellipsizeString(String txt){
            if(txt.length()>15) {
                //计算出一行15个文字所能显示的长度+后面要显示的“...”
                float availableTextWidth = mTextPaint.getTextSize() * 16;
                return TextUtils.ellipsize(txt, new TextPaint(mTextPaint), availableTextWidth, TextUtils.TruncateAt.END).toString();
            }else {
                return txt;
            }
        }

        private int getTextMaxWidth(){
            int width1 = calcTextSize(getLabelText1()).width();
            int width2 = calcTextSize(getLabelText2()).width();
            return  width1 > width2 ? width1:width2;
        }

        private Rect calcTextSize(String text){
            Rect textRect = new Rect();
            mTextPaint.getTextBounds(text,0,text.length(),textRect);
            return textRect;
        }

        public void build() {
            init();
            int lineNum = mLabelDataManager.getTextLineNum();
            if (lineNum == 1) {
                //有效的绘制区域宽度
                mLabelWidth = mLabelHeaderShaderRadius + mLabelHeader2TxtGaps + getTextMaxWidth();

                //text区域的高度 == 文字的高度 + 一个文字与下滑线的距离 + 一个下滑线的高度
                int textHeight = getTextHeight() + text2lineGapsPx + mLabelLineSize;
                if (textHeight < mLabelHeaderShaderRadius) {
                    mLabelHeaderCenterPoint.y = getPaddingTop() + mLabelHeaderShaderRadius; //头部中心点Y坐标
                    //有效的绘制区域宽度
                    mLabelHeight = mLabelHeaderShaderRadius * 2;
                } else {
                    mLabelHeaderCenterPoint.y = getPaddingTop() + textHeight;
                    //有效的绘制区域宽度
                    mLabelHeight = textHeight + mLabelHeaderShaderRadius;
                }

                if (mLabelDirection == RIGHT_LABEL) {
                    mLabelHeaderCenterPoint.x = getPaddingLeft() + mLabelHeaderShaderRadius; //头部中心点X坐标
                    //确定下划线的起始和终点坐标
                    float[] mLabelLine1 = new float[4];
                    mLabelLine1[0] = mLabelHeaderCenterPoint.x;
                    mLabelLine1[1] = mLabelHeaderCenterPoint.y;
                    mLabelLine1[2] = mLabelHeaderCenterPoint.x + mLabelHeader2TxtGaps + getTextMaxWidth();
                    mLabelLine1[3] = mLabelHeaderCenterPoint.y;
                    mLabelLinePath1 = new Path();
                    mLabelLinePath1.moveTo(mLabelLine1[0],mLabelLine1[1]);
                    mLabelLinePath1.lineTo(mLabelLine1[2],mLabelLine1[3]);

                    mLabelText1StartPoint = new Point();
                    mLabelText1StartPoint.x = mLabelHeaderCenterPoint.x + mLabelHeader2TxtGaps;
                    //文字起始点的Y坐标 - 一个文字与下滑线的距离 - 一个下滑线的高度
                    mLabelText1StartPoint.y = mLabelHeaderCenterPoint.y - text2lineGapsPx - mLabelLineSize;
                }else {
                    mLabelHeaderCenterPoint.x = getPaddingLeft() + getTextMaxWidth()+mLabelHeader2TxtGaps; //头部中心点X坐标

                    //确定下划线的起始和终点坐标
                    float[] mLabelLine1 = new float[4];
                    mLabelLine1[0] = getPaddingLeft();
                    mLabelLine1[1] = mLabelHeaderCenterPoint.y;
                    mLabelLine1[2] = mLabelHeaderCenterPoint.x;
                    mLabelLine1[3] = mLabelHeaderCenterPoint.y;
                    mLabelLinePath1 = new Path();
                    mLabelLinePath1.moveTo(mLabelLine1[0],mLabelLine1[1]);
                    mLabelLinePath1.lineTo(mLabelLine1[2],mLabelLine1[3]);

                    mLabelText1StartPoint = new Point();
                    mLabelText1StartPoint.x = getPaddingLeft();
                    //文字的起始Y坐标 - 一个文字与下滑线的距离 - 一个下滑线的高度
                    mLabelText1StartPoint.y = mLabelHeaderCenterPoint.y - text2lineGapsPx - mLabelLineSize;
                }
            } else {
                //先得到内部高度，由这个高度可以得到文字距头部中心的X方向上的距离，因为角度固定45度
                int text2Height = calcTextSize(getLabelText2()).height();
                //内部高度 == 上面下划线距下面文字的间距 + 下面文字的高度 + 一个文字与下滑线的距离 + 一个下滑线的高度
                int innerHeight = mText1ToText2Gaps + text2Height + text2lineGapsPx + mLabelLineSize;
                //文字距头部中心的X方向上的距离
                int text2HeaderCenterXdistance = innerHeight/2;

                //有效的绘制区域宽度
                mLabelWidth = mLabelHeaderShaderRadius + text2HeaderCenterXdistance + getTextMaxWidth();

                //text区域的高度 == 文字的高度 + 上面下划线距下面文字的间距 + 两个文字与下滑线的距离 + 两个下滑线的高度
                int textHeight = getTextHeight() + mText1ToText2Gaps + text2lineGapsPx*2 + mLabelLineSize*2;

                int h1 = innerHeight/2;  //头部中心点距下面下划线的Y轴距离
                //头部中心点距上面文字顶的Y轴距离
                int h2 = calcTextSize(getLabelText1()).height() + text2lineGapsPx + mLabelLineSize + h1;
                if (mLabelHeaderShaderRadius <= h1) {
                    //有效的绘制区域宽度
                    mLabelHeight = textHeight;
                    mLabelHeaderCenterPoint.y = getPaddingTop() + h2; //头部中心点Y坐标
                } else if(mLabelHeaderShaderRadius <= h2) {
                    //有效的绘制区域宽度
                    mLabelHeight = h2 + mLabelHeaderShaderRadius;
                    mLabelHeaderCenterPoint.y = getPaddingTop() + h2;
                }else {
                    //有效的绘制区域宽度
                    mLabelHeight = mLabelHeaderShaderRadius *2;
                    mLabelHeaderCenterPoint.y = getPaddingTop() + mLabelHeaderShaderRadius;
                }
                if (mLabelDirection == RIGHT_LABEL) {
                    mLabelHeaderCenterPoint.x = getPaddingLeft() + mLabelHeaderShaderRadius; //头部中心点X坐标

                    //确定上面下划线的起始，转折点和终点坐标
                    float[] mLabelLine1 = new float[6];
                    mLabelLine1[0] = mLabelHeaderCenterPoint.x;
                    mLabelLine1[1] = mLabelHeaderCenterPoint.y;
                    mLabelLine1[2] = mLabelLine1[0] + text2HeaderCenterXdistance;
                    mLabelLine1[3] = mLabelLine1[1] - text2HeaderCenterXdistance;
                    mLabelLine1[4] = mLabelLine1[2] + calcTextSize(getLabelText1()).width();
                    mLabelLine1[5] = mLabelLine1[3];

                    //确定下面下划线的起始，转折点和终点坐标
                    float[] mLabelLine2 = new float[6];
                    mLabelLine2[0] = mLabelHeaderCenterPoint.x;
                    mLabelLine2[1] = mLabelHeaderCenterPoint.y;
                    mLabelLine2[2] = mLabelLine2[0] + text2HeaderCenterXdistance;
                    mLabelLine2[3] = mLabelLine2[1] + text2HeaderCenterXdistance;
                    mLabelLine2[4] = mLabelLine2[2] + calcTextSize(getLabelText2()).width();
                    mLabelLine2[5] = mLabelLine2[3];

                    mLabelLinePath1 = new Path();
                    mLabelLinePath1.moveTo(mLabelLine1[4],mLabelLine1[5]);
                    mLabelLinePath1.lineTo(mLabelLine1[2],mLabelLine1[3]);
                    mLabelLinePath1.lineTo(mLabelLine1[0],mLabelLine1[1]);
                    mLabelLinePath1.lineTo(mLabelLine2[2],mLabelLine2[3]);
                    mLabelLinePath1.lineTo(mLabelLine2[4],mLabelLine2[5]);

                    mLabelText1StartPoint = new Point();
                    mLabelText1StartPoint.x = mLabelHeaderCenterPoint.x + text2HeaderCenterXdistance;
                    //文字的起始Y坐标 - 一个文字与下滑线的距离 - 一个下滑线的高度
                    mLabelText1StartPoint.y = mLabelHeaderCenterPoint.y -text2HeaderCenterXdistance - text2lineGapsPx - mLabelLineSize;

                    mLabelText2StartPoint = new Point();
                    mLabelText2StartPoint.x = mLabelHeaderCenterPoint.x + text2HeaderCenterXdistance;
                    //文字的起始Y坐标 - 一个文字与下滑线的距离 - 一个下滑线的高度
                    mLabelText2StartPoint.y = mLabelHeaderCenterPoint.y + text2HeaderCenterXdistance - text2lineGapsPx - mLabelLineSize;

                }else {
                    mLabelHeaderCenterPoint.x = getPaddingLeft() +getTextMaxWidth() + text2HeaderCenterXdistance; //头部中心点X坐标

                    //确定上面下划线的起始，转折点和终点坐标
                    float[] mLabelLine1 = new float[6];
                    mLabelLine1[0] = mLabelHeaderCenterPoint.x-text2HeaderCenterXdistance-calcTextSize(getLabelText1()).width();
                    mLabelLine1[1] = mLabelHeaderCenterPoint.y-text2HeaderCenterXdistance;
                    mLabelLine1[2] = mLabelLine1[0] + calcTextSize(getLabelText1()).width();
                    mLabelLine1[3] = mLabelLine1[1];
                    mLabelLine1[4] = mLabelHeaderCenterPoint.x;
                    mLabelLine1[5] = mLabelHeaderCenterPoint.y;

                    //确定下面下划线的起始，转折点和终点坐标
                    float[] mLabelLine2 = new float[6];
                    mLabelLine2[0] =  mLabelHeaderCenterPoint.x-text2HeaderCenterXdistance-calcTextSize(getLabelText2()).width();
                    mLabelLine2[1] = mLabelHeaderCenterPoint.y+text2HeaderCenterXdistance;
                    mLabelLine2[2] = mLabelLine2[0] + calcTextSize(getLabelText2()).width();
                    mLabelLine2[3] = mLabelLine2[1];
                    mLabelLine2[4] = mLabelHeaderCenterPoint.x;
                    mLabelLine2[5] = mLabelHeaderCenterPoint.y;
                    mLabelLinePath1 = new Path();
                    mLabelLinePath1.moveTo(mLabelLine1[0],mLabelLine1[1]);
                    mLabelLinePath1.lineTo(mLabelLine1[2],mLabelLine1[3]);
                    mLabelLinePath1.lineTo(mLabelLine1[4],mLabelLine1[5]);
                    mLabelLinePath1.lineTo(mLabelLine2[2],mLabelLine2[3]);
                    mLabelLinePath1.lineTo(mLabelLine2[0],mLabelLine2[1]);

                    mLabelText1StartPoint = new Point();
                    mLabelText1StartPoint.x = mLabelHeaderCenterPoint.x-text2HeaderCenterXdistance-calcTextSize(getLabelText1()).width();
                    //文字的起始Y坐标 - 一个文字与下滑线的距离 - 一个下滑线的高度
                    mLabelText1StartPoint.y = mLabelHeaderCenterPoint.y -text2HeaderCenterXdistance - text2lineGapsPx - mLabelLineSize;

                    mLabelText2StartPoint = new Point();
                    mLabelText2StartPoint.x = mLabelHeaderCenterPoint.x-text2HeaderCenterXdistance-calcTextSize(getLabelText2()).width();
                    //文字的起始Y坐标 - 一个文字与下滑线的距离 - 一个下滑线的高度
                    mLabelText2StartPoint.y = mLabelHeaderCenterPoint.y + text2HeaderCenterXdistance - text2lineGapsPx - mLabelLineSize;
                }
            }
        }
        public int getLabelTotalWidth(){
            return mLabelWidth;
        }
        public int getLabelTotalHeight(){
            return mLabelHeight;
        }

        public void drawLabelHeader(Canvas canvas){
            canvas.drawCircle(mLabelHeaderCenterPoint.x,mLabelHeaderCenterPoint.y,mLabelHeaderRadius,mHeaderPaint);
        }

        public void drawLabelHeaderShader(Canvas canvas){
            Shader mShader = new RadialGradient((float) mLabelHeaderCenterPoint.x,(float) mLabelHeaderCenterPoint.y,
                    (float) mLabelHeaderShaderRadius,0x7f000000,0x3f000000,Shader.TileMode.CLAMP);
            mHeaderShaderPaint.setShader(mShader);
            canvas.drawCircle(mLabelHeaderCenterPoint.x,mLabelHeaderCenterPoint.y,mLabelHeaderShaderRadius,mHeaderShaderPaint);
        }

        public void drawLabelLine(Canvas canvas){
            if(mLabelLinePath1 != null){
                canvas.drawPath(mLabelLinePath1,mLinePaint);
            }
        }

        public void drawLabelText(Canvas canvas){
            if(mLabelText1StartPoint != null){
                canvas.drawText(getLabelText1(),mLabelText1StartPoint.x,mLabelText1StartPoint.y,mTextPaint);
            }
            if(mLabelText2StartPoint != null){
                canvas.drawText(getLabelText2(),mLabelText2StartPoint.x,mLabelText2StartPoint.y,mTextPaint);
            }
        }

    }

    public static class LabelData{
        public String describe = "";
        public String brand = "";
        public String price = "";
        private ArrayList<String> position = new ArrayList<>();

        public LabelData(){
            position.add("0.5");
            position.add("0.5");
        }

        public float[] getPosition(){
            float[] pos = new float[2];
            pos[0] = ConvertUtil.toFloat(position.get(0));
            pos[1] = ConvertUtil.toFloat(position.get(1));
            return pos;
        }
    }
}
