package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.RemoteGetFileInfoDao;
import net.sinodata.business.entity.FileInfo;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.service.RemoteGetFileInfoService;
import net.sinodata.business.util.PageUtil;

@Service
public class RemoteGetFileInfoServiceImpl implements RemoteGetFileInfoService {

	@Autowired
	private RemoteGetFileInfoDao dao;

	@Override
	public JSONObject ftpList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<FileInfo> list = dao.ftpList(map);
		int count = dao.ftpCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

	@Override
	public JSONObject sftpList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<FileInfo> list = dao.sftpList(map);
		int count = dao.sftpCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

	@Override
	public JSONObject hdfsList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<FileInfo> list = dao.hdfsList(map);
		int count = dao.hdfsCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

	@Override
	public JSONObject httpList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<FileInfo> list = dao.httpList(map);
		int count = dao.httpCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

}