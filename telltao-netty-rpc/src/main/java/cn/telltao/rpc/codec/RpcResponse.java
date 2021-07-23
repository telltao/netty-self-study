package cn.telltao.rpc.codec;

import lombok.Data;

import java.io.Serializable;

/**
 * @author telltao@qq.com
 * rpc响应类协议
 * @Date 2021/7/23 19:00
 */
@Data
public class RpcResponse implements Serializable {


    private static final long serialVersionUID = 9107372101229740531L;

    /**
     * 每个请求都有请求Id,所以返回也需要此Id标识
     */
    private String requestId;

    private Object result;

    private Throwable throwable;

}
