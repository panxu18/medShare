package com.xp.medshare.model;

import lombok.Data;

@Data
public class CredentialVerifyDto {
    SimpleCredential credential;
    String pk;
}
