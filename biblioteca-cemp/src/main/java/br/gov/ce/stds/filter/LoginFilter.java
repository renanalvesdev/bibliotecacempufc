package br.gov.ce.stds.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {
	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
	@Override
	public void destroy() {}
    
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		try{
			HttpServletRequest httpReq = (HttpServletRequest)req;
			HttpServletResponse httpRes = (HttpServletResponse)res;
			HttpSession session = httpReq.getSession(true);
			String url = httpReq.getRequestURL().toString();
			if(session.getAttribute("usuario")==null && precisaAutenticar(url)){
				httpRes.sendRedirect(httpReq.getContextPath()+"/login.xhtml");								
			}else{
				chain.doFilter(req, res);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	boolean precisaAutenticar(String url){
		return !url.contains("login.xhtml")
		&& !url.contains("/externo/");
//		&& !url.endsWith(".jpg")
//		&& !url.endsWith(".gif")
//		&& !url.endsWith(".png");
	}
}
