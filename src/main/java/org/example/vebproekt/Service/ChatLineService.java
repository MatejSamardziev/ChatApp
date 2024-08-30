package org.example.vebproekt.Service;

import org.example.vebproekt.Model.Chat;
import org.example.vebproekt.Model.ChatLine;
import org.example.vebproekt.Model.UserInfo;

import java.util.List;

public interface ChatLineService {
    ChatLine create(Chat chat, UserInfo sender, String content);

    List<ChatLine> getChatLines(Chat chat);

    List<ChatLine> getUnreadChatLinesForUser(UserInfo recipient);

    void markMessagesAsRead(Chat chat);
}
