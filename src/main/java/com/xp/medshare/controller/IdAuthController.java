package com.xp.medshare.controller;

import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.service.IdAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("idAgent")
public class IdAuthController {

    @Autowired
    IdAuthService idAuthService;

    @PostMapping("idAuth/create")
    public Response<Object> register(@RequestBody Map<String, Object> args){
        return idAuthService.create(args);
    }
}
