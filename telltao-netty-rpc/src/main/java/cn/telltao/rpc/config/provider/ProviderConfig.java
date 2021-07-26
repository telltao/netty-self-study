package cn.telltao.rpc.config.provider;

import cn.telltao.rpc.config.RpcConfigAbstract;

/**
 * @author telltao@qq.com
 * 程序注册配置类
 * @Date 2021/7/24 17:25
 */
public class ProviderConfig extends RpcConfigAbstract {

    protected Object ref;

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }
}
