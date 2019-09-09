package com.pattern.other;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.util.UTL;

public class General 
{
	static JSONObject			xConf  = new JSONObject();
	static JSONArray       		xRows  = new JSONArray();
	public static void main(String[] args) 
	{	
		try
		{
		String path=args[0];
		UTL.readFile(path);
		File cFile=new File(path+"_Config.json");
		String string = FileUtils.readFileToString(cFile,"UTF-8");
		xConf = new JSONObject(string);
		xRows = xConf.getJSONArray("Items");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
