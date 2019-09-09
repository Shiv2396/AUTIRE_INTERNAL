// In this class the methods are defined which will used by other java Classes

package com.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONException;
import org.json.JSONObject;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

public class UTL {
	 public static void sortXML(String path) {
	     try {
	       File stylesheet = new File("pdf2xml.xsl");
	       File xmlfile = new File(path+"_INP.xml");
	       StreamSource stylesource = new StreamSource(stylesheet);
	       StreamSource xmlsource = new StreamSource(xmlfile);
	       Transformer transformer = TransformerFactory.newInstance()
	                                 .newTransformer(stylesource);
	       transformer.transform(xmlsource, new StreamResult(new File(path+"_INPSorted.xml")));

	     } catch (TransformerException e) {
	              e.printStackTrace();
	     }
	   }
	 public static void setPDFVersion(String path, String path1) throws FileNotFoundException, IOException {
		 WriterProperties wp = new WriterProperties();
		 wp.setPdfVersion(PdfVersion.PDF_1_5);
		 PdfDocument pdfDoc = new PdfDocument(new PdfReader(path1), new PdfWriter(path+"_Low_V.pdf", wp));
		 pdfDoc.close();
	 }
	 public static void extractXML(String path) throws IOException, InterruptedException {
		 try{
			 String[] cmd = new String[]{"pdftohtml","-xml",path+"_Low_V.pdf",path+"_INP"};
			 Runtime   rt = Runtime.getRuntime();
			 Process proc=rt.exec(cmd);
			 int exitVal=proc.waitFor();	
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
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
	 				DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 				if(keyVal!="")
	 				{
	 					Date date = format.parse(keyVal);
	 					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
	 					String strDate= formatter.format(date); 
	 					//Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(keyVal);
	 					//System.out.println("date "+strDate);	 			
	 					jobj.put(keys[i], strDate);
	 				}
	 			}
	 		}
	 	}
	 	catch(Exception e)
	 	{
	 		System.out.println(e);
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
	 			if(keys[i].contains("dob") || keys[i].contains("name") || keys[i].contains("ssn") || keys[i].contains("date") || keys[i].contains("dot") || keys[i].contains("doe") || keys[i].contains("doh") || keys[i].contains("hours_worked"))
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
	 
	 public static JSONObject splitNameBySpace(JSONObject jobj) throws JSONException
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
	 				jobj.put("pas_part_ee_first_name", temp[0].trim());	 				
	 				jobj.put("pas_part_ee_last_name", temp[1].trim());
	 			}
	 			if(temp.length==3)
	 			{ 				
	 				jobj.put("pas_part_ee_first_name", temp[0].trim());	 				
 					jobj.put("pas_part_ee_middle_name", temp[1].trim());
 					jobj.put("pas_part_ee_last_name", temp[2].trim());
	 			}
	 		}
	 	}
	 	return jobj;	 	
	 }
	 
	 public static void readFile(String path)
	 {
		 try
		 {			 	 
			System.out.println(path);
			String path1=path+".pdf";
			setPDFVersion(path,path1);
			System.out.println("PDF version Lower done...!!  ");
			extractXML(path);
			System.out.println("PDF to XML Conversion done..!! ");			
			Thread.sleep(1000);
			System.out.println("in sleep");
			sortXML(path);			
			Thread.sleep(500);
			System.out.println("in sleep");			
			System.out.println("XML sorting done..!! ");
			
			File 	  		xFile  = new File(path+"_INPSorted.xml");
			File 	  		cFile  = new File(path+"_Config.json");
			System.out.println("Config done "+cFile.getName()+"+ path= "+cFile.getPath());
		 }
		 catch (Exception e) {
			e.printStackTrace();
		}
	 }	 
}
