package xm;

public interface IClientBehive {
	public boolean Connect();
	public boolean Download(String filePath, String fileName, String downPath);
	public boolean Download(String filePath, String fileName, String downPath,boolean delhostfile);
	public boolean Upload(String filePath, String ftpPath, boolean delLocal);
	public boolean Upload(String filePath, String ftpPath);
	public void Destroy();
	public boolean Copy(String olePath, String newPath, String fileName);
	public boolean getIsConnected();
	

}
