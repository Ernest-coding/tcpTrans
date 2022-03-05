package com.ernest.tcp.host.server;

import com.ernest.tcp.host.client.ClientMesRunnable;
import com.ernest.tcp.utils.StreamUtils;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ServerMesRunnable {
    
    public static void run(ExecutorService pool) {
        Runnable accessMesRun = () -> {
            accessMes(pool);
        };
        pool.submit(accessMesRun);
    }
    
    public static void accessMes(ExecutorService pool) {
        try {
            ServerSocket serverSocket = new ServerSocket(9998);
            System.out.println("正在监听9998端口，等待消息通信......");
            Socket socket = serverSocket.accept();
            
            // 同时建立反方向的传输信道
            makeSendTran(pool, socket.getInetAddress().getHostAddress());
            InputStream inputStream = socket.getInputStream();
            while (true) {
                if (inputStream.available() > 0) {
                    String info = StreamUtils.streamToString(inputStream);
                    System.out.println(socket.getInetAddress().getHostAddress() +
                            ": " + info);
                    if (info.equals("bye")) {
                        break;
                    }
                }
                Thread.sleep(100);
            }
            System.out.println("系统提示：对方已结束本次通话，你可以继续给对方发消息或输入 bye 向对方告别");
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void makeSendTran(ExecutorService pool, String ip) {
        Runnable sendMesRun = () -> {
            ClientMesRunnable.sendMes(ip);
        };  // 发消息线程
        pool.submit(sendMesRun);
    }
}
