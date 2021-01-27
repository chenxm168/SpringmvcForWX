package xm.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import xm.wxbase.CONST;
import xm.wxbase.Utils;

public class UploadFileScheduler implements IScheduleJob ,ApplicationContextAware {
	private ApplicationContext context;
	Logger logger = LogManager.getLogger(ScheduleJob1.class);
	
	@Override
	public void setApplicationContext(ApplicationContext contex)
			throws BeansException {
		this.context=contex;
		
	}

	@Override
	public void doSchedule() {
		logger.info("Start Uploader Schedule");
		File[] files=this.getTaskFiles();
		for (File file : files) {
			
		  String jString= Utils.readFileContent(file);
		  logger.debug(jString);
			
			
			
			
			
		}
		
		
	}
	
	private File[] getTaskFiles() {
		
		File dFile= new File(CONST.UPLOAD_TASK_ROOT_PATH);
	  return	dFile.listFiles();
		
	}

}
