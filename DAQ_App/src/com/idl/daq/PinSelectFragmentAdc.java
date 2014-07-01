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

public class PinSelectFragmentAdc extends Fragment implements OnTouchListener,OnClickListener{

	private View rootView;
	private Context context;
	CheckBox cb1;
	CheckBox cb2;
	CheckBox cb3;
	CheckBox cb4;
	CheckBox cb5;
	CheckBox cb6;
	CheckBox cb7;
	AdcPinImageView bview;
	String s = "";
	
	Callbacks pinSelectCallbacks;
	
	public interface Callbacks {

		public void sendSelectedPins(String s);
		
		public void openForm();

		public void makeToast(String t);

		public Context getContext();
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.adc_sel, container, false);

		//Toast.makeText(context, "Zoom the highlightd area to select a pin",
			//	Toast.LENGTH_SHORT).show();

		defineAttributes();
		bview.setRectVal(875, 970, 1090, 1270);
		bview.setOnTouchListener(this);
		cb1.setOnClickListener(this);
		cb2.setOnClickListener(this);
		cb3.setOnClickListener(this);
		cb4.setOnClickListener(this);
		cb5.setOnClickListener(this);
		cb6.setOnClickListener(this);
		cb7.setOnClickListener(this);
		setHasOptionsMenu(true);
		allinvisible();
		allcolor();
		bview.setcol(0xa00099ff);
		return rootView;

	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
		bview = (AdcPinImageView) rootView.findViewById(R.id.image);
		cb1 = (CheckBox) rootView.findViewById(R.id.chk_box1);
		cb2 = (CheckBox) rootView.findViewById(R.id.chk_box2);
		cb3 = (CheckBox) rootView.findViewById(R.id.chk_box3);
		cb4 = (CheckBox) rootView.findViewById(R.id.chk_box4);
		cb5 = (CheckBox) rootView.findViewById(R.id.chk_box5);
		cb6 = (CheckBox) rootView.findViewById(R.id.chk_box6);
		cb7 = (CheckBox) rootView.findViewById(R.id.chk_box7);
		
		context = pinSelectCallbacks.getContext();
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
		L.d("pin select fragment attached!");
		pinSelectCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.form_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == R.id.done) {
			if (s != "") {
				pinSelectCallbacks.sendSelectedPins(s);
				pinSelectCallbacks.openForm();
			} else
				pinSelectCallbacks.makeToast("Please select a pin");
					//	Toast.LENGTH_SHORT).show();

		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (bview.getScale() > 1.25) {
			allvisible();
			cb1.setX(bview.getX(1100));
			cb1.setY(bview.getY(930));

			cb2.setX(bview.getX(1140));
			cb2.setY(bview.getY(930));

			cb3.setX(bview.getX(1140));
			cb3.setY(bview.getY(890));

			cb4.setX(bview.getX(1180));
			cb4.setY(bview.getY(930));

			cb5.setX(bview.getX(1180));
			cb5.setY(bview.getY(890));

			cb6.setX(bview.getX(1220));
			cb6.setY(bview.getY(930));

			cb7.setX(bview.getX(1220));
			cb7.setY(bview.getY(890));

		} else
			allinvisible();
		return false;

	}


	public void allvisible() {
		cb1.setVisibility(View.VISIBLE);
		cb2.setVisibility(View.VISIBLE);
		cb3.setVisibility(View.VISIBLE);
		cb4.setVisibility(View.VISIBLE);
		cb5.setVisibility(View.VISIBLE);
		cb6.setVisibility(View.VISIBLE);
		cb7.setVisibility(View.VISIBLE);
	}

	public void allinvisible() {
		cb1.setVisibility(View.INVISIBLE);
		cb2.setVisibility(View.INVISIBLE);
		cb3.setVisibility(View.INVISIBLE);
		cb4.setVisibility(View.INVISIBLE);
		cb5.setVisibility(View.INVISIBLE);
		cb6.setVisibility(View.INVISIBLE);
		cb7.setVisibility(View.INVISIBLE);
	}

	public void allcolor() {
		cb1.setBackgroundColor(0xaa0099ff);
		cb2.setBackgroundColor(0xaa0099ff);
		cb3.setBackgroundColor(0xaa0099ff);
		cb4.setBackgroundColor(0xaa0099ff);
		cb5.setBackgroundColor(0xaa0099ff);
		cb6.setBackgroundColor(0xaa0099ff);
		cb7.setBackgroundColor(0xaa0099ff);
	}

	public void alluncheck() {
		cb1.setChecked(false);
		cb2.setChecked(false);
		cb3.setChecked(false);
		cb4.setChecked(false);
		cb5.setChecked(false);
		cb6.setChecked(false);
		cb7.setChecked(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CheckBox tmpChkBox = (CheckBox) rootView.findViewById(v.getId());
		if (tmpChkBox.isChecked() && tmpChkBox.getVisibility() == View.VISIBLE) {

			alluncheck();
			allcolor();
			tmpChkBox.setChecked(true);
			tmpChkBox.setBackgroundColor(0xffff0000);
			bview.setcol(0xa0ff0000);
			switch (v.getId()) {
			case R.id.chk_box1:
				s = "P9_33";
				break;
			case R.id.chk_box2:
				s = "P9_35";
				break;
			case R.id.chk_box3:
				s = "P9_36";
				break;
			case R.id.chk_box4:
				s = "P9_37";
				break;
			case R.id.chk_box5:
				s = "P9_38";
				break;
			case R.id.chk_box6:
				s = "P9_39";
				break;
			case R.id.chk_box7:
				s = "P9_40";
				break;

			}
			pinSelectCallbacks.makeToast(s);
				//	.show();
		} else {
			bview.setcol(0xa00099ff);
			tmpChkBox.setBackgroundColor(0xaa0099ff);
			s = "";
		}
		
	}

 //     public void onBackPressed() {
//		Intent intent = new Intent();
//		intent.putExtra("done", s);
//		setResult(RESULT_OK, intent);
//		finish();
//	}

}
