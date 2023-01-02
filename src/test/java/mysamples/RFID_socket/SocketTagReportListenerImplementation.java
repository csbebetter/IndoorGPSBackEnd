package mysamples.RFID_socket;

import com.alibaba.fastjson2.JSONObject;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketTagReportListenerImplementation implements TagReportListener {
    private static final int port = 3309;
    private final ServerSocket serverSocket;
    public SocketTagReportListenerImplementation() throws IOException {
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(10000);
    }

    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        try {
            for (Tag t : tags) {
                Socket server = serverSocket.accept();

                JSONObject object = new JSONObject();
                object.put("EPC", t.getEpc().toString());
                object.put("Phase", t.getPhaseAngleInRadians());
                object.put("Port", t.getAntennaPortNumber());
                object.put("TimeStamp", t.getLastSeenTime());

                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF(object+"\n");
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

