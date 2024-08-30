package org.example.vebproekt.Repository;

import org.example.vebproekt.Model.Chat;
import org.example.vebproekt.Model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
    Chat findByUser1AndUser2(UserInfo user1, UserInfo user2);
}
