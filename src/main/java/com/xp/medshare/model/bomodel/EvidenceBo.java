package com.xp.medshare.model.bomodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EvidenceBo {
    String id;
    BigInteger type;
    String sharedSecret;
    String index;
    String hash;
    String signature;
    String user;
    String pk;
}
