package org.example.vebproekt.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Chat(UserInfo user1, UserInfo user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    @ManyToOne
    @JoinColumn(name = "user_1", nullable = false)
    private UserInfo user1;

    @ManyToOne
    @JoinColumn(name = "user_2", nullable = false)
    private UserInfo user2;


    public Chat() {
    }
}
