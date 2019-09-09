package com.pattern.box;



import java.io.File;
import java.io.FileWriter;
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

import com.util.UTL;
public class PPPR_Hardway_4491
{
	
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
//	System.out.println(path);
//	String path1=path+".pdf";
//	System.out.println(path1);
		
//	UTL.setPDFVersion(path,path1);
//	System.out.println("PDF version Lower done...!!  ");
//	UTL.extractXML(path);
//	System.out.println("PDF to XML Conversion done..!! ");
	try
	{
		//Thread.sleep(500);
		//System.out.println("in sleep");
	}
	catch (Exception e) {
		
	}
	UTL.sortXML(path);
	try
	{
		Thread.sleep(100);
		System.out.println("in sleep");
	}
	catch (Exception e) {
		
	}
	System.out.println("XML sorting done..!! ");
	
	File 	  		xFile  = new File(path+"_INPSorted.xml");
	File 	  		cFile  = new File("1553172104491_Roanoke_Config.json");

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
		xList = dcXML.getRootElement().selectNodes("//text[@left='27' and @height='12'and @font='1']");
System.out.println("xList="+xList.size());
	for (int i=0;i<xList.size();i++) {
		xNode =  xList.get(i);
		String names=xNode.getStringValue();
				if(names.contains("TOTALS"))
					{
					continue;
					}
					
		Node de=xNode.selectSingleNode("following::text[2]");
		String dep=de.getStringValue().trim();
		if(dep.contains("TOTALS"))
		{
			continue;
		}
			else
			{
		jItem =  getEmpData(xNode);
		xText = xNode.getStringValue().trim();
		String fname[]=xText.trim().split(",");
		jItem.put("pp_pr_participant_last_name",fname[0]);
     if(fname.length>=2)
     {
		String lname=fname[1];
         String Firstname[]=lname.trim().split("\\s+");
         if(Firstname.length==1)
         {
        	 jItem.put("pp_pr_participant_first_name",Firstname[0]);
       	}else
       	{
       		jItem.put("pp_pr_participant_first_name",Firstname[0]);
       		jItem.put("pp_pr_participant_middle_name",Firstname[1]);
       		
       	}
     }   
			jList.put(jItem);
	}
	}
	for (int i=0;i<jList.length();i++) {
		jItem = jList.getJSONObject(i);
		for (int j=0;j<xRows.length();j++) {
			xCols = xRows.getJSONObject(j);
			if (jItem.has(xCols.getString("Name"))) {
				continue;
			} else {
				jItem.put(xCols.getString("Name"), "");
			}				
		}
		jItem=dateFormat(jItem);
		jItem=addZero(jItem);
		aList.put(jItem);
	}
	xOutp.put("Participant", aList);
	xList = dcXML.getRootElement().selectNodes("//text[contains(text(),'** NET PAY')]");
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

	
System.out.println("output="+xOutp.toString());
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
	for (int j=0; j<xRows.length();j++) {
		xCols = xRows.getJSONObject(j);
		float temp=0;
		//System.out.println("xCols "+xCols);
		int num=0;
		for (int i=0; i<sList.size();i++) {
			sNode = sList.get(i);
		
		int start=0;
			String val=sNode.getStringValue();
		System.out.println("val="+val);
		//jItem.put("pp_pr_period_pay_date","12/20/2017");
		jItem.put("pp_pr_period_ending_date","07/31/2017");
		//jItem.put("pp_pr_period_beginning_date","12/01/2017");
		if(val.contains("401K"))
		{
			String contrib="";
			if(val.length()<=150)
			{
			 contrib=val.substring(32,42);
			}else
			{
				contrib=val.substring(64,74);
			}
				
			contrib=contrib.trim();
			
			if(contrib.length()>=1)
			{
				
			Float contf=Float.parseFloat(contrib);
			System.out.println("contf"+contf);
			Float co=contf/100.0f;
			String cont401k=Float.toString(co);
		
			System.out.println("contrib="+cont401k);
			
			jItem.put("pp_pr_participant_401k_deferral_amount",cont401k);
		}
			}
			
		
		
		if(val.contains("ROTH"))
		{
			String ROTH=val.substring(32,41);
			ROTH=ROTH.trim();
			if(ROTH.length()>=1)
			{
			Float rot=Float.parseFloat(ROTH);
			System.out.println("contf"+rot);
			Float ro=rot/100.0f;
			String rothdef=Float.toString(ro);
		
			System.out.println("rothdef="+rothdef);
			
			jItem.put("pp_pr_participant_roth_deferral_amount",rothdef);
		}
		}
		if(val.contains("EMPLOYEE TOTAL"))
		{
			Node gross=sNode.selectSingleNode("following::text[1]");
			String grossnext=gross.getStringValue();
			if((grossnext.contains("PAYCHEX"))||grossnext.contains("---------------------------------------------------------------"))
			{
				String Gross=val.substring(64,73).trim();
				if(Gross.length()>=1)
				{
					Float FGross=Float.parseFloat(Gross);
					FGross=FGross/100;
					String Finalgross=FGross.toString();
				jItem.put("pp_pr_participant_comp_gross_wages",Finalgross);
			}
			}else
			{
				String Gross=grossnext.substring(64,73).trim();
				
				if(Gross.length()>=1)
				{
					Float FGross=Float.parseFloat(Gross);
					FGross=FGross/100;
					String Finalgross=FGross.toString();
				jItem.put("pp_pr_participant_comp_gross_wages",Finalgross);
			}
			}
			
		}
		
	String left=sNode.valueOf("@left");
	String height=sNode.valueOf("@height");
	String font=sNode.valueOf("@font");
	if((left.equalsIgnoreCase("27"))&&(height.equalsIgnoreCase("12"))&&(font.equalsIgnoreCase("1")))
			{
		break;
			}
			jItem.put(jAttr, jText);
			}
		}
	
