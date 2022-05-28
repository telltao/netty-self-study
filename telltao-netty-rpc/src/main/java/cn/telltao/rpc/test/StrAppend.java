package cn.telltao.rpc.test;

import org.apache.commons.collections4.CollectionUtils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author Liu Tao
 * @Date 2021/10/15 10:51
 */
public class StrAppend {
    public static void main(String[] args) {
/*

        // 获取15位数字 不足则前补0
        Long timeMillis = System.currentTimeMillis();

        String s = autoGenericLongCode(str, 15);
        System.out.println(s);
        String s1 = autoGenericLongCode(315L,timeMillis.toString(), 10);
        System.out.println(s1);

        Long qwe =timeMillis;// 1636360085L;
        Date date = new Date(qwe);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

        System.out.println("qwe"+formatter.format(date));
*/




       /* long currentTime = System.currentTimeMillis();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

        Date date = new Date(currentTime);

        System.out.println(formatter.format(date));

        System.out.println(currentTime);

        String s1 = UUID.randomUUID().toString().replaceAll("-","6").substring(0,10);
        System.out.println(s1);*/
     /*   Long time = System.currentTimeMillis();

        String s1 = autoGenericCode(time.toString(), 10);
        System.out.println("XN"+s1);

        String s = autoGenericCode(time.toString(), 15);
        System.out.println("15" + 315L+s);*/
        List list = new ArrayList();
        for (;;) {
            System.out.println("生成实体");

            // 查询是否存在该设备,如果有则重新生成
            if (CollectionUtils.isEmpty(list)) {
                System.out.println("无该数据");
                break;
            }
            System.out.println("退出了");
            continue;
        }

        /*for (;;) {
            device = getVirtuallOmsDevice(cityId, deviceHardwareList.get(0).getId());
            // 查询是否存在该设备,如果有则重新生成
            if (CollectionUtils.isNotEmpty(omsDeviceMapper.selectOmsDeviceExist(device))) {
                continue;
            }
            break;
        }*/


    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code 必须为数字类型
     * @param num 保存的长度
     * @return
     */
    private  static String autoGenericCode(String code, int num) {
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        String format = String.format("%0" + num + "d", Long.valueOf(code) + 1);
        // 长度大于字符串,则截取掉 从后往前截取,并返回符合的长度
        if (format.length() > num) {
            format = format.substring(format.length() - num);
        }
        return format;
    }

}
