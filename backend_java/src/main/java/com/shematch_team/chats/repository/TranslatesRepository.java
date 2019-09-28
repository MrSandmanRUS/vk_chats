package com.shematch_team.chats.repository;

import com.shematch_team.chats.entity.Translate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslatesRepository extends JpaRepository<Translate, Long> {

    Optional<Translate> findByEnglish(String english);
}
