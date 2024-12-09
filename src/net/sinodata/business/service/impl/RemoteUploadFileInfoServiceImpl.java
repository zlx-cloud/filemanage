package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.RemoteUploadFileInfoDao;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.entity.RemoteUploadFileInfo;
import net.sinodata.business.service.RemoteUploadFileInfoService;
import net.sinodata.business.util.PageUtil;

@Service
public class RemoteUploadFileInfoServiceImpl implements RemoteUploadFileInfoService {

	@Autowired
	private RemoteUploadFileInfoDao remoteUploadFileInfoDao;

	@Override
	public JSONObject list(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<RemoteUploadFileInfo> list = remoteUploadFileInfoDao.list(map);
		int count = remoteUploadFileInfoDao.fileCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

	@Override
	public RemoteUploadFileInfo getById(String id) {
		return remoteUploadFileInfoDao.getById(id);
	}

	@Override
	public int add(RemoteUploadFileInfo remoteUploadFileInfo) {
		return remoteUploadFileInfoDao.add(remoteUploadFileInfo);
	}

	@Override
	public int updateById(RemoteUploadFileInfo remoteUploadFileInfo) {
		return remoteUploadFileInfoDao.updateById(remoteUploadFileInfo);
	}

	@Override
	public int deleteById(String id) {
		return remoteUploadFileInfoDao.deleteById(id);
	}

}