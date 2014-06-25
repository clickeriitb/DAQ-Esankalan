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

public class PinSelectFragmentUart extends Fragment implements OnTouchListener,OnClickListener{

	private View rootView;
	private Context context;
	CheckBox cb1;
	CheckBox cb2;
	CheckBox cb3;
	CheckBox cb4;
	CheckBox cb5;
	CheckBox cb6;
	CheckBox cb7;
	CheckBox cb8;
	UartPinImageView bview;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.uart_sel, container, false);

		//Toast.makeText(context, "Zoom the highlightd area to select a pin",
			//	Toast.LENGTH_SHORT).show();

		defineAttributes();
		//bview.setRectVal(875, 970, 1090, 1270);
		bview.setOnTouchListener(this);
		cb1.setOnClickListener(this);
		cb2.setOnClickListener(this);
		cb3.setOnClickListener(this);
		cb4.setOnClickListener(this);
		cb5.setOnClickListener(this);
		cb6.setOnClickListener(this);
		cb7.setOnClickListener(this);
		cb8.setOnClickListener(this);
		setHasOptionsMenu(true);
		allinvisible();
		allcolor();
		bview.setcol1(0xa00099ff);
		bview.setcol2(0xaa00ff00);
		bview.setcol3(0xaaff00ff);
		bview.setcol4(0xaaffff00);
		return rootView;

	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
		bview = (UartPinImageView) rootView.findViewById(R.id.image);
		cb1 = (CheckBox) rootView.findViewById(R.id.chk_box1);
		cb2 = (CheckBox) rootView.findViewById(R.id.chk_box2);
		cb3 = (CheckBox) rootView.findViewById(R.id.chk_box3);
		cb4 = (CheckBox) rootView.findViewById(R.id.chk_box4);
		cb5 = (CheckBox) rootView.findViewById(R.id.chk_box5);
		cb6 = (CheckBox) rootView.findViewById(R.id.chk_box6);
		cb7 = (CheckBox) rootView.findViewById(R.id.chk_box7);
		cb8=  (CheckBox) rootView.findViewById(R.id.chk_box8);
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
//				Intent intent=new Intent();
//		    intent.putExtra("uart",s1);
//		    intent.putExtra("rx",s2);
//		    intent.putExtra("tx",s3);
			if (s1 != ""){//&& s2 != "" && s3 != "") {
				pinSelectCallbacks.sendSelectedPins(s1);// + ":" + s2 + ":" + s3);
				pinSelectCallbacks.openForm();
			} else;
				//Toast.makeText(context, "Please select a pin",
					//	Toast.LENGTH_SHORT).show();

		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
				if(bview.getScale()>1.15){
					 allvisible();
					 //uart1
					 cb1.setX(bview.getX(920));//rx
					 cb1.setY(bview.getY(890));
					 
					 cb2.setX(bview.getX(880));//tx
					 cb2.setY(bview.getY(890));
					 //uart2
					 cb3.setX(bview.getX(840));//rx
					 cb3.setY(bview.getY(890));
					 
					 cb4.setX(bview.getX(840));//tx
					 cb4.setY(bview.getY(930));
					 //uart4
					 cb5.setX(bview.getX(665));//tx
					 cb5.setY(bview.getY(930));
					 
					 cb6.setX(bview.getX(625));//rx
					 cb6.setY(bview.getY(930));
					 //uart5
					 
					 cb7.setX(bview.getX(1185));//rx
					 cb7.setY(bview.getY(70));
					 
					 cb8.setX(bview.getX(1185));//tx
					 cb8.setY(bview.getY(110));
				 
				}
				else
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
		cb8.setVisibility(View.VISIBLE);
	}

	public void allinvisible() {
		cb1.setVisibility(View.INVISIBLE);
		cb2.setVisibility(View.INVISIBLE);
		cb3.setVisibility(View.INVISIBLE);
		cb4.setVisibility(View.INVISIBLE);
		cb5.setVisibility(View.INVISIBLE);
		cb6.setVisibility(View.INVISIBLE);
		cb7.setVisibility(View.INVISIBLE);
		cb8.setVisibility(View.INVISIBLE);
	}

	public void allcolor() {
		cb1.setBackgroundColor(0xaa0099ff);
		cb2.setBackgroundColor(0xaa0099ff);
		cb3.setBackgroundColor(0xaa0099ff);
		cb4.setBackgroundColor(0xaa0099ff);
		cb5.setBackgroundColor(0xaa0099ff);
		cb6.setBackgroundColor(0xaa0099ff);
		cb7.setBackgroundColor(0xaa0099ff);
		cb8.setBackgroundColor(0xaa0099ff);
	}

	public void alluncheck() {
		cb1.setChecked(false);
		cb2.setChecked(false);
		cb3.setChecked(false);
		cb4.setChecked(false);
		cb5.setChecked(false);
		cb6.setChecked(false);
		cb7.setChecked(false);
		cb8.setChecked(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CheckBox tmpChkBox = (CheckBox) rootView.findViewById(v.getId());
		if (tmpChkBox.isChecked() && tmpChkBox.getVisibility() == View.VISIBLE){
			alluncheck();
	    	allcolor();
	        tmpChkBox.setChecked(true);
	        tmpChkBox.setBackgroundColor(0xffff0000);
	        switch(v.getId())
		    {
		    case R.id.chk_box1: s1="UART1";
		    s2="P9_26";
		    s3="P9_24";
		    cb2.setChecked(true);
		    cb2.setBackgroundColor(0xffff0000);
		    bview.setcol1(0xa0ff0000);
		    //Toast.makeText(getApplicationContext(),s1+" Rx", Toast.LENGTH_SHORT).show();
		    break;
		    case R.id.chk_box2: s1="UART1";
		    s2="P9_26";
		    s3="P9_24";
		    cb1.setChecked(true);
		    cb1.setBackgroundColor(0xffff0000);
		    bview.setcol1(0xa0ff0000);
		    //Toast.makeText(getApplicationContext(),s1+" Tx", Toast.LENGTH_SHORT).show();
			break;
		    case R.id.chk_box3: s1="UART2";
		    s2="P9_22";
		    s3="P9_21";
		    cb4.setChecked(true);
		    cb4.setBackgroundColor(0xffff0000);
		    bview.setcol2(0xa0ff0000);
			//Toast.makeText(getApplicationContext(),s1+" Rx", Toast.LENGTH_SHORT).show();
			break;
		    case R.id.chk_box4: s1="UART2";
		    s2="P9_22";
		    s3="P9_21";
		    cb3.setChecked(true);
		    cb3.setBackgroundColor(0xffff0000);
		    bview.setcol2(0xa0ff0000);
		    //Toast.makeText(getApplicationContext(),s1+" Tx", Toast.LENGTH_SHORT).show();
		    break;
		    case R.id.chk_box5: s1="UART4";
		    s2="P9_11";
		    s3="P9_13";
		    cb6.setChecked(true);
		    cb6.setBackgroundColor(0xffff0000);
		    bview.setcol3(0xa0ff0000);
		    //Toast.makeText(getApplicationContext(),s1+" Tx", Toast.LENGTH_SHORT).show();
		    break;
		    case R.id.chk_box6: s1="UART4";
		    s2="P9_11";
		    s3="P9_13";
		    cb5.setChecked(true);
		    cb5.setBackgroundColor(0xffff0000);
		    bview.setcol3(0xa0ff0000);
		    //Toast.makeText(getApplicationContext(),s1+" Rx", Toast.LENGTH_SHORT).show();
		    break;
		    
		    case R.id.chk_box7: s1="UART5";
		    s2="P9_38";
		    s3="P9_37";
		    cb8.setChecked(true);
		    cb8.setBackgroundColor(0xffff0000);
		    bview.setcol4(0xa0ff0000);
		    //Toast.makeText(getApplicationContext(),s1+" Rx", Toast.LENGTH_SHORT).show();
		    break;
		    case R.id.chk_box8: s1="UART5";
		    s2="P9_38";
		    s3="P9_37";
		    cb7.setChecked(true);
		    cb7.setBackgroundColor(0xffff0000);
		    bview.setcol4(0xa0ff0000);
		    //Toast.makeText(getApplicationContext(),s1+" Tx", Toast.LENGTH_SHORT).show();
		    break;
		   
		    }
	        bview.setcol1(0xa0ff0000);
	        bview.setcol2(0xa0ff0000);
	        bview.setcol3(0xa0ff0000);
	        bview.setcol4(0xa0ff0000);
		}
		else{
	    	bview.setcol1(0xa00099ff);
			bview.setcol2(0xaa00ff00);
			bview.setcol3(0xaaff00ff);
			bview.setcol4(0xaaffff00);
	        s1="";
	        switch(v.getId())
		    {
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
		    case R.id.chk_box5: 
		    cb6.setChecked(false);
		    cb6.setBackgroundColor(0xaaff00ff);
		    cb5.setBackgroundColor(0xaaff00ff);
		    break;
		    case R.id.chk_box6: 
		    cb5.setChecked(false);
		    cb5.setBackgroundColor(0xaaff00ff);
		    cb6.setBackgroundColor(0xaaff00ff);
		    break;
		    case R.id.chk_box7: 
		    cb8.setChecked(false);
		    cb8.setBackgroundColor(0xaaffff00);
		    cb7.setBackgroundColor(0xaaffff00);
		    break;
		    case R.id.chk_box8: 
		    cb7.setChecked(false);
		    cb7.setBackgroundColor(0xaaffff00);
		    cb8.setBackgroundColor(0xaaffff00);
		    break;
		   
		    }
	    }
	}

//	public void onBackPressed() {
//		Intent intent = new Intent();
//		intent.putExtra("done", s);
//		setResult(RESULT_OK, intent);
//		finish();
//	}

}
