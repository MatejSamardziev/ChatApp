package org.example.vebproekt.Service;

import org.example.vebproekt.Model.Chat;
import org.example.vebproekt.Model.UserInfo;

public interface ChatService {

    Chat findOrCreateChat(UserInfo user1,UserInfo user2);

}
