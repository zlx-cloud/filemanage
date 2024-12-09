package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.sinodata.business.entity.RemoteUploadFileInfo;

public interface RemoteUploadFileInfoDao {

	// 信息列表
	List<RemoteUploadFileInfo> list(Map<String, Object> map);

	// 信息个数
	int fileCount(Map<String, Object> map);

	RemoteUploadFileInfo getById(@Param("id") String id);

	int add(RemoteUploadFileInfo remoteUploadFileInfo);

	int updateById(RemoteUploadFileInfo remoteUploadFileInfo);

	int deleteById(@Param("id") String id);

}