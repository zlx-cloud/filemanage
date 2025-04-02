package net.sinodata.business.service;

import java.util.Map;

import net.sf.json.JSONObject;

public interface RemoteGetFileInfoService {

	JSONObject ftpList(Map<String, Object> map);
	
	JSONObject sftpList(Map<String, Object> map);
	
	JSONObject hdfsList(Map<String, Object> map);
	
	JSONObject httpList(Map<String, Object> map);

}