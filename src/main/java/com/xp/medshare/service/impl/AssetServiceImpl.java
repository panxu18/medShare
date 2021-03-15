package com.xp.medshare.service.impl;

import com.xp.medshare.contract.client.AssetClient;
import com.xp.medshare.model.vomodel.AssetVo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

@Service
@Slf4j
public class AssetServiceImpl implements AssetService {
    @Autowired
    AssetClient assetClient;

    @Override
    public String deploy() throws ContractException, IOException {
        return assetClient.deploy();
    }

    @Override
    public Response registerAssetAccount(String assetAccount, BigInteger amount) throws Exception {
        return assetClient.registerAssetAccount(assetAccount, amount);
    }

    @Override
    public Response<AssetVo> queryAssetAmount(String assetAccount) throws Exception {
        return assetClient.queryAssetAmount(assetAccount);
    }
}
