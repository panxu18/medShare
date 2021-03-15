package com.xp.medshare.controller;

import com.xp.medshare.model.CryptoUploadRequest;
import com.xp.medshare.model.SimpleCredential;
import com.xp.medshare.model.domodel.AnonymousEvidenceDo;
import com.xp.medshare.model.domodel.EvidenceDo;
import com.xp.medshare.model.vomodel.AssetVo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("agent")
public class AgentController {

    @Autowired
    AgentService agentService;

    @GetMapping("evidence/deploy")
    public Response deploy() {
        Response response;
        try {
            agentService.deployRecordContract();
            response = Response.SUCCESS;
        } catch (Exception e) {
            log.info("deploy failed", e);
            response = Response.FAIL;
        }
        return response;
    }

    @PostMapping("evidence/createOriginal")
    public Response create(@RequestBody EvidenceDo data){
        try {
            return agentService.createOriginalRecord(data);
        } catch (Exception e) {
            return Response.FAIL;
        }
    }

    @PostMapping("evidence/createAnonymous")
    public Response create(@RequestBody AnonymousEvidenceDo anonymousEvidenceDo){
        try {
            return agentService.createAnonymousRecord(anonymousEvidenceDo);
        } catch (Exception e) {
            return Response.FAIL;
        }
    }

    @GetMapping("evidence/query")
    public Response<AssetVo> query(@RequestParam("id") String id){
        Response response;
        try {
            response = agentService.queryRecord(id);
        } catch (Exception e) {
            log.info("query failed", e);
            response = Response.FAIL;
        }
        return response;
    }

    @GetMapping("evidence/exist")
    public Response<AssetVo> exist(@RequestParam("id") String id){
        Response response;
        try {
            response = agentService.recordExist(id);
        } catch (Exception e) {
            log.info("query failed", e);
            response = Response.FAIL;
        }
        return response;
    }

    @PostMapping("evidence/confirm")
    public Response<AssetVo> confirm(@RequestParam("id") String id, @RequestParam("signature") String signature){
        Response response;
        try {
            response = agentService.confirmRecord(id, signature);
        } catch (Exception e) {
            log.info("query failed", e);
            response = Response.FAIL;
        }
        return response;
    }

    @GetMapping("user/register")
    public Response<Object> createUser() {
        return agentService.registerUser();
    }

    @GetMapping("user/document")
    public Response<Object> queryUserDocument(String id) {
        return agentService.queryDidDocument(id);
    }

    @PostMapping("cpt/register")
    public Response<Object> registerCptByMap(@RequestParam(value = "weId",required = false) String weId,
                                             @RequestBody Map<String, Object> cpt) {
        return agentService.registerCpt(weId, cpt);
    }

    @GetMapping("cpt/query")
    public Response<Object> queryCpt(@RequestParam("id") int cptId) {
        return agentService.queryCpt(cptId);
    }

    @PostMapping("reEncrypt")
    public Response<Object> reEncrypt(@RequestBody SimpleCredential credential) {
        return agentService.reEncrypt(credential);
    }

    @PostMapping("uploadCrypto")
    public Response<Object> uploadCrypto(@RequestBody CryptoUploadRequest request) {
        return agentService.uploadCrypto(request.getId(), request.getCrypto());
    }

}
