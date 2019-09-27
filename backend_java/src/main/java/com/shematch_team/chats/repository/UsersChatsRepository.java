package com.shematch_team.chats.repository;

import com.shematch_team.chats.entity.UsersChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersChatsRepository extends JpaRepository<UsersChats, Long> {}
