package org.example.vebproekt.Service.Impl;

import org.example.vebproekt.Model.Chat;
import org.example.vebproekt.Model.UserInfo;
import org.example.vebproekt.Repository.ChatRepository;
import org.example.vebproekt.Service.ChatService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public synchronized Chat findOrCreateChat(UserInfo user1, UserInfo user2) {
        //chat e shared resource i poradi toa ne e thread safe mozno e povekje threads vo isto vreme istiot
        //chat da go baraat i da vidat deka ne postoi vo isto vreme poradi sto dva pati ke se kreira chat vo bazata
        Chat chat=chatRepository.findByUser1AndUser2(user1,user2);
        if(chat==null){
            System.out.println("VLAGA TUKA CHAT");
            chat=chatRepository.findByUser1AndUser2(user2,user1);
        }
        if(chat==null){
            System.out.println("CHAT E USTE NULL");
            chat=new Chat(user1,user2);
            chatRepository.save(chat);
        }
        return chat;
    }
}
