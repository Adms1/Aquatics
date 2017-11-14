package water.works.waterworks.search;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import water.works.waterworks.R;


public class ListinSearch extends Activity implements OnClickListener,
		OnEditorActionListener, OnItemClickListener {
	ListView mListView;
	MyCustomAdapter mAdapter;
	Button btnSearch, btnLeft;
	EditText mtxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		mListView = (ListView) findViewById(R.id.mListView);
		mAdapter = new MyCustomAdapter(this);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		mtxt = (EditText) findViewById(R.id.edSearch);
		mtxt.addTextChangedListener(new TextWatcher() {


			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}


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

	public static String[] mHeader = { "Spice", "Mango", "Tango", "Hitman" };
	ArrayList<HashMap<String, String>> mAllData;
	ArrayList<HashMap<String, String>> mCacheCopy;

	public void setData() {
		mAdapter = new MyCustomAdapter(this);
		HashMap<String, String> mData = new HashMap<String, String>();
		mData.put("header", "Spice");
		mAdapter.addSeparatorItem(mData);
		Temp mTem = new Temp();
		for (int i = 0; i < 10; i++) {
			mData = new HashMap<String, String>();
			mData.put("header", "Long and Wait set");
			mTem.setData(mData);
			mAdapter.addItem(mData);
		}
		mAllVector.add(mTem);

		mData = new HashMap<String, String>();
		mData.put("header", "Mango");
		mAdapter.addSeparatorItem(mData);
		mTem = new Temp();
		for (int i = 0; i < 10; i++) {
			mData = new HashMap<String, String>();
			mData.put("header", "Hotel Kumar Jai");
			mTem.setData(mData);
			mAdapter.addItem(mData);
		}
		mAllVector.add(mTem);

		mData = new HashMap<String, String>();
		mData.put("header", "Tango");
		mAdapter.addSeparatorItem(mData);
		mTem = new Temp();
		for (int i = 0; i < 10; i++) {
			mData = new HashMap<String, String>();
			mData.put("header", "New Delhi Lounge");
			mTem.setData(mData);
			mAdapter.addItem(mData);
		}
		mAllVector.add(mTem);

		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
	}

	Vector<Temp> mAllVector = new Vector<Temp>();

	private class Temp {
		ArrayList<HashMap<String, String>> mAll = new ArrayList<HashMap<String, String>>();

		public void setData(HashMap<String, String> m) {
			mAll.add(m);
		}

		public ArrayList<HashMap<String, String>> getetData() {
			return mAll;
		}
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
		mAdapter = new MyCustomAdapter(this);
		int i = 0;
		for (Temp temp : mAllVector) {
			switch (i) {
			case 0:
				findArray("Music", str, temp.getetData());
				break;
			case 1:
				findArray("Masti", str, temp.getetData());
				break;
			case 2:
				findArray("Food", str, temp.getetData());
				break;
			}
			i++;
		}
		mListView.setAdapter(mAdapter);
	}

	public void findArray(String header, String str,
			ArrayList<HashMap<String, String>> mAll) {
		boolean isFound = false;
		for (HashMap<String, String> mTemp : mAll) {
			if (mTemp.get("header").toLowerCase().contains(str.toLowerCase())) {
				if (!isFound) {
					HashMap<String, String> head = new HashMap<String, String>();
					head.put("header", header);
					mAdapter.addSeparatorItem(head);
					mAdapter.addItem(mTemp);
					isFound = true;
				} else {
					mAdapter.addItem(mTemp);
				}
			}
		}
	}


	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		return false;
	}


	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		HashMap<String, String> mItem = mAdapter.getItem(position);
		Toast.makeText(this, mItem.get("header"), Toast.LENGTH_LONG).show();
	}
}
