package net.sinodata.business.rest.remoteupload;

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
import net.sinodata.business.dao.RemoteUploadFileInfoDao;
import net.sinodata.business.entity.ConfigInfo;
import net.sinodata.business.entity.RemoteUploadFileConfig;
import net.sinodata.business.service.RemoteUploadFileConfigService;
import net.sinodata.business.util.DateUtil;
import net.sinodata.business.util.StringUtil;

@Controller
@RequestMapping(value = "/remoteUploadFileConfig")
public class RemoteUploadFileConfigController {

	@Autowired
	RemoteUploadFileConfigService remoteUploadFileConfigService;
	@Autowired
	ConfigInfo configInfo;
	@Autowired
	RemoteUploadFileInfoDao remoteUploadFileInfoDao;

	// 列表页面
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public String listPage() {
		return "business/remoteUploadFileConfig/list";
	}

	// 信息列表
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject list(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteUploadFileConfigService.list(params);
		return result;
	}

	// 信息维护页面
	@RequestMapping(value = "/editPage", method = RequestMethod.GET)
	public String editPage(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (!StringUtil.isEmpty(id)) {
			RemoteUploadFileConfig remoteUploadFileConfig = remoteUploadFileConfigService.getById(id);
			request.setAttribute("file", remoteUploadFileConfig);
		}
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return "business/remoteUploadFileConfig/edit";
	}

	// 信息维护
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(RemoteUploadFileConfig remoteUploadFileConfig) {
		int result = 0;
		String type = remoteUploadFileConfig.getType();
		if ("add".equals(type)) {
			remoteUploadFileConfig.setId(StringUtil.createUUID());
			remoteUploadFileConfig.setCreateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteUploadFileConfigService.add(remoteUploadFileConfig);
		} else if ("edit".equals(type)) {
			remoteUploadFileConfig.setUpdateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteUploadFileConfigService.updateById(remoteUploadFileConfig);
		}
		return result + "";
	}

	// 信息删除
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteById(@RequestParam(value = "id") String id) {
		remoteUploadFileConfigService.deleteById(id);
	}

}