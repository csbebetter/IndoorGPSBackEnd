package cn.example.RFIDSocketComponent.impl;

import cn.example.RFIDSocketComponent.config.RFIDSocketConfig;
import com.alibaba.fastjson2.JSONObject;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @projectName: impinj
 * @className: RealTimeInfoTagReportListenerImpl
 * @description: 该接口的实现会将标签的最新状态信息（Phase，RSSI，TimeStamp，Port ...）发布出去
 * @author: Litecdows
 * @version: v1.0.1
 */

public class RealTimeInfoTagReportListenerImpl implements TagReportListener {
    private final ServerSocket serverSocket;
    private final JSONObject objectAll;
    private final ArrayList<JSONObject> TagInfoArray = new ArrayList<>();
    public RealTimeInfoTagReportListenerImpl() throws IOException {
        serverSocket = new ServerSocket(RFIDSocketConfig.socketPort);
        // serverSocket.setSoTimeout(1000);
        objectAll = new JSONObject();
    }

    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        try {
            Socket server = serverSocket.accept();
            for (Tag t : tags) {
                sendSocketMsg(t,server);
            }
            server.close();
            objectAll.put("endTime", new Date());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendSocketMsg(Tag t, Socket server) throws IOException {
        if (objectAll.isEmpty()) {
            JSONObject objectTag = new JSONObject();
            objectAll.put("IPAddress", RFIDSocketConfig.RFIDReaderHostname);
            objectAll.put("Frequency", t.getChannelInMhz());
            objectAll.put("StartTime",new Date());
            objectTag.put("EPC", t.getEpc().toString());
            objectTag.put("Phase", t.getPhaseAngleInRadians());
            objectTag.put("Port", t.getAntennaPortNumber());
            objectTag.put("TimeStamp", System.currentTimeMillis());
            objectTag.put("RSSI", t.getPeakRssiInDbm());
            TagInfoArray.add(objectTag);
            objectAll.put("TagInfo", TagInfoArray);
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF(objectAll + "");
        }
        else{
            boolean isIncludeEPC = false;
            for (int i=0;i<TagInfoArray.size();i++) {
                JSONObject objectTag = TagInfoArray.get(i);
                if (t.getEpc().toString().equals(objectTag.get("EPC").toString())) {
                    isIncludeEPC = true;
                    objectTag.put("Phase", t.getPhaseAngleInRadians());
                    objectTag.put("Port", t.getAntennaPortNumber());
                    objectTag.put("TimeStamp", System.currentTimeMillis());;
                    objectTag.put("RSSI", t.getPeakRssiInDbm());
                    TagInfoArray.set(i,objectTag);
                    objectAll.put("TagInfo", TagInfoArray);
                    DataOutputStream out = new DataOutputStream(server.getOutputStream());
                    out.writeUTF(objectAll + "");
                    break;
                }
            }
            if(!isIncludeEPC){
                JSONObject objectTag = new JSONObject();
                objectTag.put("EPC", t.getEpc().toString());
                objectTag.put("Phase", t.getPhaseAngleInRadians());
                objectTag.put("Port", t.getAntennaPortNumber());
                objectTag.put("TimeStamp", System.currentTimeMillis());
                objectTag.put("RSSI", t.getPeakRssiInDbm());
                TagInfoArray.add(objectTag);
                objectAll.put("TagInfo", TagInfoArray);
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF(objectAll + "");
            }
        }
    }
}


