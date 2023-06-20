package com.anabada.neighbor.member.used.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private long productId; //상품아이디
    private long postId; //게시글번호
    private long categoryId; //카테고리아이디
    private String price; //상품가격
    private String productStatus; //판매여부
}
