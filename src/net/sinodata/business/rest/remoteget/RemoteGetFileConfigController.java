package net.sinodata.business.rest.remoteget;

import java.util.List;
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
import net.sinodata.business.dao.UserDao;
import net.sinodata.business.entity.RemoteGetFileConfig;
import net.sinodata.business.entity.User;
import net.sinodata.business.service.RemoteGetFileConfigService;
import net.sinodata.business.util.DateUtil;
import net.sinodata.business.util.StringUtil;

@Controller
@RequestMapping(value = "/remoteGetFileConfig")
public class RemoteGetFileConfigController {

	@Autowired
	RemoteGetFileConfigService remoteGetFileConfigService;
	@Autowired
	private UserDao userDao;

	// 列表页面
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public String listPage() {
		return "business/remoteGetFileConfig/list";
	}

	// 信息列表
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject list(@RequestParam Map<String, Object> params) {
		JSONObject result = remoteGetFileConfigService.list(params);
		return result;
	}

	// 信息维护页面
	@RequestMapping(value = "/editPage", method = RequestMethod.GET)
	public String editPage(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (!StringUtil.isEmpty(id)) {
			RemoteGetFileConfig remoteGetFileConfig = remoteGetFileConfigService.getById(id);
			request.setAttribute("file", remoteGetFileConfig);
		}
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return "business/remoteGetFileConfig/edit";
	}

	// 信息维护
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(RemoteGetFileConfig remoteGetFileConfig) {
		int result = 0;
		String type = remoteGetFileConfig.getType();
		if ("add".equals(type)) {
			remoteGetFileConfig.setId(StringUtil.createUUID());
			remoteGetFileConfig.setCreateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteGetFileConfigService.add(remoteGetFileConfig);
		} else if ("edit".equals(type)) {
			remoteGetFileConfig.setUpdateTime(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
			result = remoteGetFileConfigService.updateById(remoteGetFileConfig);
		}
		return result + "";
	}

	// 信息删除
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteById(@RequestParam(value = "id") String id) {
		remoteGetFileConfigService.deleteById(id);
	}

	@RequestMapping(value = "/findAllUser")
	@ResponseBody
	public List<User> findAllUser() {
		return userDao.findAllUser();
	}

}