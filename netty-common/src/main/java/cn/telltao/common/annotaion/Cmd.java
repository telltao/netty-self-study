package cn.telltao.common.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author telltao@qq.com
 * 创建一个在方法中声明的自定义注解,作用域是运行时
 * @Date 2021/7/11 17:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    String cmd();
}
