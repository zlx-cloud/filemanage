package net.sinodata.business.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PageUtil {

	//layui表格格式
	public static JSONObject LayuiData(List<?> list, int count) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", count);
		JSONArray array = JSONArray.fromObject(list);
		result.put("data", array);
		return JSONObject.fromObject(result);
	}

}