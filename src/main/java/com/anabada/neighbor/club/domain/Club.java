package com.anabada.neighbor.club.domain;

import lombok.*;

import javax.persistence.Entity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Club {
    private long postId;    //게시글번호
    private long memberId;  //멤버아이디
    private long hobbyId;   //취미번호
    private int maxMax;     //최대인원수
    private int nowMan;     //현재인원수

}
