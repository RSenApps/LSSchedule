package com.Rsen.LSSchedule;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.tapfortap.AdView;
import com.tapfortap.TapForTap;
import com.viewpagerindicator.TitlePageIndicator;

public class LakesideScheduleActivity extends Activity {
	/** Called when the activity is first created. */
	private AdView adView;
	private Context appContext;
	private MyPagerAdapter adapter;
	private ViewPager myPager;
	private DictionaryOpenHelper dh;
	private Calendar today;
	Boolean newUser;
	SharedPreferences prefs;
	//final Facebook facebook = new Facebook("336419729723346");
	//String access_token;
	AlertDialog shareDialog;
	AlertDialog newUserDialog;
	//String err;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		newUser = true;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		Initialize();
		DisplayScreen();
		adView = (AdView) findViewById(R.id.ad_view);
		// Initiate a generic request to load it with an ad
	
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adapter.closeDatabase();
		if (dh != null)
		{
			dh.close();
		}
	}
	/*
	private void InsertAllenGatesCelebration()
	{
		dh.InsertCourse("Allen Gates Celebration");
		Cursor cur = dh.getDayClasses(6);
		String[] classes = new String[cur.getCount() -1];
		cur.moveToFirst();
		int a = 0;
		for (int i = 0; i< cur.getCount(); i++)
		{
			
			classes[a] = dh.getCourseNamebyID(cur.getInt(3));
			cur.moveToNext();
			if (i!=5)
			{
				a++;
			}
		}
	
		cur.close();
		InsertClass("Allen Gates Celebration", 8, 10, 9, 0, 7, dh);
		InsertClass(classes[0], 9, 5, 9, 45, 7, dh);
		InsertClass(classes[1], 9, 50, 10, 30, 7, dh);
		InsertClass(classes[2], 10, 35, 11, 15, 7, dh);
		InsertClass(classes[3], 11, 20, 12, 0, 7, dh);
		InsertClass(classes[4], 12, 5, 12, 45, 7, dh);
		InsertClass(classes[5], 12, 50, 13, 30, 7, dh);
		InsertClass(classes[6], 13, 35, 14, 15, 7, dh);
		InsertClass(classes[7], 14, 20, 15, 0, 7, dh);
		Switch swtch = new Switch();
		swtch.setDatetoSwitch(new Date(2012, 1, 27)); //0 based
		swtch.setDaytoSwitchto(7);
		dh.InsertSwitch(swtch);
		
		dh.close();
		
			

	}
	*/
	public void InsertClass(String CourseName, int StartHours, int StartMin,
			int EndHours, int EndMin, int WeekDayNumber, DictionaryOpenHelper dh) {
		Cls cls = new Cls();
		cls.setCourse(dh.getCourseIDbyName(CourseName));
		cls.setEndMin(EndHours * 60 + EndMin);
		cls.setStart(StartHours * 60 + StartMin);
		cls.setWeekDayNumber(WeekDayNumber);
		dh.InsertClass(cls);
	}
	@Override
	public void onResume() {

		today = Calendar.getInstance();
		newUser = true;
		dh = new DictionaryOpenHelper(getApplicationContext());
		adapter.notifyDataSetChanged();
		/*
		Cursor curA= dh.getDayClasses(7);
		Cursor curB = dh.getDayClasses(6);
		try
		{
			if (!(curA.moveToFirst()) && (curB.moveToFirst()) && (today.getTime().before(new java.util.Date(2012, 1, 28)))) //7=allen gates
			{
				curA.close();
				curB.close();
				changingDatabaseNow = true;
				final ProgressDialog MyDialog = ProgressDialog.show(
						LakesideScheduleActivity.this, "",
						" Adding Allen-Gates Celebration Schedule. Please wait ... ", true);
				final Handler handler = new Handler() {
					public void handleMessage(Message msg) {
						MyDialog.dismiss();
						changingDatabaseNow = false;
						adapter.notifyDataSetChanged();
					}
				};
				Thread checkUpdate = new Thread() {
					public void run() {
						try
						{
							InsertAllenGatesCelebration();
						}
						catch (Exception e)
						{
							System.err.print(e.getMessage());
						}
						handler.sendEmptyMessage(0);
					}
				};
				
				checkUpdate.start();
				
			}
			else
			{
				changingDatabaseNow=false;
				
			}
		}
		catch(Exception e)
		{
			System.err.print(e.getMessage());
		}
		*/
		TapForTap.setDefaultAppId("5a9be0c0-c964-012f-d765-40400a3f6b7e");
        TapForTap.checkIn(this);
		 
		adView.loadAds();

		Intent i = new Intent(LakesideScheduleActivity.this,
				LakesideService.class);
		i.putExtra("appCalled", true);
		startService(i);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, err, Toast.LENGTH_LONG).show();
		super.onPause();
	}

	private void DisplayScreen() {
		// TODO Auto-generated method stub
		
		adapter = new MyPagerAdapter(this);
		myPager = (ViewPager) findViewById(R.id.viewpager);

		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		myPager.setAdapter(adapter);
		indicator.setViewPager(myPager);
		myPager.setCurrentItem(adapter.getPositionofDay(today));
	}

	private void Initialize() {
		// TODO Auto-generated method stub
		appContext = getApplicationContext();
		today = Calendar.getInstance();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		newUserDialog = new AlertDialog.Builder(this).create();
		shareDialog = new AlertDialog.Builder(this).create();
		prefs.edit().putBoolean("shareNotNow", false).commit();
	}

	public View getDayView(final Calendar DaytoDisplay) {
		final int DayNumber = DaytoDisplay.get(Calendar.DAY_OF_WEEK) - 1;
		final String[] list = new String[] {};
		

		LoadDataReturn loadDataReturn = new LoadDataReturn();
		loadDataReturn = loadData(DayNumber, list, loadDataReturn, DaytoDisplay);
		/*
		if (loadDataReturn.getSelection() == -1
				&& !prefs.getBoolean("app_shared", false) && !prefs.getBoolean("shareNotNow", false)) {
			prefs.edit().putBoolean("shareNotNow", true).commit();
			
			showShareDialog();
		}
		*/
		return constructView(loadDataReturn.getList(),
				loadDataReturn.getSelection(), DayNumber);
		
	}
