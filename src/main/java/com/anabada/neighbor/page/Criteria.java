package com.anabada.neighbor.page;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Component
public class Criteria {
    private int pageNum; //현재 페이지 번호
    private int amount;//한 페이지당 보여줄 게시글의 갯수
    private String type;
    private String keyword;

    public Criteria() {
        this(1, 10);}
    public Criteria(int pageNum, int amount) {
        this.pageNum=pageNum;
        this.amount=amount;
    }

    //UriComponentsBulider를 이용하여 링크생성(?)
    public String getListLink() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("pageNum", pageNum)
                .queryParam("amount", amount);
        return builder.toUriString();
    }

    public String[] getTypeArr() {// get으로 시작해야 mybatis에서 찾을수 있음
        return type == null ? new String[]{} : type.split("");
    }

    public int getOffset() {
        return (pageNum -1 ) * amount;
    }
}
