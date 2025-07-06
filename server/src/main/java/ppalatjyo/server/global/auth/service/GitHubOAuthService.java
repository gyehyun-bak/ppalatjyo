package ppalatjyo.server.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ppalatjyo.server.global.auth.exception.GitHubAccessTokenExchangeException;
import ppalatjyo.server.global.auth.exception.GitHubEmailFetchingFailedException;
import ppalatjyo.server.global.auth.exception.GitHubNoPrimaryEmailException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubOAuthService {

    private final RestTemplate restTemplate;
    @Value("${oauth.github.client-id}")
    private String clientId;
    @Value("${oauth.github.client-secret}")
    private String clientSecret;

    public String getEmailFromCode(String code) {
        String accessToken = requestAccessToken(code);
        return requestEmail(accessToken);
    }

    private String requestAccessToken(String code) {
        String tokenUrl = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, String> request = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code
        );

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, requestEntity, Map.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new GitHubAccessTokenExchangeException();
        }

        return (String) response.getBody().get("access_token");
    }

    private String requestEmail(String accessToken) {
        String userEmailUrl = "https://api.github.com/user/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(userEmailUrl, HttpMethod.GET, entity, List.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new GitHubEmailFetchingFailedException();
        }

        // GitHub API는 기본 이메일이 여러 개일 수 있으므로 primary & verified된 것 사용
        for (Object item : response.getBody()) {
            if (item instanceof Map emailInfo) {
                if (Boolean.TRUE.equals(emailInfo.get("primary")) && Boolean.TRUE.equals(emailInfo.get("verified"))) {
                    return (String) emailInfo.get("email");
                }
            }
        }

        throw new GitHubNoPrimaryEmailException();
    }
}
