package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.sinodata.business.entity.RemoteGetFileInfo;

public interface RemoteGetFileInfoDao {

	// 信息列表
	List<RemoteGetFileInfo> list(Map<String, Object> map);

	// 信息个数
	int fileCount(Map<String, Object> map);

	RemoteGetFileInfo getById(@Param("id") String id);

	int add(RemoteGetFileInfo remoteGetFileInfo);

	int updateById(RemoteGetFileInfo remoteGetFileInfo);

	int deleteById(@Param("id") String id);

}