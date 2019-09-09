package com.pattern.column;


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

public class PS_LBUCC_6574 //extends UTL {
//ADP_Master_Control_37.pdf
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
	String names="";
	//String path=args[0];
//System.out.println(path);
	//String path1=path+".pdf";
//	System.out.println(path1);
		
//	setPDFVersion(path,path1);
	//System.out.println("PDF version Lower done...!!  ");
	//extractXML(path);
	//System.out.println("PDF to XML Conversion done..!! ");
	try
	{
		//Thread.sleep(20000);
		//System.out.println("in sleep");
	}
	catch (Exception e) {
		// TODO: handle exception
	}
	//transformXML(path);
	try
	{
		//Thread.sleep(1000);
		//System.out.println("in sleep");
	}
	catch (Exception e) {
		// TODO: handle exception
	}
	//System.out.println("XML sorting done..!! ");
	
	File 	  		xFile  = new File("sorted_1552041486574_Participant Account Summary_INP.xml");
	File 	  		cFile  = new File("1552041486574_Participant Account Summary_Config.json");
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
	//xList =dcXML.getRootElement().selectNodes("//text[contains(text(),'-----------------------------------------------------------------------------------------------------') or contains(text(),'Dr')]");
	xList = dcXML.getRootElement().selectNodes("//text[@left='81' and @font='5']");
