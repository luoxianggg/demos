package com.lx.project.demo1.service;

public interface AccountEmailService {
    void sendMail(String to, String subject, String htmlText);
}
