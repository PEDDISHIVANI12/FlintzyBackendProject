package com.flintzy.dto;

import java.util.List;

import lombok.Data;

@Data
public class FacebookPageRequest {
    private Long appUserId;
    private List<FacebookPageDTO> pages;
}
