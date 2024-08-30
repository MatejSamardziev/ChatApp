package org.example.vebproekt.Repository;

import org.example.vebproekt.Model.Chat;
import org.example.vebproekt.Model.ChatLine;
import org.example.vebproekt.Model.UserInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLineRepository extends JpaRepository<ChatLine,Long> {
    List<ChatLine>findByChatOrderByTimestamp(Chat chat, Sort sort);

    List<ChatLine>findByChatAndIsReadFalse(Chat chat);



    //site unread chat lines kade shto user e user1 ili user2 od Chat i user ne e sender na chatlineot
    @Query("SELECT cl FROM ChatLine cl WHERE cl.chat IN " +
            "(SELECT c FROM Chat c WHERE c.user1 = :user OR c.user2 = :user) " +
            "AND cl.sender <> :user AND cl.isRead = false")
    List<ChatLine> findUnreadMessagesForUser(@Param("user") UserInfo user);
}
