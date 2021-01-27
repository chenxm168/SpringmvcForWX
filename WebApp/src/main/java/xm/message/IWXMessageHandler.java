package xm.message;

import xm.Tib.ITibInterface;
import xm.message.wx.MsgBaseUtil;

public interface IWXMessageHandler {
	
	public String Process(String jsonMessage);
	public void setUtil(MsgBaseUtil util);
	public void setTib(ITibInterface tib);
	public ITibInterface getTib() ;
		


}
