package net.sinodata.business.entity;

public class FileDownloadLog {

	private String uniqueCode;// 上传用户标识
	private String uploadUserName;// 上传用户名
	private String downloadDate;// 下载时间
	private String downloadTime;// 下载时间
	private String fileName;// 文件名称
	private String fileSize;// 文件大小
	private String fastdfsFilePath;// FASTDFS文件路径
	private String uploadTime;// 上传时间
	private String downloadUser;// 下载用户标识
	private String downloadUserName;// 下载用户
	private String fileMd5;// 文件md5值

	public FileDownloadLog() {
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public String getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(String downloadDate) {
		this.downloadDate = downloadDate;
	}

	public String getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFastdfsFilePath() {
		return fastdfsFilePath;
	}

	public void setFastdfsFilePath(String fastdfsFilePath) {
		this.fastdfsFilePath = fastdfsFilePath;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getDownloadUser() {
		return downloadUser;
	}

	public void setDownloadUser(String downloadUser) {
		this.downloadUser = downloadUser;
	}

	public String getUploadUserName() {
		return uploadUserName;
	}

	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}

	public String getDownloadUserName() {
		return downloadUserName;
	}

	public void setDownloadUserName(String downloadUserName) {
		this.downloadUserName = downloadUserName;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	
}