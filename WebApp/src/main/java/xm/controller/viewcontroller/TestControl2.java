package xm.controller.viewcontroller;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import xm.Tib.ITibInterface;
import xm.controller.AbsController;
import xm.message.MsgUtils;

import com.alibaba.fastjson.JSONObject;
@Controller
@Scope(value="prototype")
public class TestControl2 extends AbsController{
	
	
	
	@RequestMapping(value="test2.do",method = RequestMethod.GET)
	public ModelAndView handleRequestForGet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
	 /*
		HttpRequest rq = new HttpRequest();
		String url="http://asd.truly.com.cn/test2.do";
		for(int i=0;;i++)
		{
		  //String string=	rq.doPost(url, "");
			String string=	rq.doGet(url, null);
		  logger.debug(string);
		  if(i==100)
		  {
			  break;
		  }
		  
		}
		*/
		/*
          xm.wx.pojo.User user = new User("ASD001", "Jack");
          
          String rtnString= JSONObject.toJSONString(user);
		
		
		
		PrintWriter out = response.getWriter();
		out.print(rtnString);
		out.flush();
		*/
		/*
		TibInterface tibInterface=TibInterface.getTibInterface("oic", "prod");
		if(tibInterface!=null)
		{
			if(tibInterface.open())
			{
				String msg=MsgUtils.MakeXmlMessage();
				
				Object reply = tibInterface.SendReply(msg, "TRULY.G5A.MES.PRD.FAB.CNMsvr");
				if(reply!=null)
				{
					logger.info((String) reply);
				}
				
				tibInterface.close();
				
			}
			
		}
		
		TibInterface tib=new TibInterface();
		Object reply = tib.sendQuery();
		*/
		/*
		TibInterface tib=new TibInterface();
		tib.sendQuery();
		Object reply =tib.SendLogin();
		tib.SendLogin(); 
		*/
		//Object ry = tib.sendQuery();
		//tib.sendQuery();
		
		/*
		TibUtils.getListenMap();
		
	  String daeString=	TibUtils.getService("prod", "oic");
		logger.debug(daeString);
		 */
		//xm.Tib.XmlAndJsonUtils.testXml2Json();
		//Tib tib =new Tib();
		//tib.SendMesNoNeedReply("test", content)
		
	 //  String contentString=	MsgUtils.getQueryMsg("GetInspectionRecordList");
		/*
		Map<String, String> map=new HashMap<String, String>();
		map.put("MACHINENAME", "A1CVD01_CV1");
		map.put("DETAILMACHINETYPE", "UNIT");
		//map.put("UNITNAME","%A1CVD01_CV1%");
		String contentString= MsgUtils.MakeQueryMessage("GetMachineInfo", map);
		if (contentString!=StringUtils.EMPTY)
		{
		  String replyString=	tib.QueryRequest("test", contentString);

		  logger.debug(MsgUtils.Xml2Json(replyString));
		}

		/*
		map.clear();
		map.put("USERID", "huanghanjie1");
		map.put("PASSWORD", "1");
		String 	contentString = MsgUtils.MakeMesMessage("UserLogin", map);
		if (contentString!=StringUtils.EMPTY)
		{
		  String replyString=	tib.SendMesRequest("test", contentString);
		  logger.debug(replyString);
		} */
		
	  // tib.SendMesNoNeedReply("test", contentString, "TRULY.G5A.MES.TST.FAB.WX");
		String mString="{JsonMessage:{nullitem:[],test1:'test1'}}";
		JSONObject jsonObject = JSONObject.parseObject(mString);
		logger.debug(jsonObject.toJSONString());
		String jString=MsgUtils.Json2Xml(mString);
		logger.debug(jString);
		
		try {
			
			//ClassPathResource classPathResource = new ClassPathResource("test.xml");
			//File file = classPathResource.getFile();
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File("D:\\WebApp\\config\\test2.xml"));
			String json= MsgUtils.Xml2Json(document.asXML());
		   // IWXMessageHandler handler	=(IWXMessageHandler) contenxt.getBean("asduserloginsrv") ;
			
			//AsdUserLoginSrv svr= new AsdUserLoginSrv();
			//ITibInterface tibInterface=(ITibInterface) contenxt.getBean("tib");
			//tibInterface.SendMesNoNeedReply("test", json, "TRULY.G5A.MES.PRD.FAB.WX");
			//tibInterface.SendMesNoNeedReply("test","oic", json, "TRULY.G5A.MES.TST.FAB.WX");
		    String reply=	wxmessagehandler.Process(json);
		    
		    //ModelAndView mv =new ModelAndView();
		  
		  
		//  response.setContentType("application/json");
			//response.setContentEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();
			out.print(reply);
			out.flush();
			out.close();
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}


}
