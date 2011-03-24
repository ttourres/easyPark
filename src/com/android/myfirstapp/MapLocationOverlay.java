package com.android.myfirstapp;

import java.util.Iterator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapLocationOverlay extends Overlay {

	private Bitmap bubbleIcon_green, bubbleIcon_orange, bubbleIcon_red, bubbleIcon_yellow, bubbleIcon_blue, shadowIcon;
    private Map vueCarte;
	private Paint innerPaint, borderPaint, textPaint;
    private Parking selectedParking;  
    private RectF infoWindowRect = null;
    
	public MapLocationOverlay(Map vueCarte) {
		
		this.vueCarte = vueCarte;
		
		bubbleIcon_green = BitmapFactory.decodeResource(vueCarte.getResources(),R.drawable.bubble_green);
		bubbleIcon_orange = BitmapFactory.decodeResource(vueCarte.getResources(),R.drawable.bubble_orange);
		bubbleIcon_yellow = BitmapFactory.decodeResource(vueCarte.getResources(),R.drawable.bubble_yellow);
		bubbleIcon_blue = BitmapFactory.decodeResource(vueCarte.getResources(),R.drawable.bubble_blue);
		bubbleIcon_red = BitmapFactory.decodeResource(vueCarte.getResources(),R.drawable.bubble_red);
		
		shadowIcon = BitmapFactory.decodeResource(vueCarte.getResources(),R.drawable.shadow);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView){
		
		
		
		return false;
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView)  {
		
		/*
		//  Store whether prior popup was displayed so we can call invalidate() & remove it if necessary.
		boolean isRemovePriorPopup = selectedParking != null;  

		//  Next test whether a new popup should be displayed
		selectedParking = getHitMapLocation(mapView,p);
		if ( isRemovePriorPopup || selectedParking != null) {
			mapView.getController().setCenter(p);
			mapView.invalidate();
		}		
		
		//  Lastly return true if we handled this onTap()
		return selectedParking != null;
		*/
		
		selectedParking = getHitMapLocation(mapView,p);
		if (selectedParking != null) {
			//mapView.getController().setCenter(p);
		}		
		
		//  Lastly return true if we handled this onTap()
		return true;
		
		
	}
	
    @Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
    	
    	if(vueCarte.myPosition != null){
    		
    		Point screenCoords = new Point();
    		mapView.getProjection().toPixels(vueCarte.myPosition, screenCoords);
    		
    		canvas.drawBitmap(shadowIcon, screenCoords.x, screenCoords.y - shadowIcon.getHeight(),null);
    		canvas.drawBitmap(bubbleIcon_blue, screenCoords.x - bubbleIcon_green.getWidth()/2, screenCoords.y - bubbleIcon_green.getHeight(),null);
    		
    	}
    	
   		drawMapLocations(canvas, mapView, shadow);
   		drawInfoWindow(canvas, mapView, shadow);
   		
    }

    /**
     * Test whether an information balloon should be displayed or a prior balloon hidden.
     */
    private Parking getHitMapLocation(MapView mapView, GeoPoint tapPoint) {
    	
    	//  Track which MapLocation was hit...if any
    	Parking hitMapLocation = null;
		
    	Point screenCoords2 = new Point();
    	mapView.getProjection().toPixels(tapPoint, screenCoords2);
    	if (infoWindowRect != null && infoWindowRect.contains(screenCoords2.x,screenCoords2.y)) {
    		
    		Intent intent = new Intent(vueCarte, InfoParking.class);
			
    		intent = intent.putExtra("name", selectedParking.getName());
    		intent = intent.putExtra("adress", selectedParking.getAdress());
    		intent = intent.putExtra("availabity", selectedParking.getAvailabity());
    		intent = intent.putExtra("capacity", selectedParking.getCapacity());
			
    		
    		
			vueCarte.startActivity(intent);
			selectedParking = null;
			infoWindowRect = null;
			
		}
    	else
    	{
    	
    	infoWindowRect = null;
    	RectF hitTestRecr = new RectF();
		Point screenCoords = new Point();
    	Iterator<Parking> iterator = vueCarte.getParkings().iterator();
    	while(iterator.hasNext()) {
    		Parking p = iterator.next();
    		
    		//  Translate the MapLocation's lat/long coordinates to screen coordinates
    		mapView.getProjection().toPixels(p.getPoint(), screenCoords);

	    	// Create a 'hit' testing Rectangle w/size and coordinates of our icon
	    	// Set the 'hit' testing Rectangle with the size and coordinates of our on screen icon
    		hitTestRecr.set(-bubbleIcon_green.getWidth()/2-10,-bubbleIcon_green.getHeight()-10,bubbleIcon_green.getWidth()/2+10,10);
    		hitTestRecr.offset(screenCoords.x,screenCoords.y);

	    	//  Finally test for a match between our 'hit' Rectangle and the location clicked by the user
    		mapView.getProjection().toPixels(tapPoint, screenCoords);
    		if (hitTestRecr.contains(screenCoords.x,screenCoords.y)) {
    			hitMapLocation = p;
    			break;
    		}
    	}
    	
    	//  Lastly clear the newMouseSelection as it has now been processed
    	tapPoint = null;
    	}
    	
    	return hitMapLocation; 
    }
    
    private void drawMapLocations(Canvas canvas, MapView mapView, boolean shadow) {
    	
		Iterator<Parking> iterator = vueCarte.getParkings().iterator();
		Point screenCoords = new Point();
    	while(iterator.hasNext()) {	   
    		Parking parking = iterator.next();
    		mapView.getProjection().toPixels(parking.getPoint(), screenCoords);
			
	    	if (shadow) {
	    		//  Only offset the shadow in the y-axis as the shadow is angled so the base is at x=0; 
	    		canvas.drawBitmap(shadowIcon, screenCoords.x, screenCoords.y - shadowIcon.getHeight(),null);
	    	} else {
	    		
	    		float dispo = (float) parking.getAvailabity() / parking.getCapacity() * 100;
	    		if(dispo > 25)
	    			canvas.drawBitmap(bubbleIcon_green, screenCoords.x - bubbleIcon_green.getWidth()/2, screenCoords.y - bubbleIcon_green.getHeight(),null);
	    		else if(dispo > 10)
	    			canvas.drawBitmap(bubbleIcon_yellow, screenCoords.x - bubbleIcon_green.getWidth()/2, screenCoords.y - bubbleIcon_green.getHeight(),null);
	    		else
		    		canvas.drawBitmap(bubbleIcon_red, screenCoords.x - bubbleIcon_green.getWidth()/2, screenCoords.y - bubbleIcon_green.getHeight(),null);
		    	
	    	}
    	}
    }

    private void drawInfoWindow(Canvas canvas, MapView	mapView, boolean shadow) {
    	
    	if (selectedParking != null) {
    		
    		
    		if (shadow) {
    			//  Skip painting a shadow in this tutorial
    		} else {
				//  First determine the screen coordinates of the selected MapLocation
				Point selDestinationOffset = new Point();
				mapView.getProjection().toPixels(selectedParking.getPoint(), selDestinationOffset);
		    		
		    	//  Setup the info window with the right size & location
				int INFO_WINDOW_WIDTH = 250;
				int INFO_WINDOW_HEIGHT = 100;
				infoWindowRect = new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);				
				int infoWindowOffsetX = selDestinationOffset.x-INFO_WINDOW_WIDTH/2;
				int infoWindowOffsetY = selDestinationOffset.y-INFO_WINDOW_HEIGHT-bubbleIcon_green.getHeight();
				infoWindowRect.offset(infoWindowOffsetX,infoWindowOffsetY);

				
				
				//  Draw inner info window
				canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
				
				//  Draw border for info window
				canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());
					
				//  Draw the MapLocation's name
				int TEXT_OFFSET_X = 10;
				int TEXT_OFFSET_Y = 30;
				
				
				canvas.drawText(selectedParking.getName(),infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y,getTextPaint());
				canvas.drawText("Cliquer sur le cadre pour plus ",infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y + 30,getTextPaint());
				canvas.drawText("d'infos",infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y + 50,getTextPaint());
				
				
    		}
    		
    	}
    }

	public Paint getInnerPaint() {
		if ( innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); //gray
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}

	public Paint getBorderPaint() {
		if ( borderPaint == null) {
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 255, 255);
			borderPaint.setAntiAlias(true);
			borderPaint.setStyle(Style.STROKE);
			borderPaint.setStrokeWidth(2);
		}
		return borderPaint;
	}

	public Paint getTextPaint() {
		if ( textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
			textPaint.setTextSize(15);
		}
		return textPaint;
	}
}