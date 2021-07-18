package cn.telltao.common.protobuf;

import com.google.protobuf.GeneratedMessageV3;
import lombok.Data;

/**
 * @author telltao@qq.com
 * 通用数据返回处理类
 * @Date 2021/7/18 22:44
 */
@Data
public class Result<T extends GeneratedMessageV3> {

    private MessageModule.ResultType resultType;

    private  T content;

    public static  <T extends GeneratedMessageV3> Result<T> success(){
        Result<T> result = new Result<T>();
        result.resultType = MessageModule.ResultType.SUCCESS;
        return result;
    }

    public static  <T extends GeneratedMessageV3> Result<T> success(T content){
        Result<T> result = new Result<T>();
        result.resultType = MessageModule.ResultType.SUCCESS;
        result.content = content;
        return result;
    }

    public static  <T extends GeneratedMessageV3> Result<T> failure(){
        Result<T> result = new Result<T>();
        result.resultType = MessageModule.ResultType.FAILURE;
        return result;
    }

    public static  <T extends GeneratedMessageV3> Result<T> failure(T content){
        Result<T> result = new Result<T>();
        result.resultType = MessageModule.ResultType.FAILURE;
        result.content = content;
        return result;
    }
}
