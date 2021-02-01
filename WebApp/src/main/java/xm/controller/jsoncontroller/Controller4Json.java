package xm.controller.jsoncontroller;

import java.io.BufferedReader;
import java.io.PrintWriter;




import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;
import org.apache.commons.lang3.StringUtils;

import xm.controller.AbsController;
import xm.message.IWXMessageHandler;
import xm.message.wx.MsgBaseUtil;


@Controller
@Scope(value="prototype")

// @RequestMapping("json.*")
public class Controller4Json extends AbsController {

	// @RequestMapping(value = "*.do", method = RequestMethod.GET)
	@RequestMapping(value = "trulyasdwx", method = RequestMethod.GET)
	public ModelAndView handleRequestForGet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PrintWriter out = response.getWriter();
		out.print("/json/GetRequest");
		out.flush();
		return null;
	}

	// @RequestMapping(value = "/json.do", method = RequestMethod.POST)
	@RequestMapping(value = "trulyasdwx", method = RequestMethod.POST)
	public ModelAndView handleRequestForPost(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		
			logger.info("http client ip:"+request.getRemoteAddr());
			request.setCharacterEncoding("UTF-8");
			BufferedReader reader = request.getReader();
			String line = StringUtils.EMPTY;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			String json = sb.toString();
			logger.debug("Received WX Message <--");
			logger.debug(json+"\n");
			/*
			if(wxmessagehandler==null)
			{
				wxmessagehandler=(IWXMessageHandler) contenxt.getBean("wxmessagehandler");
			}
            
			
			
			String reply = wxmessagehandler.Process(json); */
			
			Map<String, String[]> map = request.getParameterMap();
			String service=StringUtils.EMPTY;
			if(map.containsKey("service"))
			{
				String[] strings= map.get("service");
				service=strings[0];
			}
			
			if(service==StringUtils.EMPTY)
			{
				MsgBaseUtil util=new MsgBaseUtil();
				String xml= util.Json2Xml(json);
				service= util.getWXMsgServiceName(xml);
			}
			if(service==null||service==StringUtils.EMPTY)
			{
				
				
				return null;
			}
			IWXMessageHandler handler =(IWXMessageHandler) contenxt.getBean(service);
			String reply= handler.Process(json);

			response.setContentType("application/json");
			// response.setContentEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();
			out.print(reply);
			out.flush();
			out.close();
			logger.info("Send WX Message -->");
			logger.info(reply+"\n");
			

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;

	}

}
