package ppalatjyo.server.global.auth.exception;

import org.springframework.web.client.RestClientException;

public class GitHubEmailFetchingFailedException extends GitHubOAuthException {
    public GitHubEmailFetchingFailedException(RestClientException e) {
        super(e);
    }

    public GitHubEmailFetchingFailedException() {
        super();
    }
}
