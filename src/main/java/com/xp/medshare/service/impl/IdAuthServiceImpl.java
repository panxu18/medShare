package com.xp.medshare.service.impl;

import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CredentialService;
import com.xp.medshare.model.CredentialRequestDto;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.model.vomodel.ResponseCode;
import com.xp.medshare.service.IdAuthService;
import com.xp.medshare.service.LocalService;
import com.xp.medshare.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Slf4j
@Service
public class IdAuthServiceImpl implements IdAuthService {
    @Autowired
    LocalService localService;

    private static final int ID_AUTH_CPT = 200002;

    // 默认身份表示符
    private static final String DEFAULT_WEID = "did:weid:1:0x23fd23af356c924cdfba4f98a15b9b6409c0b56f";

    @Autowired
    CredentialService credentialService;


    @Override
    public Response<Object> createCredential(CredentialRequestDto args) {
        args.setCptId(ID_AUTH_CPT);
        args.setPublisher(DEFAULT_WEID);
        String issuerSk = KeyUtil.getPrivateKeyByWeId(DEFAULT_WEID);
        args.setPublisherSk(issuerSk);
        return localService.createCredential(args);
    }
}
