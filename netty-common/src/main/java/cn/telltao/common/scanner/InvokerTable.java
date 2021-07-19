package cn.telltao.common.scanner;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author telltao@qq.com
 * @Date 2021/7/17 15:47
 */
public class InvokerTable {

    private static ConcurrentHashMap<String, Map<String, Invoker>>
            invokerTable = new ConcurrentHashMap<>();


    /**
     * <pre>
     * @author telltao@qq.com
     *   新增对象值
     * </pre>
     * @date 2021/7/17 15:55
     * @param
     * @return
     */
    public static void addInvoker(String module,String cmd,Invoker invoker){
        Map<String, Invoker> map = invokerTable.get(module);
        if(MapUtils.isEmpty(map)){
            map = new HashMap<>();
            invokerTable.put(module,map);
        }
        map.put(cmd,invoker);
    }

    /**
     * <pre>
     * @author telltao@qq.com
     *   获取对象值
     * </pre>
     * @date 2021/7/17 15:55
     * @param
     * @return
     */
    public static Invoker getInvoker(String module,String cmd){
        Map<String, Invoker> map = invokerTable.get(module);
        if(MapUtils.isNotEmpty(map)){
            return map.get(cmd);
        }
        return null;
    }
}
