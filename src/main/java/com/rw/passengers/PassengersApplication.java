package com.rw.passengers;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
public class PassengersApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassengersApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean shallowEtagBean() {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new ShallowEtagHeaderFilter());
        frb.addUrlPatterns("/");
        frb.setOrder(2);
        return frb;
    }

    @Bean
    public boolean configureGlobalTracer()	{
        Tracer tracer = Configuration.fromEnv().getTracer();
        GlobalTracer.register(tracer);
        return true;
    }

}
