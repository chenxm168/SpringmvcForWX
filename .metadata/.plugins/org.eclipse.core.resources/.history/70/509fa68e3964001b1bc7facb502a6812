package xm.message.wx;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import org.springframework.core.io.ClassPathResource;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import xm.message.MsgUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MsgBaseUtil {
	private Logger log = LogManager.getLogger(MsgBaseUtil.class);
	private String cfgFilePath = "D:\\WebApp\\config\\";
	private String wxSettingFile = cfgFilePath + "wxsetting.xml";
	private String queryXmlFile = cfgFilePath + "querymsg.xml";
	private String mesXmlFile = cfgFilePath + "mesmsg.xml";

	public String getWxSettingFile() {
		return wxSettingFile;
	}

	public void setWxSettingFile(String wxSettingFile) {
		this.wxSettingFile = wxSettingFile;
	}

	public String getQueryXmlFile() {
		return queryXmlFile;
	}

	public void setQueryXmlFile(String queryXmlFile) {
		this.queryXmlFile = queryXmlFile;
	}

	public String getMesXmlFile() {
		return mesXmlFile;
	}

	public void setMesXmlFile(String mesXmlFile) {
		this.mesXmlFile = mesXmlFile;
	}

	/*
	 * public String MakeXmlMessage() {
	 * 
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	 * Date date=new Date(System.currentTimeMillis()) ;
	 * 
	 * 
	 * Document document = DocumentHelper.createDocument(); Element rootElement=
	 * document.addElement("message"); Element header=
	 * rootElement.addElement("header"); Element messagename =
	 * header.addElement("meesagename"); messagename.setText("UserLogin");
	 * Element transactionid=header.addElement("transactionid");
	 * transactionid.setText(formatter.format(date)); ClassPathResource
	 * classPathResource=new ClassPathResource("tibmsg.xml"); try { File file =
	 * classPathResource.getFile(); SAXReader saxReader = new SAXReader();
	 * Document document2= saxReader.read(file); Element element=
	 * document2.getRootElement(); Element element2=(Element)
	 * element.selectSingleNode("//oicMessageSet/UserLogin"); ((Element)
	 * element2.selectSingleNode("USERID")).setText("cxm"); ((Element)
	 * element2.selectSingleNode("PASSWORD")).setText("1"); ((Element)
	 * element2.selectSingleNode("WORKSTATIONNAME")).setText("172.28.48.171");
	 * ((Element) element2.selectSingleNode("FACTORYNAME")).setText("ARY");
	 * ((Element)
	 * element2.selectSingleNode("LOGGEDINUIVERSION")).setText("3.11.4.0");
	 * Element bodyElement= rootElement.addElement("body");
	 * 
	 * 
	 * for(Iterator it=element2.elementIterator();it.hasNext();){ Element el3 =
	 * (Element) it.next();
	 * 
	 * bodyElement.add((Element) el3.clone()); // do something }
	 * 
	 * //bodyElement.add((Element) element2.clone());
	 * 
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(),e); }
	 * logger.debug(document.asXML()); return document.asXML(); }
	 * 
	 * 
	 * 
	 * public String getQueryMsg(String queryname) { ClassPathResource
	 * classPathResource = new ClassPathResource("querymsg.xml"); try{
	 * 
	 * File file = classPathResource.getFile(); SAXReader saxReader = new
	 * SAXReader(); Document document = saxReader.read(file); Element root =
	 * document.getRootElement(); String xpathString = "//set[@name='"+queryname
	 * +"']"; Node elnode= root.selectSingleNode(xpathString); if(elnode!=null)
	 * { Element elmsg =(Element) ((Element)
	 * elnode).selectSingleNode("Message"); if(elmsg!=null) { Element
	 * eltransationid =(Element)
	 * elmsg.selectSingleNode("//Header/TRANSACTIONID");
	 * eltransationid.setText(getTransationid());
	 * 
	 * }
	 * 
	 * 
	 * return elmsg.asXML();
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * }catch(Exception e) { logger.error(e.getMessage()); } return
	 * StringUtils.EMPTY; }
	 */
	public String MakeToMesEventMessage(String xml) {
		return MakeToMesMessage(xml, "oic", null);

	}

	public String MakeToMesQueryMessage(String xml) {
		return MakeToMesMessage(xml, "qry", null);

	}

	public String MakeToMesEventMessage(String xml, String msgname) {
		return MakeToMesMessage(xml, "oic", null);

	}

	public String MakeToMesQueryMessage(String xml, String msgname) {
		return MakeToMesMessage(xml, "qry", null);

	}

	private String MakeToMesMessage(String xml, String systemmodule,
			String msgname) {

		// String env = MsgUtils.getWXEnv(xml);

		String msgName = StringUtils.EMPTY;
		String msg = StringUtils.EMPTY;
		String userid = getWXUserId(xml);
		Map<String, String> map = getWXParamMap(xml);
		switch (systemmodule) {
		case "qry":
			if (msgname != null & msgname != StringUtils.EMPTY) {
				msgName = msgname;
			} else {
				msgName = getWXQueryId(xml);
			}

			msg = MakeQueryMessage(msgName, null);
			break;
		case "oic":
			if (msgname != null & msgname != StringUtils.EMPTY) {
				msgName = msgname;
			} else {
				msgName = getWXMsgName(xml);
			}

			msg = MakeMesMessage(msgName, null);
			break;
		default:
			break;
		}

		try {
			Document mesdoc = DocumentHelper.parseText(msg);
			Element mesroot1 = (Element) mesdoc.getRootElement().clone();
			// log.info(mesroot.asXML());
			Document rtnDoc = DocumentHelper.createDocument();
			rtnDoc.add(mesroot1);
			Element mesroot = rtnDoc.getRootElement();
			Element mesbody = (Element) mesroot
					.selectSingleNode("//Body | //body | //BODY");

			Document wxdoc = DocumentHelper.parseText(xml);
			Element wxroot = wxdoc.getRootElement();
			Element wxbody = (Element) wxroot
					.selectSingleNode("//Body | //body | //BODY");

			// add wx msg body sub Element
			if (wxbody != null) {
				List<Node> nodelist = wxbody.elements();
				if (nodelist != null && nodelist.size() > 0) {
					for (Node node : nodelist) {
						try {
							Element subElement = (Element) node;
							String elName = subElement.getName();
							if (mesbody == null) {

								mesbody = mesroot.addElement("Body");

							} else {
								Element mesel2 = (Element) mesbody
										.selectSingleNode(elName + " | "
												+ elName.toUpperCase());
								if (mesel2 != null) {
									mesbody.remove(mesel2);

								}

								mesbody.add((Element) subElement.clone());

							}

						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}

					}
				}
			}// end add wx msg body element

			if (map != null) {
				for (String key : map.keySet()) {
					if (!key.contains("_")) {

						String valueString = map.get(key);
						if (valueString != null && valueString.length() > 0) {
							try {
								Element mapElement = (Element) mesroot
										.selectSingleNode("//" + key + " | //"
												+ key.toUpperCase() + " | //"
												+ key.toLowerCase());
								if (mapElement != null) {
									mapElement.setText(valueString);
								}

							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}
						}
					}

				} // end for
			}// end add wx parammap
			if (userid != null && userid != StringUtils.EMPTY) {
				
				
				
				List<Node> userNodes = mesroot
						.selectNodes("//EVENTUSER | //EventUser | //eventuser | //USERID | //UserID | //userid ");
				if (userNodes != null && userNodes.size() > 0) {
					for (Node usernode : userNodes) {
						try {
							Element userElement = (Element) usernode;
							userElement.setText(userid);

						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}

					}// end for
				}
			}// end add userevent

			return rtnDoc.asXML();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return StringUtils.EMPTY;
	}

	private String MakeMessage(String msgfile, String msgName,
			Map<String, String> map) {

		try {
			// ClassPathResource classPathResource = new
			// ClassPathResource(msgfile);
			// File file = classPathResource.getFile();
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(msgfile));
			Element root = document.getRootElement();
			String xpathString = "//set[@name='" + msgName + "']";
			Node elnode = root.selectSingleNode(xpathString);
			if (elnode != null) {
				Element message = (Element) ((Element) elnode)
						.selectSingleNode("Message");
				// Element elmsg =(Element) ((Element)
				// elnode).selectSingleNode("Message");
				if (message == null) {
					message = (Element) ((Element) elnode)
							.selectSingleNode("message");
				}
				if (message == null) {
					message = (Element) ((Element) elnode)
							.selectSingleNode("MESSAGE");
				}

				if (message == null) {
					return StringUtils.EMPTY;
				}

				// Document doc = .parseText(message.toString());
				Document doc = new DocumentFactory().createDocument();
				doc.add((Element) message.clone());

				Element elmsg = doc.getRootElement();
				if (elmsg != null) {

					log.debug(doc.asXML());
					/*
					 * List<Node> nodeList =
					 * elmsg.selectNodes("//TRANSACTIONID"); { if
					 * (nodeList.size()>0) { eltransationid=(Element)
					 * nodeList.get(0); } }
					 * 
					 * //eltransationid =(Element)
					 * elmsg.selectSingleNode("/Message/Header/TRANSACTIONID" );
					 * if(eltransationid==null) { eltransationid =(Element)
					 * elmsg.selectSingleNode("//header/TRANSACTIONID" ); }
					 * if(eltransationid==null) { eltransationid =(Element)
					 * elmsg.selectSingleNode("//HEADER/TRANSACTIONID" ); }
					 */
					Element eltransationid = (Element) elmsg
							.selectSingleNode("//TRANSACTIONID");
					if (eltransationid != null) {
						eltransationid.setText(getTransationid());
					}
					if (map != null) {
						for (String keyString : map.keySet()) {
							if (keyString != null) {
								try {

									Element el2 = (Element) elmsg
											.selectSingleNode("//"
													+ keyString.toUpperCase());
									/*
									 * if(el2==null) { el2= (Element)
									 * elmsg.selectSingleNode
									 * ("//body/"+keyString.toUpperCase()); }
									 * if(el2==null) { el2= (Element)
									 * elmsg.selectSingleNode
									 * ("//BODY/"+keyString.toUpperCase()); }
									 */

									if (el2 != null) {
										el2.setText(map.get(keyString)
												.toString());

										log.debug(el2.asXML());
									}

								} catch (Exception e) {
									log.error(e.getMessage(), e);
								}

							}

						}
					}

				}

				log.debug(doc.asXML());
				return doc.asXML();

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		}
		return StringUtils.EMPTY;
	}

	public String MakeMesMessage(String msgName, Map<String, String> map) {

		return MakeMessage(mesXmlFile, msgName, map);

	}

	public String MakeQueryMessage(String msgName, Map<String, String> map) {

		return MakeMessage(queryXmlFile, msgName, map);

	}

	public String getTransationid() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyyMMddHHmmssSSSSSS");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);

	}

	public Element getElement(String xml, String name) {

		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			StringBuilder sb = new StringBuilder();
			sb.append("//");
			sb.append(name.toUpperCase());
			sb.append(" | //" + name.toLowerCase() + " | //");
			sb.append(name.substring(0, 1).toUpperCase()
					+ name.substring(1).toLowerCase());
			String xString = sb.toString();
			Element element = (Element) root.selectSingleNode(xString);
			return element;

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return null;

	}

	public String Json2Xml(String jsonString) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(jsonString);
			Document document = DocumentHelper.createDocument();
			List<Element> list = getJsonElement(jsonObject);
			if (list.size() > 0) {
				document.add(list.get(0));
			}
			log.debug(document.asXML());
			return document.asXML();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		}

	}

	private List<Element> getJsonElement(JSONObject json) {
		log.debug(json.toJSONString());
		DocumentFactory factory = new DocumentFactory();
		List<Element> list = new ArrayList<Element>();
		if (json.size() < 1) {
			return null;
		}

		for (String key : json.keySet()) {

			Object object = json.get(key);
			int num = (object instanceof List<?> ? 1 : 0)
					+ (object instanceof String ? 2 : 0)
					+ (object instanceof JSONObject ? 3 : 0);

			switch (num) {
			case 1:
				Element element = factory.createElement(key);
				JSONArray jsonArray = (JSONArray) object;
				if (jsonArray.size() < 1) {
					list.add(element);
					break;
				}
				for (Object object2 : jsonArray) {
					List<Element> list2 = (List<Element>) getJsonElement((JSONObject) object2);
					for (Element element2 : list2) {
						element.add(element2);

					}
				}

				list.add(element);
				// log.debug(element.asXML());
				break;

			case 2:

				Element el2 = factory.createElement(key);
				el2.setText(object.toString().trim());
				list.add(el2);
				// log.debug(el2.asXML());

				break;
			case 3:
				JSONObject subJsonObject = (JSONObject) object;

				Element el3 = factory.createElement(key);
				if (subJsonObject.keySet().size() < 1) {
					list.add(el3);
					break;
				}
				List<Element> subel3 = (List<Element>) getJsonElement(subJsonObject);

				for (Element subel3sub : subel3) {
					el3.add(subel3sub);
				}

				list.add(el3);
				// log.debug(el3.asXML());

				break;

			default:

				break;
			}

		}

		return list;

	}

	public String Xml2Json(String xml) {

		try {

			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();

			JSONObject jsonObject = new JSONObject(
					(Map<String, Object>) getMap(root));

			log.debug(jsonObject.toJSONString());

			log.debug(jsonObject.toJSONString());

			return jsonObject.toJSONString();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return StringUtils.EMPTY;

	}

	private Object getJsonMap(Element root) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(root.getName(), getMap(root));
		return map;
	}

	private Object getMap2(Element element) {
		String xpathString = "/";
		List<Element> nodeList = element.elements();
		Map<String, Object> map = new HashMap<String, Object>();
		if (nodeList.size() < 1) {
			map.put(element.getName(), element.getText());
			return map;
		}

		try {

			String firstELName = ((Element) nodeList.get(0)).getName();

			int num = element.elements(firstELName).size();

			if (num > 1) {
				JSONArray jsonArray = new JSONArray();
				List<Element> subElements = element.elements();
				for (Element element2 : subElements) {
					Map<String, Object> subMap = new HashMap<String, Object>();
					if (element2.elements().size() < 1) {
						subMap.put(element2.getName(), element2.getText()
								.trim());
					} else {
						subMap.put(element2.getName(), getMap2(element2));
					}
					jsonArray.add(subMap);
				}
				return jsonArray;

			} else {

				List<Element> subElements = element.elements();
				for (Element element2 : subElements) {
					if (element2.elements().size() < 1) {
						map.put(element2.getName(), element2.getText().trim());
					} else {
						map.put(element2.getName(), getMap2(element2));
					}
				}

				return map;

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;

	}

	private Object getMap(Element element) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (element.elements().size() < 1) {
			String textString = element.getText();
			log.debug(textString);
			map.put(element.getName(), element.getText());
		} else {
			// map.put(element.getName(), getElementsMap(element));
			map.put(element.getName(), getMap2(element));
		}
		return map;
	}

	/*
	 * private Object getElementsMap(Element element) { Map<String, Object> map
	 * = new HashMap<String, Object>(); JSONArray list = new JSONArray();
	 * Iterator<Element> iterator = element.elements().iterator(); String arykey
	 * = StringUtils.EMPTY; while (iterator.hasNext()) {
	 * 
	 * Element element2 = iterator.next();
	 * 
	 * if (map.containsKey(element2.getName()) || ((map.size() < 1 && arykey !=
	 * StringUtils.EMPTY && arykey .equals(element2.getName())))) { if
	 * (map.size() > 0) {
	 * 
	 * Map<String, Object> map1 = new HashMap<String, Object>();
	 * map1.put(element2.getName(), map.get(element2.getName()));
	 * list.add(map1); map.remove(element2.getName()); arykey =
	 * element2.getName();
	 * 
	 * Map<String, Object> map2 = new HashMap<String, Object>(); if
	 * (element2.elements().size() < 1) { map2.put(element2.getName(),
	 * element2.getText()); } else { map2.put(element2.getName(),
	 * getElementsMap(element2));
	 * 
	 * }
	 * 
	 * list.add(map2);
	 * 
	 * 
	 * 
	 * } else { Map<String, Object> map2 = new HashMap<String, Object>();
	 * map2.put(element2.getName(), map.get(element2.getName())); if
	 * (element2.elements().size() < 1) { map2.put(element2.getName(),
	 * element2.getText().trim()); } else { map2.put(element2.getName(),
	 * getElementsMap(element2));
	 * 
	 * } list.add(map2);
	 * 
	 * }
	 * 
	 * } else { if (element2.elements().size() < 1) {
	 * map.put(element2.getName(), element2.getText()); } else {
	 * map.put(element2.getName(), getElementsMap(element2));
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } if(map.size()>0) { return map; }else { return list;
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	public void testXml2Json() {
		ClassPathResource classPathResource = new ClassPathResource("test.xml");
		try {
			File file = classPathResource.getFile();
			SAXReader saxReader = new SAXReader();
			Document document2 = saxReader.read(file);
			String jsonString = Xml2Json(document2.asXML());
			log.debug(jsonString);
			JSONObject jsonObject = JSONObject.parseObject(jsonString);
			Json2Xml(Json2Xml(jsonString));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public String getWXModule(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element module = (Element) root
					.selectSingleNode("//MODULE | //module | //Module");
			if (module != null) {
				return module.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public String getWXEnv(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element env = (Element) root
					.selectSingleNode("//ENV | //Env | //env");
			if (env != null) {
				return env.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public String getWXUserEnv(String xml, String userid) {
		if (userid == "huanghanjie1" || userid.toUpperCase().contains("TEST")) {
			return "test";
		}

		else {
			return getWXEnv(xml);
		}

	}

	public String getWXUserId(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element userid = (Element) root
					.selectSingleNode("//USERID | //Userid | //userid");
			if (userid != null) {
				return userid.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public String getWXMsgName(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element msgname = (Element) root
					.selectSingleNode("//MESSAGENAME | //MessageName | //messagename");
			if (msgname != null) {
				return msgname.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public String getWXMsgServiceName(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element service = (Element) root
					.selectSingleNode("//Service | //SERVICE | //service");
			if (service != null) {
				return service.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public Map<String, String> getWXParamMap(String xml) {
		Document doc;
		Map<String, String> map = new HashMap<String, String>();
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element mapElement = (Element) root
					.selectSingleNode("//PARAMMAP | //ParamMap | //parammap");
			if (mapElement != null) {
				List<Node> list = mapElement.elements();
				for (Node node : list) {
					Element el = (Element) node;
					map.put(el.getName(), el.getText());

				}

				// return map;
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return map;

	}

	public String getMesMsgReturnCode(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element returncode = (Element) root
					.selectSingleNode("//RETURNCODE | //ReturnCode | //returncode");
			if (returncode != null) {
				return returncode.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public String getWXQueryId(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element query = (Element) root
					.selectSingleNode("//QUERYID | //Query | //query");
			if (query != null) {
				return query.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public String getWXQueryVer(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element ver = (Element) root
					.selectSingleNode("//VERSION | //Version | //version");
			if (ver != null) {
				return ver.getText();
			}
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return StringUtils.EMPTY;

	}

	public String getXmlElementText(String xml, String name) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			StringBuilder sb = new StringBuilder();
			sb.append("//");
			sb.append(name.toUpperCase());
			sb.append(" | //" + name.toLowerCase() + " | //");
			sb.append(name.substring(0, 1).toUpperCase()
					+ name.substring(1).toLowerCase());
			String xString = sb.toString();
			Element element = (Element) root.selectSingleNode(xString);
			if (element != null) {
				return element.getText();
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return StringUtils.EMPTY;

	}

	public Element getXmlElement(String xml, String name) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			StringBuilder sb = new StringBuilder();
			sb.append("//");
			sb.append(name.toUpperCase());
			sb.append(" | //" + name.toLowerCase() + " | //");
			sb.append(name.substring(0, 1).toUpperCase()
					+ name.substring(1).toLowerCase());
			String xString = sb.toString();
			Element element = (Element) root.selectSingleNode(xString);
			if (element != null) {
				return element;
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return null;

	}

	public String makeJsonMsgForQueryReplyMsg(String mesreply) {

		String rtncodeString = getMesMsgReturnCode(mesreply);
		try {
			Document doc = DocumentHelper.parseText(mesreply);
			Element root = doc.getRootElement();
			try {

				Element head = (Element) root
						.selectSingleNode("//Header | //HEADER | //header");
				if (head != null) {
					root.remove(head);
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			if (!rtncodeString.trim().equals("0")) {
				try {
					Element body = (Element) root
							.selectSingleNode("//Body | //BODY | //body");
					if (body != null) {
						root.remove(body);
					}

				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

			}

			return MsgUtils.Xml2Json(doc.asXML());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		}

	}

	public String makeJsonMsgForEventReplyMsg(String mesreply) {
		String rtncodeString = getMesMsgReturnCode(mesreply);
		try {
			Document doc = DocumentHelper.parseText(mesreply);
			Element root = doc.getRootElement();
			try {

				Element head = (Element) root
						.selectSingleNode("//Header | //HEADER | //header");
				if (head != null) {
					root.remove(head);
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			if (rtncodeString.trim().equals("0")) {
				try {
					Element body = (Element) root
							.selectSingleNode("//Body | //BODY | //body");
					if (body != null) {
						root.remove(body);
					}

				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

			}

			return MsgUtils.Xml2Json(doc.asXML());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		}

	}

	public Document MakeBaseReturnMessageDoc() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("Message");
		root.addElement("Body");
		Element rtn = root.addElement("Return");
		rtn.addElement("RETURNCODE", "0");
		rtn.addElement("RETURNMESSAGE");
		return doc;

	}

	public String setSourceTarget(String xml, String source) {
		Document rtn = DocumentHelper.createDocument();
		try {
			Document doc = DocumentHelper.parseText(xml);
			rtn = DocumentHelper.createDocument();
			Element root = doc.getRootElement();
			rtn.add((Element) root.clone());
			Element rtnroot = rtn.getRootElement();
			Element element = (Element) rtnroot
					.selectSingleNode("//ORIGINALSOURCESUBJECTNAME");
			if (element != null) {
				element.setText(source);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return rtn.asXML();

	}

	public List<String> getParamList(String xml) {
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element element = (Element) root
					.selectSingleNode("//PARAMLIST | //ParamList |//paramlist");
			if (element != null) {
				List<Node> list = element.elements();
				for (Node node : list) {
					Element e2 = (Element) node;
					if (e2 != null) {
						arrayList.add(e2.getText());
					}

				}
				return arrayList;

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;

	}

	public Document AttachWxPageSet(String xml, String pagename) {
		try {

			// ClassPathResource classPathResource = new
			// ClassPathResource("wxsetting.xml");
			// File file = classPathResource.getFile();
			SAXReader saxReader = new SAXReader();
			Document document = saxReader
					.read(new File(this.getWxSettingFile()));
			Element setroot = document.getRootElement();
			Element setpage = (Element) setroot
					.selectSingleNode("//pages/page[@name='" + pagename + "']");

			Document mesdoc = DocumentHelper.parseText(xml);
			Element mesroot = mesdoc.getRootElement();
			Document rtndoc = DocumentHelper.createDocument();
			rtndoc.add((Element) mesroot.clone());
			Element rtnbody = (Element) rtndoc.getRootElement()
					.selectSingleNode("//Body | //Body | //BODY");
			if (rtnbody != null) {
				rtnbody.add((Element) setpage.clone());
			}
			return rtndoc;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public Document AttachTableData(Document rtndoc, String xml,
			String tablename) {

		try {
			Document mesdoc = DocumentHelper.parseText(xml);
			Element mesroot = mesdoc.getRootElement();
			Element rtnroot = rtndoc.getRootElement();
			Element rtnbody = (Element) rtnroot
					.selectSingleNode("//Body | //BODY | //body");
			Element rtnDATALIST = (Element) rtnroot
					.selectSingleNode("//DATALIST | //DataList | //datalist");
			Element mesDATALIST = (Element) mesroot
					.selectSingleNode("//DATALIST | //DataList | //datalist");
			if (mesDATALIST != null) {
				Element rtntabledata = (Element) rtnroot
						.selectSingleNode("//TABLEDATA | //TableData | //tabledata");
				if (rtntabledata == null) {
					// Element rtnbody=(Element)
					// rtnroot.selectSingleNode("//Body | //BODY | //body");
					if (rtnbody == null) {
						rtnbody.addElement("Body");
						rtntabledata = rtnbody.addElement("TABLEDATA");
					} else {
						rtntabledata = rtnbody.addElement("TABLEDATA");
					}
				}

				Element rtntable = rtntabledata.addElement(tablename);
				// rtntable.addElement("TABLENAME",tablename);
				rtntable.add((Element) mesDATALIST.clone());

				try {
					rtnbody.remove(rtnDATALIST);

				} catch (Exception e) {
					// TODO: handle exception
				}
				return rtndoc;

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return rtndoc;

	}

	public Element getWxPageSetting(String pgname) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader
					.read(new File(this.getWxSettingFile()));
			Element setroot = document.getRootElement();
			Element setpage = (Element) setroot
					.selectSingleNode("//page[@name='" + pgname
							+ "'] | //page[@name='" + pgname.toUpperCase()
							+ "']");
			if (setpage != null) {

				Element pgsetting = (Element) setpage
						.selectSingleNode(".//pagesetting | .//PageSetting | .//PAGESETTING");
				if (pgsetting != null) {
					return (Element) pgsetting.clone();
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;

	}

	public Element getWxTableSetting(String pgname, String tbname) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader
					.read(new File(this.getWxSettingFile()));
			Element setroot = document.getRootElement();
			Element setpage = (Element) setroot
					.selectSingleNode("//page[@name='" + pgname
							+ "'] | //page[@name='" + pgname.toUpperCase()
							+ "']");
			if (setpage != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(".//" + tbname + "/setting");
				sb.append(" | .//" + tbname.toUpperCase() + "/setting");
				sb.append(" | .//" + tbname.toLowerCase() + "/setting");
				log.debug(sb.toString());

				Element tbsetting = (Element) setpage.selectSingleNode(sb
						.toString());

				if (tbsetting != null) {
					return (Element) tbsetting.clone();
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public Element getWxTableHeaders(String pgname, String tbname) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader
					.read(new File(this.getWxSettingFile()));
			Element setroot = document.getRootElement();
			Element setpage = (Element) setroot
					.selectSingleNode("//page[@name='" + pgname
							+ "'] | //page[@name='" + pgname.toUpperCase()
							+ "']");
			if (setpage != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(".//" + tbname + "/headers");
				sb.append(" | .//" + tbname.toUpperCase() + "/headers");
				sb.append(" | .//" + tbname.toLowerCase() + "/headers");

				Element tbsetting = (Element) setpage.selectSingleNode(sb
						.toString());

				if (tbsetting != null) {
					return (Element) tbsetting.clone();
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public Document AttachElement(Document rtndoc, String locaction,
			Element element) {
		if (element == null) {
			return rtndoc;
		}
		if (rtndoc == null) {
			rtndoc = this.MakeBaseReturnMessageDoc();
		}

		Element rtnroot = rtndoc.getRootElement();
		Element rtnbody = (Element) rtnroot
				.selectSingleNode("//Body | //BODY | //body");
		if (rtnbody == null) {
			rtnbody = rtnroot.addElement("Body");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(".//" + locaction);
		sb.append(" | .//" + locaction.toLowerCase());
		sb.append(" | .//" + locaction.toUpperCase());

		Element element2 = (Element) rtnbody.selectSingleNode(sb.toString());
		if (element2 == null) {
			element2 = rtnbody.addElement(locaction);
		}
		// Element element3=(Element) element2.selectSingleNode(".//"+dataname
		// +" | .//"+dataname.toUpperCase());
		// if(element3==null)
		// {
		// element3=element2.addElement(dataname);
		// }

		element2.add((Element) element.clone());

		return rtndoc;
	}

	public Document DeleteBodyDataList(Document rtndoc) {
		Element rtnroot = rtndoc.getRootElement();
		Element rtnbody = (Element) rtnroot
				.selectSingleNode("//Body | //BODY | //body");
		if (rtnbody != null) {
			try {
				Element rtndatalist = (Element) rtnbody
						.selectSingleNode("./DATALIST | ./datalist | ./DataList");
				if (rtndatalist != null) {
					rtnbody.remove(rtndatalist);

				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return rtndoc;
	}

}
