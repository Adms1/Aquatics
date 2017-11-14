package water.works.waterworks.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import water.works.waterworks.R;


public class HomeGridMenuOptionAdapter extends BaseAdapter {
	 
	 private Context mContext;
	 private final String[] web;
	 private final int[] Imageid;
	  
	 public HomeGridMenuOptionAdapter(Context c,String[] web,int[] Imageid ) {
	      mContext = c;
	      this.Imageid = Imageid;
	      this.web = web;
	  }
	
	    public int getCount() {
	      // TODO Auto-generated method stub
	      return web.length;
	    }
	    
	    public Object getItem(int position) {
	      // TODO Auto-generated method stub
	      return web[position];
	    }

	    public long getItemId(int position) {
	      // TODO Auto-generated method stub
	      return Imageid[position];
	    }
	    public class Holder{
	    	TextView textView;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	      // TODO Auto-generated method stub
	      View grid;
	      LayoutInflater inflater = (LayoutInflater) mContext
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      Holder h;
	          if (convertView == null) {
	            grid = new View(mContext);
	            h  = new Holder();
	            grid = inflater.inflate(R.layout.home_grid_item, null);
	            
	            h.textView = (TextView) grid.findViewById(R.id.text);
	            h.textView.setText(web[position]);
	            h.textView.setCompoundDrawablesWithIntrinsicBounds(0, Imageid[position], 0, 0);
	          } else {
	            grid = (View) convertView;
	          }
	      return grid;
	    }
}
