package xm.message.wx;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class AsdFileUploadSrv  {
	
	private Logger log= LogManager.getLogger(AsdFileUploadSrv.class);

	
	public boolean Process(HttpServletRequest request ) throws Exception{
		String fileStoredPath = "D:\\WebApp\\TempFile\\"; 
		
		try {
			MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			 MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			 if (resolver.isMultipart(request)) {
				 String uploadFileName = file.getOriginalFilename();
				 String savePath = fileStoredPath  + uploadFileName;
				 File saveFile = new File(savePath); 
			        File parentFile = saveFile.getParentFile();
				 if (saveFile.exists()) { 
			          saveFile.delete(); 
			        } else { 
			          if (!parentFile.exists()) { 
			            parentFile.mkdirs(); 
			          } 
			        }
				 FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
				 return true;
			 
			 }
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		
		
		return false;
		
	}

}
