package water.works.waterworks.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import water.works.waterworks.MenuActivity;
import water.works.waterworks.R;


public class CustomMenuDataPageAdapter  extends PagerAdapter {
private Activity activity;
private TextView title,content,title2,content2;
public CustomMenuDataPageAdapter(Activity activity){
	this.activity = activity;
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MenuActivity.title2.size();
	}
	
@Override
	public boolean isViewFromObject(View view, Object o) {
		// TODO Auto-generated method stub
		return view == (LinearLayout)o;
	}
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
 
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.swipedata, container, false);
 
        title = (TextView) viewLayout.findViewById(R.id.tv_title1);
        title.setText(MenuActivity.title2.get(position));
        content = (TextView) viewLayout.findViewById(R.id.tv_content1);
        content.setText(MenuActivity.content2.get(position));
        try{
	        for(int i=0;i<=MenuActivity.title2.size();i++){
	        title2 = (TextView) viewLayout.findViewById(R.id.tv_title2);
	        title2.setText(MenuActivity.title1.get(position));
	        content2 = (TextView) viewLayout.findViewById(R.id.tv_content2);
	        content2.setText(MenuActivity.content1.get(position));
	        }
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }
	 @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        ((ViewPager)container).removeView((LinearLayout)object);
	    }
}
