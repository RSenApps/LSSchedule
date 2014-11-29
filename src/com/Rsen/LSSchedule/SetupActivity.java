package com.Rsen.LSSchedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by IntelliJ IDEA. User: Ryan Date: 11/5/11 Time: 2:45 PM To change
 * this template use File | Settings | File Templates.
 */
public class SetupActivity extends Activity {
	EditText class1;
	EditText class2;
	EditText class3;
	EditText class4;
	EditText class5;
	EditText class6;
	EditText class7;
	EditText class8;
	EditText[] editTexts;
	Button next;
	Boolean errorOccurred=false;
	SetupClass[] classes = new SetupClass[8];
	int[] startTimesMonTueFri = new int[] { 8 * 60 + 10, 9 * 60, 10 * 60 + 5,
			10 * 60 + 55, 11 * 60 + 45, 12 * 60 + 35, 13 * 60 + 25,
			14 * 60 + 15 };
	int[] endTimesMonTueFri = new int[] { 8 * 60 + 55, 9 * 60 + 45,
			10 * 60 + 50, 11 * 60 + 40, 12 * 60 + 30, 13 * 60 + 20,
			14 * 60 + 10, 15 * 60 };
	int[] startTimesB = new int[] { 8 * 60 + 10, 9 * 60, 9 * 60 + 50,
			10 * 60 + 40, 11 * 60 + 30, 12 * 60 + 20, 13 * 60 + 30,
			14 * 60 + 20 };
	int[] endTimesB = new int[] { 8 * 60 + 55, 9 * 60 + 45, 10 * 60 + 35,
			11 * 60 + 25, 12 * 60 + 15, 13 * 60 + 5, 14 * 60 + 15, 15 * 60 + 5 };
	int WeekDayNumber;
	Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.setup);
		new SetupHelpDialog(this);
		WeekDayNumber = this.getIntent().getExtras().getInt("editDay");
		if (WeekDayNumber == -1) {
			setTitle("Setup");
		} else {
			setTitle("Edit");
		}
		// SetupHelpDialog setupHelpDialog = new SetupHelpDialog(this);
		// setupHelpDialog.show();
		setUI();
		preloadClasses();
		
	}

	private void changeCourses() {
		DictionaryOpenHelper dh = new DictionaryOpenHelper(
				getApplicationContext());
		// if (WeekDayNumber == -1) {
		dh.recreateDatabase();
		dh.InsertCourses(new String[] { "Free", "Lunch", "Assembly", "Lifting",
				"Advisory" });
		/*
		 * } else { dh.DeleteClasses(dh.getDayClasses(WeekDayNumber)); }
		 */
		for (int a = 0; a < 8; a++) {
			try
			{
				dh.InsertCourse(classes[a].getClassName());

			}
			catch (Exception e)
			{
				GMailSender sender = new GMailSender(
						"lsScheduleApp@gmail.com", "Taig2brg");
				String body = null;
				for (int i=0; i<8; i++)
				{
					body += "classes -" + i + " = " + classes[i].getClassName() + "\n";
				}
				body += "Failed at: " + a;
				try {
					sender.sendMail("Setup Class issue detected!", 
							body,
							"lsScheduleRsen@gmail.com",
							"lsScheduleRsen@gmail.com");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}
				this.errorOccurred=true;
			}
			


		}
		/*
		 * if (WeekDayNumber == 3) { // Wednesday InsertClass(classes[0], 8, 10,
		 * 9, 25, 3, dh); InsertClass(classes[1], 9, 35, 10, 50, 3, dh);
		 * InsertClass(classes[2], 10, 55, 12, 10, 3, dh);
		 * InsertClass(classes[3], 12, 15, 12, 50, 3, dh);
		 * InsertClass(classes[4], 12, 55, 14, 10, 3, dh);
		 * InsertClass(classes[5], 13, 50, 15, 5, 3, dh); } else if
		 * (WeekDayNumber == 4) { // Thursday InsertClass(classes[0], 8, 10, 9,
		 * 25, 4, dh); InsertClass(classes[1], 9, 35, 10, 50, 4, dh);
		 * InsertClass(classes[2], 10, 55, 12, 10, 4, dh);
		 * InsertClass(classes[3], 12, 15, 12, 50, 4, dh);
		 * InsertClass(classes[4], 12, 55, 14, 10, 4, dh);
		 * InsertClass(classes[5], 14, 15, 15, 5, 4, dh);
		 * 
		 * } else { InsertClass(classes[0], 8, 10, 8, 55, WeekDayNumber, dh);
		 * InsertClass(classes[1], 9, 0, 9, 45, WeekDayNumber, dh);
		 * InsertClass("Advisory", 9, 50, 10, 0, WeekDayNumber, dh);
		 * InsertClass(classes[2], 10, 5, 10, 50, WeekDayNumber, dh);
		 * InsertClass(classes[3], 10, 55, 11, 40, WeekDayNumber, dh);
		 * InsertClass(classes[4], 11, 45, 12, 30, WeekDayNumber, dh);
		 * InsertClass(classes[5], 12, 35, 13, 20, WeekDayNumber, dh);
		 * InsertClass(classes[6], 13, 25, 14, 10, WeekDayNumber, dh);
		 * InsertClass(classes[7], 14, 15, 15, 0, WeekDayNumber, dh); }
		 */
		dh.close();
	}

	private void setUI() {

		class1 = (EditText) findViewById(R.id.class1);
		class2 = (EditText) findViewById(R.id.class2);
		class3 = (EditText) findViewById(R.id.class3);
		class4 = (EditText) findViewById(R.id.class4);
		class5 = (EditText) findViewById(R.id.class5);
		class6 = (EditText) findViewById(R.id.class6);
		class7 = (EditText) findViewById(R.id.class7);
		class8 = (EditText) findViewById(R.id.class8);
		next = (Button) findViewById(R.id.Next);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean filled = true;
				for (int a = 0; a < 8; a++) {
					EditText e = (EditText) editTexts[a];
					if ((e.getText().length() == 0) || (classes[a] == null)) {
						Toast.makeText(getApplicationContext(),
								"Please fill all fields", Toast.LENGTH_SHORT)
								.show();
						filled = false;
						break;
					}
				}
				if (filled) {
					final ProgressDialog MyDialog = ProgressDialog.show(
							SetupActivity.this, "",
							" Loading. Please wait ... ", true);

					final Handler handler = new Handler() {
						public void handleMessage(Message msg) {
							MyDialog.dismiss();
							
							finish();
						}
					};
					Thread checkUpdate = new Thread() {
						public void run() {
							changeCourses();
							if (WeekDayNumber == -1) {
								putClasses();
							}
							
							handler.sendEmptyMessage(0);
						}
					};
					
					checkUpdate.start();
					if (errorOccurred)
					{
						Toast.makeText(getApplicationContext(), "Wow, a known error has occurred, please contact me through" +
								" my facebook page/email, to help to get it resolved, thanks!", Toast.LENGTH_LONG).show();
						
					}
				}
			}
		});
		editTexts = new EditText[] { class1, class2, class3, class4, class5,
				class6, class7, class8 };
		for (int i = 0; i < editTexts.length; i++) {
			editTexts[i].setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						v.requestFocus();

						editTextClicked((EditText) v);
						return true;
					}
					return false;
				}
			});

		}
	}

	private void editTextClicked(EditText et) {
		SetupDialog setupDialog = null;
		for (int i = 0; i < editTexts.length; i++) {
			if (editTexts[i] == et) {
				setupDialog = new SetupDialog(this, et, classes, i);

			}

		}
		setupDialog.show();

	}

	public void setupDialogDismissed(SetupDialog setupDialog, EditText et) {
		SetupClass setupClass = new SetupClass();
		for (int i = 0; i < editTexts.length; i++) {

			if (editTexts[i] == et) {

				setupClass.setClassName(setupDialog.className);
				setupClass.setMonday(setupDialog.mondayChecked);
				setupClass.setTuesday(setupDialog.tuesdayChecked);
				setupClass.setFriday(setupDialog.fridayChecked);
				if (i % 2 == 0) // if Even
				{
					setupClass.setWednesday(true);
				} else // Odd
				{
					setupClass.setWednesday(false);
				}
				setupClass.setStart(startTimesMonTueFri[i]);
				setupClass.setEnd(endTimesMonTueFri[i]);
				classes[i] = setupClass;
			}

		}
		String etString = setupDialog.className;
		if (setupDialog.mondayChecked || setupDialog.tuesdayChecked
				|| setupDialog.fridayChecked) {
			etString += " (";
			if (setupDialog.mondayChecked) {
				etString += "Mon, ";

			}
			if (setupDialog.tuesdayChecked) {
				etString += "Tue, ";
			}
			if (setupClass.getWednesday()) {
				etString += " Wed";
			} else {
				etString += " Thurs";
			}
			if (setupDialog.fridayChecked) {
				etString += ", Fri)";
			} else {
				etString += ")";
			}
		} else {
			etString += " (Wed, Thurs)";
		}
		et.setText(etString);
	}

	private void putClasses() {
		DictionaryOpenHelper dh = new DictionaryOpenHelper(
				getApplicationContext());
		for (int a = 0; a < classes.length; a++) {
			InsertClass(classes[a].getClassName(), startTimesB[a] / 60,
					startTimesB[a] % 60, endTimesB[a] / 60, endTimesB[a] % 60,
					6, dh);
			int Start = classes[a].getStart();
			int End = classes[a].getEnd();
			String classOrFree = "Free";
			if (classes[a].getMonday()) {
				classOrFree = classes[a].getClassName();
			} else {
				classOrFree = "Free";
			}
			InsertClass(classOrFree, Start / 60, Start % 60, End / 60,
					End % 60, 1, dh);
			if (classes[a].getTuesday()) {
				classOrFree = classes[a].getClassName();
			} else {
				classOrFree = "Free";
			}
			InsertClass(classOrFree, Start / 60, Start % 60, End / 60,
					End % 60, 2, dh);
			if (classes[a].getFriday()) {
				classOrFree = classes[a].getClassName();
			} else {
				classOrFree = "Free";
			}
			InsertClass(classOrFree, Start / 60, Start % 60, End / 60,
					End % 60, 5, dh);
		}
		InsertClass("Free", 13, 10, 1, 25, 6, dh);
		/*
		 * int x; for (int a = 1; a < 4; a++) {
		 * 
		 * if (a < 3) { x = a; } else { x = 5; } InsertClass(classes[0], 8, 10,
		 * 8, 55, x, dh); InsertClass(classes[1], 9, 0, 9, 45, x, dh);
		 * InsertClass("Advisory", 9, 50, 10, 0, x, dh); InsertClass(classes[2],
		 * 10, 5, 10, 50, x, dh); InsertClass(classes[3], 10, 55, 11, 40, x,
		 * dh); InsertClass(classes[4], 11, 45, 12, 30, x, dh);
		 * InsertClass(classes[5], 12, 35, 13, 20, x, dh);
		 * InsertClass(classes[6], 13, 25, 14, 10, x, dh);
		 * InsertClass(classes[7], 14, 15, 15, 0, x, dh); }
		 */
		// Wednesday
		InsertClass(classes[0].getClassName(), 8, 10, 9, 25, 3, dh);
		InsertClass(classes[2].getClassName(), 9, 35, 10, 50, 3, dh);
		InsertClass(classes[4].getClassName(), 10, 55, 12, 10, 3, dh);
		InsertClass("Lunch", 12, 15, 12, 50, 3, dh);
		InsertClass("Assembly", 12, 55, 14, 10, 3, dh);
		InsertClass(classes[6].getClassName(), 13, 50, 15, 5, 3, dh);

		// Thursday
		InsertClass(classes[1].getClassName(), 8, 10, 9, 25, 4, dh);
		InsertClass(classes[3].getClassName(), 9, 35, 10, 50, 4, dh);
		InsertClass(classes[5].getClassName(), 10, 55, 12, 10, 4, dh);
		InsertClass("Lunch", 12, 15, 12, 50, 4, dh);
		InsertClass(classes[7].getClassName(), 12, 55, 14, 10, 4, dh);
		InsertClass("Free", 14, 15, 15, 5, 4, dh);
		dh.close();
	}

	public void InsertClass(String CourseName, int StartHours, int StartMin,
			int EndHours, int EndMin, int WeekDayNumber, DictionaryOpenHelper dh) {
		Cls cls = new Cls();
		cls.setCourse(dh.getCourseIDbyName(CourseName));
		cls.setEndMin(EndHours * 60 + EndMin);
		cls.setStart(StartHours * 60 + StartMin);
		cls.setWeekDayNumber(WeekDayNumber);
		dh.InsertClass(cls);
	}

	private void preloadClasses() {
		DictionaryOpenHelper dh = new DictionaryOpenHelper(
				getApplicationContext());

		Cursor cur;
		cur = dh.getDayClasses(6);

		cur.moveToFirst();

		EditText et;
		for (int a = 0; a < 8; a++) {
			et = (EditText) editTexts[a];
			if (cur.getCount() > 0 && (!cur.isAfterLast())) {
				SetupClass setupClass = new SetupClass();
				setupClass.setClassName(dh.getCourseNamebyID(cur.getInt(3)));
				setupClass.setMonday(dh.isClassOnDay(1, cur.getInt(3)));
				setupClass.setTuesday(dh.isClassOnDay(2, cur.getInt(3)));
				if (a % 2 == 0) // if Even
				{
					setupClass.setWednesday(true);
				} else // Odd
				{
					setupClass.setWednesday(false);
				}
				setupClass.setFriday(dh.isClassOnDay(5, cur.getInt(3)));
				setupClass.setStart(startTimesMonTueFri[a]);
				setupClass.setEnd(endTimesMonTueFri[a]);
				classes[a] = setupClass;
				String etString = setupClass.getClassName();
				if (setupClass.getMonday() || setupClass.getTuesday()
						|| setupClass.getFriday()) {
					etString += " (";
					if (setupClass.getMonday()) {
						etString += "Mon, ";

					}
					if (setupClass.getTuesday()) {
						etString += "Tue, ";
					}
					if (setupClass.getWednesday()) {
						etString += " Wed";
					} else {
						etString += " Thurs";
					}
					if (setupClass.getFriday()) {
						etString += ", Fri)";
					} else {
						etString += ")";
					}
				} else {
					etString += " (Wed, Thurs)";
				}
				et.setText(etString);

				if (a == 5) {
					// 5 is 5b
					cur.moveToNext();
				}

				cur.moveToNext();
			}

		}
		cur.close();
		dh.close();
	}
	
}
