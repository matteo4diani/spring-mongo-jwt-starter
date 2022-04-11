package com.sashacorp.springmongojwtapi.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sashacorp.springmongojwtapi.security.UserDetailsImpl;

@Component
public class HttpRequestLogFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(HttpRequestLogFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authenticatedUser = null;
		try {
			UserDetails userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			authenticatedUser = userDetails.getUsername();
		} catch (Exception e) {
			authenticatedUser = "undefined";
		}
		LOG.info(request.getMethod() + " to " + request.getRequestURI() + " from " + authenticatedUser);
		filterChain.doFilter(request, response);
	}
}
