package xm.Tib;
import com.tibco.tibrv.*;

public interface ITibMsgListen {
	
	public void onMessage(TibrvListener listener,TibrvMsg tibrvMsg) ;

}
