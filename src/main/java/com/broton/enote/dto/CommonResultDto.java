package com.broton.enote.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title 共用資料回傳 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResultDto {

    @JsonAlias("code")
    private String code;
    @JsonAlias("msg")
    private String msg;
    @JsonAlias("data")
    private Object data;
}
