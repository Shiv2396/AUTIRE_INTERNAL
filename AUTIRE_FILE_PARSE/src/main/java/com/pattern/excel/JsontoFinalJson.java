package com.pattern.excel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsontoFinalJson 
{
	static JSONObject			xConf  = new JSONObject();
	static JSONObject			xConf1  = new JSONObject();
	static JSONArray       		xRows  = new JSONArray();
	static JSONArray            arr=new JSONArray();
	static JSONObject xOutp=new JSONObject();
	public static void main(String[] args) throws IOException, JSONException 
	{
		JSONArray			jarr=new JSONArray();
		JSONArray list = new JSONArray();
		String name = "";
		String cval = "";
		String oval = "";
		JSONObject outp = new JSONObject();
		File cFile  = new File("Loan Summary Report_Config.json");
		String string = FileUtils.readFileToString(cFile,"UTF-8");
		JSONObject	cnfg = new JSONObject(string);
		//System.out.println("cnfg "+cnfg);
		JSONArray names = new JSONArray();
		File iFile  = new File("Loan Summary Report.json");
		String inpt = FileUtils.readFileToString(iFile,"UTF-8");
		list = new JSONArray(inpt);
		names = cnfg.names();
		for(int j=0;j<list.length();j++)
		{
			JSONObject obj=list.getJSONObject(j);
			outp = new JSONObject();
			//System.out.println("obj"+obj);			
			for(int i=0;i<names.length();i++)
			{			
				name = names.getString(i); 
				cval = cnfg.getString(name);
				if (obj.has(cval)) {
					//System.out.println("cval "+cval);
					oval = obj.getString(cval);
					if(cval.contains("Date") || cval.contains("DOB") || cval.contains("DOH") || cval.contains("Entry") || cval.contains("Hire") || cval.contains("Birth") || cval.contains("Term") || cval.contains("DATE"))
					{
						if(oval.equalsIgnoreCase("") )
						{
							outp.put(name, "");
							continue;
						}
						if( oval.contains("/"))
						{
							outp.put(name,oval);
						}
						else
						{
							String temp="";
							String ss=obj.getString(cval);
							if(ss.contains("."))
							{
								temp=ss.substring(0,ss.indexOf("."));
								//System.out.println("temp = "+temp);
							}
							else
							{
								temp=ss;
							}	    				
							int javadate=Integer.parseInt(temp);
							Date javaDate= DateUtil.getJavaDate((double)javadate);
							SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
							oval=formatter.format(javaDate);
							outp.put(name, oval);
						}
					}
					else 
					{	    			
						if(name.contains("name"))
						{
							//System.out.println("name dd "+name +" oval "+oval);
							//System.out.println("name "+oval);
							outp.put(name,oval);
						}
						else
						{
							outp.put(name,oval.replace(",", ""));
						}
					}	    		
				} else {
					outp.put(name, "");
				}	    	
			}
//			System.out.println("outp "+outp);
			//outp=splitName(outp);
			//outp=splitNameBySpace(outp);
			outp=addZero(outp);
			jarr.put(outp);
		}
		JSONObject finalObj=new JSONObject();
		finalObj.put("Participant", jarr);
		System.out.println(finalObj);	
	}
	
	public static JSONObject splitName2(JSONObject jobj)
	{
		String namearr[]=jobj.getNames(jobj);
		for(int i=0;i<namearr.length;i++)
		{
			String fullName=jobj.getString(namearr[i]);
			String temp[]=fullName.trim().split(" ");
			if(temp.length==2)
			{
				if(namearr[i].contains("first_name"))
				{
					String firstName=namearr[i];
					jobj.put(firstName, temp[0].trim());
				}
				if(namearr[i].contains("last_name"))
				{
					String lastName=namearr[i];
					jobj.put(lastName, temp[1].trim());
				}
				System.out.println("first name="+temp[1]);
				System.out.println("last name="+temp[0]);		
			}
		}
		return jobj;
	}
	public static JSONObject addZero(JSONObject jobj)
	{
		String keys[]=jobj.getNames(jobj);	
		for(int i=0;i<keys.length;i++)
		{
			String value=jobj.getString(keys[i]);
			if(value.equals(""))
			{
				if(keys[i].contains("dob") || keys[i].contains("name") || keys[i].contains("ssn") || keys[i].contains("date") || keys[i].contains("dot") || keys[i].contains("doe") || keys[i].contains("doh") || keys[i].contains("status"))
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
	
	
	
	public static JSONObject finalSplit(JSONObject jobj)
	{
		String namearr[]=jobj.getNames(jobj);
		for (int i = 0; i < namearr.length; i++) 
		{
			if(namearr[i].contains("name"))
			{				
				String fullName=jobj.getString(namearr[i]);
				System.out.println("hello ss "+fullName);
				if(fullName.equals(""))
				{
					continue;
				}
				else
				{
					String temp[]=fullName.trim().split(",");
					if(namearr[i].contains("last_name"))
					{
						String lastName=namearr[i];
						jobj.put(lastName, temp[0]);
					}
					if(namearr[i].contains("first_name"))
					{
						String lastName=namearr[i];				
						String fName=temp[1];
						if(fName.contains(" "))
							fName=fName.substring(0, fName.indexOf(" "));
						jobj.put(lastName, fName);
					}				
				}
			}
		}		
		return jobj;
	}
	
	
	public static JSONObject splitName(JSONObject jobj)
	{
		String namearr[]=jobj.getNames(jobj);
		for (int i = 0; i < namearr.length; i++) 
		{
			if(namearr[i].contains("name"))
			{
				String fullName=jobj.getString(namearr[i]);
				
				System.out.println("full name  "+fullName);
				String temp[]=fullName.trim().split(",");
				System.out.println("temp length "+temp.length);
				String samp=temp[1];
				String temp1[]=samp.trim().split("\\s+");
				System.out.println("last name"+temp[0]);
				if(namearr[i].contains("last_name"))
				{
					String lastName=namearr[i];
					jobj.put(lastName, temp[0]);
				}
				if(temp1.length==1)
				{
					if(namearr[i].contains("first_name"))
					{
						String firstName=namearr[i];
						jobj.put(firstName, temp1[0]);
					}
					if(namearr[i].contains("middle_name"))
					{
						String middleName=namearr[i];
						jobj.put(middleName, "");
					}
					
				System.out.println("first name"+temp1[0]);
				}
				if(temp1.length==2)
				{
					if(namearr[i].contains("first_name"))
					{
						String firstName=namearr[i];
						jobj.put(firstName, temp1[0]);
					}
					if(namearr[i].contains("middle_name"))
					{
						String firstName=namearr[i];
						jobj.put(firstName, temp1[1]);
					}
				System.out.println("first name"+temp1[0]);
				System.out.println("middle Name="+temp1[1]);
				}
				//System.out.println("full name "+fullName);
			}
		}
		
		//String name[]=fullName.split(",");
		//System.out.println(name[0]+"  "+name[1]);
		return jobj;
		
	}
	
	
	public static JSONObject splitNameBySpace(JSONObject jobj)
	{
	 	String namearr[]=jobj.getNames(jobj);
	 	for (int i = 0; i < namearr.length; i++) 
	 	{
	 		if(namearr[i].contains("last_name"))
	 		{
	 			String fullName=jobj.getString(namearr[i]).trim(); 			
	 			System.out.println("full name  "+fullName);
	 			String temp[]=fullName.trim().split(" ");
	 			System.out.println("temp length "+temp.length);
	 			if(temp.length==1)
	 				break;	
	 			if(temp.length==1)
	 			{
	 				if(namearr[i].contains("last_name"))
	 				{
	 					String lastName=namearr[i];
	 					jobj.put(lastName, temp[0]);
	 				}
	 			}
	 			if(temp.length==2)
	 			{
	 				jobj.put("distribsumm_part_last_name", temp[0].trim());	 				
	 				jobj.put("distribsumm_part_first_name", temp[1].trim());
	 			}
	 			if(temp.length==3)
	 			{ 				
	 				jobj.put("distribsumm_part_last_name", temp[0].trim());	 				
 					jobj.put("distribsumm_part_middle_name", temp[1].trim());
 					jobj.put("distribsumm_part_first_name", temp[2].trim());
	 			}
	 		}
	 	}
	 	return jobj;	 	
		}
}
