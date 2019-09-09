package com.pattern.column;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
//import org.apache.poi.ss.usermodel.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PS_Duvel_7064
{
	//C024_Participant_Valuation_Summary_Pork_Co.pdf
	static File					oFile = new File("1550332107064_Participant Account Summary IMPORT_Sorted.xml");
	static File 				cFile = new File("1550332107064_Participant Account Summary IMPORT_Config.json");	
	static JSONObject			jConf  = new JSONObject();
	static JSONArray			jList  = new JSONArray();
	static JSONObject			jItem  = new JSONObject();
	static List<Node>			nList  = null;
	static Node					nItem  = null;
	
	static String				sConf = "";

	
public static void main(String[] args) throws DocumentException, JSONException, IOException, NumberFormatException, ParseException {
	List<Node>		mList  = new ArrayList<Node>();
	List<Node>		nList  = new ArrayList<Node>();
	List<Node>		xList  = new ArrayList<Node>();
	List<Node>		sList  = new ArrayList<Node>();
	List<Node>		cList  = new ArrayList<Node>();

	Node 			mNode  = null;
	Node			nNode  = null;
	Node  			xNode  = null;
	Node			sNode  = null;	
	Node			cNode  = null;

	String Posn ;
	String Posx ;
	String Type ;
	String path;
	float begBal=0;
	
	sConf = FileUtils.readFileToString(cFile,"UTF-8");
	jConf = new JSONObject(sConf);
	JSONArray  conf = jConf.getJSONArray("Items");
	
		JSONObject node = new JSONObject();
		JSONObject item = new JSONObject();
		JSONArray  list = new JSONArray();
		JSONObject outp = new JSONObject();
		String	   ival = "";
		
		Document  		dcXML  = (new SAXReader()).read(oFile);
		mList = dcXML.getRootElement().selectNodes("//text[@left='47' and @font='3']");
	//	mList = dcXML.getRootElement().selectNodes("//text[contains(text(),'Participant Identifier')]");// and //text[contains(text(),',')]");
		System.out.println("mlist size "+mList.size());
		String prevName="";
		for (int i=0;i<mList.size();i++) 
		{
			mNode = mList.get(i);
			
			if(mNode.getStringValue().contains(","))
			{
				//String name=mNode.getStringValue();
				
				if(mNode.getStringValue().equalsIgnoreCase(prevName))
				{
					continue;
				}
				nList.add(mNode);
				prevName=mNode.getStringValue();
			}
			//System.out.println("mNode "+mNode.asXML());
			
			
			sList.add(mNode);
		}
		System.out.println("nlist size "+ nList.size());
		for(int j=0;j<nList.size();j++)
		{
			nNode=nList.get(j);
		//	System.out.println("name "+nNode.asXML());
			sList=new ArrayList<Node>();
			sList.add(nNode);
			xList=nNode.selectNodes("following::text");
			for(int k=0;k<xList.size();k++)
			{
				xNode=xList.get(k);
				if(j==nList.size()-1)
				{
					continue;
				}
				String nextName=nList.get(j+1).getStringValue();
				if(xNode.getStringValue().equalsIgnoreCase(nextName) && k!=xList.size())
				{
					break;
				}
				sList.add(xNode);
			}
			
			System.out.println("sList size "+sList.size());
			for(int l=0;l<sList.size();l++)
			{
				sNode=sList.get(l);
				//System.out.println("sNode "+sNode.asXML());
			}
					outp=new JSONObject();	
				for(int n=0; n<conf.length();n++)
				{
					item = conf.getJSONObject(n);
					path = item.getString("Path");
					Posn = item.getString("Posn");
					Posx = item.getString("Posx");		
					for(int l=0;l<sList.size();l++)
					{
						sNode=sList.get(l);
						if(sNode.getStringValue().contains(path))
						{
							//System.out.println("path "+path +"      sNode    "+sNode.getStringValue());
//							cList=sNode.selectNodes("following::text[@left='47']");
							//System.out.println("hellooo"+sNode.asXML());
							
							switch (path)
							{
								case "Birth Date:":
									ival=sNode.getStringValue();
									ival=ival.substring(ival.indexOf("Birth Date:")+12, 25);
									//System.out.println("iavl "+ival.trim());
									outp.put(item.getString("Name"), ival.trim());
									break;
									
								case "Participant Identifier":
								//	System.out.println("ssssss"+item.getString("Name"));
									ival=sNode.getStringValue();
									ival=ival.substring(ival.indexOf(":")+1, ival.length());
									//System.out.println("ival "+ival.trim());
									outp.put(item.getString("Name"), ival.trim());
									break;
									
								case ",":
									//System.out.println("left  "+sNode.valueOf("@left"));
									if(sNode.valueOf("@left").equals("47") && sNode.valueOf("@font").equals("3"))
									{
										ival=sNode.getStringValue();
										if(ival.contains("Participant Identifier"))
										{
											ival=ival.substring(0, ival.indexOf("Participant Identifier"));
										}
										//System.out.println("ival "+ival.trim());
										outp.put(item.getString("Name"), ival.trim());
									}									
									break;
									
								case "TOTALS":
									ival=sNode.getStringValue();
									//System.out.println("ival "+ival.trim());
									
									//cList=sNode.selectNodes("//text[@left='47' and @font='4'][text()[@left='47' and @font='3']]");
									//div[@class="blue"]/a[following-sibling::text()[contains(., "With")]]
									
									cList=new ArrayList<Node>();
									for(int k=l+1;k<sList.size();k++)
									{
										//System.out.println("Heoooll");
										sNode=sList.get(k);
										String left= sNode.valueOf("@left");
										String font= sNode.valueOf("@font");
										
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") && sNode.getStringValue().contains(",") && !sNode.getStringValue().equalsIgnoreCase(nNode.getStringValue()))
										{
											//System.out.println("aaaaa sNode "+sNode.asXML());
											//System.out.println("aaaaa nNode "+nNode.asXML());
											break;
										}
										if((left.equalsIgnoreCase("47") || left.equalsIgnoreCase("46") )&& font.equalsIgnoreCase("4"))
										{
											Node node1=null;
											//System.out.println("gggg "+sNode.asXML());
									//		List<Node> node1=new ArrayList<Node>();
											if(item.getString("Name").equals("pas_part_acct_beg_bal"))
											{
											 node1=sNode.selectSingleNode("following::text[1]");
											}
											if(item.getString("Name").equals("pas_part_acct_gain_loss"))
											{
												float twoVal;
												 node1=sNode.selectSingleNode("following::text[5]");
												 float oneVal=Float.parseFloat(node1.getStringValue().replace(",", ""));
												node1=sNode.selectSingleNode("following::text[6]");	
												if(node1.getStringValue().contains("("))
												{
													String changeInval=node1.getStringValue();
													changeInval=changeInval.replace("(", "").replace(")", "").replace(",", "");
													 twoVal=Float.parseFloat(changeInval);
													twoVal=(twoVal*-1);
													
												}
												else
												{
												 twoVal=Float.parseFloat(node1.getStringValue().replace(",", ""));
												}
												Float addition=oneVal+twoVal;
												System.out.println("oneval  "+oneVal+ "   twoval   "+twoVal);
												System.out.println("addition "+addition.toString());
												String ad=addition.toString();
												node1.setText(ad);
												//node1=addition;
											// node1=sNode.selectSingleNode("following::text[5]");
											}
											if(item.getString("Name").equals("pas_part_adjustments"))
											{
											 node1=sNode.selectSingleNode("following::text[8]");
											}
											if(item.getString("Name").equals("pas_part_distribution_payments"))
											{
											 node1=sNode.selectSingleNode("following::text[9]");
											}
											if(item.getString("Name").equals("pas_part_acct_end_bal"))
											{
											 node1=sNode.selectSingleNode("following::text[10]");
											}
											//System.out.println("node1 "+node1.getStringValue());
											if(node1!=null)
											{
												float Bal;
												String b=node1.getStringValue();
												if(b.contains("("))
												{
													 Bal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
													Bal=Bal*-1;
												}
												else
												{
													 Bal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
												}
											
											
											begBal=begBal+Bal;
											}
											cList.add(sNode);
										}
										
									}
									
									
									if(item.getString("Name").equals("pas_part_acct_beg_bal"))
									{
										
										ival=Float.toString(begBal);
									}
									if(item.getString("Name").equals("pas_part_acct_gain_loss"))
									{
										
										ival=Float.toString(begBal);
									}
									if(item.getString("Name").equals("pas_part_adjustments"))
									{
										
										ival=Float.toString(begBal);
									}
									if(item.getString("Name").equals("pas_part_acct_end_bal"))
									{
										
										ival=Float.toString(begBal);
									}
									if(item.getString("Name").equals("pas_part_distribution_payments"))
									{
										
										ival=Float.toString(begBal);
									}
								//	System.out.println("beg bal "+begBal);
									begBal=0;
									//cList.add(sNode);
								//	System.out.println("cList size "+cList.size());
									for(int c=0;c<cList.size();c++)
									{
										cNode=cList.get(c);
										//if(cNode.getStringValue().)
										//System.out.println("cNode ssss"+cNode.asXML());
									}
									
									
									break;
									
									
								case "EMPLOYEE BEFORE TAX 01":
									ival=sNode.getStringValue();
									//System.out.println("ival "+ival.trim());																		
									cList=new ArrayList<Node>();
									for(int k=l+1;k<sList.size();k++)
									{
										//System.out.println("Heoooll");
										sNode=sList.get(k);
										String left= sNode.valueOf("@left");
										String font= sNode.valueOf("@font");
										int len=sNode.getStringValue().length();
										//System.out.println("sNode "+sNode.asXML());
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") && sNode.getStringValue().contains(","))
										{											break;
										}
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") )
										{											break;
										}
										if(len>=9)
										{
										//	break;
										}
										if(left.equalsIgnoreCase("47") && font.equalsIgnoreCase("4"))
										{
											Node node1=null;
											//System.out.println("gggg "+sNode.asXML());
									//		List<Node> node1=new ArrayList<Node>();
											if(item.getString("Name").equals("pas_part_ee_401k_contrib"))
											{
											 node1=sNode.selectSingleNode("following::text[2]");
											}											
											//System.out.println("node1 "+node1.getStringValue());											
											float Bal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
								//			System.out.println("bal "+Bal);
											begBal=begBal+Bal;											
											cList.add(sNode);
										}										
									}									
									if(item.getString("Name").equals("pas_part_ee_401k_contrib"))
									{										
										ival=Float.toString(begBal);
									}									
							//		System.out.println("beg bal "+begBal);
									begBal=0;
									//cList.add(sNode);
								//	System.out.println("cList size "+cList.size());																	
									break;
									
								case "ROTH CONTRIBUTION 01":
									ival=sNode.getStringValue();
									//System.out.println("ival "+ival.trim());																			
									cList=new ArrayList<Node>();
									for(int k=l+1;k<sList.size();k++)
									{
										//System.out.println("Heoooll");
										sNode=sList.get(k);
										String left= sNode.valueOf("@left");
										String font= sNode.valueOf("@font");
										int len=sNode.getStringValue().length();
									//	System.out.println("sNode "+sNode.asXML());
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") && sNode.getStringValue().contains(","))
										{
											break;
										}
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") )
										{
											break;
										}
										if(len>=9)
										{
										//	break;
										}
										if(left.equalsIgnoreCase("47") && font.equalsIgnoreCase("4"))
										{
											Node node1=null;											
											if(item.getString("Name").equals("pas_part_ee_roth_contrib"))
											{
											 node1=sNode.selectSingleNode("following::text[2]");
											}
											float Bal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
										//	System.out.println("bal "+Bal);
											begBal=begBal+Bal;											
											cList.add(sNode);
										}										
									}									
									if(item.getString("Name").equals("pas_part_ee_roth_contrib"))
									{										
										ival=Float.toString(begBal);
									}									
							//		System.out.println("beg bal "+begBal);
									begBal=0;							
							//		System.out.println("cList size "+cList.size());																									
									break;	
									
								case "401 ROLLOVER 01":
									ival=sNode.getStringValue();
									//System.out.println("ival "+ival.trim());																			
									cList=new ArrayList<Node>();
									for(int k=l+1;k<sList.size();k++)
									{
										//System.out.println("Heoooll");
										sNode=sList.get(k);
										String left= sNode.valueOf("@left");
										String font= sNode.valueOf("@font");
										int len=sNode.getStringValue().length();
									//	System.out.println("sNode "+sNode.asXML());
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") && sNode.getStringValue().contains(","))
										{
											break;
										}
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") )
										{
											break;
										}
										if(len>=9)
										{
										//	break;
										}
										if(left.equalsIgnoreCase("47") && font.equalsIgnoreCase("4"))
										{
											Node node1=null;											
											if(item.getString("Name").equals("pas_part_rollover_contrib"))
											{
											 node1=sNode.selectSingleNode("following::text[3]");
											}
											float Bal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
									//		System.out.println("bal "+Bal);
											begBal=begBal+Bal;											
											cList.add(sNode);
										}										
									}									
									if(item.getString("Name").equals("pas_part_rollover_contrib"))
									{										
										ival=Float.toString(begBal);
									}									
								//	System.out.println("beg bal "+begBal);
									begBal=0;							
								//	System.out.println("cList size "+cList.size());																									
									break;	
									
								case "EMPLOYER MATCH 01":
									ival=sNode.getStringValue();
									//System.out.println("ival "+ival.trim());																			
									cList=new ArrayList<Node>();
									for(int k=l+1;k<sList.size();k++)
									{
										//System.out.println("Heoooll");
										sNode=sList.get(k);
										String left= sNode.valueOf("@left");
										String font= sNode.valueOf("@font");
										int len=sNode.getStringValue().length();
									//	System.out.println("sNode "+sNode.asXML());
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") && sNode.getStringValue().contains(","))
										{
											break;
										}
										if(sNode.valueOf("@left").equalsIgnoreCase("47") && sNode.valueOf("@font").equalsIgnoreCase("3") )
										{
											break;
										}
										if(len>=9)
										{
										//	break;
										}
										if(left.equalsIgnoreCase("47") && font.equalsIgnoreCase("4"))
										{
											Node node1=null;											
											if(item.getString("Name").equals("pas_part_er_match"))
											{
											 node1=sNode.selectSingleNode("following::text[2]");
											}
											float Bal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
											System.out.println("bal "+Bal);
											begBal=begBal+Bal;											
											cList.add(sNode);
										}										
									}									
									if(item.getString("Name").equals("pas_part_er_match"))
									{										
										ival=Float.toString(begBal);
									}									
								//	System.out.println("beg bal "+begBal);
									begBal=0;							
								//	System.out.println("cList size "+cList.size());																									
									break;
									
									
									
								default:
									ival="";
								//	System.out.println("default   "+item.getString("Name"));
									outp.put(item.getString("Name"), ival);
									break;																		
							}		
							outp.put(item.getString("Name"), ival.trim());
							begBal=0;
							break;
						}
						else
						{
							outp.put(item.getString("Name"), "");
						}
						//System.out.println("sNode "+sNode.asXML());
					}
				}
				outp=splitName(outp);
				outp=addZero(outp);
				outp=dateFormat(outp);
				jList.put(outp);
			//System.out.println("outp "+outp.toString());
		}
		//nList=new ArrayList<Node>();
		System.out.println(jList.toString());
		//C024DemoRoundFun.roundFunction(list);				
}

public static JSONObject dateFormat(JSONObject jobj) throws NumberFormatException, JSONException, ParseException
{
	@SuppressWarnings("static-access")
	String keys[]=jobj.getNames(jobj);
	for(int i=0;i<keys.length;i++)
	{
		if(keys[i].contains("date") || keys[i].contains("dob") || keys[i].contains("dot") || keys[i].contains("doh"))
		{
			String key=keys[i];
			String keyVal=jobj.getString(key).trim();
			//System.out.println("key  "+key);
		//	System.out.println("keyVal "+keyVal);
			
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			if(keyVal.equals(""))
			{
				break;
			}
			Date date = format.parse(keyVal);
			//System.out.println("date "+date);
			 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
			String strDate= formatter.format(date); 
			//Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(keyVal);
		//	System.out.println("date "+strDate);
			
			jobj.put(keys[i], strDate);
		}
	}
	return jobj;
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


public static JSONObject splitName2(JSONObject jobj) throws JSONException
{
	//String keys[]=jobj.getNames(jobj);
	String namearr[]=jobj.getNames(jobj);
	for(int i=0;i<namearr.length;i++)
	{
		String fullName=jobj.getString(namearr[i]);
		

		String temp[]=fullName.trim().split(",");
		if(temp.length==2)
		{
			System.out.println("fullname="+fullName);
			if(namearr[i].contains("first_name"))
			{
				System.out.println("first name "+temp[1]);
				String firstName=namearr[i];
				jobj.put(firstName, temp[1]);
			}
			if(namearr[i].contains("last_name"))
			{
				System.out.println("last name "+temp[0]);
				String lastName=namearr[i];
				jobj.put(lastName, temp[0]);
			}
		//	System.out.println("first name="+temp[1]);
		//	System.out.println("last name="+temp[0]);
		
		}
	}
	return jobj;
}


public static JSONObject splitName(JSONObject jobj) throws JSONException
{
	String namearr[]=jobj.getNames(jobj);
	for (int i = 0; i < namearr.length; i++) 
	{
		if(namearr[i].contains("name"))
		{
			String fullName=jobj.getString(namearr[i]).trim();
			
			System.out.println("full name  "+fullName);
			String temp[]=fullName.trim().split(",");
			System.out.println("temp length "+temp.length);
			if(temp.length==1)
			break;	
				
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
				jobj.put("pas_part_ee_first_name", temp1[0]);
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
					String middName=namearr[i];
					jobj.put(middName, temp1[1]);
				}
				jobj.put("pas_part_ee_first_name", temp1[0]);
				jobj.put("pas_part_ee_middle_name", temp1[1]);
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




}
