package com.xp.medshare.controller;

import com.xp.medshare.model.vomodel.AssetVo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Slf4j
@RestController
@RequestMapping("asset")
public class AssetController {

    @Autowired
    AssetService assetService;

    @GetMapping("deploy")
    public Response deploy() {
        Response response;
        try {
            assetService.deploy();
            response = Response.SUCCESS;
        } catch (Exception e) {
            log.info("deploy failed", e);
            response = Response.FAIL;
        }
        return response;
    }

    @PostMapping("register")
    public Response register(@RequestParam("account") String account, @RequestParam("value") String value){
        Response response;
        try {
            response = assetService.registerAssetAccount(account, new BigInteger(value));
        } catch (Exception e) {
            log.info("regist failed", e);
            response = Response.FAIL;
        }
        return response;
    }

    @PostMapping("query")
    public Response<AssetVo> register(@RequestParam("account") String account){
        Response response;
        try {
            response = assetService.queryAssetAmount(account);
        } catch (Exception e) {
            log.info("query failed", e);
            response = Response.FAIL;
        }
        return response;
    }
}
