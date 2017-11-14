package water.works.waterworks.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.util.ArrayList;

import water.works.waterworks.R;
import water.works.waterworks.chat.Chat_Friends_list;
import water.works.waterworks.chat.Chat_Room;
import water.works.waterworks.utils.SOAP_CONSTANTS;


public class SearchFunctionality extends Activity implements OnClickListener,
		OnEditorActionListener, OnItemClickListener {
	ListView mListView;
	MySimpleSearchAdapter mAdapter;
	Button btnSearch, btnLeft;
	EditText mtxt;
	ImageView imgBack;
	Context mContext=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_search);
		mListView = (ListView) findViewById(R.id.mListView);
		mAdapter = new MySimpleSearchAdapter(this);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		mtxt = (EditText) findViewById(R.id.edSearch);
		imgBack = (ImageView)findViewById(R.id.imgBack);
		
		imgBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		mtxt.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			public void afterTextChanged(Editable s) {
				if (0 != mtxt.getText().length()) {
					String spnId = mtxt.getText().toString();
					setSearchResult(spnId);
				} else {
					setData();
				}
			}
		});
		btnLeft.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		setData();
	}

	ArrayList<String> mAllData;

//	String[] str = { "Hit me Hard", "GIJ, Rise Of Cobra", "Troy",
//			"A walk To remember", "DDLJ", "Tom Peter Nmae", "David Miller",
//			"Kings Eleven Punjab", "Kolkata Knight Rider", "Rest of Piece" };

	public void setData() {
		mAllData = new ArrayList<String>();
		mAdapter = new MySimpleSearchAdapter(this);
		for (int i = 0; i < Chat_Friends_list.search_name.size(); i++) {
			mAdapter.addItem(Chat_Friends_list.search_name.get(i));
			mAllData.add(Chat_Friends_list.search_name.get(i));
		}
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearch:
			mtxt.setText("");
			setData();
			break;
		case R.id.btnLeft:

			break;
		}
	}

	public void setSearchResult(String str) {
		mAdapter = new MySimpleSearchAdapter(this);
		for (String temp : mAllData) {
			if (temp.toLowerCase().contains(str.toLowerCase())) {
				mAdapter.addItem(temp);
			}
		}
		mListView.setAdapter(mAdapter);
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		return false;
	}

	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		String str = mAdapter.getItem(position);
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
		
		SOAP_CONSTANTS.From="List";
		String temp_id = Chat_Friends_list.search_name_id.get(str);
		String temp_name = str;
		String chat_channel = "";
		
		if(!temp_id.contains("group")){
			if(Integer.parseInt(Chat_Friends_list.user_id)<Integer.parseInt(temp_id)){
				chat_channel = Chat_Friends_list.user_id+"_"+temp_id;
			}else{
				chat_channel = temp_id+"_"+Chat_Friends_list.user_id;
			}
		}else{
			chat_channel = temp_id;
		}


		System.out.println("Chat_channel = "+chat_channel);
		Intent i = new Intent(mContext, Chat_Room.class);
		i.putExtra("c_chan", chat_channel);
		i.putExtra("uid", Chat_Friends_list.user_id);
		i.putExtra("Touid", temp_id);
		i.putExtra("uname", temp_name);
		startActivity(i);
		finish();
	}
}