package com.android.tools;

import org.ksoap2.transport.HttpTransportSE;

public class AndroidHttpTransport extends HttpTransportSE
{

	public AndroidHttpTransport(String url) {
		super(url);
		// TODO Auto-generated constructor stub
	}
/*	
	@Override
	protected org.ksoap2.transport.ServiceConnection getServiceConnection() throws IOException
	{
		return  new AndroidServiceConnection(super.url);
	}
*/	
} // class AndroidHttpTransport
