// remove sign from o/p

package com.pattern.other;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Form5500_TrialBalance 
{

	static JSONObject			xConf  = new JSONObject();
	static JSONObject			xConf1  = new JSONObject();
	static JSONArray       		xRows  = new JSONArray();
	static JSONArray            arr=new JSONArray();
	static JSONObject			finalObject = new JSONObject();
	static JSONObject xOutp=new JSONObject();
	static JSONObject			obj=new JSONObject();
	static  JSONArray jArray= new JSONArray();
	public static void main(String[] args) throws IOException, JSONException 
	{
		JSONArray list = new JSONArray();
		JSONObject item = new JSONObject();
		JSONObject obj=new JSONObject();
		 JSONObject outObj=new JSONObject();
		String name = "";
		String cval = "";
		String oval1 = "";
		int oval;
		JSONObject outp = new JSONObject();
		File cFile  = new File("configUpdated.json");
		String string = FileUtils.readFileToString(cFile,"UTF-8");
		JSONObject	cnfg = new JSONObject(string);
		//System.out.println("cnfg= "+cnfg);
		JSONArray names = new JSONArray();
		names=cnfg.getJSONArray("item");
		//System.out.println("names "+ names);
		
		
		
//		System.out.println(keys);
		File iFile  = new File("2018 Form 5500 and Schedules.json");
		String inpt = FileUtils.readFileToString(iFile,"UTF-8");
		
		//System.out.println("inpt "+inpt);
		//list = new JSONArray(inpt);
		
	    obj = new JSONObject(inpt);
	    
	   // System.out.println("obj "+obj.toString());
	   list=obj.getJSONArray("PlanYear");
	  // System.out.println("out obj  "+list);
	   item=list.getJSONObject(0);
	//    System.out.println(item);
	   // names = cnfg.names();
	   // System.out.println(names.length());
	    
	    for (int i=0;i<names.length();i++) 
	    {
	    	obj = names.getJSONObject(i); 
	    	//System.out.println("obj= "+obj);
	    	JSONArray jArray=new JSONArray();
	    	jArray=obj.names();
	    	//System.out.println("jArray "+jArray);
	    	String sign1=obj.getString("sign");
	    	//System.out.println("sign= "+ sign1);
    		int sign=Integer.parseInt(sign1);
	    	for(int j=0; j<jArray.length();j++)
	    	{
	    		name = jArray.getString(j); 		    	
	    		//System.out.println("name "+name);	    		
	    		cval = obj.getString(name);	   
	    	//	System.out.println("cval "+cval);
	    		//int sign1=Integer.parseInt(sign);
	    		if (item.has(cval)) {
	    			oval1 = item.getString(cval);
	    			//System.out.println("oval1= "+oval1);
	    			if(!oval1.equals(""))
	    			{
    				//System.out.println("in if");
    				oval1=oval1.replaceAll(",", "");
    				oval=Integer.parseInt(oval1);
    				oval=(oval * sign);
    				oval1=Integer.toString(oval);
    				outp.put(name,oval1);
    				
	    			}
	    			else
	    			{
	    				
	    				outp.put(name, "");
	    			}
	    		} else {
	    			//System.out.println("name "+name);
	    			if(name.equals("sign"))
	    			{
	    				
	    			}
	    			else
	    			outp.put(name, "");
	    		}
	    	}
	    }
	   // System.out.println(outp);
	  jArray.put(outp);
		finalObject.put("PlanYear", jArray);
		System.out.println(finalObject);
		
		
		//JSONObject jObj=xConf.getJSONObject("PlanYear");
		//System.out.println(jObj);
		 //Iterator<String> keys1=jObj.keys();
		
	
		

	}

}
