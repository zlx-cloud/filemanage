package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.UploadFileInfoDao;
import net.sinodata.business.entity.FileInfo;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.service.UploadFileInfoService;
import net.sinodata.business.util.PageUtil;

@Service
public class UploadFileInfoServiceImpl implements UploadFileInfoService {

	@Autowired
	private UploadFileInfoDao dao;

	@Override
	public JSONObject uploadFileInfoList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<FileInfo> list = dao.uploadFileInfoList(map);
		int count = dao.uploadFileInfoCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

}