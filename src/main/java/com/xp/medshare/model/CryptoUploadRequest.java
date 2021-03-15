package com.xp.medshare.model;

import com.xp.medshare.util.crypto.Crypto;
import lombok.Data;

@Data
public class CryptoUploadRequest {
    String id;
    Crypto crypto;
}
