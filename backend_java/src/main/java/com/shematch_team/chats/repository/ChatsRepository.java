package com.shematch_team.chats.repository;

import com.shematch_team.chats.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, Long> {}
