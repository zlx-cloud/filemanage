package net.sinodata.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFilter extends FormAuthenticationFilter {

	Logger logger = LoggerFactory.getLogger(LoginFilter.class);

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		logger.info("login success--" + getUsername(request));
		String url = this.getSuccessUrl();
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hresp = (HttpServletResponse) response;
		hresp.sendRedirect(hreq.getContextPath() + url);
		return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		logger.info("login fail--" + getUsername(request));
		request.setAttribute("LOGIN_ERROR_MSG", "用户名或密码错误！");
		return super.onLoginFailure(token, e, request, response);
	}
}