package xm.message.wx;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xm.message.MsgUtils;

@Component("asdquerysrv")
@Scope(value="prototype")
public class AsdQuerySrv extends AbsWXMsgSrv {

	@Override
	public String Process(String jsonMessage) {
		log.info("Received WX Message <--");
		log.info(jsonMessage+"\n");
		
		if(util==null)
		{
			try {
				 util=new MsgBaseUtil();
				
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}

		
		if(util==null)
		{
			log.error("get message unit faill ");
			return StringUtils.EMPTY;
		}
		
		//String xml = MsgUtils.Json2Xml(jsonMessage);
		//String env = MsgUtils.getWXEnv(xml);
		//String qString = MsgUtils.MakeToMesQueryMessage(xml);
		String xml = util.Json2Xml(jsonMessage);
		String env = util.getWXEnv(xml);
		String qString = util.MakeToMesQueryMessage(xml);
		String replyString = tib.QueryRequest(env, qString);
		if (replyString != StringUtils.EMPTY) {
			return util.makeJsonMsgForQueryReplyMsg(replyString);
		} else {
			return replyString;
		}

	}

}
