//date conversion......number converted into date 
//code for Excel to Json (Excel corrected by Auditors)

package com.pattern.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class ExcelToJsonForAuditorExcel {
		
	private static final int CELL_TYPE_NUMERIC = 0;

	public static void main(String args[])  {

		int  		rows = 0;
		int  		cols = 0;
		Row  		hrow = null;
		Row  		crow = null;
		Cell 		rcol = null;
		Cell 		hcol = null;
		String		prop = "";
		String		text = "";
		JSONArray	list = new JSONArray();
		JSONObject 	item = new JSONObject();
		JSONObject finalObj = new JSONObject();
		
		try 
		{
			FileInputStream xls = new FileInputStream(new File("Loan Summary_Finaloutput (1).xlsx"));
			XSSFWorkbook 	wbk = new XSSFWorkbook (xls);
			XSSFSheet    	wsh = wbk.getSheetAt(0);
			rows = wsh.getLastRowNum();
			System.out.println("row"+rows);
			hrow = wsh.getRow(0);
			cols = hrow.getLastCellNum();
			for (int i = 1; i <= rows ;i++) {
				crow  = wsh.getRow(i);
				if(crow==null)
				{
					continue;
				}
				if (crow.getZeroHeight() == false) {
					item = new JSONObject();
					for (int j = 0;j < cols;j++) {
						rcol = crow.getCell(j);
						hcol = hrow.getCell(j);
						if (rcol == null) {
							rcol = crow.createCell(j);
						}
						rcol.setCellType(Cell.CELL_TYPE_STRING);
						prop = hcol.getStringCellValue();
						
						switch (rcol.getCellType()) {
						case CELL_TYPE_NUMERIC : 	item.put(prop, rcol.getNumericCellValue());
										System.out.println("nn"+rcol.getNumericCellValue());
										break;
										
						case Cell.CELL_TYPE_STRING : 	
										text = rcol.getStringCellValue();																			
										item.put(prop, rcol.getStringCellValue().trim());										
										break;
										
						default		 : 	item.put(prop, rcol.getDateCellValue().toString());
										System.out.println("dd");
						break;
						
						}						
					}
					item=modifyObject(item);
					list.put(item);
				}				
			}
			finalObj.put("Participant", list);
			System.out.println(finalObj);
			xls.close();
			
		} catch (Exception e) {
			 e.printStackTrace();			
		}		
	}	
	
	public static JSONObject modifyObject(JSONObject jobj)
	{
		String keys[]=jobj.getNames(jobj);
		for(int i=0;i<keys.length;i++)
		{
			if(keys[i].contains("date") || keys[i].contains("dob") || keys[i].contains("dot") || keys[i].contains("doh") || keys[i].contains("doe"))
			{
				if(jobj.getString(keys[i]).contains("/"))
				{
					continue;
				}
				if(jobj.getString(keys[i]).equalsIgnoreCase("") )
 			{
 				continue;
 			}
				if(jobj.getString(keys[i]).contains("do") || jobj.getString(keys[i]).contains("date") )
 			{
 				continue;
 			}
				String dateNumber=jobj.getString(keys[i]);
				
				System.out.println("dateNumber  "+dateNumber);
				int javadate=Integer.parseInt(dateNumber);
 			Date javaDate= DateUtil.getJavaDate((double)javadate);
 			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
 			String date=formatter.format(javaDate);
 			System.out.println(keys[i]+"   "+date);
 			jobj.put(keys[i], date);
 			//oval=oval.replace("/", "");
 			//	outp.put(name, oval);				
			}
		}		
		return jobj;
	}
}