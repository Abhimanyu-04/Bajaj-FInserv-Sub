package com.healthrx.webhooksqlsolver.service;

import com.healthrx.webhooksqlsolver.dto.SolutionRequest;
import com.healthrx.webhooksqlsolver.dto.WebhookRequest;
import com.healthrx.webhooksqlsolver.dto.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate restTemplate;
    private final SqlSolverService sqlSolverService;

    @Autowired
    public WebhookService(RestTemplate restTemplate, SqlSolverService sqlSolverService) {
        this.restTemplate = restTemplate;
        this.sqlSolverService = sqlSolverService;
    }

    public void processWebhookFlow() {
        try {
            // Step 1: Generate webhook
            WebhookResponse webhookResponse = generateWebhook();
            
            if (webhookResponse != null && webhookResponse.getWebhook() != null && webhookResponse.getAccessToken() != null) {
                System.out.println("Webhook generated successfully: " + webhookResponse.getWebhook());
                System.out.println("Access token received");
                
                // Step 2: Solve SQL problem based on regNo
                String regNo = "22BCE1165"; // Using the regNo from the request
                String sqlSolution = sqlSolverService.solveSqlProblem(regNo);
                
                System.out.println("SQL Solution generated for regNo " + regNo + ":");
                System.out.println(sqlSolution);
                
                // Step 3: Submit solution to webhook
                submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), sqlSolution);
                
            } else {
                System.err.println("Failed to generate webhook or receive proper response");
            }
            
        } catch (Exception e) {
            System.err.println("Error in webhook flow: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private WebhookResponse generateWebhook() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        
        WebhookRequest request = new WebhookRequest("Abhimanyu Rai", "22BCE1165", "abhimanyu@gmail.com");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, WebhookResponse.class);
            
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error generating webhook: " + e.getMessage());
            return null;
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        
        HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                webhookUrl, HttpMethod.POST, entity, String.class);
            
            System.out.println("Solution submitted successfully!");
            System.out.println("Response: " + response.getBody());
            
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
