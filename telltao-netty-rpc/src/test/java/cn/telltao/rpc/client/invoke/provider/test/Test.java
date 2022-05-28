package cn.telltao.rpc.client.invoke.provider.test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liu Tao
 * @Date 2021/8/19 11:30
 */
public class Test {
    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("7.90");


        BigDecimal multiply = bigDecimal.setScale(2, RoundingMode.DOWN).multiply(new BigDecimal("100"));
        System.out.println(multiply);

        BigDecimal bigDecimal1 = new BigDecimal("790");

        BigDecimal multiply1 = bigDecimal1.setScale(2, RoundingMode.DOWN).divide(new BigDecimal("100"));
        System.out.println(multiply1);

    }
}
