package ppalatjyo.server.global.auth.exception;

import org.springframework.web.client.RestClientException;

public class GitHubOAuthException extends RuntimeException{
    public GitHubOAuthException(RestClientException e) {
        super(e);
    }

    public GitHubOAuthException() {
        super();
    }
}
