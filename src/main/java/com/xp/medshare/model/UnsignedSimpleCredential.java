package com.xp.medshare.model;

import com.webank.weid.protocol.base.Credential;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;

@Data
public class UnsignedSimpleCredential {
    Map<String, Object> claim;
    Integer cptId;
    String id;
    String created;
    String issuer;

    public static UnsignedSimpleCredential of(Credential credential) {
        UnsignedSimpleCredential simpleCredential = new UnsignedSimpleCredential() ;
        simpleCredential.setClaim(credential.getClaim());
        simpleCredential.setCptId(credential.getCptId());
        simpleCredential.setId(credential.getId());
        DateTimeFormatter dtf2 = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter();
        simpleCredential.setCreated(dtf2.format(LocalDateTime.now()));
        simpleCredential.setIssuer(credential.getIssuer());
        return simpleCredential;
    }
}
