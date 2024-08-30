package org.example.vebproekt.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ChatLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserInfo sender;

    @Column(nullable = false)
    private String content;

    private LocalDateTime timestamp;

    private Boolean isRead=false;

    public ChatLine() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatLine(Chat chat, UserInfo sender, String content) {
        this.chat = chat;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
