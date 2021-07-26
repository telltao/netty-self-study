package cn.telltao.rpc.config;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author telltao@qq.com
 * @Date 2021/7/24 18:23
 */
public abstract class RpcConfigAbstract {

    private AtomicInteger generator = new AtomicInteger(0);

    protected String id;

    private final String prefixVal = "rapid-cfg-gen-";

    protected String interfaceClass = null;

    //服务的调用方(consumer端特有的属性)
    protected Class<?> proxyClass = null;


    public String getId() {
        if (StringUtils.isBlank(id)) {
            id = prefixVal + generator.getAndIncrement();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(String interfaceClass) {
        this.interfaceClass = interfaceClass;
    }


}
