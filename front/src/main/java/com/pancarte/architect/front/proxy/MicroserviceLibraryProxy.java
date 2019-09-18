package com.pancarte.architect.front.proxy;


import com.pancarte.architect.front.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "microservice-library", url = "http://localhost:9090", decode404 = true)
public interface MicroserviceLibraryProxy {

    @RequestMapping(value = "/test")
    String test();

    @RequestMapping(value = {"/user"})
    String queryUser(@RequestParam("email") String email);

    @RequestMapping(value = "/role")
    String queryRole(@RequestParam("email") String email);

    @RequestMapping(value = "/find")
    User findUserByEmail(@RequestParam("email") String email);



    }
