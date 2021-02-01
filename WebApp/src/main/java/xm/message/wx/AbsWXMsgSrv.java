package xm.message.wx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import xm.Tib.ITibInterface;
import xm.message.IWXMessageHandler;

public abstract class AbsWXMsgSrv  implements ApplicationContextAware,IWXMessageHandler{
	
	protected Logger log = LogManager.getLogger(this.getClass());
	protected ApplicationContext context ;
	protected ITibInterface tib;
	//@Autowired
	protected MsgBaseUtil util;
	public MsgBaseUtil getUtil() {
		return util;
	}
	public ITibInterface getTib() {
		return tib;
	}
	@Autowired
	public void setTib(ITibInterface tib) {
		this.tib = tib;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext arg) {
		context = arg;
		
	}
	public abstract String Process(String jsonMessage);
	
	@Override
	public void setUtil(MsgBaseUtil util) {
		this.util=util;
		
	}
	

}
