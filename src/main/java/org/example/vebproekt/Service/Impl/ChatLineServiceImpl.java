package org.example.vebproekt.Service.Impl;

import org.example.vebproekt.Model.Chat;
import org.example.vebproekt.Model.ChatLine;
import org.example.vebproekt.Model.UserInfo;
import org.example.vebproekt.Repository.ChatLineRepository;
import org.example.vebproekt.Service.ChatLineService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatLineServiceImpl implements ChatLineService {

    private final ChatLineRepository chatLineRepository;

    public ChatLineServiceImpl(ChatLineRepository chatLineRepository) {
        this.chatLineRepository = chatLineRepository;
    }

    @Override
    public ChatLine create(Chat chat, UserInfo sender, String content) {
        return chatLineRepository.save(new ChatLine(chat,sender,content));
    }

    @Override
    public List<ChatLine> getUnreadChatLinesForUser(UserInfo recipient){
        return chatLineRepository.findUnreadMessagesForUser(recipient);
    }

    @Override
    public void markMessagesAsRead(Chat chat){
        List<ChatLine>unreadChats=chatLineRepository.findByChatAndIsReadFalse(chat);
        for(ChatLine c:unreadChats){
            c.setIsRead(true);
        }
        chatLineRepository.saveAll(unreadChats);
    }

    @Override
    public List<ChatLine> getChatLines(Chat chat) {
        return chatLineRepository.findByChatOrderByTimestamp(chat, Sort.by(Sort.Direction.ASC, "timestamp"));
    }
}
