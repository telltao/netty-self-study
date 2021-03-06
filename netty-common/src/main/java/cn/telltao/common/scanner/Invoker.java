package cn.telltao.common.scanner;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author telltao@qq.com
 * @Date 2021/7/17 15:42
 */
@Data
public class Invoker {

    private Method method;
    private Object target;


    /**
     * <pre>
     * @author telltao@qq.com
     *   创建对象
     * </pre>
     *
     * @param
     * @return
     * @date 2021/7/17 15:47
     */
    public static Invoker createInvoker(Method method, Object target) {
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        return invoker;
    }


    /**
     * <pre>
     * @author telltao@qq.com
     *  调用对象
     * </pre>
     *
     * @param
     * @return
     * @date 2021/7/17 15:47
     */
    public Object invoke(Object... params) {
        try {
            return method.invoke(target, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
