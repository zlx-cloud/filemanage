package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import net.sinodata.business.entity.FileInfo;

public interface RemoteGetFileInfoDao {

	List<FileInfo> ftpList(Map<String, Object> map);

	int ftpCount(Map<String, Object> map);

	List<FileInfo> sftpList(Map<String, Object> map);

	int sftpCount(Map<String, Object> map);

	List<FileInfo> hdfsList(Map<String, Object> map);

	int hdfsCount(Map<String, Object> map);

	List<FileInfo> httpList(Map<String, Object> map);

	int httpCount(Map<String, Object> map);

}