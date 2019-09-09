package com.pattern.box;

import com.util.*;
import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PPPR_HFS_5567 extends UTL {
//ADP_Master_Control_37.pdf

	
	static List<Node>			nList  = null;
	static JSONArray			jList  = new JSONArray();
	static JSONArray       		xRows  = new JSONArray();
	static JSONArray			aList  = new JSONArray();
	static JSONObject			jItem  = new JSONObject();
	static JSONArray			bList  = new JSONArray(); 
	static Node					nItem  = null;
	static JSONObject      		xCols  = new JSONObject();
	static JSONObject			xConf  = new JSONObject();
	static JSONObject			xOutp  = new JSONObject();
public static void main(String[] args) throws DocumentException, JSONException, IOException, InterruptedException, NumberFormatException, ParseException {
	
	String path=args[0];
	System.out.println(path);
	String path1=path+".pdf";
//	System.out.println(path1);
		
	setPDFVersion(path,path1);
	System.out.println("PDF version Lower done...!!  ");
	extractXML(path);
	System.out.println("PDF to XML Conversion done..!! ");
	try
	{
		Thread.sleep(20000);
		System.out.println("in sleep");
	}
	catch (Exception e) {
		
	}
	sortXML(path);
	try
	{
		Thread.sleep(1000);
		System.out.println("in sleep");
	}
	catch (Exception e) {
		
	}
	System.out.println("XML sorting done..!! ");
	
	File 	  		xFile  = new File(path+"_INPSorted.xml");
	File 	  		cFile  = new File("1553180395567_Payroll 1-15 Fin_Config.json");
	SAXReader 		xRead  = new SAXReader();
	Document  		dcXML  = xRead.read(xFile);
	List<Node>		xList  = null; 
	Node  			xNode  = null;
	Node			nNode  = null;
	Node  			sNode  = null;
	String			xText  = null;
	int             xPosn  = -1;
	String string = FileUtils.readFileToString(cFile,"UTF-8");
	xConf = new JSONObject(string);
	xRows = xConf.getJSONArray("Items");

	xList = dcXML.getRootElement().selectNodes("//text[@left='22' and @font='6']");

	for (int i=0;i<xList.size();i++) {
		xNode =  xList.get(i);
		jItem =  getEmpData(xNode);
		xText = xNode.getStringValue().trim();
		//System.out.println("xText="+xText);
		xPosn = xText.indexOf(",");
		
		Node middNameNode=xNode.selectSingleNode("following-sibling::text[1]");
		if(middNameNode.getStringValue().length()<10 && !middNameNode.getStringValue().contains(":"))
		//{
			jItem.put("pp_pr_participant_middle_name",middNameNode.getStringValue());
		System.out.println("midName  "+middNameNode.asXML());
		//}
		if (xPosn > -1) {
			jItem.put("pp_pr_participant_last_name", xText.substring(0, xPosn).trim());
			String fName=xText.substring(xPosn+1).trim();
			if(fName.contains(" "))
			{
				//String mName=fName.substring(fName.indexOf(" "), fName.length());
				jItem.put("pp_pr_participant_first_name",fName.substring(0,fName.indexOf(" ")));
				
			}
			else
			{
			//	jItem.put("pp_pr_participant_middle_name", "");
				jItem.put("pp_pr_participant_first_name",fName);
			}
			
			//jItem.put("yepr_participant_first_name",xText.substring(xPosn+1).trim());
			sNode = xNode.selectSingleNode("following-sibling::text[1]");
			if (sNode.valueOf("@top").equalsIgnoreCase(xNode.valueOf("@top"))) {
				if (sNode.getStringValue().equalsIgnoreCase("Gross:") ) { 
					
					//jItem.put("yepr_participant_middle_name","");
				} else {
				//	jItem.put("yepr_participant_middle_name",sNode.getStringValue().trim());
					//System.out.println("gg middle name "+sNode.getStringValue().trim());
				}
			} else {
				//jItem.put("yepr_participant_middle_name","");
			}
			
		} else {
			nNode = xNode.selectSingleNode("following-sibling::text[1]");
			xText = xText + " " + nNode.getStringValue().trim();
			xPosn = xText.indexOf(",");
			if (xPosn > -1) {
				jItem.put("pp_pr_participant_last_name", xText.substring(0, xPosn).trim());
				String fName=xText.substring(xPosn+1).trim();
				if(fName.contains(" "))
				{
				//	String mName=fName.substring(fName.indexOf(" "), fName.length());
					jItem.put("pp_pr_participant_first_name",fName.substring(0,fName.indexOf(" ")));
					//jItem.put("pp_pr_participant_middle_name",mName);
				}
				else
				{
					jItem.put("pp_pr_participant_first_name",fName);
					//jItem.put("pp_pr_participant_middle_name", "");
				}
				//jItem.put("yepr_participant_first_name",xText.substring(xPosn+1).trim());
				sNode = nNode.selectSingleNode("following-sibling::text[1]");
				if (sNode.valueOf("@top").equalsIgnoreCase(nNode.valueOf("@top"))) {
					if (sNode.getStringValue().equalsIgnoreCase("Gross:") ) { 
					//	jItem.put("yepr_participant_middle_name","");
					} else {
						//jItem.put("pp_pr_participant_middle_name",sNode.getStringValue().trim());
					//	System.out.println("ss middle name "+sNode.getStringValue().trim());
					}
				} else {
					//jItem.put("yepr_participant_middle_name","");
				}
			}
		}
		//if()
		
//		jItem.put("Page", xNode.valueOf("../@number"));
		//System.out.println("jItem "+jItem);
		jList.put(jItem);
	}
	for (int i=0;i<jList.length();i++) {
		jItem = jList.getJSONObject(i);
		//System.out.println("jItem "+jItem);
		for (int j=0;j<xRows.length();j++) {
			xCols = xRows.getJSONObject(j);
			if (jItem.has(xCols.getString("Name"))) {
				continue;
			} else {
				jItem.put(xCols.getString("Name"), "");
			}				
		}
		jItem=dateFormatRemoveDot(jItem);
		jItem=addZero(jItem);
		aList.put(jItem);
	}
	xOutp.put("Participant", aList);
	
	
	xList = dcXML.getRootElement().selectNodes("//text[contains(text(),'Hourly Rates')]");
	xRows = xConf.getJSONArray("Summary");
	for (int i=0;i<xList.size();i++) {
		xNode = xList.get(i);
		jItem =  getSumData(xNode);
		for (int j=0;j<xRows.length();j++) {
			xCols = xRows.getJSONObject(j);
			if (jItem.has(xCols.getString("Name"))) {
				continue;
			} else {
				jItem.put(xCols.getString("Name"), "");
			}
		}
		JSONArray jarr=new JSONArray();
		jarr.put(jItem);
		xOutp.put("PlanYear",jarr);
	}

	
	System.out.println(xOutp.toString());
/*	try (FileWriter file = new FileWriter(path+".json")) {

    file.write(xOutp.toString());
    System.out.println("json file created..!!");
    file.flush();

} catch (IOException e) {
    e.printStackTrace();
}
*/   
}

public static JSONObject getEmpData(Node xNode) throws JSONException {
	JSONObject jItem = new JSONObject();
	List<Node> sList = null;
	Node	   sNode = null;
	String	   jText = "";
	String     jAttr = "";
	String     jPosx = "1";
	String 	   jType = "String";
	
	sList = getNodeList(xNode);
	//System.out.println("slist size "+sList.size());
	//System.out.println("xNode "+xNode.asXML());
	for (int j=0; j<xRows.length();j++) {
		xCols = xRows.getJSONObject(j);
		//System.out.println("xCols "+xCols);
		for (int i=0; i<sList.size();i++) {
			sNode = sList.get(i);
			//System.out.println("sNode "+sNode.asXML());
			jText = "";
			if (sNode.getStringValue().equalsIgnoreCase(xCols.getString("Path"))) {
				//System.out.println("sNode "+sNode.getStringValue());
				//System.out.println(" path   "+xCols.getString("Path"));
				jAttr = xCols.getString("Name");
				jPosx = xCols.getString("Posx");
				jType = xCols.getString("Type");
				if ( xCols.get("Posn").equals("F")) {
					jText = sNode.selectSingleNode("following-sibling::text["+jPosx+"]").getStringValue();
					
				} else {
					jText = sNode.selectSingleNode("preceding-sibling::text["+jPosx+"]").getStringValue();
					Pattern p=Pattern.compile("[A-Z]");
					Matcher m=p.matcher(jText);
					if(m.find())
					{
						jText = sNode.selectSingleNode("preceding-sibling::text["+1+"]").getStringValue();
						jText=jText.substring(0, jText.indexOf("Ac"));
					}
				}
				if (jType.equals("Number")) {
					jText = jText.replaceAll("\\s+","");
					if ( jText.length() > 1 ) {
						if (jText.indexOf(".") == -1 ) {
							jText = jText.substring(0, jText.length()-2) + "." + jText.substring(jText.length()-2);
						}
					}
				}
				
				jItem.put(jAttr, jText.replace(" ", "."));
				break;
			}
		}
	}
	//System.out.println("jItem emp"+jItem);
	return jItem;
}
public static JSONObject getSumData(Node xNode) throws JSONException {
	JSONObject jItem = new JSONObject();
	List<Node> sList = null;
	Node	   sNode = null;
	String	   jText = "";
	String     jAttr = "";
	String     jPosx = "1";
	String	   jType = "String";
	
	sList = xNode.selectNodes("following-sibling::*");
	for (int i=0; i<sList.size();i++) {
		sNode = sList.get(i);
		jText = "";
		for (int j=0; j<xRows.length();j++) {
			xCols = xRows.getJSONObject(j);
			if (sNode.getStringValue().equalsIgnoreCase(xCols.getString("Path"))) {
				jAttr = xCols.getString("Name");
				jPosx = xCols.getString("Posx");
				jType = xCols.getString("Type");
				if ( xCols.get("Posn").equals("F")) {
					jText = sNode.selectSingleNode("following-sibling::text["+jPosx+"]").getStringValue();
				} else {
					jText = sNode.selectSingleNode("preceding-sibling::text["+jPosx+"]").getStringValue();
				}
				if (jType.equals("Number")) {
					jText = jText.replaceAll("\\s+","");
					if ( jText.length() > 1 ) {
						jText = jText.substring(0, jText.length()-2) + "." + jText.substring(jText.length()-2);
					}
				}
				jItem.put(jAttr, jText.replace(" ", ""));
			}
		}

	}
	//System.out.println("jItem getsum"+jItem);
	return jItem;
}

public static List<Node> getNodeList(Node xNode) {
	List <Node> nList = new ArrayList<Node>();
	List<Node>  sList = null;
	Node		sNode = null;
	Number		pLine = xNode.numberValueOf("@top").intValue() - 1;
	Number 		xLine = xNode.numberValueOf("@top");
	Number      sLine = 0;
	Boolean		sNext = false;

//	sList = xNode.selectNodes("preceding-sibling::text[@top='"+ sLine +"'] | following-sibling::*");
	sList = xNode.selectNodes("preceding-sibling::text[@top='"+ pLine +"']");
	//Node tNode= xNode.selectNodes("following::text[@left='22' and @font='6']").get(0);
	
//	System.out.println(""+xNode.asXML()+' '+sList.size());
	for (int i=0;i<sList.size();i++) {
		sNode = sList.get(i);
		if (sNode.getStringValue().startsWith("Y Gross")) {
			sNext = true;
		}
		nList.add(sNode);			
	}
	sList = xNode.selectNodes("following::text");
	for (int i=0;i<sList.size();i++) {
		sNode = sList.get(i);
		if (sNode.valueOf("@left").equalsIgnoreCase("22")) {
			if (sNode.valueOf("@font").equalsIgnoreCase("6")) {
				break;
			}
		}
		
/*		
		if(sNode.getStringValue().equals(tNode.getStringValue()))
		{
			System.out.println(sNode);
			
			break;
		}
*/	
/*		if (sNode.valueOf("@top").equalsIgnoreCase("840")) {
			break;
		}
*/		
		if (sNode.getStringValue().startsWith("Y Gross")) {
			sLine = sNode.numberValueOf("@top");
			//System.out.println("sLine "+sLine +"xLine "+xLine);
			int s=sLine.intValue();
			int x=xLine.intValue();
			
			if (!sLine.equals(xLine) ||  (s-x!=1)) {
				//break;
			}
		}
		nList.add(sNode);
		
	}	
//	if (sNext == true) {
//		sList = xNode.selectNodes("../following-sibling::page[1]/text");
//		for (int i=0; i<sList.size();i++) {
//			sNode = sList.get(i);
//			if (sNode.valueOf("@left").equalsIgnoreCase("22")) {
//				if (sNode.valueOf("@font").equalsIgnoreCase("6")) {
//					break;
//				}
//			}
//			nList.add(sNode);	
//		}
//	}
	
	for(int n=0;n<nList.size();n++)
	{
	//	Node no=nList.get(n);
		//System.out.println("nList "+no.asXML());
	}
	return nList;
	
	
	
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


public static JSONObject dateFormat(JSONObject jobj) 
{
	try
	{
		
	
	String keys[]=jobj.getNames(jobj);
	for(int i=0;i<keys.length;i++)
	{
		if(keys[i].contains("date") || keys[i].contains("dob") || keys[i].contains("dot") || keys[i].contains("doh") || keys[i].contains("doe"))
		{
			String key=keys[i];
			String keyVal=jobj.getString(key);
		//	System.out.println("key  "+key);
		//	System.out.println("keyVal "+keyVal);
			
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			if(keyVal!="")
			{
			Date date = format.parse(keyVal);
			 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
			String strDate= formatter.format(date); 
			//Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(keyVal);
		//	System.out.println("date "+strDate);
			
			jobj.put(keys[i], strDate);
			}
		}
	}
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return jobj;
}


public static JSONObject dateFormatRemoveDot(JSONObject jobj) throws NumberFormatException, JSONException, ParseException
{
	String keys[]=jobj.getNames(jobj);
	for(int i=0;i<keys.length;i++)
	{
		if(keys[i].contains("date") || keys[i].contains("dob") || keys[i].contains("dot") || keys[i].contains("doh") || keys[i].contains("doe"))
		{
			String key=keys[i];
			String keyVal=jobj.getString(key);
		//	System.out.println("key  "+key);
		//	System.out.println("keyVal "+keyVal);
			
			
			
			 
			
			
			jobj.put(keys[i], keyVal.replace(".", ""));
			}
		}
	
	return jobj;
}
}


