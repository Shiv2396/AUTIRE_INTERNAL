
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

	


	public class PPPR_Hardway_1674  {
		
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
		
	/*	String path=args[0];
	//System.out.println(path);
		String path1=path+".pdf";
//		System.out.println(path1);
			
		setPDFVersion(path,path1);
		System.out.println("PDF version Lower done...!!  ");
		extractXML(path);
		System.out.println("PDF to XML Conversion done..!! ");
		try
		{
			Thread.sleep(20000);
			//System.out.println("in sleep");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		transformXML(path);
		try
		{
			Thread.sleep(1000);
			System.out.println("in sleep");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("XML sorting done..!! ");
		File            xFile  = new File(path+"_INPSorted.xml");
		
		*/
		
		File 	  		xFile  = new File("1553172091674_Roanoke_Sorted.xml");
		File 	  		cFile  = new File("1553172091674_Roanoke_Config.json");
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
		xList = dcXML.getRootElement().selectNodes("//text[@left='27' and @height='12' and @font='1']");
	       System.out.println("xList="+xList.size());
	       
	       for (int i=0;i<xList.size();i++)
	       {
			xNode =  xList.get(i);
			jItem =  getEmpData(xNode);
			String names=xNode.getStringValue();
		
			
			//String names=xNode.getStringValue();
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
	/*
	System.out.println("department="+dep);
	String nn[]=names.trim().split("\\s+");
	if(((names.contains("TOTALS"))||(nn.length==1))||(names.contains("MANAGER")))
	{
		continue;
		//continue;
		}
		*/
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
		
	                 }
	                else
		             {
			          jItem.put("pp_pr_participant_first_name",Firstname[0]);
			          jItem.put("pp_pr_participant_middle_name",Firstname[1]);
			
		            }
	              }   

	           jList.put(jItem);
		}
	       }
	       
	      // System.out.println("jlist size "+jList.length());
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
		
		
		xList = dcXML.getRootElement().selectNodes("//text[contains(text(),'NET PAY')]");
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
			xOutp.put("PayPeriod",jarr);
		}

		
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

		sList = getNodeList(xNode);
		//System.out.println("slist size "+sList.size());
		//System.out.println("xNode "+xNode.asXML());
		for (int j=0; j<xRows.length();j++) {
			xCols = xRows.getJSONObject(j);
			float temp=0;
			//System.out.println("xCols "+xCols);
			int num=0;
			for (int i=0; i<sList.size();i++) {
				sNode = sList.get(i);
			
			int start=0;
				String val=sNode.getStringValue();
			//System.out.println("val="+val);
				
			
			
					jItem.put("pp_pr_period_ending_date","05/15/2017");
				
				
				if(val.contains("EMPLOYEE TOTAL"))
				{
					//System.out.println("gross: "+val.substring(84,92).trim());
					String gross =val.substring(63,71).trim();
					if(gross.length()>=1)
					{
						Float fgross =Float.parseFloat(gross);
						fgross = fgross/100;
						String finalgross = fgross.toString();
						//System.out.println("Final gross: "+finalgross);
						
					jItem.put("pp_pr_participant_comp_gross_wages",finalgross);
					}
				}
				
				
						
			
				String samp=Float.toString(temp);
				
				//jItem.put("yepr_participant_comp_sec_125_def",samp);
				//System.out.println("sNode "+sNode.asXML());
				jText = "";
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
	private static Float parseFloat(String gross) {
		// TODO Auto-generated method stub
		return null;
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

//		sList = xNode.selectNodes("preceding::text[@top='"+ sLine +"'] | following::*");
		sList = xNode.selectNodes("preceding::text[@top='"+ pLine +"']");
		//Node tNode= xNode.selectNodes("following::text[@left='22' and @font='6']").get(0);
		
//		System.out.println(""+xNode.asXML()+' '+sList.size());
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
				//System.out.println("sLine "+sLine +"xLine "+xLine);
				int s=sLine.intValue();
				int x=xLine.intValue();
				
				if (!sLine.equals(xLine) ||  (s-x!=1)) {
					//break;
				}
			}
			nList.add(sNode);
			
		}	

		
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







