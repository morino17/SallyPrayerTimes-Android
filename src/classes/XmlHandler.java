/*******************************Copyright Block*********************************
 *                                                                             *
 *                Sally Prayer Times Calculator (Final 1.2.15)                 *
 *           Copyright (C) 2015 http://www.sallyproject.altervista.org/        *
 *                         bibali1980@gmail.com                              *
 *                                                                             *
 *     This program is free software: you can redistribute it and/or modify    *
 *     it under the terms of the GNU General Public License as published by    *
 *      the Free Software Foundation, either version 3 of the License, or      *
 *                      (at your option) any later version.                    *
 *                                                                             *
 *       This program is distributed in the hope that it will be useful,       *
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of       *
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the        *
 *                 GNU General Public License for more details.                *
 *                                                                             *
 *      You should have received a copy of the GNU General Public License      *
 *      along with this program.  If not, see http://www.gnu.org/licenses      *
 *******************************************************************************/
package classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;

public class XmlHandler{

    private Element element;//Element object
    private static XmlHandler xmlHandler;//object handle user configuration xml file
    
    private File userConfigFile;
    private File cacheDir;
    private Context context;

    public static XmlHandler getSingleton() {//get single object XmlHandler
        if (xmlHandler == null) {
            xmlHandler = new XmlHandler();
        }
        return xmlHandler;
    }
    
   public void setParameters(Context context) throws ParserConfigurationException, TransformerException, SAXException, IOException{
	   this.context = context;
	   createSallyFileConfig();
    }
   
