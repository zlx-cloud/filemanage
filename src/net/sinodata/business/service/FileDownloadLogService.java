package net.sinodata.business.service;

import java.util.Map;

import net.sf.json.JSONObject;

public interface FileDownloadLogService {

	JSONObject fileDownloadLogList(Map<String, Object> map);

}