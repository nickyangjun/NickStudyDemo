package app.study.nick.network.aspectj;

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
    public final String POINTCUT_GET_DEBUG_VALUE = "execution(* app.study.nick.network.NetworkTestActivity.getDebugValue(..))";   //
    public final String METHOD_POINTCUT_GET_DEBUG_VALUE = "getDebugValue";

    public final String POINTCUT_GLIDE_WITH = "execution(* com.bumptech.glide.Glide.with(..))";   //
    public final String METHOD_POINTCUT_GLIDE_WITH = "with";

    @Pointcut(POINTCUT_GET_DEBUG_VALUE)
    public void setPointcutGetDebugValue() {
    }

    @Pointcut(POINTCUT_GLIDE_WITH)
    public void setPointcutGlideWith() {
    }

    /**
     * 处理切面信息
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("setPointcutGetDebugValue()||setPointcutGlideWith()")
    public Object weaveJoinPoint2(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();

        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        if(methodName.equals(METHOD_POINTCUT_GET_DEBUG_VALUE)){
            result = false;
        }
        Log.e("aop","aop network: --> "+className+"."+methodName+" execute time: "+((float)(System.nanoTime()-startTime))/1000000 + "ms");
        return result;
    }


}
