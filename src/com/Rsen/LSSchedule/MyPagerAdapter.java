package com.Rsen.LSSchedule;

import java.util.Calendar;



import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

public class MyPagerAdapter extends PagerAdapter  {

	final LakesideScheduleActivity lsActivity;
	DictionaryOpenHelper dh;
	Calendar firstViewDay;
	Calendar lastViewDay;
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	public MyPagerAdapter(LakesideScheduleActivity lsActivity) {
		this.lsActivity = lsActivity;
		dh = new DictionaryOpenHelper(lsActivity);
		firstViewDay = dh.getSchoolStart();
		lastViewDay = dh.getSchoolEnd();
	}
	public void closeDatabase()
	{
		dh.close();
	}
	public int getCount() {
		// lastViewDay.get(Calendar.DAY_OF_YEAR) -
		// firstViewDay.get(Calendar.DAY_OF_YEAR) +
		// 2*(lastViewDay.get(Calendar.WEEK_OF_YEAR) -
		// firstViewDay.get(Calendar.WEEK_OF_YEAR));
		int x = lastViewDay.get(Calendar.DAY_OF_YEAR) + 365
				* lastViewDay.get(Calendar.YEAR);
		int y = firstViewDay.get(Calendar.DAY_OF_YEAR) + 365
				* firstViewDay.get(Calendar.YEAR);
		int a = lastViewDay.get(Calendar.WEEK_OF_YEAR) + 52
				* lastViewDay.get(Calendar.YEAR);
		int b = firstViewDay.get(Calendar.WEEK_OF_YEAR) + 52
				* firstViewDay.get(Calendar.YEAR);
		int c = x - y - 2 * (a - b);
		return c;
	}

	public Object instantiateItem(View collection, int position) {

		// return lsActivity.getDayView(position);
		// LayoutInflater inflater = (LayoutInflater) collection.getContext()
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View view = lsActivity.getDayView(position);
		final Calendar positionDay = getDayofPosition(position);
		final View view = lsActivity.getDayView(positionDay);
		if (view!=null)
		{
			((ViewPager) collection).addView(view, 0);
		}
		return view;

		/*
		 * ListView v = new ListView( lsActivity ); String[] from = new String[]
		 * { "str" }; int[] to = new int[] { android.R.id.text1 };
		 * List<Map<String, String>> items = new ArrayList<Map<String,
		 * String>>(); for ( int i = 0; i < 20; i++ ) { Map<String, String> map
		 * = new HashMap<String, String>(); map.put( "str", String.format(
		 * "Item %d", i + 1 ) ); items.add( map ); } SimpleAdapter adapter = new
		 * SimpleAdapter( lsActivity, items,
		 * android.R.layout.simple_list_item_1, from, to );
		 * 
		 * v.setAdapter( adapter ); ( (ViewPager) collection ).addView( v, 0 );
		 * return v;
		 */
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(((ListView) arg2));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0.equals(arg1);
	}

	@Override
	public void startUpdate(View view) {
	}

	@Override
	public void finishUpdate(View view) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable p, ClassLoader c) {
	}
	@Override
	public String getPageTitle(int position) {

		String title = null;
		Calendar positionDay = getDayofPosition(position);
		switch (positionDay.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			title = "Monday - ";
			break;
		case Calendar.TUESDAY:
			title = "Tuesday - ";
			break;
		case Calendar.WEDNESDAY:
			title = "Wednesday - ";
			break;
		case Calendar.THURSDAY:
			title = "Thursday - ";
			break;
		case Calendar.FRIDAY:
			title = "Friday - ";
			break;
		}
		return (title + (positionDay.get(Calendar.MONTH) + 1) + "/" + positionDay
				.get(Calendar.DAY_OF_MONTH)); // Months 0-indexed

	}

	public Calendar getDayofPosition(int position) {
		Calendar positionDay = (Calendar) firstViewDay.clone();
		positionDay.add(Calendar.DAY_OF_YEAR,
				(int) Math.floor(position / 5) * 7); // TODO

		if (positionDay.get(Calendar.DAY_OF_WEEK) + position % 5 > 6) {
			positionDay.add(Calendar.DAY_OF_YEAR, 2);
		}
		positionDay.add(Calendar.DAY_OF_YEAR, position % 5);
		return positionDay;
	}

	public int getPositionofDay(Calendar positionDay) {
		// TODO
		if (positionDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			positionDay.add(Calendar.DAY_OF_YEAR, 2);
		} else if (positionDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			positionDay.add(Calendar.DAY_OF_YEAR, 1);

		}
		int x = positionDay.get(Calendar.DAY_OF_YEAR) + 365
				* positionDay.get(Calendar.YEAR);
		int y = firstViewDay.get(Calendar.DAY_OF_YEAR) + 365
				* firstViewDay.get(Calendar.YEAR);
		int a = positionDay.get(Calendar.WEEK_OF_YEAR) + 52
				* positionDay.get(Calendar.YEAR);
		int b = firstViewDay.get(Calendar.WEEK_OF_YEAR) + 52
				* firstViewDay.get(Calendar.YEAR);
		int c = x - y - 2 * (a - b);
		return c;
	}

}
