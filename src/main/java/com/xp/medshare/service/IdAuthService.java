package com.xp.medshare.service;

import com.xp.medshare.model.vomodel.Response;

import java.util.Map;

public interface IdAuthService {
    Response<Object> create(Map<String, Object> data);
}
