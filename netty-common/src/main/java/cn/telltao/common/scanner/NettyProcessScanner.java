package cn.telltao.common.scanner;

import cn.telltao.common.annotaion.Cmd;
import cn.telltao.common.annotaion.Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author telltao@qq.com
 * 初始化并加载bean 并将标记@Module的对象中带@Cmd的方法并创建Invoker 随后加入到InvokerTable中
 * @Date 2021/7/12 20:46
 */
@Component
@Slf4j
public class NettyProcessScanner implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        //是否有指定注解
        boolean isAnnotation = clazz.isAnnotationPresent(Module.class);

        if (isAnnotation) {
            Method[] methods = clazz.getMethods();
            if (!Objects.isNull(methods) && methods.length > 0) {
                for (Method m : methods) {
                    Module module = clazz.getAnnotation(Module.class);
                    Cmd cmd = m.getAnnotation(Cmd.class);
                    if (Objects.isNull(cmd)) {
                        continue;
                    }
                    String moduleValue = module.module();
                    String cmdValue = cmd.cmd();

                    if (Objects.isNull(InvokerTable.getInvoker(moduleValue, cmdValue))) {
                        InvokerTable.addInvoker(moduleValue, cmdValue, Invoker.createInvoker(m, bean));
                    } else {
                        log.error("该程序缓存已经存在 module:{},cmd:{}", moduleValue, cmdValue);
                    }
                }
            }
        }
        return null;
    }
}
