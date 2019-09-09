package com.pattern.row;



import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EC_Tralongo_2118  {
	//C023_Employee_Census_Pork_co.pdf
	//static File					iFile = new File("c023_inp.xml");
	static File					oFile = new File("1550350282118_Census IMPORT_sorted.xml");
	static File 				cFile = new File("1550350282118_Census IMPORT_config.json");
		
	static JSONObject			jConf  = new JSONObject();
	static JSONArray			jList  = new JSONArray();
	static JSONObject			jItem  = new JSONObject();
	static List<Node>			nList  = null;
	static Node					nItem  = null;
	
	static String						sConf = "";

	
public static void main(String[] args) throws DocumentException, JSONException, IOException, NumberFormatException, ParseException {
	
		sConf = FileUtils.readFileToString(cFile,"UTF-8");
		jConf = new JSONObject(sConf);
		JSONArray  alst = new JSONArray();
		JSONArray  blst = new JSONArray();
		JSONArray  clst = new JSONArray();
		JSONArray  dlst = new JSONArray();
		JSONArray  nlst = new JSONArray();
		JSONObject item = new JSONObject();
		JSONArray  list = new JSONArray();
		JSONObject node = new JSONObject();
		JSONObject outp = new JSONObject();
		
		String	   rkey = "";
		String     nkey = "";
	//	transformXML(iFile,oFile);
		Document  		dcXML  = (new SAXReader()).read(oFile);
		alst = getReport_A(dcXML);
		blst = getReport_B(dcXML);
		clst = getReport_C(dcXML);
		dlst = getReport_D(dcXML);

		for (int i = 0; i<alst.length(); i++) {
			node = alst.getJSONObject(i);
			nlst = node.names();
			for (int j=0; j<nlst.length();j++) {
				outp.put(nlst.getString(j), node.getString(nlst.getString(j)));
			}
			rkey = node.getString("ec_participant_last_name") +
				   node.getString("ec_participant_first_name") +
				   node.getString("ec_participant_middle_name") +
				   node.getString("ec_participant_ssn") ;
			for (int j = 0; j<clst.length();j++) {
				item = clst.getJSONObject(j);
				nkey = item.getString("ec_participant_last_name") +
						   item.getString("ec_participant_first_name") +
						   item.getString("ec_participant_middle_name") +
						   item.getString("ec_participant_ssn") ;
				if (nkey.equalsIgnoreCase(rkey)) {
					//System.out.println("comp "+item.getString("ec_participant_eligible_comp"));
					outp.put("ec_participant_eligible_comp",item.getString("ec_participant_eligible_comp"));
					break;
				}
			}
			for (int j = 0; j<dlst.length();j++) {
				item = dlst.getJSONObject(j);
				nkey = item.getString("ec_participant_last_name") +
						   item.getString("ec_participant_first_name") +
						   item.getString("ec_participant_middle_name") +
						   item.getString("ec_participant_ssn") ;
				if (nkey.equalsIgnoreCase(rkey)) {
					outp.put("ec_participant_doe",item.getString("ec_participant_doe"));
					break;
				}
			}
			outp=addZero(outp);
			outp=dateFormat(outp);
			list.put(outp);
			outp = new JSONObject();
		}
		//System.out.println(list);
}

public static JSONArray getReport_A(Document dcXML) throws JSONException {
	List<Node>		mList  = new ArrayList<>();
	List<Node>		nList  = new ArrayList<>();
	List<Node>		xList  = new ArrayList<>();
	List<Node>		sList  = new ArrayList<>();
	List<Node>		cList  = new ArrayList<>();

	Node 			mNode  = null;
	Node			nNode  = null;
	Node  			xNode  = null;
	Node			sNode  = null;	
	Node			cNode  = null;
	
	JSONArray     conf = jConf.getJSONArray("REPORT_A");
	JSONObject    outp = new JSONObject();
	JSONArray     list = new JSONArray();
	JSONObject    item = new JSONObject();
	boolean		  flag = true;
	mList = dcXML.getRootElement().selectNodes("//text[text()='ADPCEN01']");
	int pos1 = 0;
	int pos2 = 0;
	int pos0 = 0;
	String ival = "";

	for (int i=0;i<mList.size();i++) {
		mNode = mList.get(i);
		nList = mNode.selectNodes("following-sibling::text[starts-with(text(),'XXX-XX-')]");
		for (int j=0;j<nList.size();j++) {
			nNode = nList.get(j);
			xList.add(nNode);
		}
	}
	
	for (int i = 0; i<xList.size(); i++) {
		xNode  = xList.get(i);
		sList = xNode.selectNodes("following-sibling::text");
		cList  = new ArrayList<>();
		cList.add(xNode);
		outp = new JSONObject();
		for (int j=0;j<sList.size();j++) {
			sNode = sList.get(j);
			if (sNode.getStringValue().startsWith("XXX-XX-")) {
				break;
			}
			if (sNode.getStringValue().startsWith("IF A PARTICIPANT")) {
				break;
			}
			cList.add(sNode);
		}
		for (int x = 0; x<conf.length(); x++) {
			item = conf.getJSONObject(x);
			pos1 = item.getInt("pos1");
			pos2 = item.getInt("pos2");
			flag = true;
			for (int k=0;k<cList.size();k++) {
				cNode = cList.get(k);
				pos0  = Integer.parseInt(cNode.valueOf("@left"));
				if (pos0 >= pos1 && pos0 <= pos2 ) {
					switch (item.getString("name")) {
						case "ec_participant_ssn"         :
							ival = cNode.getStringValue().substring(0,11);
							break;
						case "ec_participant_last_name"   :
							ival = cNode.getStringValue().substring(12);
							if (ival.indexOf(",") > -1) {
								ival = ival.substring(0, ival.indexOf(","));
							}
							break;
						case "ec_participant_first_name"  :
							ival = cNode.getStringValue().substring(12);
							ival = ival.substring(ival.indexOf(",")+1).trim();
							if (ival.indexOf(" ") > -1) {
								ival = ival.substring(0,ival.indexOf(" ")).trim();
							}
							break;
						case "ec_participant_middle_name" :
							ival = cNode.getStringValue().substring(12);
							ival = ival.substring(ival.indexOf(",")+1).trim();
							if (ival.indexOf(" ") > -1) {
								ival = ival.substring(ival.indexOf(" ")+1);
							} else {
								ival = "";
							}
							break;
						default :
							ival = cNode.getStringValue();
							break;
					}
					outp.put(item.getString("name"),ival.replace(",", ""));
					flag = false;
					break;
				}
			}
			if (flag) {
				outp.put(item.getString("name"), "");
			}			
		}
		list.put(outp);
		outp = new JSONObject();
	
	} 
	return list;
}
public static JSONArray getReport_B(Document dcXML) throws JSONException {
	List<Node>		mList  = new ArrayList<>();
	List<Node>		nList  = new ArrayList<>();
	List<Node>		xList  = new ArrayList<>();
	List<Node>		sList  = new ArrayList<>();
	List<Node>		cList  = new ArrayList<>();

	Node 			mNode  = null;
	Node			nNode  = null;
	Node  			xNode  = null;
	Node			sNode  = null;	
	Node			cNode  = null;
	
	int				pos1 = 0;
	int				pos2 = 0;
	int				pos0 = 0;
	JSONArray     conf = jConf.getJSONArray("REPORT_B");
	JSONObject    outp = new JSONObject();
	JSONArray     list = new JSONArray();
	JSONObject    item = new JSONObject();
	String		  ival = "";
	boolean 	  flag = true;

	mList = dcXML.getRootElement().selectNodes("//text[text()='ADPCEN02']");

	for (int i=0;i<mList.size();i++) {
		mNode = mList.get(i);
		nList = mNode.selectNodes("following-sibling::text[starts-with(text(),'XXX-XX-')]");
		for (int j=0;j<nList.size();j++) {
			nNode = nList.get(j);
			xList.add(nNode);
		}
	}
	
	for (int i = 0; i<xList.size(); i++) {
		xNode  = xList.get(i);
		sList = xNode.selectNodes("following-sibling::text");
		cList  = new ArrayList<>();
		cList.add(xNode);
		outp = new JSONObject();
		for (int j=0;j<sList.size();j++) {
			sNode = sList.get(j);
			if (sNode.getStringValue().startsWith("XXX-XX-")) {
				break;
			}
			if (sNode.getStringValue().startsWith("IF A PARTICIPANT")) {
				break;
			}
			cList.add(sNode);
		}
		for (int x = 0; x<conf.length(); x++) {
			item = conf.getJSONObject(x);
			pos1 = item.getInt("pos1");
			pos2 = item.getInt("pos2");
			flag = true;
			for (int k=0;k<cList.size();k++) {
				cNode = cList.get(k);
				pos0  = Integer.parseInt(cNode.valueOf("@left"));
				if (pos0 >= pos1 && pos0 <= pos2 ) {
					switch (item.getString("name")) {
					case "ec_participant_ssn"         :
						ival = cNode.getStringValue();
						break;
					case "ec_participant_last_name"   :
						ival = cNode.getStringValue();
						if (ival.indexOf(",") > -1) {
							ival = ival.substring(0, ival.indexOf(","));
						}
						break;
					case "ec_participant_first_name"  :
						ival = cNode.getStringValue();
						ival = ival.substring(ival.indexOf(",")+1).trim();
						if (ival.indexOf(" ") > -1) {
							ival = ival.substring(0,ival.indexOf(" ")).trim();
						}
						break;
					case "ec_participant_middle_name" :
						ival = cNode.getStringValue();
						ival = ival.substring(ival.indexOf(",")+1).trim();
						if (ival.indexOf(" ") > -1) {
							ival = ival.substring(ival.indexOf(" ")+1);
						} else {
							ival = "";
						}
						break;
					default :
						ival = cNode.getStringValue();
						break;
					}
					outp.put(item.getString("name"),ival.replace(",", ""));
					flag = false;
					break;
				}
			}
			if (flag) {
				outp.put(item.getString("name"), "");
			}
		}
		list.put(outp);
		outp = new JSONObject();
	}
	return list;
}
public static JSONArray getReport_C(Document dcXML) throws JSONException {
	List<Node>		mList  = new ArrayList<>();
	List<Node>		nList  = new ArrayList<>();
	List<Node>		xList  = new ArrayList<>();
	List<Node>		sList  = new ArrayList<>();
	List<Node>		cList  = new ArrayList<>();

	Node 			mNode  = null;
	Node			nNode  = null;
	Node  			xNode  = null;
	Node			sNode  = null;	
	Node			cNode  = null;
	
	int				pos1 = 0;
	int				pos2 = 0;
	int				pos0 = 0;
	JSONArray     conf = jConf.getJSONArray("REPORT_C");
	JSONObject    outp = new JSONObject();
	JSONArray     list = new JSONArray();
	JSONObject    item = new JSONObject();
	String		  ival = "";
	boolean 	  flag = true;

	mList = dcXML.getRootElement().selectNodes("//text[text()='ADPCEN03']");

	for (int i=0;i<mList.size();i++) {
		mNode = mList.get(i);
		nList = mNode.selectNodes("following-sibling::text[starts-with(text(),'XXX-XX-')]");
		for (int j=0;j<nList.size();j++) {
			nNode = nList.get(j);
			xList.add(nNode);
		}
	}
	
	for (int i = 0; i<xList.size(); i++) {
		xNode  = xList.get(i);
		sList = xNode.selectNodes("following-sibling::text");
		cList  = new ArrayList<>();
		cList.add(xNode);
		outp = new JSONObject();
		for (int j=0;j<sList.size();j++) {
			sNode = sList.get(j);
			if (sNode.getStringValue().startsWith("XXX-XX-")) {
				break;
			}
			if (sNode.getStringValue().startsWith("IF A PARTICIPANT")) {
				break;
			}
			cList.add(sNode);
			
		}
		for (int x = 0; x<conf.length(); x++) {
			item = conf.getJSONObject(x);
			pos1 = item.getInt("pos1");
			pos2 = item.getInt("pos2");
			flag = true;
			for (int k=0;k<cList.size();k++) {
				cNode = cList.get(k);
				System.out.println("cNode "+cNode.asXML());
				pos0  = Integer.parseInt(cNode.valueOf("@left"));
				//System.out.println("pos0 "+pos0);
				//System.out.println("pos1 "+pos1 + "   pos2 "+pos2);
				if (pos0 >= pos1 && pos0 <= pos2 ) {
					switch (item.getString("name")) {
					case "ec_participant_ssn"         :
						ival = cNode.getStringValue();
						break;
					case "ec_participant_last_name"   :
						ival = cNode.getStringValue();
						if (ival.indexOf(",") > -1) {
							ival = ival.substring(0, ival.indexOf(","));
						}
						break;
					case "ec_participant_first_name"  :
						ival = cNode.getStringValue();
						ival = ival.substring(ival.indexOf(",")+1).trim();
						if (ival.indexOf(" ") > -1) {
							ival = ival.substring(0,ival.indexOf(" ")).trim();
						}
						break;
					case "ec_participant_middle_name" :
						ival = cNode.getStringValue();
						ival = ival.substring(ival.indexOf(",")+1).trim();
						if (ival.indexOf(" ") > -1) {
							ival = ival.substring(ival.indexOf(" ")+1);
						} else {
							ival = "";
						}
						break;
					default :
						ival = cNode.getStringValue();
						System.out.println("ival "+ival);
						break;
					}
					outp.put(item.getString("name"),ival.replace(",", ""));
					flag = false;
					break;
				}
			}
			if (flag) {
				outp.put(item.getString("name"), "");
			}
		}
		list.put(outp);
		outp = new JSONObject();

	}
	//System.out.println("list "+list.toString());
	return list;
}
public static JSONArray getReport_D(Document dcXML) throws JSONException {
	List<Node>		mList  = new ArrayList<>();
	List<Node>		nList  = new ArrayList<>();
	List<Node>		xList  = new ArrayList<>();
	List<Node>		sList  = new ArrayList<>();
	List<Node>		cList  = new ArrayList<>();

	Node 			mNode  = null;
	Node			nNode  = null;
	Node  			xNode  = null;
	Node			sNode  = null;	
	Node			cNode  = null;
	
	int				pos1 = 0;
	int				pos2 = 0;
	int				pos0 = 0;
	JSONArray     conf = jConf.getJSONArray("REPORT_D");
	JSONObject    outp = new JSONObject();
	JSONArray     list = new JSONArray();
	JSONObject    item = new JSONObject();
	String		  ival = "";
	boolean 	  flag = true;

	mList = dcXML.getRootElement().selectNodes("//text[text()='ADPCEN04']");

	for (int i=0;i<mList.size();i++) {
		mNode = mList.get(i);
		nList = mNode.selectNodes("following-sibling::text[starts-with(text(),'XXX-XX-')]");
		for (int j=0;j<nList.size();j++) {
			nNode = nList.get(j);
			xList.add(nNode);
		}
	}
	
	for (int i = 0; i<xList.size(); i++) {
		xNode  = xList.get(i);
		sList = xNode.selectNodes("following-sibling::text");
		cList  = new ArrayList<>();
		cList.add(xNode);
		outp = new JSONObject();
		for (int j=0;j<sList.size();j++) {
			sNode = sList.get(j);
			if (sNode.getStringValue().startsWith("XXX-XX-")) {
				break;
			}
			if (sNode.getStringValue().startsWith("IF A PARTICIPANT")) {
				break;
			}
			cList.add(sNode);
		}
		for (int x = 0; x<conf.length(); x++) {
			item = conf.getJSONObject(x);
			pos1 = item.getInt("pos1");
			pos2 = item.getInt("pos2");
			flag = true;
			for (int k=0;k<cList.size();k++) {
				cNode = cList.get(k);
				pos0  = Integer.parseInt(cNode.valueOf("@left"));
				if (pos0 >= pos1 && pos0 <= pos2 ) {
					switch (item.getString("name")) {
					case "ec_participant_ssn"         :
						ival = cNode.getStringValue();
						break;
					case "ec_participant_last_name"   :
						ival = cNode.getStringValue();
						if (ival.indexOf(",") > -1) {
							ival = ival.substring(0, ival.indexOf(","));
						}
						break;
					case "ec_participant_first_name"  :
						ival = cNode.getStringValue();
						ival = ival.substring(ival.indexOf(",")+1).trim();
						if (ival.indexOf(" ") > -1) {
							ival = ival.substring(0,ival.indexOf(" ")).trim();
						}
						break;
					case "ec_participant_middle_name" :
						ival = cNode.getStringValue();
						ival = ival.substring(ival.indexOf(",")+1).trim();
						if (ival.indexOf(" ") > -1) {
							ival = ival.substring(ival.indexOf(" ")+1);
						} else {
							ival = "";
						}
						break;
					default :
						ival = cNode.getStringValue();
						break;
					}
					outp.put(item.getString("name"),ival.replace(",", ""));
					flag = false;
					break;
				}
			}
			if (flag) {
				outp.put(item.getString("name"), "");
			}
		}
		list.put(outp);
		outp = new JSONObject();

	}
	return list;
}


public static JSONObject dateFormat(JSONObject jobj) throws NumberFormatException, JSONException, ParseException
{
	String keys[]=jobj.getNames(jobj);
	for(int i=0;i<keys.length;i++)
	{
		if(keys[i].contains("date") || keys[i].contains("dob") || keys[i].contains("dot") || keys[i].contains("doh") || keys[i].contains("doe"))
		{
			String key=keys[i];
			String keyVal=jobj.getString(key);
			//System.out.println("key  "+key);
			//System.out.println("keyVal "+keyVal);
			
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			if(keyVal!="")
			{
			Date date = format.parse(keyVal);
			 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
			String strDate= formatter.format(date); 
			//Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(keyVal);
			//System.out.println("date "+strDate);
			
			jobj.put(keys[i], strDate);
			}
		}
	}
	return jobj;
}

public static JSONObject addZero(JSONObject jobj) throws JSONException
{
	String keys[]=jobj.getNames(jobj);
	
	for(int i=0;i<keys.length;i++)
	{
		//System.out.println("keys  "+keys[i]);
		String value=jobj.getString(keys[i]);
		if(value.equals(""))
		{
			if(keys[i].contains("dob") || keys[i].contains("name") || keys[i].contains("ssn") || keys[i].contains("date") || keys[i].contains("dot") || keys[i].contains("doe") || keys[i].contains("doh"))
			{
				jobj.put(keys[i], "");
			}
			else
			{	
				//System.out.println("hello");
				jobj.put(keys[i], "0");
			}
		}
	}
	
	return jobj;
}

}
