package com.android.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ParkingAdapter extends BaseAdapter{

	private List <Parking> listParking;
	private Context mContext;
	private LayoutInflater mInflater;

	
	public ParkingAdapter(Context context, List<Parking> listP) {
		mContext = context;
		listParking = listP;
		mInflater = LayoutInflater.from(mContext);
	}
	
	public int getCount() {
		return listParking.size();
	}

	public Object getItem(int position) {
		return listParking.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layoutItem;
		  //(1) : Réutilisation des layouts
		if (convertView == null) {
		  	//Initialisation de notre item à partir du  layout XML "personne_layout.xml"
		layoutItem = (LinearLayout) mInflater.inflate(R.layout.list_item, parent, false);
		}
		else
		{
			layoutItem = (LinearLayout) convertView;
		}
		  
		  // Récupération des TextView de notre layout      
		TextView tv_Nom = (TextView)layoutItem.findViewById(R.id.TV_Nom);
		        
		  // Renseignement des valeurs       
		tv_Nom.setText(listParking.get(position).getName());
 
		tv_Nom.setTag(position);
		  //On ajoute un listener
		
		tv_Nom.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Lorsque l'on clique sur le nom, on récupère la position de la "Personne"
				Integer position = (Integer)v.getTag();
						
				//On prévient les listeners qu'il y a eu un clic sur le TextView "TV_Nom".
				sendListener(listParking.get(position), position);			
			}    	
		});
		
		//On retourne l'item créé.
		return layoutItem;

	}

	public interface ParkingAdapterListener {
	    public void onClickNom(Parking item, int position);
	}
	
	private ArrayList<ParkingAdapterListener> mListListener = new ArrayList<ParkingAdapterListener>();
	/**
	 * Pour ajouter un listener sur notre adapter
	 */
	public void addListener(ParkingAdapterListener pListener) {
	    mListListener.add(pListener);
	}
	
	private void sendListener(Parking item, int position) {
	    for(int i = mListListener.size()-1; i >= 0; i--) {
	    	mListListener.get(i).onClickNom(item, position);
	    }
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
