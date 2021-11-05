package team.buaa.questionnaire.common.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team.buaa.questionnaire.common.config.ReturnResponse;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/websocket/{questionnaireId}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 用来存放每个客户端对应的 WebSocketServer 对象
     */
    public static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收 userId
     */
    private String questionnaireId = "";

    /**
     * 未登录的学生id及待收到的消息
     */
    public static Map<String, String> students = new HashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("questionnaireId") String questionnaireId) throws IOException {
        this.session = session;
        this.questionnaireId = questionnaireId;
        if (webSocketMap.containsKey(questionnaireId)) {
            webSocketMap.remove(questionnaireId);
            webSocketMap.put(questionnaireId, this);
        } else {
            webSocketMap.put(questionnaireId, this);
            addOnlineCount();
        }
        log.info("用户连接:" + questionnaireId + ",当前在线人数为:" + getOnlineCount());
        if (students.containsKey(questionnaireId)) {
            sendtoUser(students.get(questionnaireId), questionnaireId);
            students.remove(questionnaireId);
        }
        try {
            sendMessage("连接成功！");
        } catch (IOException e) {
            log.error("用户:" + questionnaireId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(questionnaireId)) {
            webSocketMap.remove(questionnaireId);
            subOnlineCount();
        }
        log.info("用户退出:" + questionnaireId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + questionnaireId + ",报文:" + message);
        if (!StringUtils.isEmpty(message)) {
            try {
                JSONObject jsonObject = JSON.parseObject(message);
                jsonObject.put("fromUserId", this.questionnaireId);
                String toUserId = jsonObject.getString("toUserId");
                if (!StringUtils.isEmpty(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                } else {
                    log.error("请求的 userId:" + toUserId + "不在该服务器上");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.questionnaireId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendtoUser(String message, String sendUserId) throws IOException {
        if (webSocketMap.get(sendUserId) != null) {
            if(!questionnaireId.equals(sendUserId))
                webSocketMap.get(sendUserId).sendMessage(message);
            else
                webSocketMap.get(sendUserId).sendMessage(message);
        } else {
            //如果用户不在线则返回不在线信息给自己
            sendtoUser("当前用户不在线",questionnaireId);
        }
    }

    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }
}
