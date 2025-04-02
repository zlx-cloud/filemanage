package net.sinodata.business.rest.remoteupload;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import net.sf.json.JSONObject;
import net.sinodata.business.entity.ConfigInfo;
import net.sinodata.business.service.RemoteUploadFileInfoService;
import net.sinodata.business.util.DateUtil;
import net.sinodata.security.vo.ShiroUser;

@Controller
@RequestMapping(value = "/remoteUploadFileInfo")
public class RemoteUploadFileInfoController {

	@Autowired
	ConfigInfo configInfo;
	@Autowired
	RemoteUploadFileInfoService remoteUploadFileInfoService;

	// 远程文件上传
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(@RequestParam(value = "configId") String configId,
			@RequestParam(value = "configMethod") String configMethod,
			@RequestParam("file") MultipartFile multipartFile) {
		// 返回值
		Map<String, Object> result = new HashMap<String, Object>();

		// 当前用户
		String uniqueCode = ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).getLoginName();

		Date date = new Date();
		// 上传时间
		String uploadTime = DateUtil.dateFormat(date, "yyyyMMddHHmmss");
		// 文件名
		String fileName = uniqueCode + "_" + uploadTime + "_" + multipartFile.getOriginalFilename();
		// 文件上传路径
		String filePath = null;
		if ("FTP".equals(configMethod)) {
			filePath = configInfo.getRemotefolderftp() + File.separator + fileName;
		} else if ("SFTP".equals(configMethod)) {
			filePath = configInfo.getRemotefoldersftp() + File.separator + fileName;
		} else if ("HDFS".equals(configMethod)) {
			filePath = configInfo.getRemotefolderhdfs() + File.separator + fileName;
		} else if ("HTTP".equals(configMethod)) {
			filePath = configInfo.getRemotefolderhttp() + File.separator + fileName;
		}
		try {
			// 创建文件
			File file = new File(filePath);
			// 将文件上传至文件
			multipartFile.transferTo(file);

			result.put("type", "success");
			result.put("message", "文件上传成功");
			return JSON.toJSON(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("type", "fail");
			result.put("message", "文件上传失败");
			return JSON.toJSON(result);
		}
	}

	// 列表页面
	@RequestMapping(value = "/ftpListPage", method = RequestMethod.GET)
	public String ftpListPage() {
		return "business/remoteUploadFileInfo/ftpList";
	}

	// 信息列表
	@RequestMapping(value = "/ftpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject ftpList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteUploadFileInfoService.ftpList(params);
		return result;
	}

	// 列表页面
	@RequestMapping(value = "/sftpListPage", method = RequestMethod.GET)
	public String sftpListPage() {
		return "business/remoteUploadFileInfo/sftpList";
	}

	// 信息列表
	@RequestMapping(value = "/sftpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject sftpList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteUploadFileInfoService.sftpList(params);
		return result;
	}

	// 列表页面
	@RequestMapping(value = "/hdfsListPage", method = RequestMethod.GET)
	public String hdfsListPage() {
		return "business/remoteUploadFileInfo/hdfsList";
	}

	// 信息列表
	@RequestMapping(value = "/hdfsList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject hdfsList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteUploadFileInfoService.hdfsList(params);
		return result;
	}

	// 列表页面
	@RequestMapping(value = "/httpListPage", method = RequestMethod.GET)
	public String httpListPage() {
		return "business/remoteUploadFileInfo/httpList";
	}

	// 信息列表
	@RequestMapping(value = "/httpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject httpList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteUploadFileInfoService.httpList(params);
		return result;
	}

}