package com.ernest.tcp.host;

import com.ernest.tcp.host.client.ClientMesRunnable;
import com.ernest.tcp.host.remoteClient.RemoteClient;
import com.ernest.tcp.host.server.ServerMesRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    public static void main(String[] args) {
        // 开一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(30);
        // 开启本机端口 9998 监听：传输文件
        pool.submit(() -> {
            ServerMesRunnable.run(pool);
        });
        // 开启本机端口 9997 监听：传输消息
//        pool.submit(new ServerFileRunnable());
        // 上线的准备工作
        RemoteClient.logInNoti();
        String ip = RemoteClient.getIp(RemoteClient.getOnlineInfo());
        
        // TODO: 业务流程实现
        pool.submit(() -> {
            ClientMesRunnable.sendMes(ip);
        }); // 发消息线程
        
        
        // 下线通知辅机
        Runtime.getRuntime().addShutdownHook(new Thread(RemoteClient::logOutNoti));
    }
}
