package cn.telltao.netty.controller;

import cn.telltao.common.protobuf.UserModule;
import cn.telltao.netty.client.Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author telltao@qq.com
 * @Date 2021/7/19 20:20
 */
@RestController
public class TestController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * <pre>
     * @author telltao@qq.com
     *    curl localhost:8001/save
     * </pre>
     * @date 2021/7/19 20:58
     * @param
     * @return
     */
    @RequestMapping("/save")
    public String save() {
        Client.getInstance().sendMessage("user","save",
                UserModule.User.newBuilder().setUserId("1").setUserName("小涛").build());
        return "save";
    }


    /**
     * <pre>
     * @author telltao@qq.com
     *   curl localhost:8001/update
     * </pre>
     * @date 2021/7/19 20:58
     * @param
     * @return
     */
    @RequestMapping("/update")
    public String update() {
        Client.getInstance().sendMessage("user","update",
                UserModule.User.newBuilder().setUserId("2").setUserName("小红").build());
        return "update";
    }

}
