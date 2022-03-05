package com.ernest.tcp.host.client;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientMesRunnable {
    public static void sendMes(String ip) {
        try {
            Socket socket = new Socket(ip, 9998);
            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                Thread.sleep(100);
                System.out.print("输入消息，按回车发送(输入 bye 结束聊天): ");
                String info = scanner.nextLine();
                if (info != null) {
                    outputStream.write((info + "#").getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    if (info.equals("bye")) {
                        break;
                    }
                }
            }
            System.out.println("系统提示：你已结束本次通话，请注意对方可能会给你发送重要信息");
            outputStream.close();
            scanner.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
