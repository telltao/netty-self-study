package cn.telltao.rpc.client.invoke.provider.test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Liu Tao
 * @Date 2021/9/7 16:02
 */
public class ConfusingName {

    public static void main(String[] args) {
        List<Object> objects = Arrays.asList(null);
        System.out.println(objects.size());
    }


}
