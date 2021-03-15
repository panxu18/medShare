package com.xp.medshare.controller;

import com.xp.medshare.model.CredentialRequestDto;
import com.xp.medshare.model.IdCommitmentReuest;
import com.xp.medshare.model.ReKeyRequestDto;
import com.xp.medshare.model.RecoveryRequest;
import com.xp.medshare.model.domodel.DecryptDo;
import com.xp.medshare.model.domodel.EncryptDo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.service.LocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("local")
public class LocalController {
    @Autowired
    LocalService localService;

    @PostMapping("encrypt")
    public Response<Object> encrypt(@RequestBody EncryptDo encryptDo) {
        return localService.encrypt(encryptDo.getPk(), encryptDo.getRawData());
    }

    @PostMapping("decrypt")
    Response<Object> decrypt(@RequestBody DecryptDo decryptDo){
        return localService.decrypt(decryptDo.getPk(), decryptDo.getSk(), decryptDo.getCrypto());
    }

    @PostMapping("decrypt/credential")
    Response<Object> decrypt2Credential(@RequestBody DecryptDo decryptDo){
        return localService.decrypt2Credentail(decryptDo.getPk(), decryptDo.getSk(), decryptDo.getCrypto());
    }

    @PostMapping("credential/rawData")
    Response<Object> createRecord(@RequestBody CredentialRequestDto requestDto) {
        return localService.createRawData(requestDto);
    }

    @PostMapping("credential/create")
    public Response<Object> createCredential(@RequestBody CredentialRequestDto requestDto) {
        return localService.createCredential(requestDto);
    }

    @PostMapping("credential/create/encrypt")
    public Response<Object> createEncryptCredential(@RequestBody CredentialRequestDto requestDto) {
        return localService.createEncryptCredential(requestDto);
    }

    @PostMapping("reKey")
    public Response<Object> generateReKey(@RequestBody ReKeyRequestDto requestDto) {
        return localService.generateReKey(requestDto);
    }

    @PostMapping("reKey/credential")
    public Response<Object> generateReKeyCredential(@RequestBody ReKeyRequestDto requestDto) {
        return localService.generateReKeyCredential(requestDto);
    }

    @PostMapping("reDecrypt")
    public Response<Object> reDecrypt(@RequestBody DecryptDo requestDto) {
        return localService.reDecrypt(requestDto.getSk(), requestDto.getCrypto());
    }

    @PostMapping("reDecrypt/credential")
    public Response<Object> reDecrypt2Credential(@RequestBody DecryptDo requestDto) {
        return localService.reDecrypt(requestDto.getSk(), requestDto.getCrypto());
    }

    @PostMapping("idCommit")
    public Response<Object> idCommitment(@RequestBody IdCommitmentReuest request) {
        return localService.computeIdCommit(request);
    }

    @PostMapping("idCommit/verify")
    public Response<Object> verifyIdCommitment(@RequestBody IdCommitmentReuest request) {
        return localService.verifyCommit(request);
    }

    @PostMapping("recovery")
    public Response<Object> recoveryUserPk(@RequestBody RecoveryRequest request) {
        return localService.recoverAnonymous(request.getParam(), request.getSk(), request.getPk());
    }
}
