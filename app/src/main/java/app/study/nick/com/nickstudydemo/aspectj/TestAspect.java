package app.study.nick.com.nickstudydemo.aspectj;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by yangjun1 on 2016/5/27.
 */
@Aspect
public class TestAspect {

    public final String POINTCUT_START_ACTIVITY = "execution(* app.study.nick.com.nickstudydemo.MainActivity.startActivity(..))";   //
    public final String METHOD_POINTCUT_START_ACTIVITY = "startActivity";

    public final String POINTCUT_BASE_ADAPTER_GET_COUNT = "execution(* android.widget.BaseAdapter.getCount(..))";   //
    public final String METHOD_POINTCUT_GET_CONUNT = "getCount";

    public final String POINTCUT_BASE_ADAPTER = "execution(* android.widget.BaseAdapter.getView(..))";   //
    public final String METHOD_POINTCUT_GET_VIEW = "getView";
    //TODO 无效，其他module里面无法完成aop
    public final String POINTCUT_GET_DEBUG_VALUE = "execution(* app.study.nick.network.NetworkTestActivity.getDebugValue(..))";   //
    public final String METHOD_POINTCUT_GET_DEBUG_VALUE = "getDebugValue";

    //TODO 这个方法无效，怀疑无法在jar 或 aar 包中使用aop
    public final String POINTCUT_SET_CONTENT_VALUE = "execution(* android.support.v7.app.AppCompatActivity.setContentView(..))";   //
    public final String METHOD_POINTCUT_SET_CONTENT_VALUE = "setContentView";


    @Pointcut(POINTCUT_START_ACTIVITY)
    public void setPointcutStartActivity() {
    }

    @Pointcut(POINTCUT_BASE_ADAPTER)
    public void setPointcutGetView() {
    }

    @Pointcut(POINTCUT_BASE_ADAPTER_GET_COUNT)
    public void setPointcutGetCount() {
    }

    @Pointcut(POINTCUT_GET_DEBUG_VALUE)
    public void setPointcutGetDebugValue() {
    }

    @Pointcut(POINTCUT_SET_CONTENT_VALUE)
    public void setPointcutSetContentView() {
    }
    /**
     * 处理切面信息
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("setPointcutStartActivity()||setPointcutGetView()||setPointcutSetContentView()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();

        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        Log.e("aop","aop: --> "+className+"."+methodName+" execute time: "+((float)(System.nanoTime()-startTime))/1000000 + "ms");
        return result;
    }

    @Around("setPointcutGetCount()||setPointcutGetDebugValue()")
    public Object weaveJoinPoint2(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();

        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
//        result = false;
        Log.e("aop","aop2: --> "+className+"."+methodName+" execute time: "+((float)(System.nanoTime()-startTime))/1000000 + "ms");
        return result;
    }


}
