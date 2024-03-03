package com.bassure.applicantservice.util;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;

@Service
public class ServiceCall {

    @Bean
    private final WebClient client() {
        return WebClient.create();
    }

    public <T> T performGetRequestWithQueryParams(String baseUrl, String path, MultiValueMap<String, String> queryParams, Class<T> responseType) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .queryParams(queryParams)
                .toUriString();
        return client().get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public <T> T performPost(String baseUrl, String path, String to, Class<T> responseType, String subject, String body) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .queryParam("to", to)
                .queryParam("subject", subject)
                .queryParam("body", body);

        String url = builder.build(false).toUriString();
        System.out.println(url+"service url......");
        return client().post()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public <T> T performGetRequestWithOutQueryParams(String baseUrl, String path, Class<T> responseType) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .toUriString();
        return client().get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public <T> T performGetRequestWithObjectQueryParam(String baseUrl, String path, String queryKey, List<Integer> queryValue, Class<T> responseType) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .queryParam(queryKey, queryValue)
                .toUriString();
        return client().get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

}
