package xm.Tib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;



import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvErrorCallback;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvMsgCallback;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.tibrv.TibrvTransport;

public class Tib implements TibrvErrorCallback, ITibInterface,
		TibrvMsgCallback, Destroyable {

	public Logger log = LogManager.getLogger(Tib.class);

	@Override
	public void onError(Object arg0, int arg1, String arg2, Throwable arg3) {

		log.error(arg0);
	}

	// @Getter
	private boolean isOpen;
	@Autowired
	private ITibMsgListen msgListen;
	private Thread listenThread;

	private List<TibrvTransport> transports = new ArrayList<TibrvTransport>();
	private Map<String, TibrvListener> linstenMaps = new HashMap<String, TibrvListener>();

	private Map<String, Map<String, String>> listenMap = new HashMap<String, Map<String, String>>();

	public Tib() {

		this.isOpen = this.Open();

	}

	public Tib(ITibMsgListen msgListener) {
		this.msgListen = msgListener;
		this.isOpen = this.Open();
	}

	private boolean Open() {

		try {
			listenMap = TibUtils.getListenMap();
			if(!Tibrv.isValid())
			{
				Tibrv.open(Tibrv.IMPL_NATIVE);
			}
			
			if (Tibrv.isValid()) {
				log.debug("Is Native?" + Tibrv.isNativeImpl());
				log.debug("default queue:" + Tibrv.defaultQueue());
				log.debug("TibprocessTransport:" + Tibrv.processTransport());

				// Map<String, Map<String, String>> listenMap=
				// TibUtils.getListenMap();
				if (listenMap.size() > 0) {
					for (String key : listenMap.keySet()) {
						String listenSerivceString = listenMap.get(key).get(
								"service");
						String listenNetworkString = listenMap.get(key).get(
								"network");
						String listenDaemonString = listenMap.get(key).get(
								"daemon");
						String listenSujectString = listenMap.get(key).get(
								"listenSubject");

						TibrvTransport transport = new TibrvRvdTransport(
								listenSerivceString, listenNetworkString,
								listenDaemonString);

						transports.add(transport);
						TibrvListener listener = new TibrvListener(
								Tibrv.defaultQueue(), this, transport,
								listenSujectString, null);
						this.linstenMaps.put(key, listener);
						StringBuilder sbBuilder = new StringBuilder();
						sbBuilder.append("env=" + key + ";");
						sbBuilder
								.append("service=" + listenSerivceString + ";");
						sbBuilder
								.append("network=" + listenNetworkString + ";");
						sbBuilder.append("daemon=" + listenDaemonString + ";");
						sbBuilder.append("listenSubject=" + listenSujectString
								+ ";\n");
						sbBuilder.append("TibrvListener Start Successs!");
						log.info(sbBuilder.toString());

					}
				}

				this.listenThread = new Thread(new QDispatch());
				this.listenThread.setDaemon(true);
				this.listenThread.start();

				isOpen = true;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return false;

	}

	private boolean close() {

		try {
			for (int i = 0; i < transports.size(); i++) {

				if (transports.get(i) != null) {
					transports.get(i).destroy();
				}

			}

			this.isOpen = false;

			if (this.listenThread != null
					&& this.listenThread.isAlive() == true) {
				this.listenThread.interrupt();
				this.listenThread = null;

				log.debug("Success to close listener thread.");
			}
			/*
			 * 
			 * if (this.listener != null) { this.listener.destroy();
			 * this.listener = null;
			 * 
			 * log.debug("Success to destroy listener thread."); }
			 * 
			 * if (this.transport != null) { this.transport.destroy();
			 * this.transport = null;
			 * 
			 * log.debug("Success to destroy transport."); }
			 */

			for (String key : this.linstenMaps.keySet()) {
				try {
					this.linstenMaps.get(key).destroy();

				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

				for (TibrvTransport tibrvTransport : transports) {
					try {
						tibrvTransport.destroy();

					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}

			}

			Tibrv.close();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}

		return true;

	}

	@Override
	public String QueryRequest(String envString, Object content) {
		String network = TibUtils.getNetwork(envString, "qry");
		String service = TibUtils.getService(envString, "qry");
		String daemon = TibUtils.getDaemon(envString, "qry");
		String targetSubject = TibUtils.getTargetSubject(envString, "qry");
		String fieldName = TibUtils.getFieldName(envString, "qry");
		String timeoutString = TibUtils.getTimeout(envString, "qry");

		int timeout = 30;
		if (timeoutString != null && timeoutString != StringUtils.EMPTY) {
			try {

				timeout = Integer.valueOf(timeoutString);

			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		TibrvTransport transport = null;

		try {

			transport = new TibrvRvdTransport(service, network, daemon);
			TibrvMsg tibrvMsg = new TibrvMsg();
			tibrvMsg.setSendSubject(targetSubject);
			tibrvMsg.add(fieldName, content);
			TibrvMsg rsMsg = transport.sendRequest(tibrvMsg, timeout);
			StringBuilder sbBuilder = new StringBuilder();
			sbBuilder.append("Send Query Message -->\n");
			sbBuilder.append("env=" + envString + ";");
			sbBuilder.append("service=" + service + ";");
			sbBuilder.append("network=" + network + ";");
			sbBuilder.append("daemon=" + daemon + ";");
			sbBuilder.append("targetSuject=" + targetSubject + ";\n");
			sbBuilder.append(content);
			log.info(sbBuilder.toString());

			if (rsMsg == null) {

				transport.destroy();
				return StringUtils.EMPTY;

			} else {
				transport.destroy();
				String rpyString = (String) rsMsg.getField(fieldName).data;

				StringBuilder sb = new StringBuilder();
				sb.append("Recieved Query Reply Message <-- \n");
				sb.append(rpyString);
				log.info(sb.toString());
				//log.info(rpyString);

				return rpyString;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			if (transport != null) {
				transport.destroy();
			}
			return StringUtils.EMPTY;
		}

	}

	@Override
	public String SendMesRequest(String envString, Object content) {
		return SendMesRequest(envString, "oic", content);
		/*
		 * String network = TibUtils.getNetwork(envString, "oic"); String
		 * service = TibUtils.getService(envString, "oic"); String daemon =
		 * TibUtils.getDaemon(envString, "oic"); String targetSubject =
		 * TibUtils.getTargetSubject(envString, "oic"); String fieldName =
		 * TibUtils.getFieldName(envString, "oic"); String timeoutString =
		 * TibUtils.getTimeout(envString, "oic"); String sourceSubject =
		 * TibUtils.getSourceSubject(envString, "oic");
		 * 
		 * int timeout = 30; if (timeoutString != null && timeoutString !=
		 * StringUtils.EMPTY) { try {
		 * 
		 * timeout = Integer.valueOf(timeoutString);
		 * 
		 * } catch (Exception e) { log.error(e.getMessage()); } }
		 * 
		 * TibrvTransport transport = null;
		 * 
		 * try {
		 * 
		 * transport = new TibrvRvdTransport(service, network, daemon); TibrvMsg
		 * tibrvMsg = new TibrvMsg(); tibrvMsg.setSendSubject(targetSubject);
		 * tibrvMsg.setReplySubject(sourceSubject); tibrvMsg.add(fieldName,
		 * content); StringBuilder sbBuilder = new StringBuilder();
		 * sbBuilder.append("Send Query Message -->\n"); sbBuilder.append("env="
		 * + envString + ";"); sbBuilder.append("service=" + service + ";");
		 * sbBuilder.append("network=" + network + ";");
		 * sbBuilder.append("daemon=" + daemon + ";");
		 * sbBuilder.append("targetSuject=" + targetSubject + ";\n");
		 * sbBuilder.append(content); log.info(content.toString()); TibrvMsg
		 * rsMsg = transport.sendRequest(tibrvMsg, timeout);
		 * 
		 * 
		 * if (rsMsg == null) { transport.destroy(); return StringUtils.EMPTY;
		 * 
		 * } else { transport.destroy(); String rpyString = (String)
		 * rsMsg.getField(fieldName).data;
		 * 
		 * StringBuilder sb = new StringBuilder();
		 * sb.append("Recieved  Reply Message <-- \n"); sb.append(rpyString);
		 * log.info(sb.toString()); log.info(rpyString);
		 * 
		 * return rpyString;
		 * 
		 * }
		 * 
		 * } catch (Exception e) { log.error(e.getMessage()); if (transport !=
		 * null) { transport.destroy(); } return StringUtils.EMPTY; }
		 */

	}

	@Override
	public String SendMesRequest(String envString, String module, Object content) {
		String network = TibUtils.getNetwork(envString, module);
		String service = TibUtils.getService(envString, module);
		String daemon = TibUtils.getDaemon(envString, module);
		String targetSubject = TibUtils.getTargetSubject(envString, module);
		String fieldName = TibUtils.getFieldName(envString, module);
		String timeoutString = TibUtils.getTimeout(envString, module);
		//String sourceSubject = TibUtils.getSourceSubject(envString, module);

		int timeout = 30;
		if (timeoutString != null && timeoutString != StringUtils.EMPTY) {
			try {

				timeout = Integer.valueOf(timeoutString);

			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		TibrvTransport transport = null;

		try {

			transport = new TibrvRvdTransport(service, network, daemon);
			TibrvMsg tibrvMsg = new TibrvMsg();
			tibrvMsg.setSendSubject(targetSubject);
			
			// add for test

			
			
			/*
			
			
				sourceSubject = "TRULY.G5A.MES.TST.FAB.WX-CLIENT";
				content = MsgUtils.setSourceTarget(content.toString(),
						sourceSubject);
				tibrvMsg.setReplySubject(sourceSubject);
			}*/
			//sourceSubject = transport.createInbox();
			//content = MsgUtils.setSourceTarget(content.toString(),
			//		sourceSubject);
			//tibrvMsg.setReplySubject(sourceSubject);
			/*
			String inbox=transport.createInbox();
			if (module.equals("pms")) {
				
				int n=Integer.parseInt( inbox.substring(inbox.length()-1));
				
				
				inbox= inbox.substring(0,inbox.length()-1)+String.valueOf(n+1);
				//tibrvMsg.setReplySubject(inbox);
				content = MsgUtils.setSourceTarget(content.toString(),
						inbox);
				
				 
				
			} */
			
			tibrvMsg.add(fieldName, content);
			StringBuilder sbBuilder = new StringBuilder();
			sbBuilder.append("Send Query Message -->\n");
			sbBuilder.append("env=" + envString + ";");
			sbBuilder.append("service=" + service + ";");
			sbBuilder.append("network=" + network + ";");
			sbBuilder.append("daemon=" + daemon + ";");
			sbBuilder.append("targetSuject=" + targetSubject + ";\n");
			sbBuilder.append(content);
			log.info(sbBuilder.toString());
			
			TibrvMsg rsMsg = transport.sendRequest(tibrvMsg, timeout);
			
			String inbox2= tibrvMsg.getReplySubject();
            log.info("INBOX2:"+inbox2);
           // log.info("INBOX:"+inbox);
           
			if (rsMsg == null) {
				transport.destroy();
				return StringUtils.EMPTY;

			} else {
				transport.destroy();
				String rpyString = (String) rsMsg.getField(fieldName).data;

				StringBuilder sb = new StringBuilder();
				sb.append("Recieved  Reply Message <-- \n");
				sb.append(rpyString);
				log.info(sb.toString());
				//log.info(rpyString);

				return rpyString;

			}

		} catch (Exception e) {
			log.error(e.getMessage());
			if (transport != null) {
				transport.destroy();
			}
			return StringUtils.EMPTY;
		}

	}

	@Override
	public boolean SendMesNoNeedReply(String envString, Object content) {

		return SendMesNoNeedReply(envString, "oic", content);

	}

	@Override
	public boolean SendMesNoNeedReply(String envString, String module,
			Object content) {

		String targetSubject = TibUtils.getTargetSubject(envString, module);

		return SendMesNoNeedReply(envString, content, targetSubject);

	}

	class QDispatch implements Runnable {
		// private Logger Log= LogManager.getLogger(QDispatch.class);
		@Override
		public void run() {
			while (true) {
				try {
					Tibrv.defaultQueue().dispatch();
				} catch (InterruptedException ie) {
					log.error(ie.getMessage());
					break;
				} catch (TibrvException e) {
					log.error(e.getMessage(), e);
				}

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}

		}

	}

	@Override
	public void onMsg(TibrvListener paramTibrvListener, TibrvMsg paramTibrvMsg) {
		try {

			if (paramTibrvListener != null) {

				if (this.msgListen != null) {
					this.msgListen.onMessage(paramTibrvListener, paramTibrvMsg);
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	@Override
	public void destroy() throws DestroyFailedException {
		try {
			this.close();
			// Destroyable.super.destroy();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	@Override
	public boolean isDestroyed() {

		// return Destroyable.super.isDestroyed();
		return !this.isOpen;
	}

	public ITibMsgListen getMsgListen() {
		return msgListen;
	}

	public void setMsgListen(ITibMsgListen msgListen) {
		this.msgListen = msgListen;
	}

	@Override
	public boolean SendMesNoNeedReply(String envString,String module, Object content,
			String targetSubject) {
		String network = TibUtils.getNetwork(envString, module);
		String service = TibUtils.getService(envString, module);
		String daemon = TibUtils.getDaemon(envString, module);
		// String targetSubject = targetSuject;
		String fieldName = TibUtils.getFieldName(envString, module);
		String sourceSubject = TibUtils.getSourceSubject(envString,module);

		TibrvTransport transport = null;

		try {

			transport = new TibrvRvdTransport(service, network, daemon);
			TibrvMsg tibrvMsg = new TibrvMsg();
			tibrvMsg.setSendSubject(targetSubject);
			tibrvMsg.setReplySubject(sourceSubject);
			tibrvMsg.add(fieldName, content);
			transport.send(tibrvMsg);
			StringBuilder sb = new StringBuilder();
			sb.append("TibcoSend Message Success!\n");
			sb.append("service=" + service + ";");
			sb.append("network=" + network + ";");
			sb.append("daemon=" + daemon + ";");
			sb.append("targetSubject=" + targetSubject + ";\n");
			sb.append(content);
			log.info(sb.toString());

			transport.destroy();
			return true;

		} catch (Exception e) {
			log.error(e.getMessage());
			if (transport != null) {
				transport.destroy();
			}
			return false;
		}
	}

	@Override
	public boolean SendMesNoNeedReply(String envString, Object content,
			String targetSuject) {
		
		return SendMesNoNeedReply(envString,"oic",content,targetSuject);
	}

} // end Tib class

