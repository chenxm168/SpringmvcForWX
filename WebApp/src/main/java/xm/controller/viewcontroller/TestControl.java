package xm.controller.viewcontroller;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import xm.controller.AbsController;



@Controller
//@RequestMapping("/wx/")
public class TestControl extends AbsController{
	
	//@Autowired
 //  UserRepository repository;
	
	@RequestMapping(value="test.do",method = RequestMethod.GET)
	public ModelAndView handleRequestForGet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//List<xm.wx.pojo.User> users = IteratorUtils.toList(repository.findAll().iterator()); 
        
		//Company company = new Company("TRULY","Truly Semiconductor LTD");
		
		//xm.wx.pojo.User user = new xm.wx.pojo.User("99052728","Chenxiaoming");
		
		//user.setCompany(company);
		
		//repository.save(user);
		//repository.save(company);
		
	//	Role role = new Role("Engineer", "Engineer");
		
	//	repository.save(role);
		
	//	Role role2 = repository.findOne("Engineer");
		
	//	logger.debug(role2.getDescription());
		
		
		
		PrintWriter out = response.getWriter();
		out.print("/json/GetRequest");
		out.flush();
		return null;
	}

	@RequestMapping(value="/test.do", method = RequestMethod.POST)
	public ModelAndView handleRequestForPost(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PrintWriter out = response.getWriter();
		out.print("/json/PostRequest");
		out.flush();
		return null;

	}

}
