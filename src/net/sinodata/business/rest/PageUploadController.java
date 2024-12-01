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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import net.sinodata.business.dao.FileManageDao;
import net.sinodata.business.dao.UserDao;
import net.sinodata.business.entity.ConfigInfo;
import net.sinodata.business.entity.FileInfo;
import net.sinodata.business.util.DateUtil;
import net.sinodata.business.util.FastDFSClient;
import net.sinodata.business.util.MD5Utils;
import net.sinodata.business.util.RedisUtil;
import net.sinodata.business.util.StringUtil;
import net.sinodata.security.vo.ShiroUser;

@Controller
@RequestMapping(value = "/pageUpload")
public class PageUploadController {

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
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(@RequestParam(value = "networkAreaCode", required = false) String networkAreaCode,
			MultipartFile file) {

		// 用户标识
		String uniqueCode = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).getLoginName();

		// 返回值
		Map<String, Object> result = new HashMap<String, Object>();

		/**
		 * 1类区调用此接口时需要传递'网络区域标识'参数，用以区分文件需要上传到哪个流程中的文件夹 networkAreaCode为2时 :
		 * 1->2流程 networkAreaCode为3时 : 1->2->3流程
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

		// 文件名称校验
		if (StringUtil.isContainChinese(file.getOriginalFilename())) {
			result.put("type", "fail");
			result.put("message", "文件名不符合要求");
			return JSON.toJSON(result);
		}

		// 先生成文件MD5值，以免在后续流程中文件上传后不存在而导致无法生成
		Map<String, Object> fileMd5Map = new HashMap<String, Object>();
		fileMd5Map.put(file.getOriginalFilename(), MD5Utils.getFileMd5(file));

		try {
			// 创建线程
			Callable<?> callable = new FileUpload(uniqueCode, networkAreaCode, DateUtil.getCurrTime(), file);
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
		private MultipartFile file;

		FileUpload(String uniqueCode, String networkAreaCode, String uploadTime, MultipartFile file) {
			this.uniqueCode = uniqueCode;
			this.networkAreaCode = networkAreaCode;
			this.uploadTime = uploadTime;
			this.file = file;
		}

		@Override
		public Object call() {
			try {
				// 创建文件信息集合
				List<FileInfo> list = new ArrayList<FileInfo>();

				// 遍历上传文件
				// 生成文件名
				String fileName = uniqueCode + "_" + uploadTime + "_" + file.getOriginalFilename();

				// 缓冲文件路径
				String bufferFilePath = "";
				// 目标文件路径
				String targetFilePath = "";

				/**
				 * 1类区调用此接口时需要传递'网络区域标识'参数，用以区分文件需要上传到哪个流程中的文件夹
				 * networkAreaCode为2时 : 1->2流程 networkAreaCode为3时 : 1->2->3流程
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

				// 创建缓冲文件
				File bufferFile = new File(bufferFilePath);

				// 将文件上传至缓冲文件
				file.transferTo(bufferFile);

				// 之后将缓冲文件移动到目标文件
				bufferFile.renameTo(new File(targetFilePath));

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
				// 添加至集合
				list.add(option);

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
	 * 文件下载
	 * @param fastDfsPath fastdfs路径
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 用户标识
		String uniqueCode = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).getLoginName();
		
		//fastdfs路径
		String fastDfsPath = request.getParameter("fastDfsPath");
		
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

}