package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.FileDownloadLogDao;
import net.sinodata.business.entity.FileDownloadLog;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.service.FileDownloadLogService;
import net.sinodata.business.util.PageUtil;

@Service
public class FileDownloadLogServiceImpl implements FileDownloadLogService {

	@Autowired
	private FileDownloadLogDao dao;

	@Override
	public JSONObject fileDownloadLogList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<FileDownloadLog> list = dao.fileDownloadLogList(map);
		int count = dao.fileDownloadLogCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

}