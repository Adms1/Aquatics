package water.works.waterworks.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;

import water.works.waterworks.R;
import water.works.waterworks.ViewCurrentScheduleInstructorActivity;
import water.works.waterworks.model.AllInstructorItems;


public class AllInstructorListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<AllInstructorItems> navDrawerItems;

	public AllInstructorListAdapter(Context context,
			ArrayList<AllInstructorItems> navDrawerItems) {
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
	@Override
	public int getViewTypeCount() {

		return getCount();
	}

	@Override
	public int getItemViewType(int position) {

		return position;
	}
	public class ViewHolder
    {
		TextView txtTitle;
		CheckBox checkbox;
    }


	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item_current, null);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
			holder.checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
			holder.txtTitle.setText(navDrawerItems.get(position).getTitle());
			holder.checkbox
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					try {
						if (isChecked) {
							if (ViewCurrentScheduleInstructorActivity.ClickPos.contains(""
									+ position)) {

							} else {
								ViewCurrentScheduleInstructorActivity.ClickPos
										.add("" + position);
							}
						}else{
							if (ViewCurrentScheduleInstructorActivity.ClickPos.contains(""
									+ position)) {
								ViewCurrentScheduleInstructorActivity.ClickPos.remove(""+position);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}

}
