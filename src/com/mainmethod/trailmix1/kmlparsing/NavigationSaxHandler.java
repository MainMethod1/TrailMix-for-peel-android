package com.mainmethod.trailmix1.kmlparsing;
/**
 * 
 */


import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;





import com.google.android.gms.maps.model.LatLng;
import com.mainmethod.trailmix1.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

//import com.google.android.gms.maps.model.LatLng;

/**
 * @author D4RK SAX parser that will parse the KML file
 */
public class NavigationSaxHandler extends DefaultHandler {

	// ===========================================================
	// Fields
	// ===========================================================

	
	private boolean in_placemarktag = false;
	
	private boolean in_coordinatestag = false;
	
	private boolean in_valuetag = false;
	
	private boolean in_nametag = false;
	
	private int valueCounter = 0;
	

	public StringBuffer buffer;
	private ArrayList<Placemark> placemarks = new ArrayList<Placemark>();
	private Placemark placemark = null;
	int counter = 0;
	int prevCounter = 0;
	String coordinatesLine="";
	
	String result="";
	String qName ="";
	
	//private NavigationDataSet navigationDataSet = new NavigationDataSet();
    
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void startDocument() throws SAXException {
		//this.navigationDataSet = new NavigationDataSet();
		
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	/**
	 * Gets be called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		//System.out.println("Qname is "+qName);
		this.qName = qName;
		 if (qName.equalsIgnoreCase("Placemark")) {
			this.in_placemarktag = true;
			placemark = new Placemark();
			counter++;
		} else if (qName.equalsIgnoreCase("coordinates")) {
			buffer = new StringBuffer();
			this.in_coordinatestag = true;
		} else if(qName.equalsIgnoreCase("value")){
			this.in_valuetag = true;
		} else if(qName.equalsIgnoreCase("name")){
			this.in_nametag = true;
		}
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		
		 if (qName.equalsIgnoreCase("Placemark")) {
			this.in_placemarktag = false;
			placemarks.add(placemark);
			//System.out.println("----------"+String.valueOf(counter)+"----------");
			
		}  else if (qName.equalsIgnoreCase("coordinates")) {
			this.in_coordinatestag = false;
			stringSplitter(coordinatesLine);
			coordinatesLine="";
		} else if (qName.equalsIgnoreCase("value")) {
			this.in_valuetag = false;
		} else if (qName.equalsIgnoreCase("name")) {
			this.in_nametag = false;
		}
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		
		
		
		 if (this.in_coordinatestag) {			 
			 coordinatesLine+=new String(ch,start,length);
		}
		 
		 if(this.in_valuetag){
			 String temp = new String(ch, start, length);
			 if (valueCounter == 0)
			 {
				placemark.setTrailClass(temp);
				 valueCounter++;
			 }
			 else if (valueCounter == 1)
			 {
				 placemark.setSurface(temp);
				 valueCounter++;
			 }
			 else if (valueCounter == 2)
			 {
				 placemark.setLength(Double.parseDouble(temp));
				 valueCounter = 0;
			 }
		 }
		 
		 if(this.in_nametag)
		 {
			 String temp = new String(ch, start, length);
			 placemark.setTrailName(temp);
		 }
	}

	private void stringSplitter(String temp) {
		String[] latLng = temp.split(" ");
			ArrayList<LatLng> tempCollection = new ArrayList<LatLng>();
			String[] temp2;
			int index =0;
			double lng;
			double lat;
			for(String s: latLng){
				index++;
				temp2 = s.split(",");
				if(temp2.length > 1)
				{
					if(temp2[0].trim().length()!= 0 && temp2[1].trim().length()!= 0)
					{
						lng = Double.parseDouble(temp2[0].trim());
						lat = Double.parseDouble(temp2[1].trim());
						
						if((lat > 45 || lat <40) || lng > -75)
						{
							//System.out.println("------------------------------------");
							
							//System.out.println("LatLng out of bound"+"\n"+"qName:"+qName+"\n"+"Placemark:"+counter+"\n"+"Lat:" + String.valueOf(lat) + "  Lng:"+ String.valueOf(lng)+"\n"+"Raw: "+temp+"\n"+"String: "+s);
						}
						tempCollection.add(new LatLng(lat,lng));	
					}
					else
					{
						//System.out.println("------------------------------------");
						//System.out.println("Whitespace or null"+"\n"+"Placemark:"+counter+"Raw: "+temp+"\n"+"\n"+"qName:"+qName+"\n"+"String: "+s);
					}
					
				}
				else
				{
					//System.out.println("------------------------------------");
					//System.out.println("Length < 2"+"\n"+"Placemark:"+counter+"\n"+"Index: "+index+"\n"+"Raw: "+temp+"\n"+"qName:"+qName+"\n"+"String: "+s);
				}
				
				
			}
			//System.out.println(String.valueOf(counter));
			placemark.setCoordinates(tempCollection);
		prevCounter = counter;
	}
	public ArrayList<Placemark> getPlacemarks() {
	    return placemarks;
	}
	/*public Placemark getParsedData() {
		System.out.println(buffer.toString().trim());
		placemark.setCoordinates(buffer.toString().trim());
		
		return placemark;
	}*/

}
