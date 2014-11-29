package com.Rsen.LSSchedule;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SetupDialog extends Dialog {
	private ToggleButton Monday;
	private ToggleButton Tuesday;
	private ToggleButton Friday;
	private Button Done;
	private Button Cancel;
	private AutoCompleteTextView Class;
	public String className = "";
	public boolean mondayChecked = true;
	public boolean tuesdayChecked = true;
	public boolean fridayChecked = true;
	private SetupActivity setupActivity;
	private EditText editText;

	public SetupDialog(SetupActivity setupActivity, EditText et,
			SetupClass[] classes, int ClassNumber) {
		super(setupActivity);
		this.setupActivity = setupActivity;
		editText = et;

		if (classes[ClassNumber] != null) {
			mondayChecked = classes[ClassNumber].getMonday();
			tuesdayChecked = classes[ClassNumber].getTuesday();
			fridayChecked = classes[ClassNumber].getFriday();
			className = classes[ClassNumber].getClassName();
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setupdialog);
		setupUI();
	}

	private void setupClickListeners() {
		Done.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogDone();
			}
		});
		Cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogCancel();
			}
		});

	}

	private void setupUI() {
		Monday = (ToggleButton) findViewById(R.id.Monday);
		Monday.setChecked(mondayChecked);
		Tuesday = (ToggleButton) findViewById(R.id.Tuesday);
		Tuesday.setChecked(tuesdayChecked);
		Friday = (ToggleButton) findViewById(R.id.Friday);
		Friday.setChecked(fridayChecked);
		Done = (Button) findViewById(R.id.SetupDialogDone);
		Cancel = (Button) findViewById(R.id.Cancel);
		Class = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		String[] suggestedclasses = getContext().getResources().getStringArray(
				R.array.suggestedClasses);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_dropdown_item_1line, suggestedclasses);
		Class.setAdapter(adapter);
		Class.setText(className);
		if (className != "") {
			Class.setThreshold(className.length() + 1);
			Class.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Class.setThreshold(1);
					Class.showDropDown();
				}
			});
		} else {
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}

		setupClickListeners();

	}

	private void DialogDone() {
		mondayChecked = Monday.isChecked();
		tuesdayChecked = Tuesday.isChecked();
		fridayChecked = Friday.isChecked();
		className = Class.getText().toString();

		setupActivity.setupDialogDismissed(this, editText);
		this.dismiss();

	}

	private void DialogCancel() {
		this.dismiss();
	}
}
