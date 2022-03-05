package com.ernest.tcp.host.server;

import java.net.ServerSocket;

/**
 * 主机端的文件传输服务线程
 */
public class ServerFileRunnable implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(9997);
            System.out.println("正在监听9997端口，等待文件通信......");
            
            
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
