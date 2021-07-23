package cn.telltao.rpc.codec;

import lombok.Data;

import java.io.Serializable;

/**
 * @author telltao@qq.com
 * rpc请求类协议
 * @Date 2021/7/23 18:59
 */
@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -2400864057921055356L;
    /**
     * 每个请求都需要有请求Id,这样server端才知道请求者是谁(异步)
     */
    private String requestId;

    private String className;

    private String methodName;

    private Class<?>[] paramterTypes;

    private Object[] paramters;


}
