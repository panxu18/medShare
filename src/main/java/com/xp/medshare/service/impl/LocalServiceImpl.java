package com.xp.medshare.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CredentialService;
import com.xp.medshare.model.*;
import com.xp.medshare.model.domodel.CryptoDo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.model.vomodel.ResponseCode;
import com.xp.medshare.service.LocalService;
import com.xp.medshare.util.ObjectUtil;
import com.xp.medshare.util.crypto.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocalServiceImpl implements LocalService {
    @Autowired
    CredentialService credentialService;

    @Override
    public Response<Object> createAnonymousParams(String userPk, String superivorPk) {
        AnonymousUtil anonymousUtil = new AnonymousUtil();
        AnonymousParams result = anonymousUtil.publish(userPk, superivorPk);
        return Response.of(ResponseCode.SUCCESS, result);
    }

    @Override
    public Response<Object> computeStealSk(AnonymousParams param, String userSk) {
        AnonymousUtil anonymousUtil = new AnonymousUtil();
        String stealSk = anonymousUtil.recieve(param, userSk);
        return Response.of(ResponseCode.SUCCESS, stealSk);
    }

    @Override
    public Response<Object> recoverAnonymous(AnonymousParams param, String supervisorSk, String supervisorPk) {
        AnonymousUtil anonymousUtil = new AnonymousUtil();
        String userPk = anonymousUtil.recover(param, supervisorSk, supervisorPk);
        return Response.of(ResponseCode.SUCCESS, userPk);
    }

    @Override
    public Response<Object> encrypt(String userPk, String rawData) {
        ProxyReEncryptionUtil encryption = new ProxyReEncryptionUtil();
        Crypto crypto = encryption.encrypt(new BigInteger(userPk), rawData);
        return Response.of(ResponseCode.SUCCESS, crypto);
    }

    @Override
    public Response<Object> decrypt(String userPk, String userSk, CryptoDo cryptoDo) {
        String result = doDecrypt(userPk, userSk, cryptoDo);
        return Response.of(ResponseCode.SUCCESS, result);
    }

    private String doDecrypt(String userPk, String userSk, CryptoDo cryptoDo) {
        ProxyReEncryptionUtil encryption = new ProxyReEncryptionUtil();
        String result = encryption.decrypt(Crypto.of(cryptoDo), new BigInteger(userPk), new BigInteger(userSk));
        return result;
    }

    @Override
    public Response<Object> generateReKey(ReKeyRequestDto requestDto) {
        Crypto crypto = new Crypto();
        crypto.setC1(new BigInteger(requestDto.getC1()));
        ProxyReEncryptionUtil encryption = new ProxyReEncryptionUtil();
        BigInteger reKey = encryption.generateReKey(crypto, new BigInteger(requestDto.getUserSk()),
                new BigInteger(requestDto.getOtherPk()));
        return Response.of(ResponseCode.SUCCESS, reKey.toString());
    }

    @Override
    public Response<Object> generateReKeyCredential(ReKeyRequestDto requestDto) {
        Crypto crypto = new Crypto();
        crypto.setC1(new BigInteger(requestDto.getC1()));
        ProxyReEncryptionUtil encryption = new ProxyReEncryptionUtil();
        BigInteger reKey = encryption.generateReKey(crypto, new BigInteger(requestDto.getUserSk()),
                new BigInteger(requestDto.getOtherPk()));

        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setCptId(2000009);
        credentialRequestDto.setPublisher("did:weid:1:0x4b57237c0f7873f7d8f13f8e0dc9254227f20c25");
        credentialRequestDto.setPublisherSk(requestDto.getUserSk());
        Map<String, Object> claim = new HashMap<>();
        claim.put("id", requestDto.getId());
        claim.put("recipient", requestDto.getOtherPk());
        claim.put("reKey", reKey.toString());
        credentialRequestDto.setRawData(claim);

        return createCredential(credentialRequestDto);
    }

    @Override
    public Response<Object> reDecrypt(String userSk, CryptoDo cryptoDo) {
        ProxyReEncryptionUtil encryption = new ProxyReEncryptionUtil();
        String result = encryption.reDecrypt(Crypto.of(cryptoDo), new BigInteger(userSk));
        return Response.of(ResponseCode.SUCCESS, result);
    }

    @Override
    public Response<Object> reDecrypt2Credential(String userSk, CryptoDo cryptoDo) {
        ProxyReEncryptionUtil encryption = new ProxyReEncryptionUtil();
        String result = encryption.reDecrypt(Crypto.of(cryptoDo), new BigInteger(userSk));
        SimpleCredential credential = JSONObject.parseObject(result, SimpleCredential.class);
        return Response.of(ResponseCode.SUCCESS, credential);
    }

    /**
     * create rawData
     * @return
     */
    @Override
    public Response<Object> createRawData(CredentialRequestDto requestDto) {
        Map<String,Object> claim = buildClaim(requestDto);
        return Response.of(ResponseCode.SUCCESS, claim);
    }

    /**
     * create rawData
     * @return
     */
    private Map<String, Object> buildClaim(CredentialRequestDto requestDto) {
        Map<String,Object> rawData = requestDto.getRawData();
        if (requestDto.getType() == 1) {
            AnonymousParamsDto anonymousParams = computeAnonymousParams(requestDto.getUserPk(),requestDto.getSupervisorPk());
            try {
                rawData.putAll(ObjectUtil.object2Map(anonymousParams));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("raw data parse error");
            }
        }
        return rawData;
    }

    /**
     * create anonymous params
     */
    private AnonymousParamsDto computeAnonymousParams(String userPk, String supervisorPk) {
        AnonymousUtil anonymousUtil = new AnonymousUtil();
        AnonymousParams result = anonymousUtil.publish(userPk,supervisorPk);
        AnonymousParamsDto paramsDto = AnonymousParamsDto.of(result);
        return paramsDto;
    }

    /**
     * create credential
     */
    private ResponseData<CredentialWrapper> credentialWrapper(int cpId, String issuerId, String issuerSk, Map<String, Object> data) {
        CreateCredentialArgs credentialArgs = new CreateCredentialArgs();
        credentialArgs.setCptId(cpId);
        credentialArgs.setClaim(data);
        credentialArgs.setIssuer(issuerId);
        credentialArgs.setExpirationDate(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(issuerSk);
        credentialArgs.setWeIdPrivateKey(weIdPrivateKey);
        ResponseData<CredentialWrapper> result = credentialService.createCredential(credentialArgs);
        return result;
    }


    @Override
    public Response<Object> createCredential(CredentialRequestDto requestDto) {
        if (requestDto.getPublisher()== null || requestDto.getPublisher().isEmpty()
                || requestDto.getPublisherSk() == null || requestDto.getPublisherSk().isEmpty()) {
            return null;
        }
        Map<String,Object> claim = buildClaim(requestDto);
        ResponseData<CredentialWrapper> result = credentialWrapper(requestDto.getCptId(), requestDto.getPublisher(),
                requestDto.getPublisherSk(), claim);
        SimpleCredential simpleCredential = null;
        if (result.getResult()!= null) {
            simpleCredential = signCredential(result.getResult().getCredential(), requestDto.getPublisherSk());
        }
        return new Response(result.getErrorCode(), result.getErrorMessage(), simpleCredential);
    }

    private SimpleCredential signCredential(Credential credential, String issuerSk) {
        UnsignedSimpleCredential unsignedSimpleCredential = UnsignedSimpleCredential.of(credential);
        String signature = SignUtil.sign(unsignedSimpleCredential, issuerSk);
        SimpleCredential simpleCredential = SimpleCredential.of(unsignedSimpleCredential, signature);
        return simpleCredential;
    }

    @Override
    public Response<Object> createEncryptCredential(CredentialRequestDto requestDto) {
        if (requestDto.getPublisher()== null || requestDto.getPublisher().isEmpty()
                || requestDto.getPublisherSk() == null || requestDto.getPublisherSk().isEmpty()) {
            return null;
        }
        Map<String,Object> claim = buildClaim(requestDto);
        ResponseData<CredentialWrapper> result = credentialWrapper(requestDto.getCptId(), requestDto.getPublisher(),
                requestDto.getPublisherSk(), claim);
        Credential credential = result.getResult().getCredential();
        SimpleCredential simpleCredential = signCredential(credential, requestDto.getPublisherSk());
        String message = JSONObject.toJSONString(simpleCredential,true);
        ProxyReEncryptionUtil encryption = new ProxyReEncryptionUtil();
        Crypto crypto = encryption.encrypt(new BigInteger(claim.get("pk").toString()), message);
        return Response.of(ResponseCode.SUCCESS, crypto);

    }

    @Override
    public Response<Object> decrypt2Credentail(String userPk, String userSk, CryptoDo cryptoDo) {
        String result = doDecrypt(userPk, userSk, cryptoDo);
        SimpleCredential credential = JSONObject.parseObject(result, SimpleCredential.class);
        return Response.of(ResponseCode.SUCCESS, credential);
    }

    @Override
    public Response<Object> computeIdCommit(IdCommitmentReuest request) {
        AnonymousUtil util = new AnonymousUtil();
        BigInteger value = util.computIdCommitment(request.getParam1(), request.getParam2(), request.getSk());
        request.setValue(value.toString());
        return Response.of(ResponseCode.SUCCESS, request);
    }

    @Override
    public Response<Object> verifyCommit(IdCommitmentReuest request) {
        AnonymousUtil util = new AnonymousUtil();
        boolean result = util.verifyIdCommitment(request.getParam1(), request.getParam2(), new BigInteger(request.getValue()));
        return Response.of(ResponseCode.SUCCESS, result);
    }

}
