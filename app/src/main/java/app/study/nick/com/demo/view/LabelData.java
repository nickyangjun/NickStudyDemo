package app.study.nick.com.demo.view;

import java.util.ArrayList;

import app.study.nick.com.demo.utils.ConvertUtil;

/**
 * Created by Administrator on 2016/9/4.
 */
public class LabelData{
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

    public void setPosition(float x,float y) {
        position.clear();
        position.add(ConvertUtil.toString(x));
        position.add(ConvertUtil.toString(y));
    }
    public void setPositionX(float x) {
        position.set(0,ConvertUtil.toString(x));
    }
    public void setPositionY(float y) {
        position.set(1,ConvertUtil.toString(y));
    }
}