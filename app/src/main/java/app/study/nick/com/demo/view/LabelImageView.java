package app.study.nick.com.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import app.study.nick.com.demo.R;
import app.study.nick.com.demo.utils.ConvertUtil;

/**
 * Created by yangjun1 on 2016/8/13.
 */
public class LabelImageView extends ViewGroup {
    private Context mContext;
    private ImageView mImageView;

    public LabelImageView(Context context) {
        this(context,null);
    }

    public LabelImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initImageView();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabelImageView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.LabelImageView_imageSrc:
                    Drawable d = a.getDrawable(attr);
                    if(d != null){
                        mImageView.setImageDrawable(d);
                    }
                    break;
                case R.styleable.LabelImageView_imageScaleType:
                    final int index = a.getInt(attr,-1);
                    switch (index){
                        case 0:
                            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            break;
                        case 1:
                            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            break;
                        case 2:
                            mImageView.setScaleType(ImageView.ScaleType.CENTER);
                            break;
                        case 3:
                            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                        case 4:
                            mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            break;
                    }

                    break;
            }
        }
        a.recycle();
    }

    private void initImageView(){
        mImageView = new ImageView(mContext);
        addView(mImageView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int reallyWidth;
        int reallyHeight;

        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            if(getChildAt(i) instanceof ImageView){
                //测量ImageView的宽高
                getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
            }else {
                //测量LabelView的宽高
                getChildAt(i).measure(MeasureSpec.AT_MOST,MeasureSpec.AT_MOST);
            }
        }

        if (widthMode == MeasureSpec.EXACTLY)
        {
            reallyWidth = widthSize;
        } else {
            reallyWidth = getPaddingLeft() + mImageView.getMeasuredWidth() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            reallyHeight = heightSize;
        } else {
            reallyHeight = getPaddingTop() + mImageView.getMeasuredHeight() + getPaddingBottom();
        }
        setMeasuredDimension(reallyWidth,reallyHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            int childCount = getChildCount();
            for(int i=0;i<childCount;i++){
                View v = getChildAt(i);
                if(v instanceof ImageView){
                    v.layout(l+getPaddingLeft(),t+getPaddingTop(),r-getPaddingRight(),b-getPaddingBottom());
                }else if(v instanceof LabelView){
                    LabelView.LabelData labelData = ((LabelView)v).getLabelData();
                    float pos[] = labelData.getPosition();
                    int cl = (int) (getWidth()*pos[0]);
                    int ct = (int) (getHeight()*pos[1]);
                    int cr = cl+ v.getMeasuredWidth();
                    int cb = ct+ v.getMeasuredHeight();
                    v.layout(cl,ct,cr,cb);
                }
            }
        }
    }


    public void addLabel(LabelView.LabelData labelData){
        LabelView labelView = new LabelView(mContext);
        labelView.setLabelData(labelData);
        addView(labelView);
        requestLayout();
    }

    public void addLabel(List<LabelView.LabelData> labelDataList){

    }


}
