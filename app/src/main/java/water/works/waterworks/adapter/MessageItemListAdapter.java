package water.works.waterworks.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import water.works.waterworks.R;
import water.works.waterworks.model.MessageItems;


public class MessageItemListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<MessageItems> navDrawerItems;
	
	public MessageItemListAdapter(Context context, ArrayList<MessageItems> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}


	public int getCount() {
		return navDrawerItems.size();
	}


	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
         
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        return convertView;
	}

}
