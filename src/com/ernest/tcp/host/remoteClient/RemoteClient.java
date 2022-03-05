package com.ernest.tcp.host.remoteClient;

import com.ernest.tcp.utils.StreamUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RemoteClient {
    
    private static final Integer remotePort = 9999;
    private static InetAddress ip;
    
    static {
        try {
            ip = InetAddress.getLocalHost();
//            ip = InetAddress.getByName("ernest.work");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 上线通知
     */
    public static void logInNoti() {
        try {
            // 建立连接
            Socket socket = new Socket(ip, remotePort);
            // 获取本机信息并制作辅机问候信息
            String info = "Hi-" +
                    InetAddress.getLocalHost().getHostAddress() + "-" +
                    InetAddress.getLocalHost().getHostName();
            System.out.println("准备向辅机发送上线问候信息：" + info);
            // 获取输出流
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(info.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            socket.shutdownOutput();
            // 关闭相关资源
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 下线通知
     */
    public static void logOutNoti() {
        try {
            // 建立连接
            Socket socket = new Socket(ip, remotePort);
            // 获取本机信息并制作辅机问候信息
            String info = "Bye-" +
                    InetAddress.getLocalHost().getHostAddress() + "-" +
                    InetAddress.getLocalHost().getHostName();
            System.out.println("准备向辅机发送下线告别信息：" + info);
            // 获取输出流
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(info.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            socket.shutdownOutput();
            // 关闭相关资源
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取在线设备列表
     *
     * @return 设备列表的字符数组 ["ip-name","ip-name"]
     */
    public static String[] getOnlineInfo() {
        String[] machineList = null;
        try {
            // 建立连接
            Socket socket = new Socket(ip, remotePort);
            // 发送请求信息
            OutputStream outputStream = socket.getOutputStream();
            System.out.println("向辅机发送请求信息：getNowClients");
            outputStream.write("getNowClients".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            // 每次发送完信息必须要调用这个标志，否则服务端会一直等待
            socket.shutdownOutput();
            InputStream inputStream = socket.getInputStream();
            String onlineInfo = StreamUtils.streamToString(inputStream);
            System.out.println("直接输出信息：" + onlineInfo);
            machineList = onlineInfo.split(";");
            // 关闭相关资源
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return machineList;
    }
    
    /**
     * 解析并显示在线主机信息
     *
     * @param onlineInfo 在线主机列表
     * @return 要连接的主机 ip
     */
    public static String getIp(String[] onlineInfo) {
        System.out.println("当前在线的主机列表如下: ");
        for (String info : onlineInfo) {
            System.out.println("[HostName]-" + info.split("-")[1] +
                    "\t[HostIP]-" + info.split("-")[0]);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要连接主机的 ip: ");
        String ip = scanner.next();
        scanner.close();
        return ip;
    }
}