	System.out.println("jItem emp"+jItem);
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
  	
	sList = xNode.selectNodes("following::*");
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
					jText = sNode.selectSingleNode("following::text["+jPosx+"]").getStringValue();
				} else {
					jText = sNode.selectSingleNode("preceding::text["+jPosx+"]").getStringValue();
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
	sList = xNode.selectNodes("preceding::text[@top='"+ pLine +"']");
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
		if (sNode.valueOf("@left").equalsIgnoreCase("27")) {
			if (sNode.valueOf("@font").equalsIgnoreCase("1")) {
				break;
			}
		}
		if (sNode.getStringValue().startsWith("Y Gross")) {
			sLine = sNode.numberValueOf("@top");
			int s=sLine.intValue();
			int x=xLine.intValue();
			
			if (!sLine.equals(xLine) ||  (s-x!=1)) {
			}
		}
		nList.add(sNode);
		
	}	
	for(int n=0;n<nList.size();n++)
	{
		Node no=nList.get(n);
	}
	return nList;
}
public static JSONObject addZero(JSONObject jobj) throws JSONException
{
	String keys[]=jobj.getNames(jobj);
	for(int i=0;i<keys.length;i++)
	{
		String value=jobj.getString(keys[i]);
		if(value.equals(""))
		{
			if(keys[i].contains("dob") || keys[i].contains("name") || keys[i].contains("ssn") || keys[i].contains("date") || keys[i].contains("dot") || keys[i].contains("doe") || keys[i].contains("doh"))
			{
				jobj.put(keys[i], "");
			}
			else
			{	
				jobj.put(keys[i], "0");
			}
		}
	}
	return jobj;
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
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			if(keyVal!="")
			{
			Date date = format.parse(keyVal);
			 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
			String strDate= formatter.format(date); 
			jobj.put(keys[i], strDate);
			}
		}
	}
	return jobj;
}
}
