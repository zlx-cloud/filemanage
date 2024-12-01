package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import net.sinodata.business.entity.FileInfo;

public interface NotDownloadFileInfoDao {

	// 未下载文件信息列表
	List<FileInfo> notDownloadFileInfoList(Map<String, Object> map);

	// 未下载文件信息个数
	int notDownloadFileInfoCount(Map<String, Object> map);

}