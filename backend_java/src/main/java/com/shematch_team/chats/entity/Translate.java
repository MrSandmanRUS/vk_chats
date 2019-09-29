package com.shematch_team.chats.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "translates")
public class Translate {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "russian")
    private String russian;

    @Column(name = "english")
    private String english;

    @Column(name = "created_when")
    private Date createdWhen;

    @Column(name = "usage_count")
    private BigInteger usageCount;
}
