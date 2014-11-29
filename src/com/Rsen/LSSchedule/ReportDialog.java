package com.Rsen.LSSchedule;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Dialog;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ReportDialog extends Dialog {
	final ReportDialog thisReportDialog;
	final LakesideScheduleActivity context;
	final DictionaryOpenHelper dh;

	public ReportDialog(LakesideScheduleActivity context,
			DictionaryOpenHelper dh) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reportdialog);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		this.show();
		this.getWindow().setAttributes(lp);
		// TODO Auto-generated constructor stub
		this.context = context;
		thisReportDialog = this;
		this.dh = dh;
		Button send = (Button) findViewById(R.id.reportSend);
		send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String description = ((EditText) findViewById(R.id.reportDescription))
						.getText().toString();
				final String returnEmail = ((EditText) findViewById(R.id.reportEmail))
						.getText().toString();
				if (description != "") {
					String tempname = ((EditText) findViewById(R.id.reportName))
							.getText().toString();
					final String name;
					if (tempname == "") {
						name = "Anonymous";
					} else {
						name = tempname;
					}
					final Boolean sendData = ((CheckBox) findViewById(R.id.reportSendData))
							.isChecked();
					new Thread(new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							try {
								GMailSender sender = new GMailSender(
										"lsScheduleApp@gmail.com", "Taig2brg");
								String body = "Name: " + name
										+ "\nReturn Email: " + returnEmail
										+ "\nDescription: " + description
										+ "\nData:\n";
								if (sendData) {
									Cursor cur = thisReportDialog.dh
											.getDayClasses(6);
									cur.moveToFirst();
									if (cur.getCount() < 1) {
										body += "No Data";
									} else {

										body += "\nClasses:";
										DateFormat sdf = new SimpleDateFormat(
												"h:mm a");
										for (int x = 0; x < cur.getCount(); x++) {
											Time Start = new Time(0, cur
													.getInt(1), 0);
											Time End = new Time(0, cur
													.getInt(2), 0);
											body += "\n"
													+ thisReportDialog.dh
															.getCourseNamebyID(cur
																	.getInt(3))
													+ " "
													+ sdf.format(Start)
															.toLowerCase()
													+ " - "
													+ sdf.format(End)
															.toLowerCase()
													+ " ";
											body += thisReportDialog.dh
													.isClassOnDay(1,
															cur.getInt(3))
													+ ", ";
											body += thisReportDialog.dh
													.isClassOnDay(2,
															cur.getInt(3))
													+ ", ";
											body += thisReportDialog.dh
													.isClassOnDay(5,
															cur.getInt(3));
											cur.moveToNext();
										}
									}
								} else {
									body += " Send Data unchecked";
								}
								sender.sendMail("Reported: " + description
										+ " by: " + name, body,
										"lsScheduleRsen@gmail.com",
										"lsScheduleRsen@gmail.com");
							} catch (Exception e) {
								Log.e("SendMail", e.getMessage(), e);
							}
						}
					}).start();
					thisReportDialog.context.reportDialogDismissed();
					thisReportDialog.dismiss();

				} else {
					Toast.makeText(thisReportDialog.context,
							"Please describe your report", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
	}

}
