package com.idl.daq;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class I2C_Adapter extends ArrayAdapter<I2C_ItemClass> implements
		OnClickListener {

	Context context;
	List<I2C_ItemClass> list;

	public I2C_Adapter(Context context, List<I2C_ItemClass> list,int layout) {
		// TODO Auto-generated constructor stub
		super(context, layout, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		I2C_ItemClass listObj = list.get(position);
		String type = listObj.getType();
		ImageButton edit, del;
		TextView addr, val, time;
		if (type.contentEquals("read")) {
			View view = inflater.inflate(R.layout.read, parent, false);
			addr = (TextView) view.findViewById(R.id.addr);
			addr.append(listObj.getAddr());
			edit = (ImageButton) view.findViewById(R.id.edit);
			del = (ImageButton) view.findViewById(R.id.del);
			edit.setTag(position + ":read");
			del.setTag(position + ":read");
			edit.setOnClickListener(this);
			del.setOnClickListener(this);
			return view;
		} else if (type.contentEquals("write")) {
			View view = inflater.inflate(R.layout.write, parent, false);
			 addr = (TextView) view.findViewById(R.id.addr);
			 addr.setText(listObj.getAddr());
			 val = (TextView) view.findViewById(R.id.val);
			 val.setText(listObj.getVal());
			 edit = (ImageButton) view.findViewById(R.id.edit);
				del = (ImageButton) view.findViewById(R.id.del);
				edit.setTag(position + ":write");
				del.setTag(position + ":write");
				edit.setOnClickListener(this);
				del.setOnClickListener(this);
			return view;
		} else {
			View view = inflater.inflate(R.layout.delay, parent, false);
			 time = (TextView) view.findViewById(R.id.time);
			 time.setText(listObj.getDelay() + "");
			 edit = (ImageButton) view.findViewById(R.id.edit);
				del = (ImageButton) view.findViewById(R.id.del);
				edit.setTag(position + ":delay");
				del.setTag(position + ":delay");
				edit.setOnClickListener(this);
				del.setOnClickListener(this);
			return view;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String tag = (String) v.getTag();
		String[] fields = tag.split(":");
		int position = Integer.parseInt(fields[0]);
		String type = fields[1];
		switch (v.getId()) {
		case R.id.edit:
			if (type.contentEquals("read")) {
				readInstruction(position);
			} else if (type.contentEquals("write")) {
				writeInstruction(position);
			} else {
				delayInstruction(position);
			}
			break;
		case R.id.del:
			list.remove(position);
			notifyDataSetChanged();
			break;
		}

	}

	private void delayInstruction(final int position) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(context);
		View dialogview = li.inflate(R.layout.i2c_dialog_delay, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setView(dialogview);
		final EditText addr = (EditText) dialogview
				.findViewById(R.id.edit_delay);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Commit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								list.get(position).setDelay(
										addr.getText().toString());
								notifyDataSetChanged();
								dialog.dismiss();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void writeInstruction(final int position) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(context);
		View dialogview = li.inflate(R.layout.i2c_dialog_write, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setView(dialogview);
		final EditText addr = (EditText) dialogview
				.findViewById(R.id.edit_write_addr);
		final EditText val = (EditText) dialogview
				.findViewById(R.id.edit_write_value);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Commit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								list.get(position).setAddr(
										addr.getText().toString());
								list.get(position).setVal(
										val.getText().toString());
								notifyDataSetChanged();
								dialog.dismiss();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void readInstruction(final int position) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(context);
		View dialogview = li.inflate(R.layout.i2c_dialog_read, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setView(dialogview);
		final EditText addr = (EditText) dialogview
				.findViewById(R.id.edit_read_addr);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Commit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								list.get(position).setAddr(
										addr.getText().toString());
								notifyDataSetChanged();
								dialog.dismiss();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
