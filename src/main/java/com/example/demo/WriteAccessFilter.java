package com.example.demo;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@ConditionalOnExpression("${write-access-filter.enabled:false}")
public class WriteAccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String writeAccessHeader = httpRequest.getHeader("X-Write-Access");

        // Check if the request is a write operation
        boolean isWriteOperation = httpRequest.getMethod().matches("POST|PUT|PATCH|DELETE");

        if (isWriteOperation && (writeAccessHeader == null || !writeAccessHeader.equals("true"))) {
            // If it's a write operation but the header is missing or not true, block the request
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\": \"Write access denied.\"}");
            return;
        }

        // If the header is present and valid, or the request is not a write operation, proceed normally
        chain.doFilter(request, response);
    }
}