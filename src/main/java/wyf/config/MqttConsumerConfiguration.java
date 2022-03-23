package wyf.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import wyf.entity.MqttProperties;
import wyf.handler.MqttInboundHandler;

import javax.annotation.Resource;

@Configuration
// 开启对Spring-Integration组件的扫描
@IntegrationComponentScan
/**
 * mqtt消费端配置
 */
public class MqttConsumerConfiguration {

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private MqttInboundHandler mqttInboundHandler;

    @Bean
    public MessageChannel inputChannel(){
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{mqttProperties.getUrl()});
        mqttConnectOptions.setUserName(mqttProperties.getUsername());
        mqttConnectOptions.setPassword(mqttProperties.getPassword().toCharArray());
        // 客户端连接服务端超时时间
        mqttConnectOptions.setConnectionTimeout(10);
        // 从消息发送到消息接收的时间间隔，超过视为断连
        mqttConnectOptions.setKeepAliveInterval(90);
        // 断连是是否清除会话
        mqttConnectOptions.setCleanSession(true);
        // 断线重连
        mqttConnectOptions.setAutomaticReconnect(true);
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(mqttConnectOptions);
        return clientFactory;
    }

    @Bean
    public MessageProducer inbound(MqttPahoClientFactory mqttPahoClientFactory){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttProperties.getClientId(), mqttPahoClientFactory, "hello/#");
        /**
         * By default, the default DefaultPahoMessageConverter produces a message with a String payload with the following headers:
         *  mqtt_topic: The topic from which the message was received
         *  mqtt_duplicate: true if the message is a duplicate
         *  mqtt_qos: The quality of service
         */
        adapter.setConverter(new DefaultPahoMessageConverter());
        // 服务质量：0：至多一次；1：至少一次；2：正好一次
        adapter.setQos(1);
        // 由消息队列发送到哪里
        adapter.setOutputChannel(inputChannel());
        return adapter;
    }


    @Bean
    /**
     * ServiceActivator是用于将服务实例连接到消息传递系统的通用端点。必须配置输入消息通道，如果要调用的服务方法能够返回值，还可以提供输出消息通道
     */
    @ServiceActivator(inputChannel = "inputChannel")
    public MessageHandler inBoundHandler(){
        return mqttInboundHandler;
    }

}
