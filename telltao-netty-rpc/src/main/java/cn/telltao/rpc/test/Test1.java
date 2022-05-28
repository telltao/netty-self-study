package cn.telltao.rpc.test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * @author Liu Tao
 * @Date 2021/11/22 15:22
 */
public class Test1 {
    public static void main(String[] args) {

        String s = "qwe";
        String s1 = new String("qwe");

        System.out.println(Objects.equals(s, s1));

    }
}
