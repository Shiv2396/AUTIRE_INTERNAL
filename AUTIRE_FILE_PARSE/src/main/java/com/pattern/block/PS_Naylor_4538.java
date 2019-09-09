package com.pattern.block;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;

import com.util.UTL;

public class PS_Naylor_4538 
{
		static File 				cFile = new File("1553482714538_Participant Account Summary (American Trust)_Config.json");			
		static JSONObject			jConf  = new JSONObject();
		static JSONArray			jList  = new JSONArray();
		static JSONObject			jItem  = new JSONObject();
		//static List<Node>			nList  = null;
		static Node					nItem  = null;	
		static String						sConf = "";	

		public static void main(String[] args) 
		{
			List<Node>		employeeNameList  = new ArrayList<Node>();
//			List<Node>		nList  = new ArrayList<Node>();
			List<Node>		xList  = new ArrayList<Node>();
			List<Node>		sList  = new ArrayList<Node>();
			List<Node>		cList  = new ArrayList<Node>();

			
			Node 			eNameNode  = null;
//			Node			nNode  = null;
			Node  			xNode  = null;
			Node			sNode  = null;	
			Node			cNode  = null;

			try
			{
				
				String path=args[0];
				System.out.println(path);
				String path1=path+".pdf";
//				System.out.println(path1);
				
				UTL.setPDFVersion(path,path1);
				System.out.println("PDF version Lower done...!!  ");
				UTL.extractXML(path);
				System.out.println("PDF to XML Conversion done..!! ");		
				Thread.sleep(500);
				System.out.println("in sleep");			
				UTL.sortXML(path);		
				Thread.sleep(100);
				System.out.println("in sleep");		
				System.out.println("XML sorting done..!! ");

				String Posn ;
				String Posx ;
				String Type ;
				String Cpath;
				String Name;
				float begBal=0;
				int subTotTop;
				int partTotTop;
				
				sConf = FileUtils.readFileToString(cFile,"UTF-8");
				jConf = new JSONObject(sConf);
				JSONArray  conf = jConf.getJSONArray("Items");
				
				JSONObject node = new JSONObject();
				JSONObject item = new JSONObject();
				JSONArray  list = new JSONArray();
				JSONObject outp = new JSONObject();
				String	   ival = "";
					
				Document  		dcXML  = (new SAXReader()).read(path+"_INPSorted.xml");
				employeeNameList = dcXML.getRootElement().selectNodes("//text[@left='28' and @font='2']");
				//	mList = dcXML.getRootElement().selectNodes("//text[contains(text(),'Participant Identifier')]");// and //text[contains(text(),',')]");
				System.out.println("mlist size "+employeeNameList.size());
				String prevName="";
				//System.out.println("nlist size "+ nList.size());
				for(int j=0;j<employeeNameList.size();j++)
				{
					eNameNode=employeeNameList.get(j);
					System.out.println("name "+eNameNode.asXML());
					sList=new ArrayList<Node>();
					sList.add(eNameNode);
					xList=eNameNode.selectNodes("following::text");
					for(int k=0;k<xList.size();k++)
					{
						
						if(j==employeeNameList.size())
						{
							sList.add(xNode);
						}
						xNode=xList.get(k);
						if(j==employeeNameList.size()-1)
						{
						//	System.out.println("in conti");
							sList.add(xNode);
							continue;
						}
						String nextName=employeeNameList.get(j+1).getStringValue();
						if(xNode.getStringValue().equalsIgnoreCase(nextName) && k!=xList.size())
						{
							//System.out.println("in break");
							break;
						}
						sList.add(xNode);
					}

					outp=new JSONObject();	
					for(int n=0; n<conf.length();n++)
					{
						item = conf.getJSONObject(n);					
						path = item.getString("Path");
						Posn = item.getString("Posn");
						Posx = item.getString("Posx");		
						
						String	   jText = "";
						String     jAttr = "";
						String     jPosx = "1";
						String 	   jType = "String";
						//System.out.println("slist size "+sList.size());
						for(int l=0;l<sList.size();l++)
						{
							sNode=sList.get(l);
						//	System.out.println("sNode "+sNode.asXML());
							if (sNode.getStringValue().equalsIgnoreCase(item.getString("Path"))) {
								jAttr = item.getString("Name");
								jPosx = item.getString("Posx");
								jType = item.getString("Type");
								if ( item.get("Posn").equals("F")) {
									jText = sNode.selectSingleNode("following-sibling::text["+jPosx+"]").getStringValue();
						//			System.out.println("jText "+jText);
									
								} else {
									jText = sNode.selectSingleNode("preceding-sibling::text["+jPosx+"]").getStringValue();
								}
								if (jType.equals("Number")) {
									jText = jText.replaceAll("\\s+","");
									if ( jText.length() > 1 ) {
										if (jText.indexOf(".") == -1 ) {
											jText = jText.substring(0, jText.length()-2) + "." + jText.substring(jText.length()-2);
										}
									}
								}
								
								outp.put(jAttr, jText.replace("(", "").replace(")", "").replace(",", ""));
								//System.out.println("out "+outp.toString());
								break;
							}
							else
							{
								outp.put(item.getString("Name"), "");
							}
							
							
							
							
							
							
							
/*							
							
							Node node1=null;
							String value=null;
							if(sNode.getStringValue().contains(path))
							{
								switch (path)
								{
									case "Birth Date:":
										ival=sNode.getStringValue();
										if ( Posn.equals("F")) {
											ival = sNode.selectSingleNode("following-sibling::text["+Posx+"]").getStringValue();
											System.out.println("jText "+ival);
											
										} else {
											ival = sNode.selectSingleNode("preceding-sibling::text["+Posx+"]").getStringValue();
										}
									//	ival=ival.substring(ival.indexOf("Birth Date:"));
										//System.out.println("iavl "+ival.trim());
										outp.put(item.getString("Name"), ival.trim());
									break;
												
									case "/":
										//System.out.println("ssssss"+item.getString("Name"));
										if(sNode.valueOf("@left").equals("27") && sNode.valueOf("@font").equals("0"))
										{
											ival=sNode.getStringValue();
											if(ival.contains("/"))
											{
												ival=ival.substring(ival.indexOf("/")+1,ival.length());
											}
											//System.out.println("ival "+ival.trim());
											outp.put(item.getString("Name"), ival.trim());
										}		
									break;
												
									case ",":
										//System.out.println("left  "+sNode.valueOf("@left"));
										if(sNode.valueOf("@left").equals("27") && sNode.valueOf("@font").equals("0"))
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
												
									case "Participant Total":
										ival=sNode.getStringValue();
										//System.out.println("Hello    ival "+ival.trim());											
										//cList=sNode.selectNodes("//text[@left='47' and @font='4'][text()[@left='47' and @font='3']]");
										//div[@class="blue"]/a[following-sibling::text()[contains(., "With")]]
												
										cList=new ArrayList<Node>();
										String left= sNode.valueOf("@left");
										String font= sNode.valueOf("@font");
										//System.out.println("gggg "+sNode.asXML());
										//		List<Node> node1=new ArrayList<Node>();
										if(item.getString("Name").equals("pas_part_acct_beg_bal"))
										{
											//System.out.println("sNode "+sNode.asXML());															
											node1=sNode.selectSingleNode("following::text[1]");
											value=node1.getStringValue();
											//System.out.println("node1   "+node1.asXML() );
										}										
														if(item.getString("Name").equals("pas_part_adjustments"))
														{
														 node1=sNode.selectSingleNode("following::text[3]");
														 Node node2=sNode.selectSingleNode("following::text[5]");
														 String value2=node2.getStringValue().replace(",", "");
														 value=node1.getStringValue().replace(",", "");
														 
														 String arr[]=value.trim().split("\\s+");
														 value=arr[0];
														 float f1=Float.parseFloat(value);
														 arr=value2.trim().split("\\s+");
														 value2=arr[2];
														 float f2;
														 if(value2.contains("("))
														 {
															  f2=Float.parseFloat(value2.replace("(", "").replace(")", ""));
															 f2=f2*-1;
														 }
														 else
														 {
														  f2=Float.parseFloat(value2);
														 }
														 f1=f1+f2;
														 value=Float.toString(f1);
														}
														if(item.getString("Name").equals("pas_part_forfeiture"))
														{
														 node1=sNode.selectSingleNode("following::text[3]");
														 value=node1.getStringValue();
														 String arr[]=value.trim().split("\\s+");
														 value=arr[1];
														}
														if(item.getString("Name").equals("pas_part_distribution_payments"))
														{
														 node1=sNode.selectSingleNode("following::text[4]");
														 value=node1.getStringValue();
														 String arr[]=value.trim().split("\\s+");
														 value=arr[0];
														}
														if(item.getString("Name").equals("pas_part_loan_withdrawals"))
														{
														 node1=sNode.selectSingleNode("following::text[4]");
														 value=node1.getStringValue();
														 String arr[]=value.trim().split("\\s+");
														 value=arr[1];
														}
														if(item.getString("Name").equals("pas_part_loan_principal_pay"))
														{
														 node1=sNode.selectSingleNode("following::text[4]");
														 value=node1.getStringValue();
														 String arr[]=value.trim().split("\\s+");
														 value=arr[2];
														}
														if(item.getString("Name").equals("pas_part_loan_interest_pay"))
														{
														 node1=sNode.selectSingleNode("following::text[5]");
														 value=node1.getStringValue();
														 String arr[]=value.trim().split("\\s+");
														 value=arr[0];
														 
														}
														if(item.getString("Name").equals("pas_part_acct_gain_loss"))
														{
													//		System.out.println("sNode "+sNode.asXML());											
															node1=sNode.selectSingleNode("following::text[5]");
															value=node1.getStringValue();
															 String arr[]=value.trim().split("\\s+");
															 value=arr[1];
														}
														if(item.getString("Name").equals("pas_part_acct_end_bal"))
														{
														 node1=sNode.selectSingleNode("following::text[6]");
														 value=node1.getStringValue();
														}
														//System.out.println("node1 "+node1.getStringValue());
														if(value!=null)
														{
														//	System.out.println("value  "+value);
															float Bal;
															//String b=node1.getStringValue();
															if(value.contains("("))
															{
																 Bal=Float.parseFloat(value.replace(",", "").replace("(", "").replace(")", ""));
																 Bal=Bal*-1;
															}
															else
															{
																 Bal=Float.parseFloat(value.replace(",", "").replace("(", "").replace(")", ""));
															}										
														begBal=Bal;																					
														}
														cList.add(sNode);
														
												if(item.getString("Name").equals("pas_part_acct_beg_bal"))
												{									
													ival=Float.toString(begBal);
													//System.out.println("ival  "+ival);
												}
												if(item.getString("Name").equals("pas_part_adjustments"))
												{										
													ival=Float.toString(begBal);
													
													//System.out.println("ival  "+ival);
												}
												if(item.getString("Name").equals("pas_part_forfeiture"))
												{										
													ival=Float.toString(begBal);
													
												}
												if(item.getString("Name").equals("pas_part_distribution_payments"))
												{										
													ival=Float.toString(begBal);
													
												}
												if(item.getString("Name").equals("pas_part_loan_withdrawals"))
												{										
													ival=Float.toString(begBal);
												}
												if(item.getString("Name").equals("pas_part_loan_principal_pay"))
												{										
													ival=Float.toString(begBal);
												}
												if(item.getString("Name").equals("pas_part_loan_interest_pay"))
												{										
													ival=Float.toString(begBal);
												}
												if(item.getString("Name").equals("pas_part_acct_gain_loss"))
												{										
													ival=Float.toString(begBal);
												}
												if(item.getString("Name").equals("pas_part_acct_end_bal"))
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
												
																																				
											case "Rollover":
												Node subTot=null;
												Node partTot=null;
												ival=sNode.getStringValue();
											//	System.out.println("ival "+ival.trim());																		
												cList=new ArrayList<Node>();
												subTot=sNode.selectSingleNode("following::text[contains(text(),'Sub Total')]");
												partTot=subTot.selectSingleNode("preceding::text[contains(text(),'-') and contains(text(),'/')][1]");
												System.out.println("name ss "+partTot.asXML());
												
												if(!partTot.getStringValue().equals(eNameNode.getStringValue()))
												{
													System.out.println("helloo");
													if(item.getString("Name").equals("pas_part_rollover_contrib"))
													{
														System.out.println("1");
														if(eNameNode.getStringValue().contains("KWAN,"))
														{
															node1.setText("0.00");
														}else
													 node1=sNode.selectSingleNode("following::text[5]");
													}
													
												}else
												{											
													if(item.getString("Name").equals("pas_part_rollover_contrib"))
														{
														 node1=subTot.selectSingleNode("following::text[2]");
														}											
													//	System.out.println("node1 "+node1.getStringValue());											
														begBal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
														cList.add(sNode);		
												}
												if(item.getString("Name").equals("pas_part_rollover_contrib"))
												{										
													ival=Float.toString(begBal);
												}									
												begBal=0;															
												break;
												
												
											case "Employer Matching":
												 subTot=null;
												ival=sNode.getStringValue();
												//System.out.println("ival "+ival.trim());																		
												cList=new ArrayList<Node>();
												subTot=sNode.selectSingleNode("following::text[contains(text(),'Sub Total')]");
												partTot=subTot.selectSingleNode("preceding::text[contains(text(),'-') and contains(text(),'/')][1]");
												System.out.println("name ss "+partTot.asXML());
												
												if(!partTot.getStringValue().equals(eNameNode.getStringValue()))
												{
													System.out.println("helloo");
													if(item.getString("Name").equals("pas_part_er_match"))
													{
														System.out.println("1");
														if(eNameNode.getStringValue().contains("KWAN"))
														{
															continue;
														}else
													 node1=sNode.selectSingleNode("following::text[5]");
													}
													
												}else
												{	
													if(eNameNode.getStringValue().contains("KWAN"))
													{
														continue;
													}
													//System.out.println("subtot  "+subTot.asXML());
														if(item.getString("Name").equals("pas_part_er_match"))
														{
														 node1=subTot.selectSingleNode("following::text[2]");
														}	
												}
														//System.out.println("node1 "+node1.getStringValue());											
														begBal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
														cList.add(sNode);																																						
												if(item.getString("Name").equals("pas_part_er_match"))
												{										
													ival=Float.toString(begBal);
												}									
												begBal=0;															
												break;
												
												
											case "Employee Pre-Tax Deferral":
												// subTot=null;
												ival=sNode.getStringValue();
												//System.out.println("ival "+ival.trim());																		
												cList=new ArrayList<Node>();
												subTot=sNode.selectSingleNode("following::text[contains(text(),'Sub Total')]");
												partTot=subTot.selectSingleNode("preceding::text[contains(text(),'-') and contains(text(),'/')][1]");
												System.out.println("name ss "+partTot.asXML());
												System.out.println("nNode  "+eNameNode.asXML());
												if(!partTot.getStringValue().equals(eNameNode.getStringValue()))
												{
													System.out.println("helloo");
													if(item.getString("Name").equals("pas_part_ee_401k_contrib"))
													{
														System.out.println("1");
														if(eNameNode.getStringValue().contains("ALEXANDER"))
														{
															continue;
														}else
															 node1=sNode.selectSingleNode("following::text[5]");
													}
													
												}else
												{
													System.out.println("hiii");
													subTot=sNode.selectSingleNode("following::text[contains(text(),'Sub Total')]");
														if(item.getString("Name").equals("pas_part_ee_401k_contrib"))
														{
															if(eNameNode.getStringValue().contains("KAREN"))
															{
																continue;
															}
														 node1=subTot.selectSingleNode("following::text[2]");
														}	
												}
														System.out.println("node1 401k"+node1.getStringValue());											
														begBal=Float.parseFloat(node1.getStringValue().replace(",", "").replace("(", "").replace(")", ""));
														cList.add(sNode);	
											
												if(item.getString("Name").equals("pas_part_ee_401k_contrib"))
												{										
													ival=Float.toString(begBal);
												}									
												begBal=0;															
												break;
												
											default:
												ival="";
											//	System.out.println("default   "+item.getString("Name"));
												outp.put(item.getString("Name"), ival);
												break;																		
										}		
										outp.put(item.getString("Name"), ival);
										begBal=0;
										break;
									}
									else
									{
										outp.put(item.getString("Name"), "");
									}
									//System.out.println("sNode "+sNode.asXML());
							
*/							
								}
							}
			//		System.out.println("outp "+outp.toString());
							outp=UTL.splitNameBySpace(outp);
							outp=UTL.addZero(outp);
							outp=UTL.dateFormat(outp);
							jList.put(outp);
						//System.out.println("outp "+outp.toString());
					}
					//nList=new ArrayList<Node>();
					JSONObject finalObj=new JSONObject();
					finalObj.put("Participant", jList);
					System.out.println(finalObj.toString());
					//C024DemoRoundFun.roundFunction(list);		
		
				
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
}
		
