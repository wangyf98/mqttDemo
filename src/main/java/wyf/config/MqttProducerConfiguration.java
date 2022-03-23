package wyf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import wyf.entity.MqttProperties;

import javax.annotation.Resource;

@Configuration
@IntegrationComponentScan
/**
 * mqtt生产端配置
 */
public class MqttProducerConfiguration {

    @Resource
    private MqttProperties mqttProperties;

    @Bean
    public MessageChannel outPutChannel(){
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "outPutChannel")
    public MessageHandler outBound(MqttPahoClientFactory mqttPahoClientFactory){
        MqttPahoMessageHandler mqttPahoMessageHandler = new MqttPahoMessageHandler(mqttProperties.getClientId(), mqttPahoClientFactory);
        mqttPahoMessageHandler.setAsync(true);
        return mqttPahoMessageHandler;
    }
}
