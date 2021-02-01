package xm.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvMsg;

import xm.Tib.ITibMsgListen;
import xm.Tib.Tib;

@Service("msgListener")
public class MsgService implements ApplicationContextAware, ITibMsgListen {

	private ApplicationContext contenxt = null;

	private Logger log = LogManager.getLogger(Tib.class);
	/*
	@Autowired
	private ITibInterface tib;
	

	public ITibInterface getTib() {
		return tib;
	}

	public void setTib(ITibInterface tib) {
		this.tib = tib;
	} */

	@Override
	public void onMessage(TibrvListener listener, TibrvMsg tibrvMsg) {
		
		try {
			Object object = tibrvMsg.getField("xmlData").data;
			log.info("Client <-- Serverr");
			log.info(object.toString());
			//TibrvMsg replyMsg = new TibrvMsg();
			//replyMsg.add("xmlData", object.toString());
			//listener.getTransport().sendReply(replyMsg, tibrvMsg);
			
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.contenxt = arg0;

	}

}
