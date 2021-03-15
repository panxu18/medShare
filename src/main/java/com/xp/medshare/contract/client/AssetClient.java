package com.xp.medshare.contract.client;

import com.xp.medshare.contract.sdk.Asset;
import com.xp.medshare.model.vomodel.AssetVo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.model.vomodel.ResponseCode;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class AssetClient extends SimpleClient<Asset> {
    @Autowired
    public AssetClient(Client client, CryptoKeyPair cryptoKeyPair) throws Exception {
        super(client, cryptoKeyPair);
    }

    @Override
    protected Asset deploy(Client client, CryptoKeyPair cryptoKeyPair) throws ContractException {
        return Asset.deploy(client, cryptoKeyPair);
    }

    @Override
    protected Asset load(String address, Client client, CryptoKeyPair cryptoKeyPair) throws ContractException {
        return Asset.load(address, client, cryptoKeyPair);
    }

    public Response registerAssetAccount(String assetAccount, BigInteger amount) throws Exception {
        TransactionReceipt receipt = contract.register(assetAccount, amount);
        List<Asset.RegisterEventEventResponse> result = contract.getRegisterEventEvents(receipt);

        Response response;
        if (!result.isEmpty()) {
            if (result.get(0).ret.compareTo(new BigInteger("0")) == 0) {
                response = Response.SUCCESS;
            } else {
                response = Response.FAIL;
            }
        } else {
            response = Response.FAIL;
        }
        return response;
    }

    public Response<AssetVo> queryAssetAmount(String assetAccount) throws Exception {
        Tuple2<BigInteger, BigInteger> result = contract.query(assetAccount);
        return  Response.of(ResponseCode.SUCCESS, new AssetVo(assetAccount, result.getValue2().toString()));
    }
}
