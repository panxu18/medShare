package com.xp.medshare.model;

import lombok.Data;

@Data
public class UserAccountDto {
    String id;
    String publicKey;
    String privateKey;
}
