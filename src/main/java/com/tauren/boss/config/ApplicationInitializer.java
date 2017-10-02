package com.tauren.boss.config;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Web应用初始化
 * 
 * @author niuyandong
 * @since 2017年9月23日上午12:10:46
 */
public class ApplicationInitializer implements WebApplicationInitializer {
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(SpringContextConfig.class);
        
        servletContext.addListener(new ContextLoaderListener(rootContext));
        // CAS登出监听器
        servletContext.addListener(new SingleSignOutHttpSessionListener());
        
        ConfigurableEnvironment environment = rootContext.getEnvironment();
        try {
            environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:/system.properties"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // 字符编码过滤器
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", new CharacterEncodingFilter());
        characterEncodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
        
        // 该过滤器用于实现单点登出功能，可选配置。
        FilterRegistration.Dynamic casSingleSignOutFilter = servletContext.addFilter("CAS Single Sign Out Filter", new SingleSignOutFilter());
        Map<String, String> casSingleSignOutFilterParams = new HashMap<String, String>();
        casSingleSignOutFilterParams.put("casServerUrlPrefix", environment.getProperty("casServerUrlPrefix"));
        casSingleSignOutFilter.setInitParameters(casSingleSignOutFilterParams);
        casSingleSignOutFilter.addMappingForUrlPatterns(null, false, "/*");
        // 该过滤器负责用户的认证工作，必须启用它
        FilterRegistration.Dynamic casAuthenticationFilter = servletContext.addFilter("CAS Authentication Filter", new AuthenticationFilter());
        Map<String, String> casAuthenticationFilterParams = new HashMap<String, String>();
        casAuthenticationFilterParams.put("casServerLoginUrl", environment.getProperty("casServerLoginUrl"));
        casAuthenticationFilterParams.put("serverName", environment.getProperty("serverName"));
        casAuthenticationFilter.setInitParameters(casAuthenticationFilterParams);
        casAuthenticationFilter.addMappingForUrlPatterns(null, false, "*.html");
        // 该过滤器负责对Ticket的校验工作，必须启用它
        Cas20ProxyReceivingTicketValidationFilter tic = new Cas20ProxyReceivingTicketValidationFilter();
        tic.setMillisBetweenCleanUps(30 * 60 * 1000);
        FilterRegistration.Dynamic casValidationFilter = servletContext.addFilter("CAS Validation Filter", tic);
        Map<String, String> casValidationFilterParams = new HashMap<String, String>();
        casValidationFilterParams.put("casServerUrlPrefix", environment.getProperty("casServerUrlPrefix"));
        casValidationFilterParams.put("serverName", environment.getProperty("serverName"));
        casValidationFilter.setInitParameters(casValidationFilterParams);
        casValidationFilter.addMappingForUrlPatterns(null, false, "*.html");
        //
        FilterRegistration.Dynamic casWrapperFilter = servletContext.addFilter("CAS HttpServletRequest Wrapper Filter", new HttpServletRequestWrapperFilter());
        casWrapperFilter.addMappingForUrlPatterns(null, false, "*.html");
        // 该过滤器使得开发者可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
        // 比如AssertionHolder.getAssertion().getPrincipal().getName()
        FilterRegistration.Dynamic casAssertionFilter = servletContext.addFilter("CAS Assertion Thread Local Filter", new AssertionThreadLocalFilter());
        casAssertionFilter.addMappingForUrlPatterns(null, false, "*.html");
        
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(SpringMVCConfig.class);
        
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
    
}
