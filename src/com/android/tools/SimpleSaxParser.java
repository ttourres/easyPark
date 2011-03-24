package com.android.tools;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import com.android.myfirstapp.Parking;
import com.google.android.maps.GeoPoint;
//import android.util.Log;

public class SimpleSaxParser extends DefaultHandler
{
	private ArrayList<Parking> listParkings;
	private Parking parking;
	private String strLat;
	private String strLon;
	private String str;

    //espaces
    public void ignorableWhitespace(char[] ch, int start, int end)
    {
    	//Log.d("PARSER", " --> " + "espaces inutiles rencontres : ..." + new String(ch, start, end) +  "...");
    } // ignorableWhitespace(char[] ch, int start, int end)

    // debut du document
    public void startDocument() 
	{
    	//Log.d("PARSER START", " --> " + "Start Document: ");
    } // startDocument()

    // debut de l'element
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
    {
		// recuperation du nom de l'element
		String eltName = localName;                 
		if ("".equals(eltName))
			eltName = rawName;  // namespaceAware = false
		else if("parking-list".equals(eltName) || "parking-disponibilite-list".equals(eltName))
		{
			//Log.d("PARSER NAME PARKING-LIST", " --> " + eltName);
			listParkings = new ArrayList<Parking>();
		}
		else if("parking".equals(eltName))
		{
			//Log.d("PARSER NAME PARKING", " --> " + eltName);
			parking = new Parking();
		}
		else if("coordonnees".equals(eltName))
		{
			//Log.d("PARSER NAME COORDONNEES", " --> " + eltName);
			//point = new GeoPoint(0,0);
		}
		str="";
		
		//Log.d("PARSER NAME", " --> " + "<"+ eltName+"");
        for (int i=0; i <atts.getLength(); i++)
        {
			// recuperation du nom de l'attribut et de sa valeur
        	//Log.d("PARSER", " --> " + " " + atts.getQName(i) + " = \"" + atts.getValue(i)+"\"");
		}
        //Log.d("PARSER", " --> " +">\n");
    } // startElement(String namespaceURI, String localName, String rawName, Attributes atts)

    // noeuds texte
    public void characters (char[] ch, int start, int length)
    {
		int o = 0;
		while (o < length && (ch[start+o] == ' ' || ch[start+o] == '\n' || ch[start+o] == '\t')) o++;
		String text = new String (ch, start+o, length-o);
		if (!text.equals(""))
		{
			str = text;
			//Log.d("PARSER TEXT", " --> " + str);
		}
    } // characters (char[] ch, int start, int length)

	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String rawName)
    {
		String eltName = localName;                 
		if ("".equals(eltName))
			eltName = rawName;
		else if("parking".equals(eltName))
		{
			//Log.d("PARSER NAME /PARKING", " --> " + eltName);
			listParkings.add(parking);
		}
		else if("adresse".equals(eltName))
		{
			//Log.d("PARSER NAME /ADRESSE", " --> " + eltName);
			parking.setAdress(str);
		}
		else if("capacite".equals(eltName))
		{
			//Log.d("PARSER NAME /CAPACITE", " --> " + eltName);
			parking.setCapacity(Integer.parseInt(str));
		}
		else if("id".equals(eltName))
		{
			//Log.d("PARSER NAME /ID", " --> " + eltName);
			parking.setId(Integer.parseInt(str));
		}
		else if("nom".equals(eltName))
		{
			//Log.d("PARSER NAME /NOM", " --> " + eltName);
			parking.setName(str);
		}
		else if("places_dispo".equals(eltName))
		{
			//Log.d("PARSER NAME /PLACES_DISPO", " --> " + eltName);
			parking.setAvailabity(Integer.parseInt(str));
		}
		else if("coordonnees".equals(eltName))
		{
			//Log.d("PARSER NAME /COORDONNEES", " --> " + eltName);
			parking.setPoint(new GeoPoint((int)(Double.parseDouble(strLat) * 1E6), (int)(Double.parseDouble(strLon) * 1E6)));
			strLat = "";
			strLon = "";
		}
		else if("latitude".equals(eltName))
		{
			//Log.d("PARSER NAME /LATITUDE", " --> " + eltName);
			strLat = str;
		}
		else if("longitude".equals(eltName))
		{
			//Log.d("PARSER NAME /LONGITUDE", " --> " + eltName);
			strLon = str;
		}
		
		str="";
		//Log.d("PARSER /NAME", " --> " + "</"+eltName+">\n");
	} // endElement(java.lang.String uri, java.lang.String localName, java.lang.String rawName)

    // fin du document
    public void endDocument() 
	{
    	//Log.d("PARSER END", " --> " + "End Document");
    } // endDocument()
    
    public ArrayList<Parking> getListeParkings()
    {
    	return listParkings;
    } // getListeParkings()
    
    public Parking getParking()
    {
    	return parking;
    } // getParking()
} // class SimpleSaxParser