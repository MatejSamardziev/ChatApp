package org.example.vebproekt.Web;

import org.example.vebproekt.Model.*;
import org.example.vebproekt.Service.ChatLineService;
import org.example.vebproekt.Service.ChatService;
import org.example.vebproekt.Service.Impl.OnlineUserService;
import org.example.vebproekt.Service.Impl.RabbitMQService;
import org.example.vebproekt.Service.UserInfoService;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController {

    private final RabbitMQService rabbitMQService;

    private final OnlineUserService onlineUserService;

    private final ChatService chatService;

    private final UserInfoService userInfoService;

    private final ChatLineService chatLineService;


    public WebSocketController(RabbitMQService rabbitMQService, OnlineUserService onlineUserService, ChatService chatService, UserInfoService userInfoService, ChatLineService chatLineService) {

        this.rabbitMQService = rabbitMQService;
        this.onlineUserService = onlineUserService;
        this.chatService = chatService;
        this.userInfoService = userInfoService;
        this.chatLineService = chatLineService;
    }

    @MessageMapping("/message")
    public void sendMessage(Message message) {
       UserInfo sender=userInfoService.findUserByUsername(message.getSender());
       UserInfo recipient=userInfoService.findUserByUsername(message.getRecipient());
       Chat chat=chatService.findOrCreateChat(sender,recipient);
       chatLineService.create(chat,sender, message.getContent());
       rabbitMQService.sendMessage(message);
    }

    @MessageMapping("/fetch-unread-rabbit")
    public void fetchUnreadMessagesRabbit(SimpMessageHeaderAccessor headerAccessor) {
        String username = headerAccessor.getUser().getName();
        rabbitMQService.sendUnreadMessagesToUser(username);
    }

    @MessageMapping("/fetch-unread-db")
    @SendToUser("/queue/unread-from-db")
    public List<String> fetchUnreadMessagesDB(SimpMessageHeaderAccessor headerAccessor){
        String username = headerAccessor.getUser().getName();
        UserInfo currentUser=userInfoService.findUserByUsername(username);
        List<ChatLine>unreadChats=chatLineService.getUnreadChatLinesForUser(currentUser);
        List<String>unreadMsgSenders=new ArrayList<>();
        for(ChatLine c:unreadChats){
            unreadMsgSenders.add(c.getSender().getUsername());
        }
        return unreadMsgSenders;
    }

    @MessageMapping("/user-online")
    public void addUserOnline(SimpMessageHeaderAccessor headerAccessor) {
        String username = headerAccessor.getUser().getName();
        onlineUserService.addUser(username);

    }

    @MessageMapping("/user-offline")
    public void removeUserOnline(SimpMessageHeaderAccessor headerAccessor){
        String username=headerAccessor.getUser().getName();
        onlineUserService.removeUser(username);
    }

    @MessageMapping("/fetch-db")
    @SendToUser("/queue/db-messages")
    public List<DBMessage> fetchFromDB(SimpMessageHeaderAccessor headerAccessor,Map<String,String>payload){
        String username1=headerAccessor.getUser().getName();
        String username2= payload.get("otherUser");
        UserInfo currentUser=userInfoService.findUserByUsername(username1);
        UserInfo otherUser=userInfoService.findUserByUsername(username2);
        Chat chat=chatService.findOrCreateChat(currentUser,otherUser);
        List<ChatLine>chatLines=chatLineService.getChatLines(chat);
        List<DBMessage>messages=new ArrayList<>();
        for(ChatLine c:chatLines){
            messages.add(new DBMessage(c.getSender().getUsername(),username2,c.getContent()));
        }
        return messages;
    }

    @MessageMapping("/mark-as-read")
    public void markMessagesAsRead(SimpMessageHeaderAccessor headerAccessor,Map<String,String>payload){
        String username1 = headerAccessor.getUser().getName();
        String username2 = payload.get("otherUser");
        UserInfo currentUser = userInfoService.findUserByUsername(username1);
        UserInfo otherUser = userInfoService.findUserByUsername(username2);
        Chat chat=chatService.findOrCreateChat(currentUser,otherUser);
        chatLineService.markMessagesAsRead(chat);
    }
}