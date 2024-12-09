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
import net.sinodata.business.entity.RemoteGetFileInfo;
import net.sinodata.business.service.RemoteGetFileInfoService;
import net.sinodata.business.util.DateUtil;
import net.sinodata.business.util.StringUtil;

@Controller
@RequestMapping(value = "/remoteGetFileInfo")
public class RemoteGetFileInfoController {

	@Autowired
	RemoteGetFileInfoService remoteGetFileInfoService;

	// 列表页面
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public String listPage() {
		return "business/remoteGetFileInfo/list";
	}

	// 信息列表
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject list(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteGetFileInfoService.list(params);
		return result;
	}

	// 信息维护页面
	@RequestMapping(value = "/editPage", method = RequestMethod.GET)
	public String editPage(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (!StringUtil.isEmpty(id)) {
			RemoteGetFileInfo remoteGetFileInfo = remoteGetFileInfoService.getById(id);
			request.setAttribute("file", remoteGetFileInfo);
		}
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return "business/remoteGetFileInfo/edit";
	}

	// 信息维护
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(RemoteGetFileInfo remoteGetFileInfo) {
		int result = 0;
		String type = remoteGetFileInfo.getType();
		if ("add".equals(type)) {
			remoteGetFileInfo.setId(StringUtil.createUUID());
			remoteGetFileInfo.setCreateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteGetFileInfoService.add(remoteGetFileInfo);
		} else if ("edit".equals(type)) {
			remoteGetFileInfo.setUpdateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteGetFileInfoService.updateById(remoteGetFileInfo);
		}
		return result + "";
	}

	// 信息删除
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteById(@RequestParam(value = "id") String id) {
		remoteGetFileInfoService.deleteById(id);
	}

}