package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.sinodata.business.entity.RemoteUploadFileConfig;

public interface RemoteUploadFileConfigDao {

	// 信息列表
	List<RemoteUploadFileConfig> list(Map<String, Object> map);

	// 信息个数
	int fileCount(Map<String, Object> map);

	RemoteUploadFileConfig getById(@Param("id") String id);

	int add(RemoteUploadFileConfig remoteUploadFileConfig);

	int updateById(RemoteUploadFileConfig remoteUploadFileConfig);

	int deleteById(@Param("id") String id);

}