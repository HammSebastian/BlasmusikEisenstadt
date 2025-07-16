package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

public interface EmailService {

    void sendWelcomeEmail(String email, String emailTemplate, String username);
}
