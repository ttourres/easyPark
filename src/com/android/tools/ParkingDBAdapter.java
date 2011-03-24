package com.android.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.android.myfirstapp.Parking;
import com.google.android.maps.GeoPoint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class ParkingDBAdapter
{
	private static final String SOAP_ACTION_1 = "urn:getParkingList";
	private static final String METHODE_NAME_1 = "getParkingList";
	//private static final String SOAP_ACTION_2 = "urn:getParking";
	//private static final String METHODE_NAME_2 = "getParking";
	private static final String SOAP_ACTION_3 = "urn:getPlacesDisponibles";
	private static final String METHODE_NAME_3 = "getPlacesDisponibles";
	private static final String SOAP_ACTION_4 = "urn:getVersion";
	private static final String METHODE_NAME_4 = "getVersion";
	private static final String NAMESPACE = "http://service.pojo";
	private static final String URL = "http://scolin.homeip.net:8080/axis2/services/ServiceParking";
	public static final String TABLE_EASYPARK = "easyPark";
	public static final String COLUMN_ID = "id";
	public static final int COLUMN_ID_ID = 0;
	public static final String COLUMN_NAME = "name";
	public static final int COLUMN_NAME_ID = 1;
	public static final String COLUMN_ADRESS = "adress";
	public static final int COLUMN_ADRESS_ID = 2;
	public static final String COLUMN_CAPACITY = "capacity";
	public static final int COLUMN_CAPACITY_ID = 3;
	public static final String COLUMN_AVAILABILITY = "availabity";
	public static final int COLUMN_AVAILABILITY_ID = 4;
	public static final String COLUMN_COOR_LAT = "coordLat";
	public static final int COLUMN_COOR_LAT_ID = 5;
	public static final String COLUMN_COOR_LON = "coordLon";
	public static final int COLUMN_COOR_LON_ID = 6;
	private SQLiteDatabase myDB;
	private MyDbSqlite myDBHelper;
	
	// Constructeur
	public ParkingDBAdapter(Context ctx)
	{
		// obtenir la version actuelle du serveur
		int version = getVersion();
		myDBHelper = new MyDbSqlite(ctx, TABLE_EASYPARK, null, version);
	} // ParkingDBAdapter(Context ctx)
	
	// Ouverture de la BD en lecture
	public SQLiteDatabase openR()
	{
		Log.w("Client SOAP", " --> open ");
		
		myDB = myDBHelper.getReadableDatabase();
//		myDBHelper.initDB(myDB);
		return myDB;
	} // openR()
	
	// Ouverture de la BD en écriture
	public SQLiteDatabase openW()
	{
		myDB = myDBHelper.getWritableDatabase();
//		myDBHelper.initDB(myDB);
		return myDB;
	} // openR()
	
	// Fermeture de la BD
	public void close()
	{
		myDB.close();
	} // close()

	public SQLiteDatabase getMyDB() {
		return myDB;
	}

	public void setMyDB(SQLiteDatabase myDB) {
		this.myDB = myDB;
	}

	public MyDbSqlite getMyDBHelper() {
		return myDBHelper;
	}

	public void setMyDBHelper(MyDbSqlite myDBHelper) {
		this.myDBHelper = myDBHelper;
	}
	
	// retourne la liste de tous les parkings
	public ArrayList<Parking> getParkings()
	{
		Cursor c = myDB.query(TABLE_EASYPARK, null, null, null, null, null, COLUMN_NAME);
		return cursorToParkings(c);
	} // getParkings()
	
	// retourne un parking en fonction de son ID
	public Parking getParking(int id)
	{
		Cursor c = myDB.query(TABLE_EASYPARK, null, COLUMN_ID + " = " + id, null, null, null, null);
		return cursorToParking(c);
	} // getParkings()
	
	public void refresh()
	{
		this.myDBHelper.onUpdate(myDB);
	} // refresh()
	
	private int getVersion()
	{
		String result = "";
		SoapObject requete = new SoapObject(NAMESPACE, METHODE_NAME_4);
		SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		enveloppe.setOutputSoapObject(requete);
		AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
		try
		{
			androidHttpTransport.call(SOAP_ACTION_4, enveloppe);
			Object resultat = enveloppe.getResponse();
			result = resultat.toString();
			result = result.substring(result.indexOf("<v>")+3, result.indexOf("</v>"));
			Log.d("Client SOAP", " --> Version " + result);
		} // try()
		catch( Exception e)
		{
			e.printStackTrace();
		} // catch
		return Integer.parseInt(result);
	} // getVersion()
	
	private ArrayList<Parking> cursorToParkings(Cursor c)
	{
		if(c.getCount() == 0)
			return null;
		ArrayList<Parking> listParkings = new ArrayList<Parking>(c.getCount());
		c.moveToFirst();
		do
		{
			Parking p = new Parking();
			p.setId(c.getInt(COLUMN_ID_ID));
			p.setName(c.getString(COLUMN_NAME_ID));
			p.setAdress(c.getString(COLUMN_ADRESS_ID));
			p.setCapacity(c.getInt(COLUMN_CAPACITY_ID));
			p.setAvailabity(c.getInt(COLUMN_AVAILABILITY_ID));
			p.setPoint(new GeoPoint(c.getInt(COLUMN_COOR_LAT_ID), c.getInt(COLUMN_COOR_LON_ID)));
			listParkings.add(p);
		}
		while(c.moveToNext());
		// fermer le cursor
		c.close();
		return listParkings;
	} // cursorToParkings(Cursor c)
	
	private Parking cursorToParking(Cursor c)
	{
		if(c.getCount() == 0)
			return null;
		Parking p = new Parking();
		p.setId(c.getInt(COLUMN_ID_ID));
		p.setName(c.getString(COLUMN_NAME_ID));
		p.setAdress(c.getString(COLUMN_ADRESS_ID));
		p.setCapacity(c.getInt(COLUMN_CAPACITY_ID));
		p.setAvailabity(c.getInt(COLUMN_AVAILABILITY_ID));
		p.setPoint(new GeoPoint(c.getInt(COLUMN_COOR_LAT_ID), c.getInt(COLUMN_COOR_LON_ID)));
		return p;
	} // cursorToParking(Cursor c)
	
	private class MyDbSqlite extends SQLiteOpenHelper
	{
		private ArrayList<Parking> listeParkings;
		private ArrayList<Parking> listeParkingsDispo;
		//private Parking parking;
		
		// obtenir les informations sur la liste des parkings
		private void executeAppelSOAP_1()
		{
			SoapObject requete = new SoapObject(NAMESPACE, METHODE_NAME_1);
			SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			enveloppe.setOutputSoapObject(requete);
			AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
			try
			{
				androidHttpTransport.call(SOAP_ACTION_1, enveloppe);
				Object resultat = enveloppe.getResponse();
				String result = resultat.toString();
				//Log.d("Client SOAP", " --> " + resultat.toString());
				// creation du xmlreader
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = null;
				sp = spf.newSAXParser();
				XMLReader xmlReader = null;
				xmlReader = sp.getXMLReader();
				// create an input stream
				InputStream xmlIn = new ByteArrayInputStream(result.getBytes());
				InputSource s = new InputSource(xmlIn);
				// createion du SimpleSaxParser pour recuperer la liste des parkings
				SimpleSaxParser sSaxP = new SimpleSaxParser();
				xmlReader.setContentHandler(sSaxP);
				//Log.d("XMLREADER", "start parse"); 
				xmlReader.parse(s);
				//Log.d("XMLREADER", "stop parse");
				listeParkings = sSaxP.getListeParkings();
			} // try()
			catch( Exception e)
			{
				e.printStackTrace();
			} // catch
		} // executeAppelSOAP_1()
		
		/*
		// obtenir les informations sur un parking en fonction de son ID
		private void executeAppelSOAP_2()
		{
			SoapObject requete = new SoapObject(NAMESPACE, METHODE_NAME_2);
			SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			enveloppe.setOutputSoapObject(requete);
			AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
			try
			{
				androidHttpTransport.call(SOAP_ACTION_2, enveloppe);
				Object resultat = enveloppe.getResponse();
				String resutl = resultat.toString();
				//Log.d("Client SOAP", " --> " + resultat.toString());
				// creation du xmlreader
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = null;
				sp = spf.newSAXParser();
				XMLReader xmlReader = null;
				xmlReader = sp.getXMLReader();
				// create an input stream
				InputStream xmlIn = new ByteArrayInputStream(resutl.getBytes());
				InputSource s = new InputSource(xmlIn);
				// createion du SimpleSaxParser pour recuperer la liste des parkings
				SimpleSaxParser sSaxP = new SimpleSaxParser();
				xmlReader.setContentHandler(sSaxP);
				//Log.d("XMLREADER", "start parse"); 
				xmlReader.parse(s);
				//Log.d("XMLREADER", "stop parse");
				parking = sSaxP.getParking();
			} // try()
			catch( Exception e)
			{
				e.printStackTrace();
			} // catch
		} // executeAppelSOAP_2()
		*/
		
		// obtenir les informations sur la disponibilité des places des parkings
		private void executeAppelSOAP_3()
		{
			SoapObject requete = new SoapObject(NAMESPACE, METHODE_NAME_3);
			SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			enveloppe.setOutputSoapObject(requete);
			AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
			try
			{
				androidHttpTransport.call(SOAP_ACTION_3, enveloppe);
				Object resultat = enveloppe.getResponse();
				String result = resultat.toString();
				//Log.d("Client SOAP", " --> " + resultat.toString());
				// creation du xmlreader
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = null;
				sp = spf.newSAXParser();
				XMLReader xmlReader = null;
				xmlReader = sp.getXMLReader();
				// create an input stream
				InputStream xmlIn = new ByteArrayInputStream(result.getBytes());
				InputSource s = new InputSource(xmlIn);
				// createion du SimpleSaxParser pour recuperer la liste des parkings
				SimpleSaxParser sSaxP = new SimpleSaxParser();
				xmlReader.setContentHandler(sSaxP);
				//Log.d("XMLREADER", "start parse"); 
				xmlReader.parse(s);
				//Log.d("XMLREADER", "stop parse");
				listeParkingsDispo = sSaxP.getListeParkings();
			} // try()
			catch( Exception e)
			{
				e.printStackTrace();
			} // catch
		} // executeAppelSOAP_3()
		
		
		private static final String CREATE_DB = "CREATE TABLE " + TABLE_EASYPARK + " (" + 
													COLUMN_ID + " integer primary key, " +
													COLUMN_NAME + " text not null, " +
													COLUMN_ADRESS + " text not null, " +
													COLUMN_CAPACITY + " integer, " +
													COLUMN_AVAILABILITY + " integer, " +
													COLUMN_COOR_LAT + " integer, " +
													COLUMN_COOR_LON + " integer);";

		public MyDbSqlite(Context context, String name, CursorFactory factory, int version)
		{
			super(context, name, factory, version);
		} // MyDbSqlite(Context context, String name, CursorFactory factory, int version)

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			Log.w("Client SOAP", " --> onCreate ");
			// Connexion au server pour avoir la list de tous les parkings
			executeAppelSOAP_1();
			Log.w("Client SOAP", " --> soap1 ");
			
			// création de la BD
			db.execSQL(CREATE_DB);
			Log.w("Client SOAP", " --> exec ");
			
			// insertion des valeurs dans la BD
			ContentValues myVals = new ContentValues();
			Log.w("Client SOAP", " --> content ");
			//Log.w("Client SOAP", " --> " + listeParkings.isEmpty());
			if(listeParkings != null){
				for (Parking p : listeParkings)
				{
					myVals.put(COLUMN_ID,p.getId());
					myVals.put(COLUMN_NAME,p.getName());
					myVals.put(COLUMN_ADRESS,p.getAdress());
					myVals.put(COLUMN_CAPACITY,p.getCapacity());
					myVals.put(COLUMN_AVAILABILITY,p.getAvailabity());
					myVals.put(COLUMN_COOR_LAT,p.getPoint().getLatitudeE6());
					myVals.put(COLUMN_COOR_LON,p.getPoint().getLongitudeE6());
					db.insert(TABLE_EASYPARK, null, myVals);
					myVals.clear();
				} // for()
			}
		} // onCreate(SQLiteDatabase db)

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w("Client SOAP", " --> onUpgrade database from version " +
						oldVersion + " to " + newVersion +
						", which will destroy all old data");
			// suppression de la BD
			db.execSQL("DROP TABLE " + TABLE_EASYPARK + ";");
			onCreate(db);
		} // onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		
		public void onUpdate(SQLiteDatabase db)
		{
			Log.w("Client SOAP", " --> onUpdate ");
			// Connexion au server pour avoir la list du couple (ID, PLACE_DISPO) de tous les parkings
			executeAppelSOAP_3();
			// update des valeurs dans la BD
			ContentValues myVals = new ContentValues();
			for (Parking p : listeParkingsDispo)
			{
				myVals.put(COLUMN_AVAILABILITY,p.getAvailabity());
				db.update(TABLE_EASYPARK, myVals, COLUMN_ID + " = " + p.getId(), null);
				myVals.clear();
			} // for()
		} // onUpdate(SQLiteDatabase db)

	} // class MyDbSqlite
	
} // class ParkingDBAdapter