package cn.telltao.common.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

/**
 * @author telltao@qq.com
 *  请求和响应数据封装类
 * @Date 2021/7/18 23:09
 */
public class MessageBuilder {

    private static final int crcCode = 0xabef0101;


    /**
     * <pre>
     * @author telltao@qq.com
     *   请求封装
     * </pre>
     * @date 2021/7/18 23:44
     * @param
     * @return
     */
    public static Message getRequestMessage(String module,
                                             String cmd,
                                             GeneratedMessageV3 data) {
        MessageModule.Message message = MessageModule.Message.newBuilder()
                .setCycCode(crcCode)
                .setMessageType(MessageModule.MessageType.REQUEST)
                .setModule(module)
                .setCmd(cmd)
                .setBody(ByteString.copyFrom(data.toByteArray()))
                .build();

        return message;
    }


    /**
     * <pre>
     * @author telltao@qq.com
     *  响应封装
     * </pre>
     * @date 2021/7/18 23:44
     * @param
     * @return
     */
    public static Message getResponseMessage(String module,
                                             String cmd,
                                             MessageModule.ResultType resultType,
                                             GeneratedMessageV3 data) {
        MessageModule.Message message = MessageModule.Message.newBuilder()
                .setCycCode(crcCode)
                .setMessageType(MessageModule.MessageType.RESPONSE)
                .setModule(module)
                .setCmd(cmd)
                .setResultType(resultType)
                .setBody(ByteString.copyFrom(data.toByteArray()))
                .build();

        return message;
    }
}
