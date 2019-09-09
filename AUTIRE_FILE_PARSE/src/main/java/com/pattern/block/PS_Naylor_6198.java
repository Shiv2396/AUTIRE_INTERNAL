package com.pattern.block;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.util.*;
import org.apache.commons.io.FileUtils;
//import org.apache.poi.ss.usermodel.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.util.UTL;

public class PS_Naylor_6198 {
	//C024_Participant_Valuation_Summary_Pork_Co.pdf
	static File 				cFile = new File("1553482726198_Participant Account Summary (ADP)_Config.json");
		
	static JSONObject			jConf  = new JSONObject();
	static JSONArray			jList  = new JSONArray();
	static JSONObject			jItem  = new JSONObject();
	static List<Node>			nList  = null;
	static Node					nItem  = null;
	
	static String				sConf = "";

	
public static void main(String[] args) throws DocumentException, JSONException, IOException, NumberFormatException, ParseException {
	List<Node>		employeeNameList  = new ArrayList<Node>();
//	List<Node>		nList  = new ArrayList<Node>();
	List<Node>		xList  = new ArrayList<Node>();
	List<Node>		sList  = new ArrayList<Node>();
	List<Node>		cList  = new ArrayList<Node>();

	
	Node 			eNameNode  = null;
	Node			nNode  = null;
	Node  			xNode  = null;
	Node			sNode  = null;	
	Node			cNode  = null;

	try
	{
		
		String path=args[0];
		System.out.println(path);
		String path1=path+".pdf";
//		System.out.println(path1);
		
		UTL.setPDFVersion(path,path1);
		System.out.println("PDF version Lower done...!!  ");
		UTL.extractXML(path);
		System.out.println("PDF to XML Conversion done..!! ");		
		Thread.sleep(500);
		System.out.println("in sleep");			
		UTL.sortXML(path);		
		Thread.sleep(100);
		System.out.println("in sleep");		
		System.out.println("XML sorting done..!! ");

		
		
		sConf = FileUtils.readFileToString(cFile,"UTF-8");
		jConf = new JSONObject(sConf);
		JSONArray  clst = jConf.getJSONArray("Items");
		JSONObject node = new JSONObject();
		JSONObject item = new JSONObject();
		JSONArray  list = new JSONArray();
		JSONObject outp = new JSONObject();
		String	   ival = "";
//		transformXML(iFile,oFile);
		Document  		dcXML  = (new SAXReader()).read(path+"_INPSorted.xml");
		employeeNameList = dcXML.getRootElement().selectNodes("//text[starts-with(text(),' ***-**-')]");	
		
		for (int i=0;i<employeeNameList.size();i++) {
			eNameNode = employeeNameList.get(i);
			nList = eNameNode.selectNodes("following::text");
			sList = new ArrayList<Node>();
			sList.add(eNameNode);
			for (int j=0;j<nList.size();j++) {
				nNode = nList.get(j);
				if (nNode.getStringValue().startsWith(" ***-**-")) {			
					break;
				}
				if (nNode.getStringValue().startsWith(" ______")) {			
					break;
				}
				sList.add(nNode);
			}
			for (int j=0;j<sList.size();j++) {
				sNode = sList.get(j);
				ival = sNode.getStringValue();
			
				if (ival.startsWith(" ***-**")) {
					ival = ival.substring(1, 12);
				//	System.out.println("ival=  "+ival);
					ival=ival.replaceAll("[-]", "");
				//	System.out.println("ivalafter ="+ival);
					outp.put("pas_part_ssn", ival);
				}
				if (ival.startsWith(" =>")) {
					//System.out.println("ival name=  "+ival);
					outp.put("pas_part_last_name", ival);
					outp.put("pas_part_first_name", ival);
					//outp.put("pas_part_middle_name", ival);
					//ival = ival.substring(3);
					//System.out.println("substring of ival= "+ival);
				/*	if ( ival.indexOf(",") > -1 ) {
						outp.put("pas_part_last_name", ival.substring(0,ival.indexOf(",")));
						outp.put("pas_part_first_name", ival.substring(ival.indexOf(",")+1));
						String mname= ival.substring(ival.indexOf(" ")+1).trim();
						System.out.println("maname  "+mname);
						System.out.println("mname "+mname.substring(mname.indexOf(" ")+1, mname.length()));
						if(mname.contains(" "))
						{
						outp.put("pas_part_middle_name", mname.substring(mname.indexOf(" ")+1, mname.length()));
						//outp.put("pas_part_first_name", ival.trim().substring(ival.trim().indexOf(",")+1,mname.trim().indexOf(" ")));
						}
					}
					*/
				}
				if (ival.startsWith(" Rollover          **9:")) {
					outp.put("pas_part_rollover_contrib", ival);
				}
				if (ival.startsWith(" BIRTH:")) {
					outp.put("pas_part_dob", ival.substring(8,18));
				}
				if (ival.contains("Employee Before-T **A")) {
					ival = ival.trim();
					//System.out.println("401k     "+ival);
					//System.out.println(ival.substring(45,60));
					if(ival.substring(9).trim().equalsIgnoreCase(".00"))
					{
						outp.put("pas_part_ee_401k_contrib","0.00");
						outp.put("pas_part_acct_gain_loss","0.00");
					//	outp.put("pas_part_ee_401k_contrib",ival.substring(9).trim());
					}
					else
					{
						String pival[]=ival.split(" ");
						//System.out.println("lenght "+pival.length +"val "+pival[pival.length-1]);
						//outp.put("pas_part_ee_401k_contrib",ival.substring(9).trim());
						outp.put("pas_part_ee_401k_contrib",ival.substring(45,60).replace(",", "").trim());
						outp.put("pas_part_acct_gain_loss",ival.substring(62,70).replace(",", "").trim());
					}
					
				}
				if (ival.contains("Employee Roth 401 **O")) {
				//	System.out.println("roth "+ival);
				//	System.out.println(ival.substring(45,60));
					outp.put("pas_part_ee_roth_contrib",ival.substring(45,60).trim().replace(",", ""));
				}
				if(ival.contains(" ** TOTAL"))
				{
					//System.out.println("ival==  "+ival);
					ival=ival.substring(3);
					if(ival.indexOf(" ")>-1)
					{
						System.out.println("begbal  "+ival.substring(ival.indexOf(" ")+15,43));
						//-------------------
						if(ival.substring(ival.indexOf(" ")+10,45).trim().equalsIgnoreCase(".00"))
						{
							//System.out.println("in if 0.00");
							outp.put("pas_part_acct_beg_bal", "0.00");
						}
						else
						{
							outp.put("pas_part_acct_beg_bal", ival.substring(ival.indexOf(" ")+15,43).trim().replace(",", ""));
							
						}
						//---------------
						if(ival.substring(ival.indexOf(" ")+119,128).trim().equalsIgnoreCase(".00"))
						{
							//System.out.println("in if 0.00");
							outp.put("pas_part_acct_end_bal", "0.00");
						}
						else
						{
							outp.put("pas_part_acct_end_bal",ival.substring(ival.indexOf(" ")+117,128).trim().replace(",", ""));
							
						}
						//-------------
						
						
						//System.out.println(ival.substring(ival.indexOf(" ")+40,52).trim());
						
					}
				}
				
	/*			if (ival.contains("       A C T I V I T Y      ")) {
					sNode = sList.get(j+2);
					if (!sNode.getStringValue().startsWith("***** INVESTMENTS")) {
						ival = sNode.getStringValue().trim();
						if (ival.length() > 42) {
							outp.put("pas_part_acct_beg_bal",ival.substring(30,42).trim());
							outp.put("pas_part_acct_beg_bal",ival.substring(30,42).trim());
							outp.put("pas_part_acct_end_bal",ival.substring(ival.length()-10,ival.length()).trim());
						}
					}
				}
	*/	/*		if(ival.contains(" ** TOTAL"))
				{
					System.out.println("ival==  "+ival);
					ival=ival.substring(10);
					if(ival.indexOf(" ")>-1)
					{
						outp.put("pas_part_acct_beg_bal", ival.substring(ival.indexOf(" ")+55,60).trim());
						outp.put("pas_part_acct_end_bal",ival.substring(ival.indexOf(" ")+65,70).trim());
						
						System.out.println("beg bal"+ival.substring(ival.indexOf(" ")+55,60).trim());
						System.out.println("end bal"+ival.substring(ival.indexOf(" ")+65,70).trim());
				
					}
				}
		*/
				if (ival.contains("     UNIT/SHARE     ")) {
					sNode = sList.get(j+2);
					ival = sNode.getStringValue().trim();
					if (ival.length() > 114) {
						outp.put("pas_part_forfeiture",ival.substring(104,113).trim().replace(",", ""));
					}
				}
			}
			for (int j=0;j<clst.length();j++) {
				node = clst.getJSONObject(j);
				if (!outp.has(node.getString("name"))) {
					outp.put(node.getString("name"), "");
				}
				
			}
			
			outp=splitName(outp);
		outp=addZero(outp);
		outp=dateFormat(outp);
			//outp=spli(outp);
			//System.out.println("outp "+outp);
			list.put(outp);
			outp = new JSONObject();
		}
		
		JSONObject finalObj=new JSONObject();
		JSONObject summObj=new JSONObject();
		finalObj.put("Participant", list);
		finalObj.put("PlanYear", summObj);
		//System.out.println(finalObj);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		
		//C024DemoRoundFun.roundFunction(list);
		
		
}

public static JSONObject dateFormat(JSONObject jobj) throws NumberFormatException, JSONException, ParseException
{
	@SuppressWarnings("static-access")
	String keys[]=jobj.getNames(jobj);
	for(int i=0;i<keys.length;i++)
	{
		if(keys[i].contains("date") || keys[i].contains("dob") || keys[i].contains("dot") || keys[i].contains("doh"))
		{
			String key=keys[i];
			String keyVal=jobj.getString(key);
			//System.out.println("key  "+key);
			//System.out.println("keyVal "+keyVal);
			
			DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Date date = format.parse(keyVal);
			 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
			String strDate= formatter.format(date); 
			//Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(keyVal);
			//System.out.println("date "+strDate);
			
			jobj.put(keys[i], strDate);
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


public static JSONObject splitName2(JSONObject jobj) throws JSONException
{
	//String keys[]=jobj.getNames(jobj);
	String namearr[]=jobj.getNames(jobj);
	for(int i=0;i<namearr.length;i++)
	{
		String fullName=jobj.getString(namearr[i]);
		System.out.println("Myname="+fullName);

		String temp[]=fullName.trim().split(",");
		if(temp.length==2)
		{
			if(namearr[i].contains("first_name"))
			{
				String firstName=namearr[i];
				jobj.put(firstName, temp[1]);
			}
			if(namearr[i].contains("last_name"))
			{
				String lastName=namearr[i];
				jobj.put(lastName, temp[0]);
			}
		//	System.out.println("first name="+temp[1]);
		//	System.out.println("last name="+temp[0]);
		
		}
	}
	return jobj;
}
public static JSONObject splitName(JSONObject jobj) throws JSONException
{
	String namearr[]=jobj.getNames(jobj);
	for (int i = 0; i < namearr.length; i++) 
	{
		if(namearr[i].contains("name"))
		{
			String fullName=jobj.getString(namearr[i]);
			System.out.println("ggg full name "+fullName);
			String temp[]=fullName.trim().split(",");
			String samp="";
			if(temp.length==2)
			{
			samp=temp[1];
			}
			String temp1[]=samp.trim().split("\\s+");
//			System.out.println("temp1 lenth "+temp1.length);
//			System.out.println("last name"+temp[0]);
			if(namearr[i].contains("last_name"))
			{
				String lastName=namearr[i];
				jobj.put("pas_part_last_name", temp[0]);
			}
			if(temp1.length==1)
			{
				if(namearr[i].contains("first_name"))
				{
					String firstName=namearr[i];
					jobj.put(firstName,temp1[0]);
					//System.out.println("first name "+temp1[0]);
				//	jobj.put(middleName,"");
					
				//}
				//if(namearr[i].contains("middle_name"))
				//{
					String middleName=namearr[i];
					//jobj.put("pas_part_last_name","");
				}
				
			
			}
			if(temp1.length==2)
			{
				if(namearr[i].contains("first_name"))
				{
					String firstName=namearr[i];
				//	System.out.println("ll first name "+temp1[0]);
					jobj.put(firstName, temp1[0]);
				}
				if(namearr[i].contains("middle_name"))
				{
					String middleName=namearr[i];
			//		System.out.println("middleName "+middleName);
					jobj.put("pas_part_middle_name", temp1[1]);
				//	System.out.println("ll middle Name="+temp1[1]);
				}
			
			
			}
			//System.out.println("full name "+fullName);
		}
	}
	
	//String name[]=fullName.split(",");
	//System.out.println(name[0]+"  "+name[1]);
	return jobj;
	
}




public static JSONArray getReport_A(Document dcXML) throws JSONException {
	List<Node>		mList  = new ArrayList<Node>();
	List<Node>		nList  = new ArrayList<Node>();
	List<Node>		xList  = new ArrayList<Node>();
	List<Node>		sList  = new ArrayList<Node>();
	List<Node>		cList  = new ArrayList<Node>();

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
		cList  = new ArrayList<Node>();
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
					outp.put(item.getString("name"),ival);
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
					outp.put(item.getString("name"),ival);
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
					outp.put(item.getString("name"),ival);
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
					outp.put(item.getString("name"),ival);
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
}
