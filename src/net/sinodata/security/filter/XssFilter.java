package net.sinodata.security.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * XSS过滤
 *
 * @author Mark sunlightcs@gmail.com
 */
public class XssFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);
		HttpServletResponse response1 = (HttpServletResponse)response;
		//SAMEORIGIN：页面只能加载入同源域名下的页面
		//		X-Frame-Options三个参数:
		//		1、DENY
		//		表示该页面不允许在frame中展示，即便是在相同域名的页面中嵌套也不允许。
		//		2、SAMEORIGIN
		//		表示该页面可以在相同域名页面的frame中展示。
		//		3、ALLOW-FROM uri
		//		表示该页面可以在指定来源的frame中展示。
		response1.setHeader("X-Frame-Options", "DENY");
	    //1; mode=block 启用XSS保护，并在检查到XSS攻击时，停止渲染页面
		response1.setHeader("X-XSS-Protection", "1; mode=block");
		chain.doFilter(xssRequest, response1);
	}

	@Override
	public void destroy() {
	}

}