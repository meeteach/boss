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
 * WebӦ�ó�ʼ��
 * 
 * @author niuyandong
 * @since 2017��9��23������12:10:46
 */
public class ApplicationInitializer implements WebApplicationInitializer {
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(SpringContextConfig.class);
        
        servletContext.addListener(new ContextLoaderListener(rootContext));
        // CAS�ǳ�������
        servletContext.addListener(new SingleSignOutHttpSessionListener());
        
        ConfigurableEnvironment environment = rootContext.getEnvironment();
        try {
            environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:/system.properties"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // �ַ����������
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", new CharacterEncodingFilter());
        characterEncodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
        
        // �ù���������ʵ�ֵ���ǳ����ܣ���ѡ���á�
        FilterRegistration.Dynamic casSingleSignOutFilter = servletContext.addFilter("CAS Single Sign Out Filter", new SingleSignOutFilter());
        Map<String, String> casSingleSignOutFilterParams = new HashMap<String, String>();
        casSingleSignOutFilterParams.put("casServerUrlPrefix", environment.getProperty("casServerUrlPrefix"));
        casSingleSignOutFilter.setInitParameters(casSingleSignOutFilterParams);
        casSingleSignOutFilter.addMappingForUrlPatterns(null, false, "/*");
        // �ù����������û�����֤����������������
        FilterRegistration.Dynamic casAuthenticationFilter = servletContext.addFilter("CAS Authentication Filter", new AuthenticationFilter());
        Map<String, String> casAuthenticationFilterParams = new HashMap<String, String>();
        casAuthenticationFilterParams.put("casServerLoginUrl", environment.getProperty("casServerLoginUrl"));
        casAuthenticationFilterParams.put("serverName", environment.getProperty("serverName"));
        casAuthenticationFilter.setInitParameters(casAuthenticationFilterParams);
        casAuthenticationFilter.addMappingForUrlPatterns(null, false, "*.html");
        // �ù����������Ticket��У�鹤��������������
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
        // �ù�����ʹ�ÿ����߿���ͨ��org.jasig.cas.client.util.AssertionHolder����ȡ�û��ĵ�¼����
        // ����AssertionHolder.getAssertion().getPrincipal().getName()
        FilterRegistration.Dynamic casAssertionFilter = servletContext.addFilter("CAS Assertion Thread Local Filter", new AssertionThreadLocalFilter());
        casAssertionFilter.addMappingForUrlPatterns(null, false, "*.html");
        
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(SpringMVCConfig.class);
        
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
    
}
