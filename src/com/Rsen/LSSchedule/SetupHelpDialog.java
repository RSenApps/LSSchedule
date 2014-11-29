package com.Rsen.LSSchedule;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SetupHelpDialog extends Dialog {
	SetupHelpDialog setupHelpDialog;

	public SetupHelpDialog(Context context) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.setuphelpdialog);
		setupHelpDialog = this;
		Button Done = (Button) findViewById(R.id.setuphelpdialogdone);
		Done.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				setupHelpDialog.dismiss();
			}
		});
		this.show();
		// TODO Auto-generated constructor stub
	}

}
