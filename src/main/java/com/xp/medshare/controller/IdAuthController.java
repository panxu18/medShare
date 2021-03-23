package com.xp.medshare.controller;

import com.xp.medshare.model.CredentialRequestDto;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.service.IdAuthService;
import com.xp.medshare.service.LocalService;
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
    public Response<Object> register(@RequestBody CredentialRequestDto args){
        log.info("idAuth start");
        try {
            return idAuthService.createCredential(args);
        } catch (Exception e) {
            log.info("idAuth error ",e);
            return Response.FAIL;
        }

    }
}
