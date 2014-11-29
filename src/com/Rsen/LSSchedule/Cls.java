package com.Rsen.LSSchedule;


/**
 * Created by IntelliJ IDEA. User: Ryan Date: 10/15/11 Time: 11:58 AM To change
 * this template use File | Settings | File Templates.
 */
public class Cls {
	public int EndMin;
	public int StartMin;
	public int CourseID;
	public int ClassID;
	public int WeekDayNumber; // Sunday=0, Monday=1... Saturday = 6

	public int getWeekDayNumber() {
		return WeekDayNumber;
	}

	public void setWeekDayNumber(int newWeekDayNumber) {
		WeekDayNumber = newWeekDayNumber;
	}

	public int getStart() {
		return StartMin;
	}

	public void setStart(int newStartMin) {
		StartMin = newStartMin;
	}

	public int getEnd() {
		return EndMin;
	}

	public void setEndMin(int newEndMin) {
		EndMin = newEndMin;
	}

	public int getCourse() {
		return CourseID;
	}

	public void setCourse(int newCourse) {
		CourseID = newCourse;
	}

	public int getClassID() {
		return ClassID;
	}

	public int setClassID(int newClassId) {
		return ClassID = newClassId;
	}

}
