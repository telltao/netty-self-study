package cn.telltao.rpc.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Liu Tao
 * 集合处理
 * @Date 2021/10/15 14:12
 */
public class Collections {
    public static void main(String[] args) {

        String s = "1";
        if ("1".equals(s)) {
            System.out.println("q");
        } else {

        }

        boolean tag = "q".equals(s) ? true : false;
        // test1();
        // qew
        BaseEntity b = new BaseEntity();

    }

    private void str(String s, String str, String val) {

    }


    /**
     * <pre>
     * @author Liu Tao
     *  ArrayList 的 subList 结果不可强转成 ArrayList
     * </pre>
     *
     * @param
     * @return
     * @date 2021/10/29 11:14
     */
    private static void test1() {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        List list = new ArrayList();
        list.add("qwe");
        list.add("qwe");

        List list1 = (ArrayList) list.subList(1, 1);

        System.out.println(list1.size());
    }

}
