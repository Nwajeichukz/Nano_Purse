package com.NanoPurse.configuration;
import io.github.bucket4j.*;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitingFilter implements Filter {
    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket resolveBucket(String key) {
        return bucketCache.computeIfAbsent(key, k -> createNewBucket());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String username = (req.getUserPrincipal() != null) ? req.getUserPrincipal().getName() : req.getRemoteAddr();
        Bucket bucket = resolveBucket(username);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setStatus(429);
            res.getWriter().write("Too Many Requests - Rate limit exceeded");
        }
    }

}
