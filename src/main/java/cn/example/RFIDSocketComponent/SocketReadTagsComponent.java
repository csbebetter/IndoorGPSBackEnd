package cn.example.RFIDSocketComponent;

import cn.example.RFIDSocketComponent.impl.RealTimeInfoTagReportListenerImpl;
import com.impinj.octane.*;

public class SocketReadTagsComponent {
    private static final ImpinjReader reader = new ImpinjReader();
    public static void RFIDReaderInit(String hostname) {
        try {
            System.out.println("Connecting");

            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();
            ReportConfig report = settings.getReport();

            report.setIncludeAntennaPortNumber(true);
            report.setIncludePhaseAngle(true);
            report.setIncludeFirstSeenTime(true);
            report.setIncludeDopplerFrequency(true);
            report.setIncludeChannel(true);
            report.setIncludePeakRssi(true);
            report.setMode(ReportMode.Individual);
            report.setMode(ReportMode.Individual);

            settings.setRfMode(1002);
            settings.setSearchMode(SearchMode.DualTarget);
            settings.setSession(2);

            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1,2,3,4});

            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(true);
            antennas.getAntenna((short) 1).setIsMaxTxPower(true);
            antennas.getAntenna((short) 2).setIsMaxRxSensitivity(true);
            antennas.getAntenna((short) 2).setIsMaxTxPower(true);
            antennas.getAntenna((short) 3).setIsMaxRxSensitivity(true);
            antennas.getAntenna((short) 3).setIsMaxTxPower(true);
            antennas.getAntenna((short) 4).setIsMaxRxSensitivity(true);
            antennas.getAntenna((short) 4).setIsMaxTxPower(true);

            System.out.println("Applying Settings");
            reader.applySettings(settings);

            //reader.setTagReportListener(new AllInfoTagReportListenerImpl());
            reader.setTagReportListener(new RealTimeInfoTagReportListenerImpl());
        } catch (OctaneSdkException ex) {
            System.out.println("Octane SDK exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    public static void RFIDReaderStart() throws OctaneSdkException {
        reader.start();
    }

    public static void RFIDReaderStop() throws OctaneSdkException {
        reader.stop();
    }

    public static void RFIDReaderDisconnect() throws OctaneSdkException {
        reader.disconnect();
    }
}
