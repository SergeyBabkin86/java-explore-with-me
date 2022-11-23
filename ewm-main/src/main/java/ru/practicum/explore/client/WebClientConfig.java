package ru.practicum.explore.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    WebClient webClient = WebClient.builder().build();
}
