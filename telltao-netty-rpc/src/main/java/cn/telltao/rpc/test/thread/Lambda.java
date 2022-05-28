package cn.telltao.rpc.test.thread;

import cn.telltao.rpc.test.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liu Tao
 * @Date 2021/10/15 15:44
 */
public class Lambda {

    public static void main(String[] args) {

        BaseEntity entityOne = BaseEntity.builder().id(1L).count(1).money(BigDecimal.ONE).name("一块钱").build();
        BaseEntity entityTen = BaseEntity.builder().id(10L).count(2).money(BigDecimal.TEN).name("十块钱").build();

        List<BaseEntity> list = new ArrayList<>();

        list.add(entityOne);
        // 重复
        list.add(entityOne);
        list.add(entityTen);


        // test list


        //listTest(list);

        // test map
        mapTest(list);



    }

    private static List mapTest(List<BaseEntity> list) {

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        //取map FIXME 注意,如果有多个重复数据会报错
        // key 名称 val 实体
        // Map<String, BaseEntity> mapEntity = list.stream().collect(Collectors.toMap(k -> k.getName(), val -> val));


        // 取map 有同名则覆盖
        Map<String, BaseEntity> distinctMap = list.stream()
                .collect(Collectors.toMap(BaseEntity::getName, a -> a, (k1, k2) -> k1));

        if (MapUtils.isNotEmpty(distinctMap)) {
            // 输出 2
            System.out.println(distinctMap.size());
        }

        return list;

    }

    private static List listTest(List<BaseEntity> list) {

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        /**
         *  list相关
         * */

        // 取 list中的Id 并且不要数量为2的
        List<Long> idList = list.stream().filter(item ->  2 != item.getCount())
                .map(BaseEntity::getId).collect(Collectors.toList());
        // 输出2
        System.out.println(idList.size());

        // 取Id 并去重 distinct 针对返回单个字段有效,如果返回对象,需要重写hashCode 和toString
        List<Long> distinctIdList = list.stream().map(BaseEntity::getId).distinct().collect(Collectors.toList());
        // 输出2
        System.out.println(distinctIdList.size());

        // 计算List中的金钱
        BigDecimal reduce = list.stream().map(BaseEntity::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 输出 12
        System.out.println(reduce);

        // 计算List中的 总数量
        int sumInt = list.stream().mapToInt(a -> a.getCount()).sum();
        // 输出 4
        System.out.println(sumInt);

        // 按照名称排序并Id升序排序
        List<String> sortCollect = list.stream().sorted(Comparator.comparing(BaseEntity::getName))
                .map(BaseEntity::getName).collect(Collectors.toList());
        // 输出 一块钱,一块钱,十块钱
        sortCollect.forEach((item) -> System.out.println(item));

        // 按照名称排序并Id倒序排序
        List<String> reversedCollect = list.stream().sorted(Comparator.comparing(BaseEntity::getName).reversed())
                .map(BaseEntity::getName).collect(Collectors.toList());
        // 输出 十块钱,一块钱,一块钱
        reversedCollect.forEach((item) -> System.out.println(item));

        // List 转数组
        Long[] userIds = list.stream().filter(item -> !Objects.isNull(item.getId()))
                .map(BaseEntity::getId).distinct().toArray(Long[]::new);

        return list;

    }
}
