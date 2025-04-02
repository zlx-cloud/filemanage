package net.sinodata.business.service;

import java.util.Map;

import net.sf.json.JSONObject;
import net.sinodata.business.entity.RemoteGetFileConfig;

public interface RemoteGetFileConfigService {

	JSONObject list(Map<String, Object> map);

	RemoteGetFileConfig getById(String id);
	
	int add(RemoteGetFileConfig remoteGetFileConfig);

	int updateById(RemoteGetFileConfig remoteGetFileConfig);

	int deleteById(String id);

}