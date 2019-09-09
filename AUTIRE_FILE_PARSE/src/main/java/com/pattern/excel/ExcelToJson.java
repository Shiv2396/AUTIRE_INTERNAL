package com.pattern.excel;

import java.io.File;
import java.io.FileInputStream;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class ExcelToJson {		
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
			FileInputStream xls = new FileInputStream(new File("Loan Summary Report_INP.xlsx"));
			XSSFWorkbook 	wbk = new XSSFWorkbook (xls);
			XSSFSheet    	wsh = wbk.getSheetAt(0);
			//System.out.println("wsh"+wsh);
			rows = wsh.getLastRowNum();
			System.out.println("row"+rows);
			hrow = wsh.getRow(0);
			System.out.println("hrow"+hrow.getLastCellNum());
			cols = hrow.getLastCellNum();
			System.out.println("col"+cols);
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
						System.out.println(" hcol  "+hcol.getStringCellValue());
						rcol.setCellType(Cell.CELL_TYPE_STRING);
						prop = hcol.getStringCellValue();
						
						switch (rcol.getCellType()) {
						case CELL_TYPE_NUMERIC : 	item.put(prop, rcol.getNumericCellValue());
										System.out.println("nn"+rcol.getNumericCellValue());
										break;
										
						case Cell.CELL_TYPE_STRING : 	
										text = rcol.getStringCellValue();
										item.put(prop, rcol.getStringCellValue().trim());
										System.out.println("ss"+rcol.getStringCellValue());
										break;
										
						default		 : 	item.put(prop, rcol.getDateCellValue().toString());
										System.out.println("dd");
						break;						
						}												
					}
					list.put(item);
				}				
			}
			System.out.println(list.toString());
			xls.close();			
		} catch (Exception e) {
			 e.printStackTrace();			
		}		
	}
}