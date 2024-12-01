package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.NotDownloadFileInfoDao;
import net.sinodata.business.entity.FileInfo;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.service.NotDownloadFileInfoService;
import net.sinodata.business.util.PageUtil;

@Service
public class NotDownloadFileInfoServiceImpl implements NotDownloadFileInfoService {

	@Autowired
	private NotDownloadFileInfoDao dao;

	@Override
	public JSONObject notDownloadFileInfoList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<FileInfo> list = dao.notDownloadFileInfoList(map);
		int count = dao.notDownloadFileInfoCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

}