package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.CorsFilter;

import co.elastic.clients.elasticsearch.nodes.Http;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@Component
public class CorsFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void destroy() {


    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("Reached the filter");
        HttpServletResponse response =(HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*"); // TODO: devi says, in production environments, face this * inwards
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, X-Auth-Token, Content-Type, x-access-token, Authorization");
        logger.info("CORS headers were set");
        chain.doFilter(req,res);

    }

    public void init(FilterConfig filterConfig) throws ServletException{

    }


}
