package com.store.CamelitesMinimart;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.core.Ordered;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long start = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;

            logRequest(wrappedRequest);
            logResponse(wrappedResponse, duration);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        String contentType = request.getContentType();

        if (contentType != null && contentType.contains("text/html")) {
            // Log only metadata for HTML requests
            log.info("HTTP REQUEST → method={} uri={} query={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString());
        } else {
            // Log full request body for other content types
            log.info("HTTP REQUEST → method={} uri={} query={} body={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    body.isEmpty() ? "<empty>" : body);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        String contentType = response.getContentType();

        if (contentType != null && contentType.contains("text/html")) {
            // Log only metadata for HTML responses
            log.info("HTTP RESPONSE ← status={} duration={}ms",
                    response.getStatus(),
                    duration);
        } else {
            // Log full response body for other content types
            log.info("HTTP RESPONSE ← status={} duration={}ms body={}",
                    response.getStatus(),
                    duration,
                    body.isEmpty() ? "<empty>" : body);
        }
    }
}