package com.healthrx.webhooksqlsolver.runner;

import com.healthrx.webhooksqlsolver.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final WebhookService webhookService;

    @Autowired
    public StartupRunner(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting webhook flow on application startup...");
        webhookService.processWebhookFlow();
        System.out.println("Webhook flow completed.");
    }
}
