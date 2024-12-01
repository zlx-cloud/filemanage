package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.sinodata.business.entity.FileDownloadLog;
import net.sinodata.business.entity.FileInfo;

public interface FileManageDao {

	// 一、二类区 记录上传文件的信息
	int addFileInfo(@Param("fileInfo") FileInfo[] fileInfo);

	// 三类区 记录监听文件的信息
	int addMonitorFileInfo(Map<String, Object> map);

	// 三类区根据fastdfsFilePath加密值删除oracle中文件信息
	int deleteFileInfo(@Param("jmFastDfsPath") String jmFastDfsPath);

	// 三类区记录文件下载日志
	int addFileDownloadLog(Map<String, Object> map);

	// 根据fastdfsFilePath查询下载日志
	List<FileDownloadLog> getLogList(@Param("fastdfsFilePath") String fastdfsFilePath);

	// 记录文件下载日志
	int insertLog(FileDownloadLog fdl);

}