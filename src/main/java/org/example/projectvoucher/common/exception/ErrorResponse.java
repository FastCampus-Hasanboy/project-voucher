package org.example.projectvoucher.common.exception;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record ErrorResponse(
        String message,
        LocalDateTime timeStamp,
        UUID traceId) {
}