package com.xp.medshare.model;

import lombok.Data;

@Data
public class SimpleCredential {
    UnsignedSimpleCredential rawData;
    String signature;

    public static SimpleCredential of(UnsignedSimpleCredential unsignedSimpleCredential,String signature) {
        SimpleCredential simpleCredential = new SimpleCredential() ;
        simpleCredential.setRawData(unsignedSimpleCredential);
        simpleCredential.setSignature(signature);
        return simpleCredential;
    }

}
