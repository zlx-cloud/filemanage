package net.sinodata.business.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class MainController {

	@RequestMapping(method = RequestMethod.GET)
	public String business(HttpServletRequest request) {
		return "index";
	}

}