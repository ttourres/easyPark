package com.android.tools;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import android.util.Log;

public class Test
{

	private static final String SOAP_ACTION = "urn:getParkingList";
	private static final String METHODE_NAME = "getParkingList";
	private static final String NAMESPACE = "http://service.pojo";
	private static final String URL = "http://scolin.homeip.net:8080/axis2/services/ServiceParking";
	
	public void executeAppelSOAP()
	{
		SoapObject requete = new SoapObject(NAMESPACE, METHODE_NAME);
		SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		enveloppe.setOutputSoapObject(requete);
		
		AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
		try
		{
			androidHttpTransport.call(SOAP_ACTION, enveloppe);
			Object resultat = enveloppe.getResponse();
			Log.d("Client SOAP", " --> " + resultat.toString());
			Log.d("Client SOAP", " FIN ");
			
		} // try()
		catch( Exception e)
		{
			e.printStackTrace();
		} // catch

	} // executeAppelSOAP()
} // class Test