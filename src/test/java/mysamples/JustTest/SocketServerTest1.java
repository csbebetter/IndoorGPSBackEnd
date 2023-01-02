package mysamples.JustTest;

import com.alibaba.fastjson2.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketServerTest1 extends Thread {
    private final ServerSocket serverSocket;

    public SocketServerTest1(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(10000);
    }

    public void run() {
        int i=0;
        while(true) {
            try {
                System.out.println("\nTimes:"+i+". \tWaiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                //DataInputStream in = new DataInputStream(server.getInputStream());
                //System.out.println(in.readUTF());

                JSONObject object = new JSONObject();
                //string
                object.put("EPC", "E200 001A 0411 0124 1040 5F82");
                object.put("Port", 2);
                object.put("TimeStamp", System.currentTimeMillis ());
                object.put("freMhz", 960.29);

                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF(object+"");
                server.close();
                i++;
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String [] args) {
        int port = Integer.parseInt(args[0]);
        try {
            Thread t = new SocketServerTest1(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


