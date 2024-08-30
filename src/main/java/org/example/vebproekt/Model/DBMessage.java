package org.example.vebproekt.Model;

import lombok.Data;

@Data
public class DBMessage {
    private String sender;
    private String otherUser;
    private String content;

    public DBMessage(String sender, String otherUser, String content) {
        this.sender = sender;
        this.otherUser = otherUser;
        this.content = content;
    }
}
