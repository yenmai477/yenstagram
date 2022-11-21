package me.yenmai.yenstagramdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class YenstagramDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(YenstagramDiscoveryApplication.class, args);
    }

}
