package xm.Tib;

public interface ITibInterface {
	
	public String QueryRequest(String envString,Object content);
	public String SendMesRequest(String envString,Object content) ;
	public boolean SendMesNoNeedReply(String envString,Object content);
	public String SendMesRequest(String envString,String module,Object content) ;
	public boolean SendMesNoNeedReply(String envString,String module,Object content);
	public boolean SendMesNoNeedReply(String envString,Object content,String targetSuject);
	public boolean SendMesNoNeedReply(String envString,String module,Object content,String targetSuject);

}
