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
public class NettyProcessScanner implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //	1.首先获取当前bean的class类型
        Class<?> clazz = bean.getClass();

        boolean isPresent = clazz.isAnnotationPresent(Module.class);

        if(isPresent) {
            Method[] methods = clazz.getMethods();
            if (!Objects.isNull(methods) && methods.length > 0) {
                for(Method m : methods) {
                    Module module = clazz.getAnnotation(Module.class);
                    Cmd cmd = m.getAnnotation(Cmd.class);
                    if (Objects.isNull(cmd)) {
                        continue;
                    }
                    String moduleValue = module.module();
                    String cmdValue = cmd.cmd();

                    //	只需要把moduleValue + cmdValue的值与对应的反射对象(invoker) 管理起来(map)
                    if (Objects.isNull(InvokerTable.getInvoker(moduleValue, cmdValue))) {
                        InvokerTable.addInvoker(moduleValue, cmdValue, Invoker.createInvoker(m, bean));
                    } else {
                        System.err.println("模块下的命令对应的程序缓存已经存在, module: " + moduleValue + " ,cmd: " + cmdValue);
                    }
                }
            }
        }

        return bean;
    }
}
