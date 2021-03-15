package com.xp.medshare.service.impl;

import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CredentialService;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.model.vomodel.ResponseCode;
import com.xp.medshare.service.IdAuthService;
import com.xp.medshare.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Slf4j
@Service
public class IdAuthServiceImpl implements IdAuthService {
    private static final int ID_AUTH_CPT = 200002;

    // 默认身份表示符
    private static final String DEFAULT_WEID = "did:weid:1:0x23fd23af356c924cdfba4f98a15b9b6409c0b56f";

    @Autowired
    CredentialService credentialService;

    @Override
    public Response<Object> create(Map<String, Object> data) {
        CreateCredentialArgs credentialArgs = new CreateCredentialArgs();
        credentialArgs.setCptId(ID_AUTH_CPT);
        credentialArgs.setClaim(data);
        credentialArgs.setIssuer(DEFAULT_WEID);
        credentialArgs.setExpirationDate(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(KeyUtil.getPrivateKeyByWeId(DEFAULT_WEID));
        credentialArgs.setWeIdPrivateKey(weIdPrivateKey);
        ResponseData<CredentialWrapper> result = credentialService.createCredential(credentialArgs);
        return Response.of (ResponseCode.SUCCESS, result.getResult());
    }
}
