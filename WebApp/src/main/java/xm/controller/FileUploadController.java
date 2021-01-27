package xm.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import xm.message.wx.AsdFileUploadSrv;

@Controller
@Scope(value="prototype")
public class FileUploadController extends AbsController {
	
	@RequestMapping(value = "fileupload", method = RequestMethod.POST)
	public ModelAndView handleRequestForPost(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*
		request.setCharacterEncoding("UTF-8");
		BufferedReader reader = request.getReader();
		String line = StringUtils.EMPTY;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		String formDataString=sb.toString();
		logger.info("Received WX Data <--");
		logger.info(sb.toString());
		*/
		boolean result=false;

		try {
			AsdFileUploadSrv handler= (AsdFileUploadSrv) contenxt.getBean("asdfileuploadsrv");
			if(handler!=null)
			{
				result=handler.Process(request);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		response.setContentType("application/json");
		// response.setContentEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String reply = result? "{'result':'OK'}": "{'result':'NG'}";
		

		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();
		out.close();
		logger.info("Send WX Message -->");
		logger.info(reply+"\n");
		
		
		
	return null;
	}

}
