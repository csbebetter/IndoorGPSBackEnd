package cn.example.websocket.controller;

import cn.example.RFIDSocketComponent.config.RFIDSocketConfig;
import cn.example.RFIDSocketComponent.SocketReadTagsComponent;
import com.impinj.octane.OctaneSdkException;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ServerEndPoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端，
 * 注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL连接到websocket服务器端
 */

@ServerEndpoint("/")
@Component
public class WebSockTest {
    private static int onlineCount = 0;
    private static final CopyOnWriteArrayList<WebSockTest> webSocketSet = new CopyOnWriteArrayList<WebSockTest>();
    private Session session;
    private static Socket socket;
    private static InputStream is;
    private static Thread t;

    @OnOpen
    public void onOpen(Session session) throws OctaneSdkException, IOException {
        this.session = session;
        webSocketSet.add(this);//加入set中
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        if(getOnlineCount()==1){
            RFIDWebSocketStart();
        }
    }

    @OnClose
    public void onClose() throws OctaneSdkException, IOException {
        webSocketSet.remove(this);
        if(getOnlineCount()==1){
            RFIDWebSocketStop();
        }
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息：" + message);
        if(Objects.equals(message, "0X00")){
            RFIDWebSocketInit();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生错误！");
        throwable.printStackTrace();
    }

    //   下面是自定义的一些方法
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSockTest.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSockTest.onlineCount--;
    }

    //0X00
    public synchronized void RFIDWebSocketInit(){
        SocketReadTagsComponent.RFIDReaderInit(RFIDSocketConfig.RFIDReaderHostname);
    }

    //0X01
    public synchronized void RFIDWebSocketStart() throws OctaneSdkException {
        SocketReadTagsComponent.RFIDReaderStart();
        t = new Thread(() -> {
            try{while (true) {
                socket = new Socket(RFIDSocketConfig.ServerHostname, RFIDSocketConfig.socketPort);
                is = socket.getInputStream();
                DataInputStream in = new DataInputStream(is);
                sendMessage(in.readUTF());
            }}
            catch(IOException ignored) {}
        });
        t.start();
    }

    //0X02
    public synchronized void RFIDWebSocketStop() throws OctaneSdkException, IOException {
        t.interrupt();
        is.close();
        socket.close();
        SocketReadTagsComponent.RFIDReaderStop();
    }

    //0X03
    public synchronized void RFIDWebSocketDisconnect() throws OctaneSdkException {
        SocketReadTagsComponent.RFIDReaderDisconnect();
    }
}
