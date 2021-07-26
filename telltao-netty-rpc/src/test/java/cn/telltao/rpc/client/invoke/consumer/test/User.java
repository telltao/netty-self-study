package cn.telltao.rpc.client.invoke.consumer.test;

import lombok.Data;

/**
 * @author telltao@qq.com
 * @Date 2021/7/26 13:32
 */
@Data
public class  User {

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;

    private String name;
}
