package cn.example.websocket;

import cn.example.RFIDSocketComponent.config.RFIDSocketConfig;
import cn.example.RFIDSocketComponent.SocketReadTagsComponent;
import com.impinj.octane.OctaneSdkException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@SpringBootApplication(exclude = GsonAutoConfiguration.class)
public class DemoApplication {
    public static void main(String[] args) throws OctaneSdkException {
        SpringApplication.run(DemoApplication.class, args);
        SocketReadTagsComponent.RFIDReaderInit(RFIDSocketConfig.RFIDReaderHostname);
    }
}
