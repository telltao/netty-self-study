package cn.telltao.test.thread;

import cn.telltao.test.BaseEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Liu Tao
 * @Date 2022/5/11 17:19
 */
public class Test {

    public static void main(String[] args) {
        List<BaseEntity> list = new ArrayList<>();
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId(2L);
        list.add(baseEntity);

        BaseEntity baseEntity1 = new BaseEntity();
        baseEntity1.setId(3L);
        list.add(baseEntity1);

        List<BaseEntity> list2 = new ArrayList<>();
        BaseEntity baseEntity2 = new BaseEntity();
        baseEntity2.setId(2L);
        list2.add(baseEntity2);

        BaseEntity baseEntity3 = new BaseEntity();
        baseEntity3.setId(4L);
        list2.add(baseEntity3);


//List去重 方法一
        for (BaseEntity aa : list) {
            list2 = list2.stream().distinct().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(x -> x.getId().equals(aa.getId())))), ArrayList::new));
        }
        list2.stream().forEach(item -> {
            System.out.println(item.getId());
        });
      //  System.out.println("去重后："+list2);
    }
}