package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.RemoteUploadFileConfigDao;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.entity.RemoteUploadFileConfig;
import net.sinodata.business.service.RemoteUploadFileConfigService;
import net.sinodata.business.util.PageUtil;

@Service
public class RemoteUploadFileConfigServiceImpl implements RemoteUploadFileConfigService {

	@Autowired
	private RemoteUploadFileConfigDao remoteUploadFileConfigDao;

	@Override
	public JSONObject list(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<RemoteUploadFileConfig> list = remoteUploadFileConfigDao.list(map);
		int count = remoteUploadFileConfigDao.fileCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

	@Override
	public RemoteUploadFileConfig getById(String id) {
		return remoteUploadFileConfigDao.getById(id);
	}

	@Override
	public int add(RemoteUploadFileConfig remoteUploadFileConfig) {
		return remoteUploadFileConfigDao.add(remoteUploadFileConfig);
	}

	@Override
	public int updateById(RemoteUploadFileConfig remoteUploadFileConfig) {
		return remoteUploadFileConfigDao.updateById(remoteUploadFileConfig);
	}

	@Override
	public int deleteById(String id) {
		return remoteUploadFileConfigDao.deleteById(id);
	}

}