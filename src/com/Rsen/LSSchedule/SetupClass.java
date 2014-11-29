package com.Rsen.LSSchedule;


public class SetupClass {
	private String className;
	private Boolean Monday = false;
	private Boolean Tuesday = false;
	private Boolean Wednesday = false;
	private Boolean Friday = false;
	private int Start;
	private int End;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Boolean getMonday() {
		return Monday;
	}

	public void setMonday(Boolean monday) {
		Monday = monday;
	}

	public Boolean getTuesday() {
		return Tuesday;
	}

	public void setTuesday(Boolean tuesday) {
		Tuesday = tuesday;
	}

	public Boolean getWednesday() {
		return Wednesday;
	}

	public void setWednesday(Boolean wednesday) {
		Wednesday = wednesday;
	}

	public Boolean getFriday() {
		return Friday;
	}

	public void setFriday(Boolean friday) {
		Friday = friday;
	}

	public int getStart() {
		return Start;
	}

	public void setStart(int start) {
		Start = start;
	}

	public int getEnd() {
		return End;
	}

	public void setEnd(int end) {
		End = end;
	}

}
