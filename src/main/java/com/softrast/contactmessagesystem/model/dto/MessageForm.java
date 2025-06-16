package com.softrast.contactmessagesystem.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MessageForm {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\+7\\d{10}$",
            message = "Phone must be in format +7XXXXXXXXXX")
    private String phone;

    @NotNull(message = "Topic is required")
    private Long topicId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "CAPTCHA verification is required")
    private String captchaResponse;
}
