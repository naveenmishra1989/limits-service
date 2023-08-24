package com.example.limitsservice.controller;

import com.example.limitsservice.config.Configuration;
import com.example.limitsservice.config.LimitConfiguration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RefreshScope
public class LimitConfigurationController {

    @Autowired
    private Configuration configuration;

    //localhost:9090/limits
    @GetMapping("limits")

    public LimitConfiguration retrieveLimitConfig ( ) {
        LimitConfiguration limitConfiguration = new LimitConfiguration(configuration.getMax(), configuration.getMin());
        log.info(limitConfiguration);
        return limitConfiguration;
    }

    //localhost:9090/fault-tolerance
    @GetMapping("/fault-tolerance")
    @HystrixCommand(fallbackMethod = "fallbackConfiguration")
    public LimitConfiguration getFaultTolerance(){
        throw new RuntimeException("Not Available");

    }

    public LimitConfiguration fallbackConfiguration(){ //graceful behaviour
        log.info("fallbackConfiguration");
        return new LimitConfiguration(1, 10);

    }
}
