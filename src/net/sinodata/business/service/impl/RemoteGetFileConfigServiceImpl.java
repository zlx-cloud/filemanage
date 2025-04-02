package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.RemoteGetFileConfigDao;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.entity.RemoteGetFileConfig;
import net.sinodata.business.service.RemoteGetFileConfigService;
import net.sinodata.business.util.PageUtil;

@Service
public class RemoteGetFileConfigServiceImpl implements RemoteGetFileConfigService {

	@Autowired
	private RemoteGetFileConfigDao remoteGetFileConfigDao;

	@Override
	public JSONObject list(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<RemoteGetFileConfig> list = remoteGetFileConfigDao.list(map);
		int count = remoteGetFileConfigDao.fileCount(map);
		JSONObject result = PageUtil.LayuiData(list, count);
		return result;
	}

	@Override
	public RemoteGetFileConfig getById(String id) {
		return remoteGetFileConfigDao.getById(id);
	}

	@Override
	public int add(RemoteGetFileConfig remoteGetFileConfig) {
		return remoteGetFileConfigDao.add(remoteGetFileConfig);
	}

	@Override
	public int updateById(RemoteGetFileConfig remoteGetFileConfig) {
		return remoteGetFileConfigDao.updateById(remoteGetFileConfig);
	}

	@Override
	public int deleteById(String id) {
		return remoteGetFileConfigDao.deleteById(id);
	}

}