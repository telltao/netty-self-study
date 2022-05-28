package cn.telltao.test;

import java.math.BigDecimal;

/**
 * @author Liu Tao
 * @Date 2021/10/15 10:51
 */
public class StrAppend {
    public static void main(String[] args) {

        int s = 1;

        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("0.9");
        BigDecimal c = new BigDecimal("0.8");
        BigDecimal x = a.subtract(b);
        BigDecimal y = b.subtract(c);

        x.compareTo(y);
        if (x.equals(y)) {
            System.out.println("true");
        }



    }
}
