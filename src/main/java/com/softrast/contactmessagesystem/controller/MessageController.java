package com.softrast.contactmessagesystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softrast.contactmessagesystem.model.dto.MessageForm;
import com.softrast.contactmessagesystem.model.entity.Contact;
import com.softrast.contactmessagesystem.model.entity.Message;
import com.softrast.contactmessagesystem.model.entity.MessageTopic;
import com.softrast.contactmessagesystem.repository.ContactRepository;
import com.softrast.contactmessagesystem.repository.MessageRepository;
import com.softrast.contactmessagesystem.repository.MessageTopicRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final ContactRepository contactRepository;
    private final MessageTopicRepository topicRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/")
    @Transactional
    public String showForm(Model model) {
        List<MessageTopic> topics = topicRepository.findAll();

        model.addAttribute("messageForm", new MessageForm());
        model.addAttribute("topics", topics);
        return "form";
    }

    @PostMapping("/submit")
    public String submitForm(@Valid MessageForm messageForm,
                             BindingResult bindingResult,
                             @RequestParam("g-recaptcha-response") String captchaResponse,
                             Model model,
                             HttpServletRequest request) {

        messageForm.setCaptchaResponse(captchaResponse);

        if (!validateCaptcha(captchaResponse)) {
            bindingResult.reject("captcha.invalid", "CAPTCHA validation failed");
        }

        Contact contact = contactRepository.findByEmailAndPhone(messageForm.getEmail(), messageForm.getPhone())
                .orElseGet(() -> {
                    Contact newContact = new Contact();
                    newContact.setEmail(messageForm.getEmail());
                    newContact.setPhone(messageForm.getPhone());
                    newContact.setFullName(messageForm.getFullName());
                    return contactRepository.save(newContact);
                });

        Message message = new Message();
        message.setContact(contact);
        message.setTopic(topicRepository.findById(messageForm.getTopicId()).orElseThrow());
        message.setContent(messageForm.getMessage());
        messageRepository.save(message);

        model.addAttribute("message", message);

        return "result";
    }

    private boolean validateCaptcha(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            String params = "secret=6LermGIrAAAAAIbmdaoW5gAsd5TvwSvPmfGOoDG1" +
                    "&response=" + response;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> httpResponse = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JsonNode node = new ObjectMapper().readTree(httpResponse.body());
            return node.get("success").asBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}