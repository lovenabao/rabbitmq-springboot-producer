package com.springboot.rabbitmq.producer;

import com.springboot.rabbitmq.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * @author shkstart
 * @create 2019-06-20 17:26
 */
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback=new RabbitTemplate.ConfirmCallback() {
        /**
         *
         * @param correlationData
         * @param ack  返回的结果
         * @param cause  返回的异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("CorrelationData :   ==  " + correlationData);
            System.out.println("ack : == " + ack);
            if(!ack){
                System.out.println(" 异常处理 ..... ");
            }
            System.out.println("cause :  ===  "+cause);
        }
    };
    final RabbitTemplate.ReturnCallback returnCallback=new RabbitTemplate.ReturnCallback() {
        /**
         *
         * @param message
         * @param replyCode
         * @param replyText
         * @param exchange
         * @param routingKey
         */
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("return exchange : " + exchange + " , " + " routingKey : " + routingKey);
            System.out.println("return replyCode : " + replyCode + " replyText : " + replyText);
            System.out.println("Message : " + message);
        }
    };

    public void send(Object message, Map<String,Object> properties) throws Exception{
        MessageHeaders messageHeaders=new MessageHeaders(properties);
        Message message1 = MessageBuilder.createMessage(message,messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData=new CorrelationData();
        correlationData.setId("rabbitmq :"+UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("exchange_1","springboot.hello",message1,correlationData);
    }

    public void sendOrder(Order order) throws Exception{
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData=new CorrelationData("rabbitmq :"+UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("exchange-2","springboot.hello",order,correlationData);
    }
}
