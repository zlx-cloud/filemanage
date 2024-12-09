package net.sinodata.business.service;

import java.util.Map;

import net.sf.json.JSONObject;
import net.sinodata.business.entity.RemoteGetFileInfo;

public interface RemoteGetFileInfoService {

	JSONObject list(Map<String, Object> map);

	RemoteGetFileInfo getById(String id);
	
	int add(RemoteGetFileInfo remoteGetFileInfo);

	int updateById(RemoteGetFileInfo remoteGetFileInfo);

	int deleteById(String id);

}