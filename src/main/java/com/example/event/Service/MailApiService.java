package com.example.event.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailApiService {

    private final Resend resend;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.from-name}")
    private String mailFromName;

    public MailApiService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public String sendHtml(String to, String subject, String html) throws ResendException {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(mailFromName + " <" + mailFrom + ">")
                .to(List.of(to))
                .subject(subject)
                .html(html)
                .build();

        CreateEmailResponse data = resend.emails().send(params);
        return data.getId();
    }

    public String sendText(String to, String subject, String text) throws ResendException {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(mailFromName + " <" + mailFrom + ">")
                .to(List.of(to))
                .subject(subject)
                .text(text)
                .build();

        CreateEmailResponse data = resend.emails().send(params);
        return data.getId();
    }
}
