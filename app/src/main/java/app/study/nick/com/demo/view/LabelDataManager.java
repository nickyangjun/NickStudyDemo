package app.study.nick.com.demo.view;

import android.content.Context;
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

import app.study.nick.com.demo.utils.ConvertUtil;

/**
 * Created by Administrator on 2016/9/4.
 */
public class LabelDataManager{
    private final int RIGHT_LABEL = 0;  //标签的方向
    private final int LEFT_LABEL = 1;
    private int mLabelDirection = RIGHT_LABEL;     //标签的方向，0，文字在标签头右边，1文字在标签头左边
    private LabelData mLabelData;
    public Paint mTextPaint;
    public int mLabelTextColor = Color.WHITE;       //标签文字的颜色
    public int mLabelTextSize;  //标签文字的size
    public int text2lineGapsPx = 4;  //标签文字与下划线的间距
    public int mText1ToText2Gaps = 14; //dp  第一排文字下划线与第二排文字的间距
    public int mLabelLineColor = Color.WHITE;      //标签下划线的颜色
    public int mLabelLineSize = 2;      //标签下划线的size
    public int mLabelHeader2TxtGaps = 20; //文字与标签头中心的间距
    public int mLabelHeaderRadius;  //标签头半径
    private int mLabelHeaderShaderRadius;  //标签头阴影半径
    public int mLabelHeaderColor = Color.WHITE;  //标签头颜色
    public int mLabelHeaderShaderStartColor =0x7f000000;  //标签头阴影开始颜色
    public int mLabelHeaderShaderEndColor = 0x3f000000;  //标签头阴影结束颜色
    private Paint mLinePaint;  //下划线的画笔
    private int mLabelWidth;   //Label 所占的宽度，除去padding
    private int mLabelHeight;  //Label 所占的高度, 除去padding
    private Paint mHeaderPaint; //头部画笔
    private Paint mHeaderShaderPaint; //头部画笔

    public Point mLabelHeaderCenterPoint = new Point();  //标签的头部中心坐标
    public Point mLabelText1StartPoint;    //标签的文字1的起点坐标，若只有一行，使用它
    public Point mLabelText2StartPoint;    //标签的文字1的起点坐标
    private Path mLabelLinePath1;  //标签下划线1的坐标，若只有一行，使用它

    private int mPaddingTop;
    private int mPaddingLeft;
    private float mScale;

    public LabelDataManager(Context context){
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mLabelData = new LabelData();
        mLabelTextSize = ConvertUtil.dip2px(context,12);
        mText1ToText2Gaps = ConvertUtil.dip2px(context,mText1ToText2Gaps);
        mLabelHeaderRadius = ConvertUtil.dip2px(context,4);
        mLabelHeaderShaderRadius = mLabelHeaderRadius + ConvertUtil.dip2px(context,10);

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
        mTextPaint.setShadowLayer(4,1,0,0xd0000000);
        mLinePaint.setColor(mLabelLineColor);
        mLinePaint.setStrokeWidth(mLabelLineSize);
        mLinePaint.setShadowLayer(4,1,0,0x7f000000);
        mHeaderPaint.setColor(mLabelHeaderColor);
    }

    public void setLabelData(LabelData data){
        mLabelData = data;
        build();
    }

    public void setLabelHeaderShaderEndColor(int mLabelHeaderShaderEndColor) {
        this.mLabelHeaderShaderEndColor = mLabelHeaderShaderEndColor;
    }

    public void setLabelHeaderShaderStartColor(int mLabelHeaderShaderStartColor) {
        this.mLabelHeaderShaderStartColor = mLabelHeaderShaderStartColor;
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

    public void setPaddingLeft(int mPaddingLeft) {
        this.mPaddingLeft = mPaddingLeft;
    }

    public void setPaddingTop(int mPaddingTop) {
        this.mPaddingTop = mPaddingTop;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public void setDirection(int direct){
        if(direct == LEFT_LABEL) {
            mLabelDirection = LEFT_LABEL;
        }else {
            mLabelDirection = RIGHT_LABEL;
        }
    }

    public void build() {
        init();
        int lineNum = getTextLineNum();
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
                (float) mLabelHeaderShaderRadius,mLabelHeaderShaderStartColor,mLabelHeaderShaderEndColor,Shader.TileMode.CLAMP);
        mHeaderShaderPaint.setShader(mShader);
        canvas.drawCircle(mLabelHeaderCenterPoint.x,mLabelHeaderCenterPoint.y,mLabelHeaderShaderRadius*mScale,mHeaderShaderPaint);
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


    protected void onDraw(Canvas canvas) {
        drawLabelLine(canvas);
        drawLabelText(canvas);
        drawLabelHeaderShader(canvas);
        drawLabelHeader(canvas);
    }

    public void changeLabelHeaderRaduis(float scale) {
        mScale = scale;
    }
}
