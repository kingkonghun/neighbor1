package com.anabada.neighbor.used.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportType {
    private long reportTypeId; // 신고타입번호
    private String reportTypeName; // 신고타입
}
