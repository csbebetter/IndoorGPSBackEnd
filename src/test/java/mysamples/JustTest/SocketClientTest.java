package mysamples.JustTest;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketClientTest {
// 文件名称：GreetingClient.java


    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            while(true) {
                System.out.println("Connecting to " + serverName + " on port " + port);
                Socket client = new Socket(serverName, port);

                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);

                System.out.println(in.readUTF());
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
