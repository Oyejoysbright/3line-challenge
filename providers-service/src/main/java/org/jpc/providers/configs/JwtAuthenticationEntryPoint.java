package org.jpc.providers.configs;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.jpc.tool.models.CustomResponse;
import org.jpc.tool.utils.Helper;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException authException) throws IOException, ServletException {
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		log.info("::::::::: JWT Exception message ===> " + httpServletRequest.getAttribute("exception"));

		CustomResponse response = new CustomResponse(true, httpServletRequest.getAttribute("exception").toString(), null);

		byte[] body = Helper.MAPPER.writeValueAsBytes(response);
		httpServletResponse.getOutputStream().write(body);
	}
}