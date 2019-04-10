package org.lys.demo.helloworld.feign.client;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SERVICE-HELLOWORLD", fallback = HelloWorldServiceFailure.class)
public interface HelloWorldService {
    @RequestMapping(value = "/",method = RequestMethod.GET)
    String sayHello();
}
