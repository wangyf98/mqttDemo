package wyf.handler;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "outPutChannel")
public interface MqttGateWay {
    void sendToMqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);
}
