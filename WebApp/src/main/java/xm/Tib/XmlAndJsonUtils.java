package xm.Tib;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.*;
import org.apache.logging.log4j.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

public class XmlAndJsonUtils {
	private static Logger log = LogManager.getLogger(XmlAndJsonUtils.class);

	public static String Json2Xml(String jsonString) {
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
			// TODO: handle exception
			return StringUtils.EMPTY;
		}

	}

	public static String Xml2Json(String xml) {

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

	private static Object getJsonMap(Element root) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(root.getName(), getMap(root));
		return map;
	}

	private static Object getMap2(Element element) {
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

	private static Object getMap(Element element) {

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
	 * private static Object getElementsMap(Element element) { Map<String,
	 * Object> map = new HashMap<String, Object>(); JSONArray list = new
	 * JSONArray(); Iterator<Element> iterator = element.elements().iterator();
	 * String arykey = StringUtils.EMPTY; while (iterator.hasNext()) {
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

	private static List<Element> getJsonElement(JSONObject json) {
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
				for (Object object2 : jsonArray) {
					List<Element> list2 = (List<Element>) getJsonElement((JSONObject) object2);
					for (Element element2 : list2) {
						element.add(element2);

					}
				}

				list.add(element);
				//log.debug(element.asXML());
				break;

			case 2:

				Element el2 = factory.createElement(key);
				el2.setText(object.toString().trim());
				list.add(el2);
				//log.debug(el2.asXML());

				break;
			case 3:
				JSONObject subJsonObject = (JSONObject) object;
				Element el3 = factory.createElement(key);
				List<Element> subel3 = (List<Element>) getJsonElement(subJsonObject);
				for (Element subel3sub : subel3) {
					el3.add(subel3sub);
				}

				list.add(el3);
				//log.debug(el3.asXML());

				break;

			default:

				break;
			}

		}

		return list;

	}


	public static void testXml2Json() {
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

}

