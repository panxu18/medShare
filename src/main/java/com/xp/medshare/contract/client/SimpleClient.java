package com.xp.medshare.contract.client;

import com.xp.medshare.contract.sdk.Record;
import com.xp.medshare.contract.utils.ContractStoreHelper;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class SimpleClient<T extends Contract> {

    private Client client;
    private CryptoKeyPair cryptoKeyPair;

    protected T contract;
    private String name;

    public SimpleClient(Client client, CryptoKeyPair cryptoKeyPair) throws Exception {
        pareName();
        this.client = client;
        this.cryptoKeyPair = cryptoKeyPair;
        if (!ContractStoreHelper.isDeployed(name)) {
            deploy();
        }
        contract = load();
    }

    private void pareName() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if(null != typeArray && typeArray.length>0) {
                Class<T> clazz = (Class) typeArray[0];
                name = clazz.getSimpleName();
            }
        }
    }

    public String deploy() throws ContractException, IOException {
        T contract = deploy(client, cryptoKeyPair);
        ContractStoreHelper.store(name, contract.getContractAddress());
        return contract.getContractAddress();
    }

    public T load() throws ContractException {
        try {
            String address = ContractStoreHelper.load(name);
            return load(address, client, cryptoKeyPair);
        } catch (Exception e) {
            throw new ContractException("contract load error",e);
        }
    }

    protected abstract T deploy(Client client, CryptoKeyPair cryptoKeyPair) throws ContractException;

    protected abstract T load(String address, Client client, CryptoKeyPair cryptoKeyPair) throws ContractException;
}
