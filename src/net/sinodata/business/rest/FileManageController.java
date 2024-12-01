package net.sinodata.business.rest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.FileManageDao;
import net.sinodata.business.dao.UserDao;
import net.sinodata.business.entity.ConfigInfo;
import net.sinodata.business.entity.FileDownloadLog;
import net.sinodata.business.entity.FileInfo;
import net.sinodata.business.entity.User;
import net.sinodata.business.util.DateUtil;
import net.sinodata.business.util.FastDFSClient;
import net.sinodata.business.util.FileUtil;
import net.sinodata.business.util.MD5Utils;
import net.sinodata.business.util.RedisUtil;
import net.sinodata.business.util.StringUtil;

@Controller
@RequestMapping(value = "/fileManage")
public class FileManageController {

	/*
	 * 创建一个线程池，用来处理一、二类区文件上传及文件信息记录
	 */
	private static final ExecutorService pool = Executors.newFixedThreadPool(10);

	@Autowired
	ConfigInfo configInfo;

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	UserDao userDao;

	@Autowired
	FileManageDao fileManageDao;

	/**
	 * 文件上传
	 * 
	 * @param uniqueCode 用户唯一标志
	 * @param files      文件
	 * @return 提示信息
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(@RequestParam(value = "uniqueCode", required = false) String uniqueCode,
			@RequestParam(value = "networkAreaCode", required = false) String networkAreaCode,
			@RequestParam(value = "files", required = false) MultipartFile[] files) {

		// 返回值
		Map<String, Object> result = new HashMap<String, Object>();

		// 参数完整性校验
		if (StringUtil.isEmpty(uniqueCode) || files.length == 0) {
			result.put("type", "fail");
			result.put("message", "参数不完整");
			return JSON.toJSON(result);
		}

		// 查询用户信息
		User user = userDao.findUserByCode(uniqueCode);
		if (null == user) {
			result.put("type", "fail");
			result.put("message", "用户不存在");
			return JSON.toJSON(result);
		}

		// 判断用户是否可用
		if (user.getEnabled().equals("N")) {
			result.put("type", "fail");
			result.put("message", "用户不可用");
			return JSON.toJSON(result);
		}

		/**
		 * 1类区调用此接口时需要传递'网络区域标识'参数，用以区分文件需要上传到哪个流程中的文件夹
		 * networkAreaCode为2时 : 1->2流程
		 * networkAreaCode为3时 : 1->2->3流程
		 */
		if (configInfo.getNetworkareacode().equals("1")) {
			if (StringUtil.isEmpty(networkAreaCode)) {
				result.put("type", "fail");
				result.put("message", "参数不完整");
				return JSON.toJSON(result);
			} else {
				if (!"2".equals(networkAreaCode) && !"3".equals(networkAreaCode)) {
					result.put("type", "fail");
					result.put("message", "参数错误");
					return JSON.toJSON(result);
				}
			}
		}

		// 判断文件个数
		if (files.length > configInfo.getFileno()) {
			result.put("type", "fail");
			result.put("message", "文件个数超过限制");
			return JSON.toJSON(result);
		}

		// 文件大小、名称校验
		for (int i = 0; i < files.length; i++) {
			if (files[i].getSize() / 1048576 > configInfo.getFilesize()) {
				result.put("type", "fail");
				result.put("message", "文件大小不符合要求");
				return JSON.toJSON(result);
			}
			if (StringUtil.isContainChinese(files[i].getOriginalFilename())) {
				result.put("type", "fail");
				result.put("message", "文件名不能包含中文");
				return JSON.toJSON(result);
			}

			if (!StringUtil.isHaveSuffix(files[i].getOriginalFilename())) {
				result.put("type", "fail");
				result.put("message", "文件扩展名不存在");
				return JSON.toJSON(result);
			}

			if (!files[i].getOriginalFilename().matches("^[0-9a-zA-Z._-]+$")) {
				result.put("type", "fail");
				result.put("message", "文件名不符合要求");
				return JSON.toJSON(result);
			}

		}

		// 先生成文件MD5值，以免在后续流程中文件上传后不存在而导致无法生成
		Map<String, Object> fileMd5Map = new HashMap<String, Object>();
		for (MultipartFile file : files) {
			fileMd5Map.put(file.getOriginalFilename(), MD5Utils.getFileMd5(file));
		}

