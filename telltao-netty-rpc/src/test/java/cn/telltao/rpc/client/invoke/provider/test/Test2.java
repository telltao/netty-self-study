package cn.telltao.rpc.client.invoke.provider.test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Liu Tao
 * @Date 2021/8/25 16:56
 */
public class Test2 {

    public static void main(String[] args) {
//        String str ="qwe,123,qqq,aaa,333,555,";
//
//
//        String str1 ="1试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501测试楼盘082506q,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,测试楼盘082501,测试楼盘082502,测试楼盘082501,测试楼盘082503,";
////        System.out.println(str1.length());
//
//        BigDecimal bigDecimal = new BigDecimal("2000.66");
//        BigDecimal bigDecimal1 = bigDecimal.setScale(1, BigDecimal.ROUND_FLOOR);
//        //BigDecimal divide = bigDecimal.subtract(new BigDecimal(1000), 1, BigDecimal.ROUND_UP);
//
//        System.out.println(bigDecimal1);
        String defailtSecret = "q8HUcP5zoX";
        String str = "dcbbca8ec57cdde0bf6583fce911228df2ef6cf9";
        String apiSecret = defailtSecret;
        String apiSecret1 = "{\"msisdns\": [\"89860427102090550784\",\"8986071915002429197V\"]}"+defailtSecret;


        /*POST请求：
        3. 拼接API_SECRET(由平台提供) eg: '{"k1": "v1", "k2": "v2"}AAAAAAAAAA'
b7056afeaaca062987bb9a56f515b251
b7056afeaaca062987bb9a56f515b251
        1. url格式：HOST + /api/v2/{API_KEY}/API_PATH/?_sign=${SIGN}
        2. 请求数据(_sign除外)需要使用json数据格式，eg：{"k1":"v1", "k2": "v2"}
        3. 拼接API_SECRET(由平台提供) eg: '{"k1": "v1", "k2": "v2"}AAAAAAAAAA'
        4. 使用MD5进行签名计算得出 _sign值 eg: "b7056afeaaca062987bb9a56f515b251"

        备注：如果没有请求参数则_sign为API_SECRET的MD5加密串； MD5加密的字符串使用UTF-8编码。*/


        /*{
            "msisdns": [89860469101980850056],
            "month": 202111
        }*/

        System.out.println(md5(apiSecret));
        System.out.println(md5(apiSecret1));

    }

    private static String md5(String value) {
        String result = null;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update((value).getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException error) {
            error.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte b[] = md5.digest();
        int i;
        StringBuffer buf = new StringBuffer("");

        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }

        result = buf.toString();
        return result;
    }
}
