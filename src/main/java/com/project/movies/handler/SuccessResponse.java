package com.project.movies.handler;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SuccessResponse<T> {
    private String message;
    private LocalDateTime timeStamp;
    private T data;
}