		try {
			// 创建线程
			Callable<?> callable = new FileUpload(uniqueCode, networkAreaCode, DateUtil.getCurrTime(), files);
			// 提交线程
			Future<?> future = pool.submit(callable);
			// 从线程获取结果
			String type = future.get().toString();

			// 根据线程返回值进行判断
			if ("success".equals(type)) {
				result.put("type", type);
				result.put("message", "上传成功");
				// 文件MD5值
				result.put("file_md5", fileMd5Map);
			} else {
				result.put("type", "fail");
				result.put("message", "上传失败");
			}

		} catch (InterruptedException e) {
			result.put("type", "fail");
			result.put("message", "上传失败");
			e.printStackTrace();
		} catch (ExecutionException e) {
			result.put("type", "fail");
			result.put("message", "上传失败");
			e.printStackTrace();
		}
		return JSON.toJSON(result);
	}

	@SuppressWarnings("rawtypes")
	class FileUpload implements Callable {
		private String uniqueCode;
		private String networkAreaCode;
		private String uploadTime;
		private MultipartFile[] files;

		FileUpload(String uniqueCode, String networkAreaCode, String uploadTime, MultipartFile[] files) {
			this.uniqueCode = uniqueCode;
			this.networkAreaCode = networkAreaCode;
			this.uploadTime = uploadTime;
			this.files = files;
		}

		@Override
		public Object call() {
			try {
				// 创建文件信息集合
				List<FileInfo> list = new ArrayList<FileInfo>();

				// 遍历上传文件
				for (MultipartFile file : files) {
					// 生成文件名
					String fileName = uniqueCode + "_" + uploadTime + "_" + file.getOriginalFilename();

					// 缓冲文件路径
					String bufferFilePath = "";
					// 目标文件路径
					String targetFilePath = "";

					/**
					 * 1类区调用此接口时需要传递'网络区域标识'参数，用以区分文件需要上传到哪个流程中的文件夹
					 * networkAreaCode为2时 : 1->2流程
					 * networkAreaCode为3时 : 1->2->3流程
					 */
					if (configInfo.getNetworkareacode().equals("1")) {
						if ("2".equals(networkAreaCode)) {
							bufferFilePath = configInfo.getBufferfoldertwofinal() + File.separator + fileName;
							targetFilePath = configInfo.getTargetfoldertwofinal() + File.separator + fileName;
						} else if ("3".equals(networkAreaCode)) {
							bufferFilePath = configInfo.getBufferfolder() + File.separator + fileName;
							targetFilePath = configInfo.getTargetfolder() + File.separator + fileName;
						}
					} else {
						bufferFilePath = configInfo.getBufferfolder() + File.separator + fileName;
						targetFilePath = configInfo.getTargetfolder() + File.separator + fileName;
					}

					// 创建文件信息实体类
					FileInfo option = new FileInfo();
					option.setId(MD5Utils.getMD5String(fileName));
					option.setUniqueCode(uniqueCode);
					option.setUploadTime(uploadTime);
					option.setUploadDate(uploadTime.substring(0, 8));
					option.setFileName(file.getOriginalFilename());
					option.setTargetFileName(fileName);
					option.setFileSize(String.valueOf(file.getSize()));
					option.setFilePath(targetFilePath);
					option.setFileMd5(MD5Utils.getFileMd5(file));
					// 添加至集合
					list.add(option);
					
					// 创建缓冲文件
					File bufferFile = new File(bufferFilePath);
					// 将文件上传至缓冲文件
					file.transferTo(bufferFile);
					// 之后将缓冲文件移动到目标文件
					bufferFile.renameTo(new File(targetFilePath));
				}

				// 文件信息存储
				fileManageDao.addFileInfo(list.toArray(new FileInfo[list.size()]));

				return "success";
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return "fail";
			} catch (IOException e) {
				e.printStackTrace();
				return "fail";
			}
		}
	}

	/**
	 * 监听文件并记录文件信息
	 * 
	 * @param json 监听文件的 文件路径、文件名、文件大小
	 */
	@RequestMapping(value = "/fileMonitor", method = RequestMethod.POST)
	@ResponseBody
	public void fileMonitor(@RequestBody JSONObject json) {
		// 文件路径
		String filePath = json.getString("FILE_PATH");
		// 文件大小
		String fileSize = json.getString("FILE_SIZE");
		// 目标文件名
		String targetFileName = json.getString("FILE_NAME");

		// 解析文件名
		String[] options = targetFileName.split("_");
		// 文件名不符合自定文件名特征，判定为异常文件，放至异常文件夹内
		if (options.length < 3) {
			new File(filePath + targetFileName).renameTo(new File(
					configInfo.getErrorfolder() + File.separator + DateUtil.getCurrTime() + "_" + targetFileName));
			return;
		}

		// 用户标志
		String uniqueCode = options[0];
		// 查询用户信息
		User user = userDao.findUserByCode(uniqueCode);
		if (null == user) {
			new File(filePath + targetFileName).renameTo(new File(
					configInfo.getErrorfolder() + File.separator + DateUtil.getCurrTime() + "_" + targetFileName));
			return;
		}
		// 判断用户是否可用
		if (user.getEnabled().equals("N")) {
			new File(filePath + targetFileName).renameTo(new File(
					configInfo.getErrorfolder() + File.separator + DateUtil.getCurrTime() + "_" + targetFileName));
			return;
		}

		// 上传时间
		String uploadTime = options[1];
		// 判断时间格式是否符合要求
		if (!StringUtil.isValidDate(uploadTime, "yyyyMMddHHmmss")) {
			new File(filePath + targetFileName).renameTo(new File(
					configInfo.getErrorfolder() + File.separator + DateUtil.getCurrTime() + "_" + targetFileName));
			return;
		}

		// 原文件名
		StringBuffer sb = new StringBuffer();
		for (int i = 2; i < options.length; i++) {
			sb.append(options[i] + "_");
		}
		String str = sb.toString();
		String fileName = str.substring(0, str.length() - 1);

		// 将文件上传至fastdfs
		String fastDfsFilePath = FastDFSClient.uploadFile(filePath + targetFileName);

		// 在文件同步过程中发现fastDfsFilePath是文件内容，故进行判断
		if (fastDfsFilePath.startsWith("group")) {
			// 文件md5值
			String fileMd5 = MD5Utils.getFileMd5Two(filePath + targetFileName);
			// 将文件移动至备份文件夹
			new File(filePath + targetFileName)
					.renameTo(new File(configInfo.getGabakfolder() + File.separator + targetFileName));

			// 创建文件信息集合
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uniqueCode", uniqueCode);// 用户标志
			map.put("uploadTime", uploadTime);// 上传时间
			map.put("fileName", fileName);// 文件名
			map.put("fileSize", fileSize);// 文件大小
			map.put("fastDfsPath", fastDfsFilePath);// fastdfs路径
			map.put("fileMd5", fileMd5);

			// 在redis中缓存监听文件信息，key值以file开头，表明是文件信息
			String redisKey = "file||" + uniqueCode + "||" + uploadTime + "||" + fastDfsFilePath;
			redisUtil.set(redisKey, map, Long.parseLong(configInfo.getExpire()));

			map.put("id", MD5Utils.getMD5String(targetFileName));// ID
			map.put("targetFileName", targetFileName);// 目标文件名
			map.put("filePath", filePath + targetFileName);// 文件路径
			map.put("jmFastDfsPath", MD5Utils.getMD5String(fastDfsFilePath));// fastdfs路径加密值，用于删除文件信息

			// 在oracle添加监听文件信息
			fileManageDao.addMonitorFileInfo(map);
		} else {
			// 创建文件信息集合
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uniqueCode", uniqueCode);// 用户标志
			map.put("uploadTime", uploadTime);// 上传时间
			map.put("fileName", fileName);// 文件名
			map.put("fileSize", fileSize);// 文件大小
			map.put("targetFileName", targetFileName);// 目标文件名
			map.put("filePath", filePath + targetFileName);// 文件路径

			// 将监听有误文件信息存入文件，方便后续排查
			try {
				FileUtil.writeFile(configInfo.getErrorfolder(), configInfo.getUploaderrorfile(),
						JSON.toJSONString(map));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * redis查询文件信息列表
	 * 
	 * @param uniqueCode 用户唯一标志
	 * @param time       上传时间
	 * @return 文件信息列表
	 */
	@RequestMapping(value = "/fileList", method = RequestMethod.POST)
	@ResponseBody
	public Object fileList(@RequestParam(value = "uniqueCode", required = false) String uniqueCode,
			@RequestParam(value = "time", required = false) String time) {

		// 返回值
		Map<String, Object> result = new HashMap<String, Object>();

		// 参数完整性校验
		if (StringUtil.isEmpty(uniqueCode) || StringUtil.isEmpty(time)
				|| (time.length() != 8 && time.length() != 10 && time.length() != 12)) {
			result.put("type", "fail");
			result.put("message", "参数不完整");
			return JSON.toJSON(result);
		}

		// 查询用户信息
		User user = userDao.findUserByCode(uniqueCode);
		if (null == user) {
			result.put("type", "fail");
			result.put("message", "用户不存在");
			return JSON.toJSON(result);
		}

		// 判断用户是否可用
		if (user.getEnabled().equals("N")) {
			result.put("type", "fail");
			result.put("message", "用户不可用");
			return JSON.toJSON(result);
		}

		// 判断时间格式
		if (!StringUtil.isValidDate(time, "yyyyMMdd") && !StringUtil.isValidDate(time, "yyyyMMddHH")
				&& !StringUtil.isValidDate(time, "yyyyMMddHHmm")) {
			result.put("type", "fail");
			result.put("message", "时间格式不正确");
			return JSON.toJSON(result);
		}

		// 拼接key
		String key = "file||" + uniqueCode + "||" + time + "*";
		// 模糊查询keys
		Set<String> keys = redisUtil.afterMatch(key);
		if (keys.isEmpty()) {
			result.put("type", "fail");
			result.put("message", "无数据");
			return JSON.toJSON(result);
		}
		// 根据keys查询values
		List<Object> values = redisUtil.getValuesByKeys(new ArrayList<>(keys));
		// 返回文件信息
		result.put("type", "success");
		result.put("message", "查询成功");
		result.put("result", values);
		return JSON.toJSON(result);
	}

	/**
	 * 文件下载
	 * 
	 * @param uniqueCode  用户唯一标志
	 * @param fastDfsPath fastdfs路径
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam(value = "uniqueCode", required = false) String uniqueCode,
			@RequestParam(value = "fastDfsPath", required = false) String fastDfsPath, HttpServletResponse response)
			throws Exception {

		// 参数完整性校验
		if (StringUtil.isEmpty(uniqueCode) || StringUtil.isEmpty(fastDfsPath)) {
			return;
		}

		// 查询用户信息
		User user = userDao.findUserByCode(uniqueCode);
		if (null == user) {
			return;
		}

		// 判断用户是否可用
		if (user.getEnabled().equals("N")) {
			return;
		}

		// 拼接key
		String pattern = "*" + fastDfsPath;
		// 模糊查询keys
		Set<String> keys = redisUtil.afterMatch(pattern);
		if (keys.isEmpty()) {
			return;
		}
		// 根据keys查询values
		List<Object> redisValues = redisUtil.getValuesByKeys(new ArrayList<>(keys));

		// 获取文件信息
		@SuppressWarnings("unchecked")
		Map<String, Object> fileInfo = (Map<String, Object>) redisValues.get(0);
		// 获取文件名
		String fileName = fileInfo.get("fileName").toString();

		// 删除redis文件信息
		String key = new ArrayList<>(keys).get(0);
		redisUtil.del(key);

		// 删除oracle文件信息
		String jmFastDfsPath = MD5Utils.getMD5String(fastDfsPath);
		fileManageDao.deleteFileInfo(jmFastDfsPath);

		// 下载时间
		String downloadTime = DateUtil.getCurrTime();
		fileInfo.put("downloadTime", downloadTime);
		fileInfo.put("downloadDate", downloadTime.substring(0, 8));
		fileInfo.put("downloadUser", uniqueCode);

		// 在oracle中记录文件下载日志信息
		fileManageDao.addFileDownloadLog(fileInfo);

		// 执行下载
		byte[] content = FastDFSClient.downloaBytes(fastDfsPath);
		// 响应到客户端下载
		response.setContentType("application/ms-mp3;charset=UTF-8");
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		OutputStream out = response.getOutputStream();
		out.write(content);
		out.flush();
		out.close();
	}

	/**
	 * 文件重复下载
	 * 
	 * @param uniqueCode  用户唯一标志
	 * @param fastDfsPath fastdfs路径
	 */
	@RequestMapping(value = "/downloadRepeat", method = RequestMethod.GET)
	public void downloadRepeat(@RequestParam(value = "uniqueCode", required = false) String uniqueCode,
			@RequestParam(value = "fastDfsPath", required = false) String fastDfsPath, HttpServletResponse response)
			throws Exception {

		// 参数完整性校验
		if (StringUtil.isEmpty(uniqueCode) || StringUtil.isEmpty(fastDfsPath)) {
			return;
		}

		// 查询用户信息
		User user = userDao.findUserByCode(uniqueCode);
		if (null == user) {
			return;
		}

		// 判断用户是否可用
		if (user.getEnabled().equals("N")) {
			return;
		}

		// 查询文件下载日志
		List<FileDownloadLog> logList = fileManageDao.getLogList(fastDfsPath);
		if (logList.size() == 0) {
			return;
		}

		// 获取文件下载日志信息
		FileDownloadLog fdl = logList.get(0);

		// 下载时间
		String downloadTime = DateUtil.getCurrTime();
		fdl.setDownloadDate(downloadTime.substring(0, 8));
		fdl.setDownloadTime(downloadTime);
		fdl.setDownloadUser(uniqueCode);

		// 在oracle中记录文件下载日志信息
		fileManageDao.insertLog(fdl);

		// 执行下载
		byte[] content = FastDFSClient.downloaBytes(fastDfsPath);
		// 响应到客户端下载
		response.setContentType("application/ms-mp3;charset=UTF-8");
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fdl.getFileName(), "UTF-8"))));
		OutputStream out = response.getOutputStream();
		out.write(content);
		out.flush();
		out.close();
	}

}