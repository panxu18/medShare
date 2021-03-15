package com.xp.medshare.model;

import com.webank.weid.protocol.base.Credential;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Data
public class UnsignedSimpleCredential {
    Map<String, Object> claim;
    Integer cptId;
    String id;
    long created;
    String issuer;

    public static UnsignedSimpleCredential of(Credential credential) {
        UnsignedSimpleCredential simpleCredential = new UnsignedSimpleCredential() ;
        simpleCredential.setClaim(credential.getClaim());
        simpleCredential.setCptId(credential.getCptId());
        simpleCredential.setId(credential.getId());
        simpleCredential.setCreated(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        simpleCredential.setIssuer(credential.getIssuer());
        return simpleCredential;
    }
}
