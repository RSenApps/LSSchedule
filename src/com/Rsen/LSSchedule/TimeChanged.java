package com.Rsen.LSSchedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeChanged extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context, LakesideService.class);
		context.startService(i);

	}

}
