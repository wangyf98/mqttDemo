package wyf.interfaces.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wyf.handler.MqttGateWay;

import javax.annotation.Resource;

@RestController
@RequestMapping("/say")
public class ProductMqttMessageController {
    @Resource
    private MqttGateWay mqttGateWay;

    @GetMapping("/hello/{message}")
    public void sendMqttMessage(@PathVariable("message") String message){
        mqttGateWay.sendToMqtt(message,"hello");
    }
}
