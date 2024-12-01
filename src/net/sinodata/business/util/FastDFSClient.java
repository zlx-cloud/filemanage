package net.sinodata.business.util;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class FastDFSClient {

	private static TrackerClient trackerClient;
	private static TrackerServer trackerServer;
	private static StorageServer storageServer;
	private static StorageClient1 storageClient;

	static {
		String conf_filename = GetResource.class.getClassLoader().getResource("fdfs_client.conf").getPath();

		try {
			ClientGlobal.init(conf_filename);
			trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();
			storageClient = new StorageClient1(trackerServer, storageServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FastDFSClient() throws Exception {

	}

	/**
	 * 上传文件方法
	 * 
	 * @param fileName 文件全路径
	 * @param extName  文件扩展名，不包含（.）
	 * @param metas    文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(String fileName, String extName, NameValuePair[] metas) {
		String result = null;
		try {
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
			result = storageClient.upload_file1(fileName, extName, metas);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 上传文件,传fileName
	 * 
	 * @param fileName 文件的磁盘路径名称 如：D:/image/aaa.jpg
	 * @return null为失败
	 */
	public static String uploadFile(String fileName) {
		return uploadFile(fileName, null, null);
	}

	/**
	 *
	 * @param fileName 文件的磁盘路径名称 如：D:/image/aaa.jpg
	 * @param extName  文件的扩展名 如 txt jpg等
	 * @return null为失败
	 */
	public static String uploadFile(String fileName, String extName) {
		return uploadFile(fileName, extName, null);
	}

	/**
	 * 上传文件方法
	 * 
	 * @param fileContent 文件的内容，字节数组
	 * @param extName     文件扩展名
	 * @param metas       文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) {
		String result = null;
		try {
			result = storageClient.upload_file1(fileContent, extName, metas);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 上传文件
	 * 
	 * @param fileContent 文件的字节数组
	 * @return null为失败
	 * @throws Exception
	 */
	public static String uploadFile(byte[] fileContent) throws Exception {
		return uploadFile(fileContent, null, null);
	}

	/**
	 * 上传文件
	 * 
	 * @param fileContent 文件的字节数组
	 * @param extName     文件的扩展名 如 txt jpg png 等
	 * @return null为失败
	 */
	public static String uploadFile(byte[] fileContent, String extName) {
		return uploadFile(fileContent, extName, null);
	}

	/**
	 * 文件下载到磁盘
	 * 
	 * @param path   图片路径
	 * @param output 输出流 中包含要输出到磁盘的路径
	 * @return -1失败,0成功
	 */
	public static int downloadFile(String path, BufferedOutputStream output) {
		int result = -1;
		try {
			byte[] b = storageClient.download_file1(path);
			try {
				if (b != null) {
					output.write(b);
					result = 0;
				}
			} catch (Exception e) {
			} // 用户可能取消了下载
			finally {
				if (output != null)
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取文件数组
	 * 
	 * @param path 文件的路径 如group1/M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
	 * @return
	 */
	public static byte[] downloaBytes(String path) {
		byte[] b = null;
		try {
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
			b = storageClient.download_file1(path);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 删除文件
	 * 
	 * @param group       组名 如：group1
	 * @param storagePath 不带组名的路径名称 如：M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
	 * @return -1失败,0成功
	 */
	public static Integer deleteFile(String group, String storagePath) {
		int result = -1;
		try {
			result = storageClient.delete_file(group, storagePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *
	 * @param storagePath 文件的全部路径
	 *                    如：group1/M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
	 * @return -1失败,0成功
	 * @throws IOException
	 * @throws Exception
	 */
	public static Integer deleteFile(String storagePath) {
		int result = -1;
		try {
			result = storageClient.delete_file1(storagePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return result;
	}
}