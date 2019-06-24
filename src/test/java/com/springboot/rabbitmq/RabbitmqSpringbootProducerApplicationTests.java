package com.springboot.rabbitmq;

import com.springboot.rabbitmq.entity.Order;
import com.springboot.rabbitmq.producer.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringbootProducerApplicationTests {

    @Autowired
    RabbitSender rabbitSender;

    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    @Test
    public void testSender1() throws Exception{
        Map<String,Object> properties=new HashMap<>();
        properties.put("number","1234567");
        properties.put("name","xiaochen");
        properties.put("orderNumber","6325467212531235");
        properties.put("send_time",simpleDateFormat.format(new Date()));
        rabbitSender.send("Hello RabbitMQ For Springboot",properties);
    }
    @Test
    public void testSender2() throws Exception{
        Order order=new Order();
        order.setId("12123");
        order.setName("xiaochen");
        rabbitSender.sendOrder(order);
    }

}
