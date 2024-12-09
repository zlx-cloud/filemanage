package net.sinodata.business.service;

import java.util.Map;

import net.sf.json.JSONObject;
import net.sinodata.business.entity.RemoteUploadFileInfo;

public interface RemoteUploadFileInfoService {

	JSONObject list(Map<String, Object> map);

	RemoteUploadFileInfo getById(String id);
	
	int add(RemoteUploadFileInfo remoteUploadFileInfo);

	int updateById(RemoteUploadFileInfo remoteUploadFileInfo);

	int deleteById(String id);

}