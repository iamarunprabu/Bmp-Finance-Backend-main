package com.security.JWT.Filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.security.JWT.Constant.SecurityConstant.*;
import com.security.JWT.Domain.HttpResponse;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException Exception) throws IOException, ServletException {
		
		HttpResponse httpresponse = new HttpResponse(UNAUTHORIZED.value(),UNAUTHORIZED,UNAUTHORIZED.getReasonPhrase().toUpperCase(),ACCESS_DENIED_MESSAGE);
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(UNAUTHORIZED.value());
		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream,httpresponse);
		outputStream.flush();
		
		
	}

}
