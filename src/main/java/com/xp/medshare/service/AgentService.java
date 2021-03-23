package com.xp.medshare.service;

import com.xp.medshare.model.CredentialRequestDto;
import com.xp.medshare.model.SimpleCredential;
import com.xp.medshare.model.domodel.AnonymousEvidenceDo;
import com.xp.medshare.model.domodel.CryptoDo;
import com.xp.medshare.model.domodel.EvidenceDo;
import com.xp.medshare.model.vomodel.Response;
import com.xp.medshare.util.crypto.Crypto;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface AgentService {
    String deployRecordContract() throws ContractException, IOException;

    Response createRecord(BigInteger type, List<String> params) throws Exception;

    Response<Object> queryRecord(String id) throws Exception;

    Response<Object> recordExist(String id) throws Exception;

    Response<Object> confirmRecord(String id, String signature) throws Exception;

    Response<Object> registerUser();

    Response<Object> queryDidDocument(String id);

    Response<Object> registerCpt(String weId, Map<String, Object> cpt);

    Response<Object> queryCpt(int cptId);

    Response<Object> createAnonymousRecord(AnonymousEvidenceDo anonymousEvidenceDo) throws Exception;

    Response<Object> createOriginalRecord(EvidenceDo data) throws Exception;

    Response<Object> reEncrypt(SimpleCredential credential);

    Response<Object> uploadCrypto(String id, CryptoDo crypto);

    Response<Object> querySupervisor();

    Response<Object> queryUserAccount(String id);
}