/*
	private void showShareDialog() {
		// TODO Auto-generated method stub
		if (!newUserDialog.isShowing()) {
			
			shareDialog.setTitle("Like this app???");
			shareDialog.setMessage("Please help share it!");
			DialogInterface.OnClickListener shareListener = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (AlertDialog.BUTTON_POSITIVE == which) {
						shareButton();
					} else if (AlertDialog.BUTTON_NEGATIVE == which) {
						SharedPreferences.Editor editor = prefs.edit();
						editor.putBoolean("app_shared", true);
						editor.commit();

					}
				}
			};
			
			shareDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sure!",
					shareListener);
			shareDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Never :(",
					shareListener);
			shareDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Not Now",
					shareListener);
			shareDialog.setIcon(R.drawable.ic_menu_share);
			shareDialog.show();
		}
		
	}
*/
	private LoadDataReturn loadData(int DayNumber, String[] list,
			LoadDataReturn loadDataReturn, Calendar DaytoDisplay) {
		// TODO Auto-generated method stub
		DateFormat sdf = new SimpleDateFormat("h:mm a");
		Time now = new Time(today.get(Calendar.HOUR_OF_DAY),
				today.get(Calendar.MINUTE), 0);

		int nowmin = now.getHours() * 60 + now.getMinutes();
		Date daytoDisplayDate = new Date(DaytoDisplay.get(Calendar.YEAR),
				DaytoDisplay.get(Calendar.MONTH),
				DaytoDisplay.get(Calendar.DAY_OF_MONTH));
		Cursor switchForToday = dh.getSwitchforToday(daytoDisplayDate);
		
			if (switchForToday.getCount() > 0) {
				switchForToday.moveToFirst();
				DayNumber = switchForToday.getInt(1);
			}
			switchForToday.close();
		
		Cursor cur = dh.getDayClasses(DayNumber);
		int Selection = -1;
		int startColumn = cur.getColumnIndex("Start");
		int courseColumn = cur.getColumnIndex("Course");
		int endColumn = cur.getColumnIndex("End");
		int SelectionStartmin = 0;
		int i = 0;

		cur.moveToFirst();

		if (cur.getCount() > 0) {
			list = new String[cur.getCount()];
			cur.moveToFirst();
			do {

				Time Start = new Time(0, cur.getInt(startColumn), 0);
				Time End = new Time(0, cur.getInt(endColumn), 0);
				if ((Start.after(now)) & (Selection == -1)) {
					Selection = i;
					SelectionStartmin = cur.getInt(startColumn);
				}

				i++;
				int CourseID = cur.getInt(courseColumn);
				String CourseName = dh.getCourseNamebyID(CourseID);
				list[i - 1] = CourseName + "\n"
						+ sdf.format(Start).toLowerCase() + " - "
						+ sdf.format(End).toLowerCase();

			} while (cur.moveToNext());

		} else {
			list = new String[1];
			list[0] = "No School!";
			// Run Tutorial
			final Date curDate = new Date(DaytoDisplay.get(Calendar.YEAR),
					DaytoDisplay.get(Calendar.MONTH),
					DaytoDisplay.get(Calendar.DAY_OF_MONTH));

			if (newUser && dh.getSwitchforToday(curDate).getCount() < 1
					&& !newUserDialog.isShowing()) {
				newUser = false;
				if (shareDialog.isShowing()) {
					shareDialog.dismiss();
				}

				newUserDialog.setTitle("New User Detected");
				newUserDialog.setIcon(R.drawable.icon);
				newUserDialog
						.setMessage("I have detected you are a new user. Would you like to setup your classes?");
				newUserDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								new Thread(new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										Intent i = new Intent(
												LakesideScheduleActivity.this,
												SetupActivity.class);
										i.putExtra("editDay", -1);
										LakesideScheduleActivity.this
												.startActivity(i);
									}
								}).start();

							}
						});
				newUserDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newUserDialog.dismiss();
							}
						});
				newUserDialog.show();
			}

		}
		cur.close();
		// TODO make sure same date not just same day of week
		if ((Selection != -1)
				&& (DaytoDisplay.get(Calendar.DAY_OF_YEAR) == today
						.get(Calendar.DAY_OF_YEAR))) {
			if (((SelectionStartmin - nowmin) > 60)) {
				list[Selection] = "NEXT: " + list[Selection].toString()
						+ "\nin " + (SelectionStartmin - nowmin) / 60
						+ " hr and " + (SelectionStartmin - nowmin) % 60
						+ " min";
			} else if ((SelectionStartmin - nowmin) % 60 == 0) {
				list[Selection] = "NEXT: " + list[Selection].toString()
						+ "\nin " + (SelectionStartmin - nowmin) / 60 + " hr";
			} else {
				list[Selection] = "NEXT: " + list[Selection].toString()
						+ "\nin " + (SelectionStartmin - nowmin) + " min";
			}
		}

		loadDataReturn.setList(list);
		loadDataReturn.setSelection(Selection);
		return loadDataReturn;
	}

	private ListView constructView(String[] list, int Selection, int DayNumber) {
		ListView listView = new ListView(appContext);
		listView = new ListView(this);
		ArrayAdapter<String> arrayAdapter;

		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, list);
		listView.setAdapter(arrayAdapter);
		// TODO make sure same date, not just same day of week
		if ((DayNumber + 1 == today.get(Calendar.DAY_OF_WEEK))) {

			listView.setSelection(Selection);
		}

		return listView;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return true;
	}

	/**
	 * when menu button option selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		case R.id.share:
			shareButton();
			break;
			*/
		case R.id.setup:

			Intent i = new Intent(LakesideScheduleActivity.this,
					SetupActivity.class);
			i.putExtra("editDay", -1);
			LakesideScheduleActivity.this.startActivity(i);
			break;

		case R.id.markToday:
			// List items

			markTodayButton();
			break;
		case R.id.Report:
			new ReportDialog(this, dh);	
			break;
		case R.id.settings:


			
			Intent a = new Intent(this, SettingsActivity.class);
			startActivity(a);
			
			
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void markTodayButton() {
		// TODO Auto-generated method stub
		int b = 1;
		final String[] items;
		Calendar curCalendar = adapter.getDayofPosition(myPager
				.getCurrentItem());
		final int curDayOfWeek = curCalendar.get(Calendar.DAY_OF_WEEK);
		final Date curDate = new Date(curCalendar.get(Calendar.YEAR),
				curCalendar.get(Calendar.MONTH),
				curCalendar.get(Calendar.DAY_OF_MONTH));
		if (dh.getSwitchforToday(curDate).getCount() > 0) {
			items = new String[7];
			items[6] = "Revert to Default";
		} else {
			items = new String[6];
		}
		items[5] = "B Schedule";
		items[0] = "No School";
		for (int a = 1; a < 6; a++) {

			if (a + 1 != curDayOfWeek) {
				String stringItem = "";
				switch (a + 1) {
				case Calendar.MONDAY:
					stringItem = "Monday";
					break;
				case Calendar.TUESDAY:
					stringItem = "Tuesday";
					break;
				case Calendar.WEDNESDAY:
					stringItem = "Wednesday";
					break;
				case Calendar.THURSDAY:
					stringItem = "Thursday";
					break;
				case Calendar.FRIDAY:
					stringItem = "Friday";
					break;
				}
				items[b] = stringItem;
				b++;
			}
		}

		// Prepare the list dialog box

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Set its title

		builder.setTitle("Which day would you like to switch to?");

		// Set the list items and assign with the click listener

		builder.setItems(items, new DialogInterface.OnClickListener() {

			// Click listener

			public void onClick(DialogInterface dialog, int item) {
				Switch swtch = new Switch();

				Calendar CalendartoSwitch = adapter.getDayofPosition(myPager
						.getCurrentItem());
				Date DatetoSwitch = new Date(CalendartoSwitch
						.get(Calendar.YEAR), CalendartoSwitch
						.get(Calendar.MONTH), CalendartoSwitch
						.get(Calendar.DAY_OF_MONTH));

				swtch.setDatetoSwitch(DatetoSwitch);
				int DaytoSwitchto = 0;
				switch (items[item].charAt(0)) {
				case 'M':
					DaytoSwitchto = 1;
					break;
				case 'T':
					DaytoSwitchto = items[item].charAt(1) == 'u' ? 2 : 4;
					break;
				case 'W':
					DaytoSwitchto = 3;
					break;
				case 'F':
					DaytoSwitchto = 5;
					break;
				case 'B':
					DaytoSwitchto = 6;
					break;
				case 'R':
					DaytoSwitchto = -1;
					break;
				case 'N':
					DaytoSwitchto = -2;
					break;
				}
				if (DaytoSwitchto == -1) {
					dh.deleteSwitch(curDate);
				} else {
					swtch.setDaytoSwitchto(DaytoSwitchto);
					dh.InsertSwitch(swtch);
				}

				adapter.notifyDataSetChanged();
				Intent i = new Intent(LakesideScheduleActivity.this,
						LakesideService.class);
				i.putExtra("appCalled", true);
				startService(i);

			}

		});

		AlertDialog alert = builder.create();

		// display dialog box

		alert.show();
	}

	public void reportDialogDismissed() {
		Toast.makeText(
				getApplicationContext(),
				"Thank you for your feedback!\nYour changes will be made ASAP.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//facebook.authorizeCallback(requestCode, resultCode, data);
		//shareOnFacebook();
	}
	/*
	public void shareButton() {
		appContext = this;
		DialogInterface.OnClickListener shareListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int whichButton) {
				// TODO Auto-generated method stub
				if (whichButton == AlertDialog.BUTTON_POSITIVE) {
					// Share
					logIntoFacebook();
					
					
				} 
				
				else if (whichButton == AlertDialog.BUTTON_NEUTRAL) {
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("app_shared", true);
					editor.commit();
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri
							.parse("http://www.facebook.com/LakesideSchedule"));
					startActivity(i);
				}
				
				else if (whichButton == AlertDialog.BUTTON_NEGATIVE)
				{
					 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=com.Rsen.LSSchedule")));
				}
			}
		};
		AlertDialog shareDialog = new AlertDialog.Builder(this).create();
		shareDialog.setIcon(R.drawable.ic_menu_share);
		shareDialog.setMessage("Thank you! How would you like to share?");
		shareDialog.setTitle("Share");
		shareDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Facebook",
				shareListener);
		shareDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Market",
				shareListener);
		
	
		shareDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Toast.makeText(appContext, "Ok maybe later :)",
						Toast.LENGTH_SHORT);
			}
		});
		shareDialog.show();

	}

	private void logIntoFacebook() {
		
		final SharedPreferences mPrefs;
		mPrefs = getPreferences(MODE_PRIVATE);
		access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		
		 * Only call authorize if the access_token has expired.
		 
		if (!facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "publish_stream" },
					new DialogListener() {

						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
							

						}

						public void onFacebookError(FacebookError error) {
							Toast.makeText(appContext, error.getMessage(), Toast.LENGTH_LONG).show();
						}

						public void onError(DialogError e) {
							Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG).show();
						}

						public void onCancel() {
						}
					});
		}
		else
		{
			shareOnFacebook();
		}
	}
	public void shareOnFacebook()
	{
		Bundle bundle = new Bundle();
		bundle.putString("link",
				"http://www.facebook.com/LakesideSchedule");
		bundle.putString(
				"picture",
				"http://i48.tinypic.com/des134.png");
		facebook.dialog(appContext, "feed", bundle,
				new DialogListener() {

					public void onFacebookError(FacebookError e) {
						// TODO Auto-generated method stub
						Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG).show();
					}

					public void onError(DialogError e) {
						// TODO Auto-generated method stub
						Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG).show();
					}

					public void onComplete(Bundle values) {
						// TODO Auto-generated method stub
						Toast.makeText(appContext,
								"Thank you for sharing!",
								Toast.LENGTH_SHORT);

					}

					public void onCancel() {
						// TODO Auto-generated method stub

					}
				});
	}
	*/
}