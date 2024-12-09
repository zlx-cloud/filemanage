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
import net.sinodata.business.entity.RemoteUploadFileInfo;
import net.sinodata.business.service.RemoteUploadFileInfoService;
import net.sinodata.business.util.DateUtil;
import net.sinodata.business.util.StringUtil;

@Controller
@RequestMapping(value = "/remoteUploadFileInfo")
public class RemoteUploadFileInfoController {

	@Autowired
	RemoteUploadFileInfoService remoteUploadFileInfoService;

	// 列表页面
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public String listPage() {
		return "business/remoteUploadFileInfo/list";
	}

	// 信息列表
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject list(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteUploadFileInfoService.list(params);
		return result;
	}

	// 信息维护页面
	@RequestMapping(value = "/editPage", method = RequestMethod.GET)
	public String editPage(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (!StringUtil.isEmpty(id)) {
			RemoteUploadFileInfo remoteUploadFileInfo = remoteUploadFileInfoService.getById(id);
			request.setAttribute("file", remoteUploadFileInfo);
		}
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return "business/remoteUploadFileInfo/edit";
	}

	// 信息维护
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(RemoteUploadFileInfo remoteUploadFileInfo) {
		int result = 0;
		String type = remoteUploadFileInfo.getType();
		if ("add".equals(type)) {
			remoteUploadFileInfo.setId(StringUtil.createUUID());
			remoteUploadFileInfo.setCreateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteUploadFileInfoService.add(remoteUploadFileInfo);
		} else if ("edit".equals(type)) {
			remoteUploadFileInfo.setUpdateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteUploadFileInfoService.updateById(remoteUploadFileInfo);
		}
		return result + "";
	}

	// 信息删除
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteById(@RequestParam(value = "id") String id) {
		remoteUploadFileInfoService.deleteById(id);
	}

}