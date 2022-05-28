package cn.telltao.rpc.client.invoke.provider.test;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liu Tao
 * @Date 2021/8/25 13:46
 */
public class Test1 {

    public static void main(String[] args) {
        String str = "qwe,123,456,789,qqq,aaa,eee";
        int splitLength = 1;
        int length = 197;
        String splitStr = ",";
        List<String> list =  new ArrayList<>(Arrays.asList(str.split(",")));
        list = list.stream().filter(item -> StringUtils.isNotEmpty(item)).collect(Collectors.toList());
        // remove first 3 stack
        // remove first splitLength stack
        /*for (;;) {
            list.subList(0, splitLength).clear();
            String collect = list.stream().collect(Collectors.joining(splitStr));
            if (collect.length() >=length) {
                continue;
            } else {
                System.out.println(collect);
                System.out.println(collect.length());
                break;
            }
        }*/
        List<Integer> integers = Arrays.asList(2, 3,1, 4, 5,6);
        for (final Integer s : integers) {
            if (s.equals(1)) {
                System.out.println("这个数是1,继续下次循环");
                break;
            } else {
                System.err.println("这个数不是1,继续下次循环"+s);

            }

        }



      /*  String str ="qwe,123,qqq,aaa,333,555,";
        int length = 15;
        if (str.length() > length) {
            // 截取
            for (; ; ) {
                int i = str.indexOf(",");
                str = str.substring(i+1, str.length() - 1);
                if (str.length() > length) {
                    continue;
                }
                break;
            }
        }
        System.out.println(str);*/
    }
}
