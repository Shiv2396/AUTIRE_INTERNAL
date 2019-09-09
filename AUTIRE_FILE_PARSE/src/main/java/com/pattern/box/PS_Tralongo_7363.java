package com.pattern.box;

//package com.zencon.pdf;

import com.util.*;
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

public class PS_Tralongo_7363 extends UTL {
//ADP_Master_Control_37.pdf

	
	static List<Node>			nList  = new ArrayList();
	static List<Node>			sList  = null;
	static JSONArray			jList  = new JSONArray();
	static JSONArray       		xRows  = new JSONArray();
	static JSONArray			aList  = new JSONArray();
	static JSONObject			jItem  = new JSONObject();
	static JSONArray			bList  = new JSONArray(); 
	static Node					nItem  = null;
	static JSONObject      		xCols  = new JSONObject();
	static JSONObject			xConf  = new JSONObject();
	static JSONObject			xOutp  = new JSONObject();
	static String				sConf = "";
	static JSONObject			jConf  = new JSONObject();
public static void main(String[] args) throws DocumentException, JSONException, IOException, InterruptedException, NumberFormatException, ParseException 
{
	
	File 	  		xFile  = new File("Sorted_1550345512637_YTD IMPORT.xml");
	File 	  		cFile  = new File("1550345512637_YTD IMPORT_Config.json");
	SAXReader 		xRead  = new SAXReader();
	Document  		dcXML  = xRead.read(xFile);
	List<Node>		xList  = null; 
	Node  			xNode  = null;
	Node			nNode  = null;
	Node  			sNode  = null;
	String			xText  = null;
	int             xPosn  = -1;
	JSONObject item = new JSONObject();
	
	
	
	sConf = FileUtils.readFileToString(cFile,"UTF-8");
	jConf = new JSONObject(sConf);
	JSONArray  conf = jConf.getJSONArray("Items");

	xList = dcXML.getRootElement().selectNodes("//text[contains(text(),',')]");
	//System.out.println("xList size "+xList.size());
	
	for (int i=0;i<5;i++) 
	{
		xNode =  xList.get(i);
		System.out.println("xNode "+xNode.asXML());
		nList  = new ArrayList();
		nList.add(xNode);
		sList=xNode.selectNodes("following::text");
		//System.out.println("sList size "+sList.size());
		
		for(int s=0;s<sList.size();s++)
		{
			sNode=sList.get(s);
			//System.out.println("sNode "+sNode.getStringValue());
			nList.add(sNode);
			if(sNode.getStringValue().contains(","))
			{
				break;
			}
			if(sNode.getStringValue().contains("-----"))
			{
				break;
			}
			//System.out.println("sNode "+sNode.getStringValue());
			
		}
		
			//System.out.println("nNode "+nNode.asXML());
			
			for (int x = 0; x<conf.length(); x++) 
			{
				item = conf.getJSONObject(x);
						
				System.out.println("nList size "+nList.size());
				for (int k=0;k<nList.size();k++) 
				{
					nNode=nList.get(k);
					System.out.println("nNode dddd "+nNode.getStringValue());
					if(nNode.getStringValue().contains(","))
					{
						System.out.println("name "+nNode.getStringValue());
						System.out.println("config "+item.getString("yepr_participant_first_name"));
						//outp.put(item.getString("name"),ival);
					}
				//	System.out.println("item "+item.getString(""));
					//nNode = nList.get(k);
					//System.out.println("cNode="+cNode.getStringValue());
					
					
					
				}
				
			}
			
		
		
		
	}
	
	
	
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





}


