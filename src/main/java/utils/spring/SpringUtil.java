package utils.spring;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SpringUtil
{
	public static Object getBean(HttpServletRequest request, String name)
	{
		WebApplicationContext ctx  = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		return ctx.getBean(name);
	}
	
	public static Object getBean(ServletContext servletContext, String name)
	{
		WebApplicationContext ctx  = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		return ctx.getBean(name);		
	}
}