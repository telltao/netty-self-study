package cn.telltao.rpc.test;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    private Integer count;

    private Boolean isUser;


}
