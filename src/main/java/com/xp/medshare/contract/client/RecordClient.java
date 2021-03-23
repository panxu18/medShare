package com.xp.medshare.contract.client;

import com.xp.medshare.contract.sdk.Record;
import com.xp.medshare.model.domodel.AnonymousEvidenceDo;
import com.xp.medshare.model.bomodel.EvidenceBo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.model.vomodel.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@Slf4j
public class RecordClient extends SimpleClient<Record> {

    @Autowired
    public RecordClient(Client client, CryptoKeyPair cryptoKeyPair) throws Exception {
        super(client, cryptoKeyPair);
    }

    @Override
    protected Record load(String address, Client client, CryptoKeyPair cryptoKeyPair) {
        return Record.load(address, client, cryptoKeyPair);
    }

    @Override
    public Record deploy(Client client, CryptoKeyPair cryptoKeyPair) throws ContractException {
        return Record.deploy(client, cryptoKeyPair);
    }

    public Response create(BigInteger type, List<String> params) throws ContractException {
        Record record = load();
        List<String> requestParams;
        if (BigInteger.ZERO.equals(type)) {
            requestParams = params.subList(0,8);
        } else {
            requestParams = params.subList(0,13);
        }
        TransactionReceipt receipt = record.create(type, requestParams);
        log.info("create result {}",receipt);
        List<Record.AddEventEventResponse> result = record.getAddEventEvents(receipt);

        Response response;
        if (!result.isEmpty()) {
            int ret = result.get(0).ret.intValue();
            if (ret ==0) {
                response = Response.SUCCESS;
            } else if(ret == -1){
                response = Response.of(ResponseCode.EVIDENCE_EXISTED);
            } else {
                response = Response.of(ResponseCode.CONTRACT_ERROR);
            }
        } else {
            response = Response.of(ResponseCode.CONTRACT_ERROR);
        }
        return response;
    }

    public Response<Object> query(String id) throws Exception {
        Response<Object> response;
        Record record = load();
        Tuple3<BigInteger, BigInteger, List<String>> result = record.select(id);
        if (result.getValue1().equals(BigInteger.ZERO)) {
            List<String> details = result.getValue3();
            BigInteger type = result.getValue2();
            if (BigInteger.ZERO.equals(type)) {
                EvidenceBo data = new EvidenceBo(details.get(0), type, details.get(2), details.get(3), details.get(4), details.get(5),
                        details.get(6), details.get(7));
                response = Response.of(ResponseCode.SUCCESS, data);
            } else {
                AnonymousEvidenceDo data = new AnonymousEvidenceDo(details.get(0), type.toString(), details.get(2), details.get(3), details.get(4), details.get(5),
                        details.get(6), details.get(7), details.get(8),
                        details.get(9), details.get(10), details.get(11), details.get(12));
                response = Response.of(ResponseCode.SUCCESS, data);
            }
        } else {
            response = Response.SUCCESS;
        }
        return response;
    }

    public Response<Object> exist(String id) throws Exception {
        Response<Object> response;
        Record record = load();
        BigInteger result = record.exist(id);
        if (BigInteger.ZERO.equals(result)) {
            return Response.of(ResponseCode.SUCCESS, true);
        } else {
            return Response.of(ResponseCode.SUCCESS, false);
        }
    }

    public Response<Object> confirm(String id, String signature) throws Exception {
        Response<Object> response;
        Record record = load();
        TransactionReceipt receipt = record.confirm(id, signature);
        List<Record.AddEventEventResponse> result = record.getAddEventEvents(receipt);

        if (!result.isEmpty()) {
            if (result.get(0).ret.compareTo(new BigInteger("0")) == 0) {
                return Response.SUCCESS;
            } else {
                return Response.FAIL;
            }
        } else {
            return Response.FAIL;
        }
    }

}
