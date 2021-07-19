package cn.telltao.netty.service;

import cn.telltao.common.annotaion.Cmd;
import cn.telltao.common.annotaion.Module;
import cn.telltao.common.protobuf.Result;
import cn.telltao.common.protobuf.UserModule;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author telltao@qq.com
 * @Date 2021/7/12 20:40
 */
@Service
@Module(module = "user")
public class UserService {

    @Cmd(cmd = "save")
    public Result<?> save(byte[] data) {
        UserModule.User user = null;
        try {
            user = UserModule.User.parseFrom(data);
            System.err.println(" save ok , userId: " + user.getUserId() + " ,userName: " + user.getUserName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.success(user);
    }

    @Cmd(cmd = "update")
    public Result<?> update(byte[] data) {
        UserModule.User user = null;
        try {
            user = UserModule.User.parseFrom(data);
            System.err.println(" update ok , userId: " + user.getUserId() + " ,userName: " + user.getUserName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.success(user);
    }

}
