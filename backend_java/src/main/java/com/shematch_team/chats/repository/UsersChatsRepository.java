package com.shematch_team.chats.repository;

import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.entity.UsersChats;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface UsersChatsRepository extends JpaRepository<UsersChats, Long> {
    ArrayList<UsersChats> findAllByUserIdAndIdLessThanEqualOrderById(Long userId, Long id, Pageable pageable);
    ArrayList<UsersChats> findAllByUserIdOrderById(Long userId, Pageable pageable);

    ArrayList<UsersChats> findAllByChatIdOrderById(Long id, PageRequest pageable);
}
