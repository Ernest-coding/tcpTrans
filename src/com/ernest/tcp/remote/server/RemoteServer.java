package com.ernest.tcp.remote.server;

import com.ernest.tcp.remote.pojo.Clients;
import com.ernest.tcp.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteServer {
    public static void main(String[] args) {
        try {
            // 启动 9999 端口监听
            ServerSocket serverSocket = new ServerSocket(9999);
            // 创建一个当前在线用户表
            List<Clients> nowClients = new ArrayList<>();
            // 创建一个线程池
            ExecutorService threadPool = Executors.newFixedThreadPool(50);
            // 开启循环等待客户端连接
            while (true) {
                // 等待客户端连接
                Socket socket = serverSocket.accept();
                // 实现 Runnable 接口来创建线程，这里直接用 lambda 表达式
                // 将此线程放入线程池
                threadPool.submit(() -> {
                    // TODO：这里写服务端的代码
                    // 首先获取输入流  用户主机发来的信息包含 ip、name
                    InputStream inputStream = null;
                    try {
                        inputStream = socket.getInputStream();
                        String helloInfo = StreamUtils.streamToString(inputStream);
                        socket.shutdownInput();
                        
                        if (helloInfo.equals("getNowClients")) {
                            // 客户端请求获取在线主机列表
                            System.out.println("DEBUG====> 主机：" + socket.getInetAddress().getHostAddress() + " 正在请求获取在线设备信息");
                            StringBuilder nowClientInfo = new StringBuilder();
                            // 获取当前信息，制作返回字符串
                            for (Clients client : nowClients) {
                                if (client.getState()) {
                                    nowClientInfo.append(client.getInfos()).append(";");
                                }
                            }
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(nowClientInfo.toString().getBytes());
                            outputStream.flush();

//                            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
//                            printWriter.println(nowClientInfo.toString());
//                            System.out.println(nowClientInfo.toString());
//                            printWriter.println("test");

//                            outputStream.close();
                            socket.shutdownOutput();
                            
                            // 关闭相关的资源
                            outputStream.close();
                            
                        } else {
                            String[] params = helloInfo.split("-");
                            // 将主机添加到在线列表中
                            if (params[0].equals("Hi")) {
                                // 主机上线
                                System.out.println("DEBUG====> 有一台主机上线：" + helloInfo);
                                nowClients.add(new Clients(params[1], params[2], true));
                            } else if (params[0].equals("Bye")) {
                                // 主机下线
                                System.out.println("DEBUG====> 有一台主机下线：" + helloInfo);
                                for (Clients client : nowClients) {
                                    if (client.getIp().equals(params[1]) && client.getName().equals(params[2])) {
                                        client.setState(false);
                                    }
                                }
                            }
                            // 更新一下当前的列表，删除已经下线的主机
                            nowClients.removeIf(client -> !client.getState());
                        }
                        // 关闭相关资源
                        inputStream.close();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
