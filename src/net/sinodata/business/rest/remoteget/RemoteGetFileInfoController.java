package net.sinodata.business.rest.remoteget;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONObject;
import net.sinodata.business.entity.ConfigInfo;
import net.sinodata.business.service.RemoteGetFileInfoService;

@Controller
@RequestMapping(value = "/remoteGetFileInfo")
public class RemoteGetFileInfoController {

	@Autowired
	ConfigInfo configInfo;
	@Autowired
	RemoteGetFileInfoService remoteGetFileInfoService;

	// 列表页面
	@RequestMapping(value = "/ftpListPage", method = RequestMethod.GET)
	public String ftpListPage() {
		return "business/remoteGetFileInfo/ftpList";
	}

	// 信息列表
	@RequestMapping(value = "/ftpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject ftpList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteGetFileInfoService.ftpList(params);
		return result;
	}

	// 列表页面
	@RequestMapping(value = "/sftpListPage", method = RequestMethod.GET)
	public String sftpListPage() {
		return "business/remoteGetFileInfo/sftpList";
	}

	// 信息列表
	@RequestMapping(value = "/sftpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject sftpList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteGetFileInfoService.sftpList(params);
		return result;
	}

	// 列表页面
	@RequestMapping(value = "/hdfsListPage", method = RequestMethod.GET)
	public String hdfsListPage() {
		return "business/remoteGetFileInfo/hdfsList";
	}

	// 信息列表
	@RequestMapping(value = "/hdfsList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject hdfsList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteGetFileInfoService.hdfsList(params);
		return result;
	}

	// 列表页面
	@RequestMapping(value = "/httpListPage", method = RequestMethod.GET)
	public String httpListPage() {
		return "business/remoteGetFileInfo/httpList";
	}

	// 信息列表
	@RequestMapping(value = "/httpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject httpList(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteGetFileInfoService.httpList(params);
		return result;
	}

}