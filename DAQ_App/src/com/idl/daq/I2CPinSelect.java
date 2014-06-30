package com.idl.daq;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

public class I2CPinSelect extends Fragment implements OnTouchListener,
		OnClickListener {

	
	View rootView;
	CheckBox cb1;
	CheckBox cb2;
	CheckBox cb3;
	CheckBox cb4;

	I2cImageView bview;
	String s1 = "";
	String s2 = "";
	String s3 = "";

	Callbacks pinSelectCallbacks;

	public interface Callbacks {

		public void sendSelectedPins(String s);

		public void openForm();

		public void makeToast(String t);

		public Context getContext();

	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		setRetainInstance(true);
		L.d("i2c fragment attached!");
		pinSelectCallbacks = (Callbacks) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.i2c_sel, container, false);
		
		Toast.makeText(getActivity(),"Touch to select a pin",
		Toast.LENGTH_SHORT).show();
		
		bview = (I2cImageView) rootView.findViewById(R.id.image);
		bview.setOnTouchListener(this);
		cb1 = (CheckBox) rootView.findViewById(R.id.chk_box1);
		cb2 = (CheckBox) rootView.findViewById(R.id.chk_box2);
		cb3 = (CheckBox) rootView.findViewById(R.id.chk_box3);
		cb4 = (CheckBox) rootView.findViewById(R.id.chk_box4);
		
		cb1.setOnClickListener(this);
		cb2.setOnClickListener(this);
		cb3.setOnClickListener(this);
		cb4.setOnClickListener(this);
		
		allinvisible();
		allcolor();
		bview.setcol1(0xa00099ff);
		bview.setcol2(0xaa00ff00);
		setHasOptionsMenu(true);
		return rootView;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.form_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.done) {
			if (s1 != "") {
				s1 = s1 + ":" + s2 + ":" + s3;
				L.d(s1);
				pinSelectCallbacks.sendSelectedPins(s1);
				pinSelectCallbacks.openForm();
				// setResult(RESULT_OK, intent);
				// finish();
			} else
				pinSelectCallbacks.makeToast("Please select a pin");
			// Toast.makeText(getApplicationContext(),
			// "Please select the pins", Toast.LENGTH_SHORT).show();

		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (bview.getScale() > 1.15) {
			allvisible();
			// I2c1
			cb1.setX(bview.getX(920));// scl
			cb1.setY(bview.getY(890));

			cb2.setX(bview.getX(880));// sda
			cb2.setY(bview.getY(890));
			// i2c2
			cb3.setX(bview.getX(800));// scl
			cb3.setY(bview.getY(890));

			cb4.setX(bview.getX(800));// sda
			cb4.setY(bview.getY(930));

		} else
			allinvisible();
		return false;
	}

	public void handleCheckBoxClick(View view) {

	}

	public void allvisible() {
		cb1.setVisibility(View.VISIBLE);
		cb2.setVisibility(View.VISIBLE);
		cb3.setVisibility(View.VISIBLE);
		cb4.setVisibility(View.VISIBLE);
	}

	public void allinvisible() {
		cb1.setVisibility(View.INVISIBLE);
		cb2.setVisibility(View.INVISIBLE);
		cb3.setVisibility(View.INVISIBLE);
		cb4.setVisibility(View.INVISIBLE);
	}

	public void allcolor() {
		cb1.setBackgroundColor(0xaa0099ff);
		cb2.setBackgroundColor(0xaa0099ff);
		cb3.setBackgroundColor(0xaa00ff00);
		cb4.setBackgroundColor(0xaa00ff00);
	}

	public void alluncheck() {
		cb1.setChecked(false);
		cb2.setChecked(false);
		cb3.setChecked(false);
		cb4.setChecked(false);
	}

	public void onBackPressed() {
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		CheckBox tmpChkBox = (CheckBox) rootView.findViewById(view.getId());
		if (tmpChkBox.isChecked() && tmpChkBox.getVisibility() == View.VISIBLE) {

			alluncheck();
			allcolor();
			tmpChkBox.setChecked(true);
			tmpChkBox.setBackgroundColor(0xffff0000);
			switch (view.getId()) {
			case R.id.chk_box1:
				s1 = "I2C1";
				s2 = "P9_24";
				s3 = "P9_26";
				cb2.setChecked(true);
				cb2.setBackgroundColor(0xffff0000);
				bview.setcol1(0xa0ff0000);
				Toast.makeText(getActivity(), s1 + " scl",
				Toast.LENGTH_SHORT).show();
				break;
			case R.id.chk_box2:
				s1 = "I2C1";
				s2 = "P9_24";
				s3 = "P9_26";
				cb1.setChecked(true);
				cb1.setBackgroundColor(0xffff0000);
				bview.setcol1(0xa0ff0000);
				 Toast.makeText(getActivity(), s1 + " sda",
				 Toast.LENGTH_SHORT).show();
				break;
			case R.id.chk_box3:
				s1 = "I2C2";
				s2 = "P9_19";
				s3 = "P9_20";
				cb4.setChecked(true);
				cb4.setBackgroundColor(0xffff0000);
				bview.setcol2(0xa0ff0000);
				Toast.makeText(getActivity(), s1 + " scl",
				Toast.LENGTH_SHORT).show();
				break;
			case R.id.chk_box4:
				s1 = "I2C2";
				s2 = "P9_19";
				s3 = "P9_20";
				cb3.setChecked(true);
				cb3.setBackgroundColor(0xffff0000);
				bview.setcol2(0xa0ff0000);
				Toast.makeText(getActivity(), s1 + " sda",
				Toast.LENGTH_SHORT).show();
				break;

			}
			bview.setcol1(0xa0ff0000);
			bview.setcol2(0xa0ff0000);

		} else {
			bview.setcol1(0xa00099ff);
			bview.setcol2(0xaa00ff00);
			s1 = "";
			switch (view.getId()) {
			case R.id.chk_box1:
				cb2.setChecked(false);
				cb2.setBackgroundColor(0xaa0099ff);
				cb1.setBackgroundColor(0xaa0099ff);
				break;
			case R.id.chk_box2:
				cb1.setChecked(false);
				cb1.setBackgroundColor(0xaa0099ff);
				cb2.setBackgroundColor(0xaa0099ff);
				break;
			case R.id.chk_box3:
				cb4.setChecked(false);
				cb4.setBackgroundColor(0xaa00ff00);
				cb3.setBackgroundColor(0xaa00ff00);
				break;
			case R.id.chk_box4:
				cb3.setChecked(false);
				cb3.setBackgroundColor(0xaa00ff00);
				cb4.setBackgroundColor(0xaa00ff00);
				break;
			}
		}
	}
}
