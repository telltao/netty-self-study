package cn.telltao.guidance.chapter1.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author telltao
 * 创建一个阻塞网络编程 一次请求只可处理一次连接
 * @Date 2021/6/7 20:54
 */
public class OIOChannel {
    public static void main(String[] args) throws IOException {
        // 终端执行  telnet localhost 7777
        //输出：Hi~ Mao Mao ,I'm a monkey!
        oioServer(7777);
    }

    /**
     * 现在我们已经创建了一个对于阻塞的简单demo <br/>
     * 大致如下，建立一个新连接,然后发送数据 即：<br/>
     * 有只猴子在树下等着毛毛的到来，于是毛毛来了之后，猴子说，你好毛毛，我是吉吉国王
     *
     * @see NettyOIOChannel
     */
    public static void oioServer(int port) throws IOException {
        //创建一个服务端的socket
        //这里使用的是JDK1.1的ServerSocket 元老级
        ServerSocket serverSocket = new ServerSocket(port);
        try {
            //阻塞，等待连接
            Socket clientSocket = serverSocket.accept();
            System.out.println("有一个连接被建立。。。 正在发送数据");
            //新建线程去处理该连接
            new Thread(() -> {
                OutputStream out;
                try {
                    //将消息写给已连接的客户端
                    out = clientSocket.getOutputStream();
                    //告诉毛毛，我是吉吉国王
                    out.write("Hi~ Mao Mao ,I'm a monkey!\r\n".getBytes(Charset.forName("UTF-8")));
                    out.flush();
                    //关闭连接
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException ex) {
                        //关闭连接一般不会异常
                    }
                }
            }).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
