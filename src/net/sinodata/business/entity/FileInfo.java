package net.sinodata.business.entity;

public class FileInfo {

	private String id; // ID
	private String uniqueCode;// 用户唯一标志
	private String userName;// 用户名
	private String uploadTime;// 上传时间
	private String uploadDate;// 上传时间
	private String fileName;// 原文件名称
	private String targetFileName;// 目标文件名
	private String fileSize;// 文件大小
	private String filePath;// 文件上传路径
	private String fastDfsPath;// fastDfs文件路径
	private String jmFastDfsPath;// fastDfs文件路径加密值，用于删除
	private String fileMd5;// 文件MD5值

	public FileInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public String getFastDfsPath() {
		return fastDfsPath;
	}

	public void setFastDfsPath(String fastDfsPath) {
		this.fastDfsPath = fastDfsPath;
	}

	public String getJmFastDfsPath() {
		return jmFastDfsPath;
	}

	public void setJmFastDfsPath(String jmFastDfsPath) {
		this.jmFastDfsPath = jmFastDfsPath;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	
}