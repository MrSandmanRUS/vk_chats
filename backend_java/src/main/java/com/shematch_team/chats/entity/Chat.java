package com.shematch_team.chats.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "interest")
    private String interest;
    @Column(name = "preview")
    private String preview;
    @Column(name = "link")
    private String link;
}
