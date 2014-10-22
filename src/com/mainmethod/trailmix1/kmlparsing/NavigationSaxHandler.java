package com.mainmethod.trailmix1.kmlparsing;
import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.android.gms.maps.model.LatLng;

//import com.google.android.gms.maps.model.GeoPoint;

/**
 * @author D4RK SAX parser that will parse the KML file
 */
public class NavigationSaxHandler extends DefaultHandler {

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean in_placemarktag = false;

	private boolean in_coordinatestag = false;
	private boolean in_simpledatatag = false;
	private boolean in_classTag = false;
	private boolean in_surfaceTag = false;
	private boolean in_nametag = false;
	private boolean in_lengthtag = false;
	private int valueCounter = 0;
	private int unnamedCounter = 0;

	public String other;
	public StringBuffer buffer;
	private ArrayList<PlacemarkObj> placemarkObjs = new ArrayList<PlacemarkObj>();
	private PlacemarkObj placemarkObj = null;
	int counter = 0;
	int prevCounter = 0;
	String coordinatesLine = "";

	String result = "";
	String qName = "";

	/**
	 * Gets be called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		// System.out.println("Qname is "+qName);
		this.qName = qName;
		if (qName.equalsIgnoreCase("Placemark")) {
			this.in_placemarktag = true;
			placemarkObj = new PlacemarkObj();
			counter++;
		} else if (qName.equalsIgnoreCase("coordinates")) {
			buffer = new StringBuffer();
			this.in_coordinatestag = true;
		} else if (qName.equalsIgnoreCase("simpledata")
				&& atts.getValue(0).equalsIgnoreCase("name")) {
			this.in_simpledatatag = true;
			this.in_nametag = true;
		} else if (qName.equalsIgnoreCase("simpledata")
				&& atts.getValue(0).equalsIgnoreCase("surface")) {
			this.in_simpledatatag = true;
			this.in_surfaceTag = true;
		} else if (qName.equalsIgnoreCase("simpledata")
				&& atts.getValue(0).equalsIgnoreCase("length")) {
			this.in_simpledatatag = true;
			this.in_lengthtag = true;
		} else if (qName.equalsIgnoreCase("simpledata")
				&& atts.getValue(0).equalsIgnoreCase("class")) {
			this.in_simpledatatag = true;
			this.in_classTag = true;
		} else if (qName.equalsIgnoreCase("simpledata")
				&& atts.getValue(0).equalsIgnoreCase("name")) {
			this.in_simpledatatag = true;
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

			if (placemarkObj.getTrailName() == null) {
				unnamedCounter++;
				placemarkObj.setTrailName("UnnamedTrail");
			}
			placemarkObjs.add(placemarkObj);
			// System.out.println("----------"+String.valueOf(counter)+"----------");
			// counter++;
		} else if (qName.equalsIgnoreCase("coordinates")) {
			this.in_coordinatestag = false;
			stringSplitter(coordinatesLine);
			coordinatesLine = "";
		} else if (qName.equalsIgnoreCase("simpledata")) {
			if (in_classTag) {
                placemarkObj.setTrailClass(other);
                

            }else if (in_surfaceTag) {
                placemarkObj.setSurface(other);
         
            } else if (in_lengthtag) {

                placemarkObj.setLength(Double.parseDouble(other));
              

            } else if (in_nametag) {
                placemarkObj.setTrailName(other);
        
            }
			other ="";
            this.in_simpledatatag = false;
            this.in_classTag = false;
            this.in_lengthtag = false;
            this.in_surfaceTag = false;
            this.in_nametag = false;
		}
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {

		if (this.in_coordinatestag) {
			coordinatesLine += new String(ch, start, length);
		}

		if (this.in_simpledatatag) {
			other += new String(ch, start, length);
			
		}

	}

	private void stringSplitter(String temp) {
		String[] GeoPoint = temp.split(" ");
		ArrayList<LatLng> tempCollection = new ArrayList<LatLng>();
		String[] temp2;
		int index = 0;
		double lng;
		double lat;
		for (String s : GeoPoint) {
			index++;
			temp2 = s.split(",");
			lng = Double.parseDouble(temp2[0].trim());
			lat = Double.parseDouble(temp2[1].trim());
			tempCollection.add(new LatLng(lat, lng));
		}
		// System.out.println(String.valueOf(counter));
		placemarkObj.setCoordinates(tempCollection);
		prevCounter = counter;
	}

	public ArrayList<PlacemarkObj> getPlacemarks() {
		return placemarkObjs;
	}

	/** This method is called when warnings occur */
	public void warning(SAXParseException exception) {
		System.err.println("WARNING: line " + exception.getLineNumber() + ": "
				+ exception.getMessage());
	}

	/** This method is called when errors occur */
	public void error(SAXParseException exception) {
		System.err.println("ERROR: line " + exception.getLineNumber() + ": "
				+ exception.getMessage());
	}

	/** This method is called when non-recoverable errors occur. */
	public void fatalError(SAXParseException exception) throws SAXException {
		System.err.println("FATAL: line " + exception.getLineNumber() + ": "
				+ exception.getMessage());
		throw (exception);
	}
}
