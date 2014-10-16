package com.application.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MyWebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		// TODO Auto-generated method stub
		  XmlWebApplicationContext appContext = new XmlWebApplicationContext();
	      appContext.setConfigLocation("/WEB-INF/applicationContext.xml");
	      ServletRegistration.Dynamic dispatcher =
	    	        container.addServlet("dispatcher", new DispatcherServlet(appContext));
	    	      dispatcher.setLoadOnStartup(1);
	    	      dispatcher.addMapping("/");
		
	}

}
