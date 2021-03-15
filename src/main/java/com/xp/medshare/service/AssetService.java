package com.xp.medshare.service;

import com.xp.medshare.model.vomodel.Response;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.io.IOException;
import java.math.BigInteger;

public interface AssetService {
    String deploy() throws ContractException, IOException;

    Response registerAssetAccount(String assetAccount, BigInteger amount) throws Exception;

    Response queryAssetAmount(String assetAccount) throws Exception;
}
