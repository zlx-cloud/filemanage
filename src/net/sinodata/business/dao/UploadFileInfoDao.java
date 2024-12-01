package net.sinodata.business.dao;

import java.util.List;
import java.util.Map;

import net.sinodata.business.entity.FileInfo;

public interface UploadFileInfoDao {

	// 上传文件信息列表
	List<FileInfo> uploadFileInfoList(Map<String, Object> map);

	// 上传文件信息个数
	int uploadFileInfoCount(Map<String, Object> map);

}