package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.RemoteGetFileInfoDao;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.entity.RemoteGetFileInfo;
import net.sinodata.business.service.RemoteGetFileInfoService;
import net.sinodata.business.util.PageUtil;

@Service
public class RemoteGetFileInfoServiceImpl implements RemoteGetFileInfoService {

	@Autowired
	private RemoteGetFileInfoDao remoteGetFileInfoDao;

	@Override
	public JSONObject list(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<RemoteGetFileInfo> list = remoteGetFileInfoDao.list(map);
		int count = remoteGetFileInfoDao.fileCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

	@Override
	public RemoteGetFileInfo getById(String id) {
		return remoteGetFileInfoDao.getById(id);
	}

	@Override
	public int add(RemoteGetFileInfo remoteGetFileInfo) {
		return remoteGetFileInfoDao.add(remoteGetFileInfo);
	}

	@Override
	public int updateById(RemoteGetFileInfo remoteGetFileInfo) {
		return remoteGetFileInfoDao.updateById(remoteGetFileInfo);
	}

	@Override
	public int deleteById(String id) {
		return remoteGetFileInfoDao.deleteById(id);
	}

}