package com.Rsen.LSSchedule;

import java.sql.Date;

/**
 * Created by IntelliJ IDEA. User: Ryan Date: 10/15/11 Time: 11:58 AM To change
 * this template use File | Settings | File Templates.
 */
public class Switch {
	public Date DatetoSwitch;
	public int DaytoSwitchto;

	public Date getDatetoSwitch() {
		return DatetoSwitch;
	}

	public void setDatetoSwitch(Date newDatetoSwitch) {
		DatetoSwitch = newDatetoSwitch;
	}

	public int getDaytoSwitchto() {
		return DaytoSwitchto;
	}

	public void setDaytoSwitchto(int newDaytoSwitchto) {
		DaytoSwitchto = newDaytoSwitchto;
	}

}
