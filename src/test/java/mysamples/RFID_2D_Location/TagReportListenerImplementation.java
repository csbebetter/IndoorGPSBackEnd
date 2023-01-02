package mysamples.RFID_2D_Location;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TagReportListenerImplementation implements TagReportListener {

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        for (Tag t : tags) {
            if(t.isRfPhaseAnglePresent()){PhaseData.PhaseGet(t);}
        }
    }
}

class PhaseData {
    private static final String StrEPC_1 = "E200 001A 0411 0124 1040 5F82";
    private static final String StrEPC_2 = "E200 001A 0411 0107 1040 510D";
    private static final String StrEPC_3 = "E200 001A 0411 0121 1040 599C";
    private static final String StrEPC_4 = "E200 001A 0411 0123 1040 624D";

    private static final int RfidPortA = 1;
    private static final int RfidPortB = 4;

    public static List<Double> A1ListPhaseData = new ArrayList<>();
    public static List<Double> A2ListPhaseData = new ArrayList<>();
    public static List<Double> A3ListPhaseData = new ArrayList<>();
    public static List<Double> A4ListPhaseData = new ArrayList<>();
    public static List<Double> B1ListPhaseData = new ArrayList<>();
    public static List<Double> B2ListPhaseData = new ArrayList<>();
    public static List<Double> B3ListPhaseData = new ArrayList<>();
    public static List<Double> B4ListPhaseData = new ArrayList<>();
    public static Date date = new Date();
    private static final String filePath = "D:\\RFID_Data\\Data"+ date +".csv";
    private static final FileWriter writer;
    static {
        try {
            writer = new FileWriter(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Thread t;

    public static void PhaseGet(Tag tag) {
        //A1
        //System.out.println(tag.getAntennaPortNumber()+"   "+tag.getPhaseAngleInRadians());
        if (Objects.equals(tag.getEpc().toString(), StrEPC_1) && tag.getAntennaPortNumber() == RfidPortA) {
            A1ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
        //A2
        if (Objects.equals(tag.getEpc().toString(), StrEPC_2) && tag.getAntennaPortNumber() == RfidPortA) {
            A2ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
        //A3
        if (Objects.equals(tag.getEpc().toString(), StrEPC_3) && tag.getAntennaPortNumber() == RfidPortA) {
            A3ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
        //A4
        if (Objects.equals(tag.getEpc().toString(), StrEPC_4) && tag.getAntennaPortNumber() == RfidPortA) {
            A4ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
        //B1
        if (Objects.equals(tag.getEpc().toString(), StrEPC_1) && tag.getAntennaPortNumber() == RfidPortB) {
            B1ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
        //B2
        if (Objects.equals(tag.getEpc().toString(), StrEPC_2) && tag.getAntennaPortNumber() == RfidPortB) {
            B2ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
        //B3
        if (Objects.equals(tag.getEpc().toString(), StrEPC_3) && tag.getAntennaPortNumber() == RfidPortB) {
            B3ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
        //B4
        if (Objects.equals(tag.getEpc().toString(), StrEPC_4) && tag.getAntennaPortNumber() == RfidPortB) {
            B4ListPhaseData.add(tag.getPhaseAngleInRadians());
        }
    }

    public static double[][] GetData() {
        while (true) {
            try {
                return new double[][]{
                        {
                                A1ListPhaseData.get(A1ListPhaseData.size() - 1),
                                A2ListPhaseData.get(A2ListPhaseData.size() - 1),
                                A3ListPhaseData.get(A3ListPhaseData.size() - 1),
                                A4ListPhaseData.get(A4ListPhaseData.size() - 1),
                        },
                        {
                                B1ListPhaseData.get(B1ListPhaseData.size() - 1),
                                B2ListPhaseData.get(B2ListPhaseData.size() - 1),
                                B3ListPhaseData.get(B3ListPhaseData.size() - 1),
                                B4ListPhaseData.get(B4ListPhaseData.size() - 1),
                        }
                };
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    public static void SaveDataStart() throws IOException {
        t = new Thread(()->{
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Thread.sleep(50);
                        double[][] OnceData = GetData();
                        String record = Angle2Radian(OnceData[0][0]) + "," +
                                Angle2Radian(OnceData[0][1]) + "," +
                                Angle2Radian(OnceData[0][2]) + "," +
                                Angle2Radian(OnceData[0][3]) + "," +
                                Angle2Radian(OnceData[1][0]) + "," +
                                Angle2Radian(OnceData[1][1]) + "," +
                                Angle2Radian(OnceData[1][2]) + "," +
                                Angle2Radian(OnceData[1][3]) + "\n";
                        writer.write(record);
                    }
                } catch (InterruptedException | IOException ignored) {
                }
        });
        t.start();
    }

    public static void SaveDataStop() throws IOException {
        t.interrupt();
        if(t.isInterrupted()){
            writer.close();
        }
        System.out.println("数据存储进程已终止");
    }

    public static double Angle2Radian(double x){
        return x;
    }

}
