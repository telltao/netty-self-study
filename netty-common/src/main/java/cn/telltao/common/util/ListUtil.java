package cn.telltao.common.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Create By LiuTao
 * List工具类
 *
 * @Date 2021-11-03 11:21
 */
public class ListUtil {


    /**
     * <pre>
     * Create By Liu Tao
     *  将List分割为 多个list 适用于数据拆分和实时更新等
     *  请注意,如果返回的数据大小为 0 ,则返回  原collection
     * </pre>
     *
     * @param list 集合数据
     * @param splitSize  每个list数据中的大小
     * @return 数据为空则返回空可变集合
     * @date 2019-08-03 15:26
     */
    public static <T> List<List<T>> splitList(List<T> list, Integer splitSize) {

        if (CollectionUtils.isEmpty(list) || Objects.isNull(splitSize)) {
            return new ArrayList<>(Arrays.asList(list));
        }
        try {
            return Stream.iterate(0, f -> f + 1)
                    // 自动计算要拆分的大小 集合的大小 除以 每个大小 并 +1  -> 102 / 20  +1
                    .limit(list.size() / splitSize + 1).parallel()
                    .map(a -> list.parallelStream().skip(a * splitSize).limit(splitSize)
                            .collect(Collectors.toList()))
                    .filter(b -> !b.isEmpty()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(Arrays.asList(list));
    }


    /**
     * 将一组数据固定分组，每组n个元素
     *
     * @param source 要分组的数据源
     * @param n      每组n个元素
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping2(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;
        int size = (source.size() / n);
        for (int i = 0; i < size; i++) {
            List<T> subset = null;
            subset = source.subList(i * n, (i + 1) * n);
            result.add(subset);
        }
        if (remainder > 0) {
            List<T> subset = null;
            subset = source.subList(size * n, size * n + remainder);
            result.add(subset);
        }
        return result;
    }
/*
    public static void main(String[] args) {
        int s = 121 / 20  +1;
        System.out.println(s);

        List<String> list = new ArrayList<>(50020);
        for (int i = 0; i < 50020; i++) {
            list.add("数据为" + i);
        }
        List<List<String>> collections = splitList(list, 5000);

        collections.forEach(item -> {
            System.out.println("当前大小" + item.size());
        });


        //System.out.println(collections);
    }*/
}


