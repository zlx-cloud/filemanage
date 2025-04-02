package net.sinodata.business.service;

import java.util.Map;

import net.sf.json.JSONObject;
import net.sinodata.business.entity.RemoteUploadFileConfig;

public interface RemoteUploadFileConfigService {

	JSONObject list(Map<String, Object> map);

	RemoteUploadFileConfig getById(String id);
	
	int add(RemoteUploadFileConfig remoteUploadFileConfig);

	int updateById(RemoteUploadFileConfig remoteUploadFileConfig);

	int deleteById(String id);

}