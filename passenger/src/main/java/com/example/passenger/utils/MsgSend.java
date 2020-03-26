package com.example.passenger.utils;

import com.example.passenger.config.AmqpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MsgSend implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendMsg(String routingKey, String content) {
        this.rabbitTemplate.setReturnCallback(this);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, routingKey, content, correlationData);
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + cause);
        }
    }

    /**
     * 失败回退消息
     */
    @Override
    public final void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        this.logger.info("return-----message:" + message.toString() + "---replyCode:" + replyCode + "----replyText:" + replyText + "----exchange:" + exchange + "----routingKey:" + routingKey);
    }

}
