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
public class PPPR_Duvel_7730
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
	File 	  		xFile  = new File("1550336007730_January 5th 2017 All Companies IMPORT_Sorted.xml");//sortedxml
	File 	  		cFile  = new File("1550336007730_January 5th 2017 All Companies IMPORT_NewConfig.json");
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
	xList = dcXML.getRootElement().selectNodes("//text[@left='27'and @height='11' and @font='3']");
System.out.println("xList="+xList.size());
	for (int i=0;i<xList.size();i++) {
		xNode =  xList.get(i);
		String str=xNode.getStringValue();
		Pattern p=Pattern.compile("[0-9]");
		Matcher m=p.matcher(str);
		if((!str.contains(","))||(m.find()==true))
		{
			continue;
		}
		System.out.println("xNode="+xNode.getStringValue());

		jItem=Employee(xNode);
	jItem =  getEmpData(xNode);
	
	//	System.out.println("name="+xNode.getStringValue()+"jItem="+jItem+"length="+jItem.length());
		xText = xNode.getStringValue().trim();
		xPosn = xText.indexOf(",");
	
		if (xPosn > -1) {
			jItem.put("pp_pr_participant_last_name", xText.substring(0, xPosn).trim());
			String fName=xText.substring(xPosn+1).trim();
			if(fName.contains(" "))
			{
				String mName=fName.substring(fName.indexOf(" "), fName.length());
				jItem.put("pp_pr_participant_first_name",fName.substring(0,fName.indexOf(" ")));
				jItem.put("pp_pr_participant_middle_name",mName);
			}
			else
			{
				jItem.put("pp_pr_participant_middle_name", "");
				jItem.put("pp_pr_participant_first_name",fName);
			}
			sNode = xNode.selectSingleNode("following::text[1]");
			if (sNode.valueOf("@top").equalsIgnoreCase(xNode.valueOf("@top"))) {
				if (sNode.getStringValue().equalsIgnoreCase("Gross:") ) { 
									} 
				else {
				}
			} 
			else {
			
			     }
			
		} 
		else {
			nNode = xNode.selectSingleNode("following::text[1]");
			xText = xText + " " + nNode.getStringValue().trim();
			xPosn = xText.indexOf(",");
			if (xPosn > -1) {
				jItem.put("pp_pr_participant_last_name", xText.substring(0, xPosn).trim());
				String fName=xText.substring(xPosn+1).trim();
				if(fName.contains(" "))
				{
					String mName=fName.substring(fName.indexOf(" "), fName.length());
					jItem.put("pp_pr_participant_first_name",fName.substring(0,fName.indexOf(" ")));
					jItem.put("pp_pr_participant_middle_name",mName);
				}
				else
				{
					jItem.put("pp_pr_participant_first_name",fName);
					jItem.put("pp_pr_participant_middle_name", "");
				}
				//jItem.put("yepr_participant_first_name",xText.substring(xPosn+1).trim());
				sNode = nNode.selectSingleNode("following::text[1]");
				if (sNode.valueOf("@top").equalsIgnoreCase(nNode.valueOf("@top"))) {
					if (sNode.getStringValue().equalsIgnoreCase("Gross:") ) { 
					} else {
						jItem.put("pp_pr_participant_middle_name",sNode.getStringValue().trim());
					}
				} else {
				}
			}
		}
		if(!xNode.getStringValue().contains(","))
		{
			jItem.put("pp_pr_participant_middle_name", "");
			jItem.put("pp_pr_participant_first_name","");
			jItem.put("pp_pr_participant_last_name",xNode.getStringValue().trim());
		}
		
		jList.put(jItem);
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
	xList = dcXML.getRootElement().selectNodes("//text[contains(text(),'REGULAR')]");
	xRows = xConf.getJSONArray("Summary");
	for (int i=0;i<xList.size();i++) {
		xNode = xList.get(i);
		jItem =  getSumData(xNode);
		for (int j=0;j<xRows.length();j++) {
			xCols = xRows.getJSONObject(j);
		}
		JSONArray jarr=new JSONArray();
		jarr.put(jItem);
		xOutp.put("PlanYear",jarr);
	}
	System.out.println("output="+xOutp.toString());
}
private static JSONObject Employee(Node xNode) throws JSONException {
	//System.out.println("Name="+xNode.getStringValue());
	
	String num1="";
	String num2="";
	float ans=0;
	String itos="";
	float add=0;
//	System.out.println("name="+xNode.getStringValue());
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
		float tempes=0;
		int count=0;
		int CR=0;
		float GT=0;
		float comp=0,car=0;
		float hs=0;
		int tp=0;
		//System.out.println("name="+xNode.getStringValue());
	//	System.out.println("Size of sList="+sList.size());
		for (int i=0; i<sList.size();i++) {
			float output=0;
			
			sNode = sList.get(i);
			String hawa="";
			String value=sNode.getStringValue();
			
			float fun=0;
float tempar=0;
			jText = "";
			//System.out.println("Value="+value);
		
	/*		if(value.contains("Bonus"))
			{
				String hh=value;
				String bonus[]=hh.trim().split("\\s+");
				jItem.put("pp_pr_participant_comp_bonuses",bonus[0].replace(",","").replace("(","-").replace(")",""));
			}          */
			
	/*		if(value.contains("*Car Reimb"))
			{
				String cel=value;
				String str[]=cel.trim().split("Car");
				String A=str[0].replace(",","").replace("(","-").replace(")","").replace("*","");
				car=Float.parseFloat(A);
			//	System.out.println("car= "+car); 
				//jItem.put("pp_pr_participant_comp_fringe_benefits",str[0]);
				CR=5;	
			}        */
			
			
	/*		if(value.contains("*Cell Reimb"))
		{
			String cel=value;
		//	System.out.println("cell= "+value);
			String str[]=cel.trim().split("Cell");
			String A=str[0].replace(",","").replace("(","-").replace(")","").replace("*","");
			comp=Float.parseFloat(A);
			//System.out.println(A);
			jItem.put("pp_pr_participant_comp_fringe_benefits",A);
			CR=1;	
		}               */
			
			if(value.contains("SOC"))    //||(value.contains("MED"))&&(tp!=1)))
			{
				
	       Node gross= sNode.selectSingleNode("preceding-sibling::text[1]");
	    //   System.out.println("gross="+gross.getStringValue());
				 num1=gross.getStringValue();
				// System.out.println("num1= "+num1);
				num1=num1.replace(",","").replace("(","-").replace(")","");
				jItem.put("pp_pr_participant_comp_gross_wages",num1);
			//	tp=1;
			}
			

			if((value.contains("401K")&&(!value.contains("Loan"))))
			{
				String defamt = value;
			//	System.out.println("defamt= "+value);
				String damt[] = defamt.trim().split(" ");
			//	System.out.println("damt= "+damt[0]);
				//Node dist= sNode.selectSingleNode("preceding-sibling::text[1]");
				//System.out.println("dist="+dist.getStringValue());
				  jItem.put("pp_pr_participant_401k_deferral_amount",damt[0]);
				
			}
			if(value.contains("401KLoan"))
			{
				String loan = value;
				System.out.println("loan= "+value);
				//Node loan= sNode.selectSingleNode("preceding-sibling::text[1]");
			//	System.out.println("dist="+loan.getStringValue());
			jItem.put("pp_pr_participant_401k_loan_repayments",loan);
				
				
			}
			
			if(value.contains("GTLN401K"))
		{
			String temp=value;
			String arr[]=temp.trim().split("GT");
			//System.out.println("fringe="+arr[0]);
			String GTL=arr[0];
			GTL=GTL.replace(",","");
			//System.out.println("GTL="+GTL);
			GT=Float.parseFloat(GTL);
			
			  if(CR==1)
			  {  
				  GT=GT+comp;
				  
			  }
			  if(CR==5)
			  {
				  GT=GT+car;
			  }
			  if(CR==2)
			  {
				  GT=GT+hs;
			  }
			  CR=0;
		//	jItem.put("pp_pr_participant_comp_fringe_benefits",arr[0]);
			
//	break;		
		}
		
		
		if(value.contains("‡"))
		{
			System.out.println("name val="+xNode.getStringValue());
			System.out.println("inside +++++++++++++++");
			Node gross= sNode.selectSingleNode("preceding-sibling::text[1]");
			   num2=gross.getStringValue();
			   num2=num2.replace(",","").replace("(","-").replace(")","");
			 //  System.out.println("num1="+num1);
			   float val1=0;
			   float val2=0;
			   if(num1.length()!=0)
			  {
			   
				   val1=Float.parseFloat(num1);
			  }
			   if(num2.length()!=0)
			   {
			    val2=Float.parseFloat(num2);
			   }
			   ans=val1+val2;
			    itos=Float.toString(ans);
			    hawa=itos;
			System.out.println("itos="+itos);
		//	jItem.put("pp_pr_participant_comp_gross_wages",hawa);
			
			count=1;
			
		//break;
		}
/*		if(tp==0)
		{
			
			
			
		}
	*/	
		if(value.contains("Hsng Allow"))
		{
			String B=value;
           String hsn[]=B.trim().split("Hsng");
           String aaa=hsn[0];
           aaa=aaa.replace(",","");
           hs=Float.parseFloat(aaa);
			if(CR==0)
			{
				hs=hs+GT;
				
			}
			if(CR==1)
			{
				hs=hs+comp;
			}
			if(CR==5)
			{
				hs=hs+car;
			}
           CR=2;
			
		}
		//break;
		if(value.contains("Match"))
		{
			String ss=value;
		//	String hi[]=ss.trim().split("\\s+");
		
			/*if(hi.length==2)
			{
			jItem.put("pp_pr_participant_401k_er_match_amount",hi[0].replace(",","").replace("(","-").replace(")",""));
			}
			if(hi.length==1)
			{
				Node Match= sNode.selectSingleNode("preceding-sibling::text[1]");
				jItem.put("pp_pr_participant_401k_er_match_amount",Match.getStringValue().trim().replace(",","").replace("(","-").replace(")",""));
						
			}          */
				
		}
		if(value.contains("Roth"))
		{
			
			String ss=value;
			String hi[]=ss.trim().split("\\s+");
			if(hi.length==2)
			{
			jItem.put("pp_pr_participant_roth_deferral_amount",hi[0].replace(",","").replace("(","-").replace(")",""));
			}
			if(hi.length==1)
			{
				Node Match= sNode.selectSingleNode("preceding-sibling::text[1]");
				jItem.put("pp_pr_participant_roth_deferral_amount",Match.getStringValue().trim().replace(",","").replace("(","-").replace(")",""));
						
			}
		
			
			
			
		}
		
		if(count==0)
		{
			//jItem.put("pp_pr_participant_comp_gross_wages",num1);
		}
		
		if(count==1)
		{
			//jItem.put("pp_pr_participant_comp_gross_wages",num1);
	//		jItem.put("pp_pr_participant_comp_gross_wages",hawa);
					
		}
		if(CR==0)
		{
			String dd=Float.toString(GT);
			
			jItem.put("pp_pr_participant_comp_fringe_benefits",dd);
		}
		if(CR==1)
		{
			String dd=Float.toString(comp);
			
			jItem.put("pp_pr_participant_comp_fringe_benefits",dd);
			
		}
		if(CR==2)
		{
String dd=Float.toString(hs);
			
			jItem.put("pp_pr_participant_comp_fringe_benefits",dd);
			
			
		}
		if(CR==5)
		{
String dd=Float.toString(car);
			
			jItem.put("pp_pr_participant_comp_fringe_benefits",dd);
			
			
		}
		
		
		
		String left=sNode.valueOf("@left");
		String font=sNode.valueOf("@font");
		String height=sNode.valueOf("@height");
		
		if(((left.equals("27"))&&(height.equals("11"))&&(font.equals("3"))))////||((left.equals("36"))&&(height.equals("14"))&&(font.equals("0"))))
				{
			System.out.println("inside loop");
			break;
		}
		
		
		// break;
		/*
		if(value.contains("FITWH"))
		{
			break;
		}
		*/
		
//break;
		
		
		
		
		//break;
		
		
		
	
		}

	}
	
	
	
	

	
	// TODO Auto-generated method stub
			return jItem;
}
public static JSONObject getEmpData(Node xNode) throws JSONException {
	float add=0;
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
		float tempes=0;
		
		for (int i=0; i<sList.size();i++) {
			float output=0;
			sNode = sList.get(i);
			String value=sNode.getStringValue();
			float fun=0;
float tempar=0;
int CR=0;
float GT=0;
float comp=0,car=0;
			jText = "";
			
			
			if(value.contains("*Cell Reimb"))
		{
			String cel=value;
		//	System.out.println("cell= "+value);
			String str[]=cel.trim().split("Cell");
			String A=str[0].replace(",","").replace("(","-").replace(")","").replace("*","");
			comp=Float.parseFloat(A);
			//System.out.println(A);
			jItem.put("pp_pr_participant_comp_fringe_benefits",A);
			CR=1;	
		}
		
			if(value.contains("XXX-X"))
			{
				String temp[]=value.trim().split("\\s+");
				//System.out.println("ssn="+temp[1]);
				jItem.put("pp_pr_participant_ssn",temp[1]);
			}
			if(value.contains("K2"))
			{
				if(value.length()>=160)
				{
					String str=value.substring(64,74).trim();
					if(str.length()!=0)
					{
					int num=Integer.parseInt(str);
					float values=0;
					values=(float)num/100;
					System.out.println("k2 vlue="+values);
					String fin=Float.toString(values);
					jItem.put("pp_pr_participant_401k_er_match_amount",fin);
					}
				}
				else
					{
					String str=value.substring(32,42).trim();
					if(str.length()!=0)
					{
					int num=Integer.parseInt(str);
					float values=0;
					values=(float)num/100;
					System.out.println();
					String fin=Float.toString(values);
					
				jItem.put("pp_pr_participant_401k_er_match_amount",fin);
					}
			}
			}
			if((value.contains("XM ROTH")))//||(value.contains("401K EE-")))
			{
				String str=value.trim().substring(32,42).trim();
				if(str.length()!=0)
				{
					//System.out.println("str="+str);
				int num=Integer.parseInt(str);
				float values=0;
				values=(float)num/100;
				String fin=Float.toString(values);
				System.out.println("roth="+fin);
				jItem.put("pp_pr_participant_roth_deferral_amount",fin);
				}
			}
							 if(value.contains("EMPLOYEE TOTAL")||(value.contains("TOTAL")))
			{
				if((value.contains("DEPARTMENT TOTALS"))||(value.contains("COMPANY TOTALS")))
				{
					continue;
				}
				Node n=sNode.selectSingleNode("following::text[1]");
				//System.out.println("n="+n.asXML());
				String temp=n.getStringValue();
				if(temp.contains("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------"))
{
				/*	String vals=value.substring(64,74).trim();
					int num=Integer.valueOf(vals);
					float values=0;
					values=(float)num/100;
					String fin=Float.toString(values);
						
					jItem.put("pp_pr_participant_comp_gross_wages",fin);
	*/
	break;
}
/*else
{
			String vals[]=temp.trim().split("\\s+");
				if(vals[0].contains("PAYCHEX"))
				{
					break;
				}
				String str1=temp.substring(64,74).trim();
				if(str1.length()!=0)
				{
				int num=Integer.parseInt(str1);
				float values=0;
				values=(float)num/100;
				String fin=Float.toString(values);
				jItem.put("pp_pr_participant_comp_gross_wages",fin);
				}	
			break;
			}    */
			}
			if(value.contains("K3"))
			{
			if(value.length()>=160)
			{
				String str=value.substring(64,74).trim();
				if(str.length()!=0)
				{
				int num=Integer.parseInt(str);
				float values=0;
				values=(float)num/100;
		String fin=Float.toString(values);
							jItem.put("pp_pr_participant_401k_deferral_amount",fin);
			}
			}
			else
			{
			String	str=value.substring(32,42).trim();
				
				if(str.length()!=0)
				{
				int num=Integer.parseInt(str);
				float values=0;
				values=(float)num/100;
				String fin=Float.toString(values);
							jItem.put("pp_pr_participant_401k_deferral_amount",fin);
				}				
			}
			}
			  float num=0;
				float demo=0;
				String vvv="";
			if((value.contains("MED125"))||(value.contains("DISABILI")))//||(val.contains("MED125-PA")))
			{	String sas="";
			//  int add1=0;
				if(value.length()>=160)
			 {
				  sas=value.substring(64,74).trim();
				if(sas.length()!=0)
				{
					 num=0;
				   num=Float.valueOf(sas);
				   demo=num;
				}
				 tempes=tempes+demo;
				}
			 else
			 {
				 sas=value.substring(32,42).trim();
				if(sas.length()!=0)
				{
					num=0;
				 num=Float.valueOf(sas);
				 demo=num;
				}
				 tempes=tempes+demo;
			 }
				 String fin=Float.toString(tempes);
				vvv=fin;
			float s=Float.valueOf(vvv);
			s=s/100;
			String man=Float.toString(s);
			jItem.put("pp_pr_participant_comp_sec_125_def",man);
			}
					jText = "";
			if (sNode.getStringValue().equalsIgnoreCase(xCols.getString("Path"))) 
			{
				jAttr = xCols.getString("Name");
				jPosx = xCols.getString("Posx");
				jType = xCols.getString("Type");
				if(value.contains("XXX-X"))
			{
				jItem.put("pp_pr_participant_ssn",value);
			}
				if ( xCols.get("Posn").equals("F")) {
					jText = sNode.selectSingleNode("following::text["+jPosx+"]").getStringValue();
					
				} else {
					jText = sNode.selectSingleNode("preceding::text["+jPosx+"]").getStringValue();
				}
				if (jType.equals("Number")) {
					jText = jText.replaceAll("\\s+","");
					if ( jText.length() > 1 ) {
						if (jText.indexOf(".") == -1 ) {
							jText = jText.substring(0, jText.length()-2) + "." + jText.substring(jText.length()-2);
						}
					}
				}
				jItem.put(jAttr, jText);
				break;
			}
		}
	}
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

//	sList = xNode.selectNodes("preceding-sibling::text[@top='"+ sLine +"'] | following-sibling::*");
	sList = xNode.selectNodes("preceding::text[@top='"+ pLine +"']");
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
String temps=sNode.valueOf("@top");
int t=Integer.parseInt(temps);

		if (sNode.valueOf("@left").equalsIgnoreCase("27")) {
			if (sNode.valueOf("@height").equalsIgnoreCase("12")) {
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


