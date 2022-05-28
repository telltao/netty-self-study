package cn.telltao.test;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liu Tao
 * @Date 2021/10/14 11:35
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class BaseEntity {

    private Long id;

    private String name;

    private BigDecimal money;

    private Boolean isUser;


    public static void main(String[] args) {
        List<BaseEntity> newPeriodList = new ArrayList<>();
        BaseEntity b = new BaseEntity();
        b.setName("啥也不是");
        newPeriodList.add(b);

      newPeriodList.stream().map(item -> {
                    item.setName("啥");
                    return item;
                }).collect(Collectors.toList());

        System.out.println(newPeriodList.get(0).getName());
    }

}