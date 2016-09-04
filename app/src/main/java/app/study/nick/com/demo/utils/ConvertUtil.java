package app.study.nick.com.demo.utils;

import android.content.Context;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tsijinshi on 15/3/6.
 * 这是一个用于转换单位的类
 */
public class ConvertUtil {

    /**
     * 这个类用来消除Object转具体对象时候的小黄条(编辑器的警告)
     * 但是我们要注意，Object转具体对象，会抛出ClassCastException，所以要强制调用方try catch
     */
    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(Object obj) throws Exception {
        return (T) obj;
    }

    public static JSONObject toJson(String str) {
        try {
            if ((str == null) || str.equals("")) {
                str = "{}";
            }

            // 头
            String jsonStr = str;
            Pattern p = Pattern.compile("^\uFEFF?(try\\s*\\{\\s*)?\\s*\\w*\\(");
            Matcher m = p.matcher(jsonStr);
            jsonStr = m.replaceAll("");

            // 尾
            p = Pattern.compile("\\)[;]*\\s*$");
            m = p.matcher(jsonStr);
            jsonStr = m.replaceAll("");

            // 逗号
            p = Pattern.compile("[,]\\}");
            m = p.matcher(jsonStr);
            jsonStr = m.replaceAll("}");

            return new JSONObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将int转换为String
     */
    public static String toString(int i) {
        return i + "";
    }

    /**
     * 将long转换为String
     */
    public static String toString(long l) {
        return l + "";
    }

    public static String toString(float l) {
        return l + "";
    }

    /**
     * 将String装换为float
     */
    public static float toFloat(String s) {
        if ((s == null) || s.equals("")) {
            return 0;
        }

        float f = 0;
        try {
            f = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 将String转换为double
     */
    public static double toDouble(String s) {
        if ((s == null) || s.equals("")) {
            return 0;
        }

        double d = 0;
        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将String转换为int
     */
    public static int toInt(String s) {
        if ((s==null) || s.equals("")) {
            return 0;
        }
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 将String转换为long
     */
    public static long toLong(String s) {
        if ((s==null) || s.equals("")) {
            return 0;
        }
        long l = 0;
        try {
            l = Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 将String转换为Boolean
     */
    public static Boolean toBoolean(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return false;
        }
    }

    // 根据720基准分辨率下的像素，获得该设备的真实像素
    public static float getpx(Context context, float px) {
        // 1080/720 * px
        return getWindowWidthPX(context) / (float)720.0 * px;
    }

    //dip To  px
    public static int dip2px(Context context, int dp) {
        //dp和px的转换关系
        float density = context.getResources().getDisplayMetrics().density;
        //2*1.5+0.5  2*0.75 = 1.5+0.5
        return (int)(dp*density+0.5);
    }

    public static int dp2px(Context context, int dp) {
        return dip2px(context, dp);
    }

    //px  To  dip
    public static int px2dip(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(px/density+0.5);
    }

    public static int px2dp(Context context, int px) {
        return px2dip(context, px);
    }

    /**
     * 获得当前手机屏幕宽度的dp值
     * @return 返回DP值
     */
    public static int getWindowWidthDP(Context context) {
        int widthDP = ConvertUtil.px2dip(context, getWindowWidthPX(context));
        return widthDP;
    }

    /**
     * 获得当前手机屏幕高度的px值
     * @return 返回像素值
     */
    public static int getWindowHeightPX(Context context){
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

    /**
     * 获得当前手机屏幕高度的px值
     * @return 返回像素值
     */
    public static int getWindowWidthPX(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }


    public static String getStringFromStream(InputStream inputStream, String charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        return new String(out.toByteArray(), charset);
    }

    public static String getEncodedCookie(String cookie){
        String encodeCookie = "";
        String[] cookies = cookie.split(";");
        for (String str:cookies) {
            try {
                String key = str.split("=")[0];
                if(str.split("=").length<2){
                    encodeCookie += key + "=;";
                }else{
                    String value = str.split("=")[1];
                    for (int i = 0, length = value.length(); i < length; i++) {
                        char c = value.charAt(i);
                        if (c <= '\u001f' || c >= '\u007f') {
                            value = URLEncoder.encode(value,"UTF-8");
                            break;
                        }
                    }
                    encodeCookie += key + "=" +value +";";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return encodeCookie;
    }
}
