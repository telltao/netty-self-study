package cn.telltao.rpc.test;

import cn.telltao.rpc.util.ListUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Liu Tao
 * 将文件读取并拼接字符,然后分段存入其他文件中
 * @Date 2021/11/15 15:38
 */
public class FileTest {

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(20);


        String readFileName = "/Users/telltao/Desktop/temp/tikin/elevator_id.txt";
        String encoder = "UTF-8";

        String writeFileName = "/Users/telltao/Desktop/temp/tikin/elevator/";

        FileInputStream in = new FileInputStream(readFileName);
        List<String> list = IOUtils.readLines(in, encoder);
        List<String> newList = new ArrayList<>(list.size());
        list.forEach(item -> {
            newList.add(item += ",");
        });


        List<List<String>> lists = ListUtil.splitList(newList, 500);

        AtomicInteger atomicInteger = new AtomicInteger();
        lists.forEach(temp -> {

            executorService.execute(() -> {
                //指定写路径
                PrintWriter out = null;
                try {
                    out = new PrintWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(
                                            new File(writeFileName + "elevator_id" + atomicInteger.getAndIncrement() + ".txt")), encoder));

                    for (final String s : temp) {
                        out.write(s);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    out.flush();
                    out.close();
                }

            });


        });


        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
                                new File(writeFileName + "elevator_id_all.txt")), encoder));
        for (final String s : newList) {
            out.write(s);
        }
        out.flush();
        out.close();

    }

}
