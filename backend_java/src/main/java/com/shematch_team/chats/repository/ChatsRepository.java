package com.shematch_team.chats.repository;

import com.shematch_team.chats.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, Long> {
    ArrayList<Chat> findAllByOrderByIdDesc(Pageable pageable);
    ArrayList<Chat> findAllByIdLessThanEqualOrderByIdDesc(Long id, Pageable pageable);
    Chat findFirstByInterest(String interest);
}
