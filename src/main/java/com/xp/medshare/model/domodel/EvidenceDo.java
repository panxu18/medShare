package com.xp.medshare.model.domodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
public class EvidenceDo {
    String id;
    String  type;
    String sharedSecret;
    String index;
    String hash;
    String signature;
    String user;
    String pk;
}
