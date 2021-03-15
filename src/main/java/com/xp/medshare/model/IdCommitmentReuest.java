package com.xp.medshare.model;

import com.xp.medshare.util.crypto.AnonymousParams;
import lombok.Data;

@Data
public class IdCommitmentReuest {
    AnonymousParams param1;
    AnonymousParams param2;
    String sk;
    String value;
}
