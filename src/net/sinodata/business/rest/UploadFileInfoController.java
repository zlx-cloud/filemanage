package net.sinodata.business.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.sinodata.business.service.UploadFileInfoService;
import net.sinodata.business.util.DateUtil;

@Controller
@RequestMapping(value = "/uploadFileInfo")
public class UploadFileInfoController {

	@Autowired
	UploadFileInfoService service;

	// 上传文件信息页面
	@RequestMapping(value = "/uploadFileInfoListPage", method = RequestMethod.GET)
	public String uploadFileInfoListPage(HttpServletRequest request) {
		request.setAttribute("time", DateUtil.getNowDate("yyyyMMdd"));
		return "business/uploadfileinfo/uploadFileInfoList";
	}

	// 上传文件信息列表
	@RequestMapping(value = "/uploadFileInfoList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject uploadFileInfoList(@RequestParam Map<String, Object> params) {
		JSONObject result = service.uploadFileInfoList(params);
		return result;
	}

}