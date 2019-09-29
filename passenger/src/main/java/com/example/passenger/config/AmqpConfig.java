package com.example.passenger.config;


import com.example.passenger.utils.IPUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.*;

@Configuration
public class AmqpConfig {
    public static final String EXCHANGE = "ExChange_MID";
    public static final String ROUTINGKEY = "occ.*.*";
    public static final String QUEUEA = "topic.queueA";
    public static final String QUEUEB = "topic.queueB";
    public static List<Map<String,String>> deviceList=new ArrayList<>();
    public static List<String> updateList=new ArrayList<>();
    //public static List<String> downloadList=new ArrayList<>();
    public static List<String> insertStyleList=new ArrayList<>();
    //public static List<String> selectList=new ArrayList<>();

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String vhost;


    @Bean
    public ConnectionFactory connectionFactory() {
        //定义一个连接工厂
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        //设置服务地址
        connectionFactory.setHost(host);
        //AMQP 端口 5672
        connectionFactory.setPort(port);
        //Virtual Hosts
        connectionFactory.setVirtualHost(vhost);
        //用户名
        connectionFactory.setUsername(username);
        //密码
        connectionFactory.setPassword(password);
        //发送确认
        connectionFactory.setPublisherConfirms(true);
        //失败退回
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    /*@Bean
    public TopicExchange defaultExchange() {
        return new TopicExchange(EXCHANGE);
    }*/

    @Bean
    public Queue queueA() {
        return new Queue(QUEUEA, true); //队列持久
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUEB, true); //队列持久
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueA()).to(new TopicExchange(AmqpConfig.EXCHANGE)).with(AmqpConfig.ROUTINGKEY);
    }

    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(queueB()).to(new TopicExchange(AmqpConfig.EXCHANGE)).with(AmqpConfig.ROUTINGKEY);
    }

    /*  @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(AmqpConfig.ROUTINGKEY);
    }*/

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(this.connectionFactory());
        template.setMandatory(true);
        return template;
    }

   /* @Bean
    //信息格式为json
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }*/

    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        //加载处理消息A的队列
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        //设置接收多个队列里面的消息，这里设置接收队列A
        //假如想一个消费者处理多个队列里面的信息可以如下设置：
        //container.setQueues(queueA(),queueB(),queueC());
        container.setQueues(queueA());
        container.setExposeListenerChannel(true);
        //设置最大的并发的消费者数量
        container.setMaxConcurrentConsumers(10);
        //最小的并发消费者的数量
        container.setConcurrentConsumers(1);
        //设置确认模式手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                channel.basicQos(1);
                byte[] body = message.getBody();
                String msg=new String(body);
                String routingKey=message.getMessageProperties().getReceivedRoutingKey();
                System.out.println("接收处理队列当中的消息:" + msg);
                //System.out.println("接收处理:"+routingKey);

                //获取消息头
                String goOnline=msg.substring(0,5);
                //初始化ip地址
                String ipPath= null;
                //判断上线
                if(msg.substring(0,5).equals("ONLI:")){
                    System.out.println("上线:"+goOnline);
                    System.out.println("ip截取:"+msg.substring(5,14));
                    //转换ip地址
                    ipPath= IPUtil.longToIP(Long.valueOf(msg.substring(5,14)));
                    //初始化map
                    Map<String,String> map=new HashMap<>();
                    map.put(ipPath,routingKey);
                    //添加至上线设备列表
                    deviceList.add(map);
                }
                //判断下线
                if(msg.substring(0,5).equals("OFLI:")){
                    System.out.println("下线:"+goOnline);
                    System.out.println("ip截取:"+msg.substring(5,14));
                    //转换ip地址
                    ipPath= IPUtil.longToIP(Long.valueOf(msg.substring(5,14)));
                    //初始化map
                    Map<String,String> map=new HashMap<>();
                    map.put(ipPath,routingKey);
                    //删除下线设备
                    deviceList.remove(map);
                }

                if (msg.substring(0,5).equals("SQLS:")){
                    //截取sql类型1:操作,0:查询
                    String sqlType=msg.substring(msg.indexOf("<SQLType>")+9,msg.indexOf("</SQLType>"));
                    //截取操作对象
                    String typeName=msg.substring(msg.indexOf("<TableName>")+11,msg.indexOf("</TableName>"));
                    //截取操作sql
                    String sql=msg.substring(msg.indexOf("<SQL>")+5,msg.indexOf("</SQL>"));
                    //判断是sql类型
                    if(sqlType.equals("1")){
                        if(typeName.equals("Play_Style") || typeName.equals("Style_Content")){
                           insertStyleList.add(sql);
                        }
                    }
                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        return container;
    }

}