System.out.println("xList="+xList.size());
	for (int i=0;i<xList.size();i++) {
		xNode =  xList.get(i);
		System.out.println("name="+xNode.getStringValue());
	jItem =  getEmpData(xNode);
	names=xNode.getStringValue();
	//System.out.println("name="+names);
	/*	if(!names.contains(","))
		{
			continue;
		//	jItem.put("yepr_participant_first_name","");
			//jItem.put("yepr_participant_middle_name","");
			//jItem.put("yepr_participant_last_name",names.trim());
		}
		*/
		//System.out.println("name="+names);
		xText = xNode.getStringValue().trim();
		//System.out.println("xText="+xText);
		xPosn = xText.indexOf(",");
		if (xPosn > -1) {
			jItem.put("yepr_participant_last_name", xText.substring(5, xPosn).trim());
			String fName=xText.substring(xPosn+1).trim();
			if(fName.contains(" "))
			{
				String mName=fName.substring(fName.indexOf(" "), fName.length());
				jItem.put("yepr_participant_first_name",fName.substring(0,fName.indexOf(" ")));
				jItem.put("yepr_participant_middle_name",mName);
			}
			else
			{
				jItem.put("yepr_participant_middle_name", "");
				jItem.put("yepr_participant_first_name",fName);
			}
			
			//jItem.put("yepr_participant_first_name",xText.substring(xPosn+1).trim());
			sNode = xNode.selectSingleNode("following::text[1]");
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
			nNode = xNode.selectSingleNode("following::text[1]");
			xText = xText + " " + nNode.getStringValue().trim();
			xPosn = xText.indexOf(",");
			if (xPosn > -1) {
			//	jItem.put("yepr_participant_last_name", xText.substring(0, xPosn).trim());
				String fName=xText.substring(xPosn+1).trim();
				if(fName.contains(" "))
				{
					String mName=fName.substring(fName.indexOf(" "), fName.length());
				//	jItem.put("yepr_participant_first_name",fName.substring(0,fName.indexOf(" ")));
					//jItem.put("yepr_participant_middle_name",mName);
				}
				else
				{
					//jItem.put("yepr_participant_first_name",fName);
					//jItem.put("yepr_participant_middle_name", "");
				}
				//jItem.put("yepr_participant_first_name",xText.substring(xPosn+1).trim());
				sNode = nNode.selectSingleNode("following::text[1]");
				if (sNode.valueOf("@top").equalsIgnoreCase(nNode.valueOf("@top"))) {
					if (sNode.getStringValue().equalsIgnoreCase("Gross:") ) { 
					//	jItem.put("yepr_participant_middle_name","");
					} else {
						//jItem.put("yepr_participant_middle_name",sNode.getStringValue().trim());
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
		jItem=dateFormat(jItem);
		jItem=addZero(jItem);
		aList.put(jItem);
	}
	xOutp.put("Participant", aList);
	
	
	xList = dcXML.getRootElement().selectNodes("//text[contains(text(),'** NET PAY')]");
	xRows = xConf.getJSONArray("PlanYears");
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

	System.out.print("name="+names+"\t");
System.out.println("output="+xOutp.toString());
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
	//String s=xNode.getStringValue();

	
	sList = getNodeList(xNode);
	System.out.println("Rows len="+xRows.length());
	for (int j=0; j<xRows.length();j++) {
		xCols = xRows.getJSONObject(j);
		float temp=0;
		int num=0;
	//	System.out.println("size="+sList.size());
		for (int i=0; i<sList.size();i++) {
			sNode = sList.get(i);
			System.out.println("SNode="+sNode.getStringValue());
		int start=0;
			String val=sNode.getStringValue();
		System.out.println("sNode="+sNode.getStringValue());
			Node nNode=null;
			if(val.contains("*** - Balance too small - statement not printed - ***"))
					{
				       break;
					}
		
			if(val.contains("Location:"))
			{
		nNode = sNode.selectSingleNode("preceding::text[1]");
		System.out.println("SSN="+nNode.getStringValue());
		jItem.put("pas_part_ee_ssn",nNode.getStringValue());
		//	break;
			}

			if(val.contains("Birth Date:"))
			{
				String birth[]=val.trim().split("\\s+");
				
		//nNode = sNode.selectSingleNode("preceding::text[1]");
		if(birth.length==3)
		{
				System.out.println("birth="+birth[2]);
		jItem.put("pas_part_ee_dob",birth[2]);
		//	break;
			}
			}
			if(val.contains("EMPLOYEE 401(K)"))
					{
				nNode = sNode.selectSingleNode("following::text[3]");
				System.out.println("401k="+nNode.getStringValue());
				jItem.put("pas_part_ee_401k_contrib",nNode.getStringValue());
				//	break;
					}
		
			if(val.contains("EMPLOYEE ROLLOVER"))
			{
		nNode = sNode.selectSingleNode("following::text[3]");
		//System.out.println("401k="+nNode.getStringValue());
		jItem.put("pas_part_rollover_contrib",nNode.getStringValue());
		//	break;
			}
			if(val.contains("ER MATCH - SAFE HARBOR"))
			{
		nNode = sNode.selectSingleNode("following::text[3]");
		//System.out.println("401k="+nNode.getStringValue());
		jItem.put("pas_part_er_match",nNode.getStringValue());
		//	break;
			}
			
			if(val.contains("EMPLOYEE ROTH 401(K) DEFERRAL"))
			{
		nNode = sNode.selectSingleNode("following::text[3]");
		//System.out.println("401k="+nNode.getStringValue());
		jItem.put("pas_part_ee_roth_contrib",nNode.getStringValue());
		//	break;
			}
			
			
			if(val.contains("TOTAL"))
			{
			
				
				
				
			//	if(val.contains("ER MATCH - SAFE HARBOR"))
				//{
		nNode = sNode.selectSingleNode("following::text[1]");
		jItem.put("pas_part_acct_beg_bal",nNode.getStringValue());
		nNode = sNode.selectSingleNode("following::text[3]");
		jItem.put("pas_part_distribution_payments",nNode.getStringValue());
	
		nNode = sNode.selectSingleNode("following::text[4]");
		jItem.put("pas_part_adjustments",nNode.getStringValue());
	
		nNode = sNode.selectSingleNode("following::text[7]");
		jItem.put("pas_part_loan_withdrawals",nNode.getStringValue());
	
		nNode = sNode.selectSingleNode("following::text[8]");
		jItem.put("pas_part_forfeiture",nNode.getStringValue());
	
		nNode = sNode.selectSingleNode("following::text[9]");
		jItem.put("pas_part_acct_gain_loss",nNode.getStringValue());
	
		nNode = sNode.selectSingleNode("following::text[10]");
		jItem.put("pas_part_acct_end_bal",nNode.getStringValue());
	
			}

String hi=xNode.getStringValue();
String bye[]=hi.trim().split("\\s+");
if(bye.length==3)
{
	jItem.put("pas_part_ee_middle_name",bye[1]);
	jItem.put("pas_part_ee_last_name",bye[2]);
	jItem.put("pas_part_ee_first_name",bye[0]);

}
if(bye.length==2)
{
	jItem.put("pas_part_ee_middle_name","");
	jItem.put("pas_part_ee_last_name",bye[1]);
	jItem.put("pas_part_ee_first_name",bye[0]);

}
if((bye.length!=2)&&(bye.length!=3))
		{
	jItem.put("pas_part_ee_middle_name","");
	jItem.put("pas_part_ee_last_name","");
	jItem.put("pas_part_ee_first_name",hi);

		}

			if(val.contains("Activity by Fund"))
			{
			break;
			}
			
		//	break;
			
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
	sList = xNode.selectNodes("preceding::text[@top='"+ sLine +"'] | following::*");
	sList = xNode.selectNodes("preceding::text[@top='"+ pLine +"']");
	//Node tNode= xNode.selectNodes("following::text[@left='22' and @font='6']").get(0);
	
	//System.out.println(""+xNode.asXML()+' '+sList.size());
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
			if (sNode.valueOf("@font").equalsIgnoreCase("3")) {
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
//		sList = xNode.selectNodes("../following::page[1]/text");
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
		Node no=nList.get(n);
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


public static JSONObject dateFormat(JSONObject jobj) throws NumberFormatException, JSONException, ParseException
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
	return jobj;
}





}


