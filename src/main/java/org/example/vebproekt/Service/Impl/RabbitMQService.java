package org.example.vebproekt.Service.Impl;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.example.vebproekt.Model.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public class RabbitMQService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;
    private final TopicExchange exchange;

    private final OnlineUserService onlineUserService;

    public RabbitMQService(SimpMessagingTemplate messagingTemplate, RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin, TopicExchange exchange, OnlineUserService onlineUserService) {
        this.messagingTemplate = messagingTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
        this.exchange = exchange;
        this.onlineUserService = onlineUserService;
    }

    public void sendMessage(Message message) {
        String sanitizedContent = HtmlUtils.htmlEscape(message.getContent());
        String routingKey = "chat.message." + message.getRecipient();

        // Check if the queue exists
        if (rabbitAdmin.getQueueProperties(routingKey) == null) {
            // Create a queue for the recipient if it doesn't exist
            Queue recipientQueue = new Queue(routingKey, false);
            rabbitAdmin.declareQueue(recipientQueue);

            // Bind the queue to the exchange with a routing key specific to the recipient
            Binding binding = BindingBuilder.bind(recipientQueue).to(exchange).with(routingKey);
            rabbitAdmin.declareBinding(binding);
        }

        // Send the message to the appropriate queue
        if (onlineUserService.isUserOnline(message.getRecipient())) {
            messagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/reply",
                    new Message(sanitizedContent, message.getSender(), message.getRecipient()));
        } else {
            System.out.println("BEING SENT TO RABBITMQ");
            rabbitTemplate.convertAndSend("chatExchange", routingKey,
                    new Message(sanitizedContent, message.getSender(), message.getRecipient()));
        }
    }



    public int getUnreadMessagesCount(String recipientUsername){
        String queueName = "chat.message." + recipientUsername;
        int messageCount = 0;

        // Check the message count in the queue
        QueueInformation queueInfo = rabbitAdmin.getQueueInfo(queueName);
        if (queueInfo != null) {
            messageCount = queueInfo.getMessageCount();
        }

        return messageCount;
    }

    public List<Message> fetchUnreadMessages(String recipientUsername) {
        String queueName = "chat.message." + recipientUsername;
        String routingKey = "chat.message." + recipientUsername;
        List<Message> messages = new ArrayList<>();
        if (rabbitAdmin.getQueueProperties(routingKey) == null){
            return messages;
        }

        // Retrieve messages from the queue
        Message message;
        while ((message = (Message) rabbitTemplate.receiveAndConvert(queueName)) != null) {
            messages.add(message);
        }

        return messages;
    }

    public void sendUnreadMessagesToUser(String recipientUsername){
        List<Message>messages=this.fetchUnreadMessages(recipientUsername);
        for(Message m:messages){
            messagingTemplate.convertAndSendToUser(recipientUsername,"/queue/unread-messages-rabbit",m);
        }
    }
}
