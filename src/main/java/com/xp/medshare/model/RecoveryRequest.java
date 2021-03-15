package com.xp.medshare.model;

import com.xp.medshare.util.crypto.AnonymousParams;
import lombok.Data;

@Data
public class RecoveryRequest {
    AnonymousParams param;
    String pk;
    String sk;
}
