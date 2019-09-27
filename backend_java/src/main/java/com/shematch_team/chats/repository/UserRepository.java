package com.shematch_team.chats.repository;

import com.shematch_team.chats.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByVkId(String vkId);
}