   public void createSallyFileConfig()throws ParserConfigurationException, TransformerException, SAXException, IOException{
	   try {
	       cacheDir = context.getCacheDir();
	       userConfigFile = new File(cacheDir, "userconfig.xml");
		} catch (Exception e)
	   {
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			   {
		           cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),".sally");
		           userConfigFile = new File(cacheDir, "userconfig.xml");
		    	}
	   }
       
	   if (!cacheDir.exists())
	   {
		   cacheDir.mkdirs();
	   }
       if (!userConfigFile.exists()){
           try {
        		 userConfigFile.createNewFile();
        		 addDefaultUserConfig();
			} catch (IOException e) {}
	    }
	}
    
    public UserConfig getUserConfig() throws ParserConfigurationException, SAXException, IOException, TransformerException{
    	UserConfig userConfig = UserConfig.getSingleton();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(userConfigFile);
		NodeList cronologyNodes = document.getElementsByTagName("userConfig");
            Node node = cronologyNodes.item(0);
            if(node.getNodeType() == Node.ELEMENT_NODE){
            	Element element = (Element)node;
            	
            	NodeList country = element.getElementsByTagName("country");
                Element countryElement = (Element)country.item(0);
                NodeList countryList = countryElement.getChildNodes();
                userConfig.setCountry(((Node)countryList.item(0)).getNodeValue().trim());
                
                NodeList city = element.getElementsByTagName("city");
                Element cityElement = (Element)city.item(0);
                NodeList cityList = cityElement.getChildNodes();
                userConfig.setCity(((Node)cityList.item(0)).getNodeValue().trim());
                
                NodeList longitude = element.getElementsByTagName("longitude");
                Element longitudeElement = (Element)longitude.item(0);
                NodeList longitudeList = longitudeElement.getChildNodes();
                userConfig.setLongitude(((Node)longitudeList.item(0)).getNodeValue().trim());
                
                NodeList latitude = element.getElementsByTagName("latitude");
                Element latitudeElement = (Element)latitude.item(0);
                NodeList latitudeList = latitudeElement.getChildNodes();
                userConfig.setLatitude(((Node)latitudeList.item(0)).getNodeValue().trim());
                
                NodeList timezone = element.getElementsByTagName("timezone");
                Element timezoneElement = (Element)timezone.item(0);
                NodeList timezoneList = timezoneElement.getChildNodes();
                userConfig.setTimezone(((Node)timezoneList.item(0)).getNodeValue().trim());
                
                NodeList language = element.getElementsByTagName("language");
                Element languageElement = (Element)language.item(0);
                NodeList languageList = languageElement.getChildNodes();
                userConfig.setLanguage(((Node)languageList.item(0)).getNodeValue().trim());
                
                NodeList hijri = element.getElementsByTagName("hijri");
                Element hijriElement = (Element)hijri.item(0);
                NodeList hijriList = hijriElement.getChildNodes();
                userConfig.setHijri(((Node)hijriList.item(0)).getNodeValue().trim());
            	
                NodeList typetime = element.getElementsByTagName("typetime");
                Element typetimeElement = (Element)typetime.item(0);
                NodeList typetimeList = typetimeElement.getChildNodes();
                userConfig.setTypetime(((Node)typetimeList.item(0)).getNodeValue().trim());
                
                NodeList mazhab = element.getElementsByTagName("mazhab");
                Element mazhabElement = (Element)mazhab.item(0);
                NodeList mazhabList = mazhabElement.getChildNodes();
                userConfig.setMazhab(((Node)mazhabList.item(0)).getNodeValue().trim());
                
                NodeList calculationMethod = element.getElementsByTagName("calculationMethod");
                Element calculationMethodElement = (Element)calculationMethod.item(0);
                NodeList calculationMethodList = calculationMethodElement.getChildNodes();
                userConfig.setCalculationMethod(((Node)calculationMethodList.item(0)).getNodeValue().trim());
    
                NodeList fajr_athan = element.getElementsByTagName("fajr_athan");
                Element fajr_athanElement = (Element)fajr_athan.item(0);
                NodeList fajr_athanList = fajr_athanElement.getChildNodes();
                userConfig.setFajr_athan(((Node)fajr_athanList.item(0)).getNodeValue().trim());
                
                NodeList shorouk_athan = element.getElementsByTagName("shorouk_athan");
                Element shorouk_athanElement = (Element)shorouk_athan.item(0);
                NodeList shorouk_athanList = shorouk_athanElement.getChildNodes();
                userConfig.setShorouk_athan(((Node)shorouk_athanList.item(0)).getNodeValue().trim());
                
                NodeList duhr_athan = element.getElementsByTagName("duhr_athan");
                Element duhr_athanElement = (Element)duhr_athan.item(0);
                NodeList duhr_athanList = duhr_athanElement.getChildNodes();
                userConfig.setDuhr_athan(((Node)duhr_athanList.item(0)).getNodeValue().trim());
                
                NodeList asr_athan = element.getElementsByTagName("asr_athan");
                Element asr_athanElement = (Element)asr_athan.item(0);
                NodeList asr_athanList = asr_athanElement.getChildNodes();
                userConfig.setAsr_athan(((Node)asr_athanList.item(0)).getNodeValue().trim());
                
                NodeList maghrib_athan = element.getElementsByTagName("maghrib_athan");
                Element maghrib_athanElement = (Element)maghrib_athan.item(0);
                NodeList maghrib_athanList = maghrib_athanElement.getChildNodes();
                userConfig.setMaghrib_athan(((Node)maghrib_athanList.item(0)).getNodeValue().trim());
                
                NodeList ishaa_athan = element.getElementsByTagName("ishaa_athan");
                Element ishaa_athanElement = (Element)ishaa_athan.item(0);
                NodeList ishaa_athanList = ishaa_athanElement.getChildNodes();
                userConfig.setIshaa_athan(((Node)ishaa_athanList.item(0)).getNodeValue().trim());
                
                NodeList fajr_time = element.getElementsByTagName("fajr_time");
                Element fajr_timeElement = (Element)fajr_time.item(0);
                NodeList fajr_timeList = fajr_timeElement.getChildNodes();
                userConfig.setFajr_time(((Node)fajr_timeList.item(0)).getNodeValue().trim());
                
                NodeList shorouk_time = element.getElementsByTagName("shorouk_time");
                Element shorouk_timeElement = (Element)shorouk_time.item(0);
                NodeList shorouk_timeList = shorouk_timeElement.getChildNodes();
                userConfig.setShorouk_time(((Node)shorouk_timeList.item(0)).getNodeValue().trim());
                
                NodeList duhr_time = element.getElementsByTagName("duhr_time");
                Element duhr_timeElement = (Element)duhr_time.item(0);
                NodeList duhr_timeList = duhr_timeElement.getChildNodes();
                userConfig.setDuhr_time(((Node)duhr_timeList.item(0)).getNodeValue().trim());
                
                NodeList asr_time = element.getElementsByTagName("asr_time");
                Element asr_timeElement = (Element)asr_time.item(0);
                NodeList asr_timeList = asr_timeElement.getChildNodes();
                userConfig.setAsr_time(((Node)asr_timeList.item(0)).getNodeValue().trim());
                
                NodeList maghrib_time = element.getElementsByTagName("maghrib_time");
                Element maghrib_timeElement = (Element)maghrib_time.item(0);
                NodeList maghrib_timeList = maghrib_timeElement.getChildNodes();
                userConfig.setMaghrib_time(((Node)maghrib_timeList.item(0)).getNodeValue().trim());
                
                NodeList ishaa_time = element.getElementsByTagName("ishaa_time");
                Element ishaa_timeElement = (Element)ishaa_time.item(0);
                NodeList ishaa_timeList = ishaa_timeElement.getChildNodes();
                userConfig.setIshaa_time(((Node)ishaa_timeList.item(0)).getNodeValue().trim());
                
                NodeList athan = element.getElementsByTagName("athan");
                Element athanElement = (Element)athan.item(0);
                NodeList athanList = athanElement.getChildNodes();
                userConfig.setAthan(((Node)athanList.item(0)).getNodeValue().trim());
                
                NodeList time12or24 = element.getElementsByTagName("time12or24");
                Element time12or24Element = (Element)time12or24.item(0);
                NodeList time12or24List = time12or24Element.getChildNodes();
                userConfig.setTime12or24(((Node)time12or24List.item(0)).getNodeValue().trim());

            	}
		return userConfig;
        }
    
    public void addUserConfig(UserConfig userConfig) throws ParserConfigurationException, TransformerException, SAXException, IOException{
    	createSallyFileConfig();
    	FileOutputStream localFileOutputStream1 = new FileOutputStream(userConfigFile);
		FileOutputStream localFileOutputStream = localFileOutputStream1;
		XmlSerializer localXmlSerializer = Xml.newSerializer();
	      localXmlSerializer.setOutput(localFileOutputStream, "UTF-8");
	      localXmlSerializer.startDocument(null, Boolean.valueOf(true));
	      localXmlSerializer.startTag(null, "configurations");

	    	  localXmlSerializer.startTag(null, "userConfig");
	    	  
	    	  localXmlSerializer.startTag(null, "country");
		      localXmlSerializer.text(userConfig.getCountry());
		      localXmlSerializer.endTag(null, "country");
		      
		      localXmlSerializer.startTag(null, "city");
		      localXmlSerializer.text(userConfig.getCity());
		      localXmlSerializer.endTag(null, "city");
		      
		      localXmlSerializer.startTag(null, "longitude");
		      localXmlSerializer.text(userConfig.getLongitude());
		      localXmlSerializer.endTag(null, "longitude");
		      
		      localXmlSerializer.startTag(null, "latitude");
		      localXmlSerializer.text(userConfig.getLatitude());
		      localXmlSerializer.endTag(null, "latitude");
		      
		      localXmlSerializer.startTag(null, "timezone");
		      localXmlSerializer.text(userConfig.getTimezone());
		      localXmlSerializer.endTag(null, "timezone");
		      
		      localXmlSerializer.startTag(null, "language");
		      localXmlSerializer.text(userConfig.getLanguage());
		      localXmlSerializer.endTag(null, "language");
		      
		      localXmlSerializer.startTag(null, "hijri");
		      localXmlSerializer.text(userConfig.getHijri());
		      localXmlSerializer.endTag(null, "hijri");
		      
		      localXmlSerializer.startTag(null, "typetime");
		      localXmlSerializer.text(userConfig.getTypetime());
		      localXmlSerializer.endTag(null, "typetime");
		      
		      localXmlSerializer.startTag(null, "mazhab");
		      localXmlSerializer.text(userConfig.getMazhab());
		      localXmlSerializer.endTag(null, "mazhab");
		      
		      localXmlSerializer.startTag(null, "calculationMethod");
		      localXmlSerializer.text(userConfig.getCalculationMethod());
		      localXmlSerializer.endTag(null, "calculationMethod");
		      
		      localXmlSerializer.startTag(null, "fajr_athan");
		      localXmlSerializer.text(userConfig.getFajr_athan());
		      localXmlSerializer.endTag(null, "fajr_athan");
		      
		      localXmlSerializer.startTag(null, "shorouk_athan");
		      localXmlSerializer.text(userConfig.getShorouk_athan());
		      localXmlSerializer.endTag(null, "shorouk_athan");
		      
		      localXmlSerializer.startTag(null, "duhr_athan");
		      localXmlSerializer.text(userConfig.getDuhr_athan());
		      localXmlSerializer.endTag(null, "duhr_athan");
		      
		      localXmlSerializer.startTag(null, "asr_athan");
		      localXmlSerializer.text(userConfig.getAsr_athan());
		      localXmlSerializer.endTag(null, "asr_athan");
		      
		      localXmlSerializer.startTag(null, "maghrib_athan");
		      localXmlSerializer.text(userConfig.getMaghrib_athan());
		      localXmlSerializer.endTag(null, "maghrib_athan");
		      
		      localXmlSerializer.startTag(null, "ishaa_athan");
		      localXmlSerializer.text(userConfig.getIshaa_athan());
		      localXmlSerializer.endTag(null, "ishaa_athan");
	
		      localXmlSerializer.startTag(null, "fajr_time");
		      localXmlSerializer.text(userConfig.getFajr_time());
		      localXmlSerializer.endTag(null, "fajr_time");
		      
		      localXmlSerializer.startTag(null, "shorouk_time");
		      localXmlSerializer.text(userConfig.getShorouk_time());
		      localXmlSerializer.endTag(null, "shorouk_time");
		      
		      localXmlSerializer.startTag(null, "duhr_time");
		      localXmlSerializer.text(userConfig.getDuhr_time());
		      localXmlSerializer.endTag(null, "duhr_time");
		      
		      localXmlSerializer.startTag(null, "asr_time");
		      localXmlSerializer.text(userConfig.getAsr_time());
		      localXmlSerializer.endTag(null, "asr_time");
		      
		      localXmlSerializer.startTag(null, "maghrib_time");
		      localXmlSerializer.text(userConfig.getMaghrib_time());
		      localXmlSerializer.endTag(null, "maghrib_time");
		      
		      localXmlSerializer.startTag(null, "ishaa_time");
		      localXmlSerializer.text(userConfig.getIshaa_time());
		      localXmlSerializer.endTag(null, "ishaa_time");
		      
		      localXmlSerializer.startTag(null, "athan");
		      localXmlSerializer.text(userConfig.getAthan());
		      localXmlSerializer.endTag(null, "athan");
		      
		      localXmlSerializer.startTag(null, "time12or24");
		      localXmlSerializer.text(userConfig.getTime12or24());
		      localXmlSerializer.endTag(null, "time12or24");
		      
		      localXmlSerializer.endTag(null, "userConfig");

	      localXmlSerializer.endTag(null, "configurations");
	      localXmlSerializer.endDocument();
	      localXmlSerializer.flush();
	      localFileOutputStream.close();
        }

    private void addDefaultUserConfig() throws ParserConfigurationException, TransformerException, SAXException, IOException {//adding the default user configuration for the first time
    	UserConfig userConfig = UserConfig.getSingleton();
        userConfig.setCountry("Saudi Arabia");
        userConfig.setCity("Makkah");
        userConfig.setLongitude("39.8409");
        userConfig.setLatitude("21.4309");
        userConfig.setTimezone("3.0");
        userConfig.setCalculationMethod("MuslimWorldLeague");
        userConfig.setHijri("0");
        userConfig.setTypetime("standard");
        userConfig.setLanguage("en");
        userConfig.setMazhab("shafi3i");
        userConfig.setFajr_athan("athan");
        userConfig.setShorouk_athan("athan");
        userConfig.setDuhr_athan("athan");
        userConfig.setAsr_athan("athan");
        userConfig.setMaghrib_athan("athan");
        userConfig.setIshaa_athan("athan");
        userConfig.setFajr_time("0");
        userConfig.setShorouk_time("0");
        userConfig.setDuhr_time("0");
        userConfig.setAsr_time("0");
        userConfig.setMaghrib_time("0");
        userConfig.setIshaa_time("0");
        userConfig.setAthan("ali_ben_ahmed_mala");
        userConfig.setTime12or24("24");
        addUserConfig(userConfig);
    }
    
	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
