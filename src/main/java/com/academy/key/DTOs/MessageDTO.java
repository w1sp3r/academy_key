package com.academy.key.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MessageDTO {

    private String messageSid;

    private String body;

    private String from;

    private String to;

    private LocalDateTime createdDate;

}
