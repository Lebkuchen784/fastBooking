package com.project.bookingService.config.email_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipientDTO {
    private String recipient;
    private String subject;
    private String messageBody;
}
