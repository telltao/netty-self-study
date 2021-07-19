package cn.telltao.netty.service;

import cn.telltao.common.annotaion.Cmd;
import cn.telltao.common.annotaion.Module;
import cn.telltao.common.protobuf.MessageModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author telltao@qq.com
 * @Date 2021/7/19 14:43
 */
@Service
@Module(module = "user-return")
@Slf4j
public class UserReturnService {

    @Cmd(cmd = "save-return")
    public void saveReturn(MessageModule.ResultType resultType,byte [] data) {
        if(MessageModule.ResultType.SUCCESS.equals(resultType)){
            log.info("处理 user-save-return 方法成功");
        } else {
            log.error("处理 user-save-return 方法成功");

        }


    }

    @Cmd(cmd = "update-return")
    public void updateReturn(MessageModule.ResultType resultType,byte [] data) {
        if(MessageModule.ResultType.SUCCESS.equals(resultType)){
            log.info("处理 user-save-return 方法成功");
        } else {
            log.error("处理 user-save-return 方法成功");

        }
    }
}
