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

    public final String POINTCUT_BASE_ADAPTER = "execution(* android.widget.BaseAdapter.getView(..))";   //
    public final String METHOD_POINTCUT_GET_VIEW = "getView";

    @Pointcut(POINTCUT_START_ACTIVITY)
    public void setPointcutStartActivity() {
    }

    @Pointcut(POINTCUT_BASE_ADAPTER)
    public void setPointcutGetView() {
    }
    /**
     * 处理切面信息
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("setPointcutStartActivity()||setPointcutGetView()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();

        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        Log.e("aop","aop: --> "+className+"."+methodName+" execute time: "+((float)(System.nanoTime()-startTime))/1000000 + "ms");
        return result;
    }


}
