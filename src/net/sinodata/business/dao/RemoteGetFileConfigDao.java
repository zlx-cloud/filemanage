package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.sinodata.business.entity.RemoteGetFileConfig;

public interface RemoteGetFileConfigDao {

	// 信息列表
	List<RemoteGetFileConfig> list(Map<String, Object> map);

	// 信息个数
	int fileCount(Map<String, Object> map);

	RemoteGetFileConfig getById(@Param("id") String id);

	int add(RemoteGetFileConfig remoteGetFileConfig);

	int updateById(RemoteGetFileConfig remoteGetFileConfig);

	int deleteById(@Param("id") String id);

}