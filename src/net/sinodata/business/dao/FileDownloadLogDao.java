package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import net.sinodata.business.entity.FileDownloadLog;

public interface FileDownloadLogDao {

	// 文件下载日志列表
	List<FileDownloadLog> fileDownloadLogList(Map<String, Object> map);

	// 文件下载日志个数
	int fileDownloadLogCount(Map<String, Object> map);

}