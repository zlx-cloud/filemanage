package net.sinodata.business.service;

import java.util.Map;

import net.sf.json.JSONObject;

public interface UploadFileInfoService {

	JSONObject uploadFileInfoList(Map<String, Object> map);

}