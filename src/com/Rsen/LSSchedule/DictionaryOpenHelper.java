package com.Rsen.LSSchedule;

import java.sql.Date;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryOpenHelper extends SQLiteOpenHelper {
	static final String dbName = "Schedule";
	static final String clsesTable = "Clses";
	static final String colClassID = "ClassID";
	static final String colCourse = "Course";
	static final String colStart = "Start";
	static final String colWeekDayNumber = "WeekDayNumber";
	static final String colEnd = "End";

	static final String coursesTable = "Courses";
	static final String colCourseID = "CourseID";
	static final String colName = "Name";

	static final String switchesTable = "Switches";
	static final String colSwitchID = "SwitchID";
	static final String colDatetoSwitch = "DatetoSwitch";
	static final String colDayNumbertoSwitchto = "DayNumbertoSwitchto";

	private static final Calendar SchoolStart = Calendar.getInstance(); // TODO:
																		// Allow
																		// edit
																		// of
																		// schools
																		// start
																		// and
																		// endd
	private static final Calendar SchoolEnd = Calendar.getInstance(); // TODO:
																		// Allow
																		// edit
																		// of
																		// schools
																		// start
																		// and
																		// end

	public DictionaryOpenHelper(Context context) {
		super(context, dbName, null, 33);
		SchoolStart.set(2012, 8, 5); // 0 based months
		SchoolEnd.set(2013, 5, 5); //DEBUG set to school start and end
	}
	

	public void onCreate(SQLiteDatabase db) {
		// TODO using constants

		db.execSQL("CREATE TABLE " + coursesTable + " (" + colCourseID
				+ " INTEGER PRIMARY KEY , " + colName + " TEXT)");

		db.execSQL("CREATE TABLE " + clsesTable + "(" + colClassID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + colEnd
				+ " INTEGER, " + colStart + " INTEGER, " + colWeekDayNumber
				+ " INTEGER, " + colCourse + " INTEGER NOT NULL ,FOREIGN KEY ("
				+ colCourse + ") REFERENCES " + coursesTable + " ("
				+ colCourseID + "));");
		db.execSQL("CREATE TABLE " + switchesTable + "(" + colSwitchID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + colDatetoSwitch
				+ " TEXT, " + colDayNumbertoSwitchto + " INTEGER)");

		/*
		 * db.execSQL("CREATE TRIGGER fk_clscourses_courseid " +
		 * " BEFORE INSERT "+ " ON "+clsesTable+
		 * 
		 * " FOR EACH ROW BEGIN"+
		 * " SELECT CASE WHEN ((SELECT "+colCourseID+" FROM "
		 * +coursesTable+" WHERE "+colCourseID+"=new."+colCourse+" ) IS NULL)"+
		 * " THEN RAISE (ABORT,'Foreign Key Violation') END;"+ "  END;");
		 */
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + clsesTable);
		db.execSQL("DROP TABLE IF EXISTS " + coursesTable);
		db.execSQL("DROP TABLE IF EXISTS " + switchesTable);
		onCreate(db);
	}

	public void InsertCourse(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colName, name);
		db.insert(coursesTable, colCourseID, cv);
		db.close();
	}

	public void InsertClass(Cls cls) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colStart, cls.getStart());
		cv.put(colEnd, cls.getEnd());
		cv.put(colCourse, cls.getCourse());
		cv.put(colWeekDayNumber, cls.getWeekDayNumber());

		db.insert(clsesTable, null, cv);

		db.close();
	}

	public void InsertSwitch(Switch swtch) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colDatetoSwitch, swtch.getDatetoSwitch().toString());
		cv.put(colDayNumbertoSwitchto, swtch.DaytoSwitchto);
		deleteSwitch(swtch.getDatetoSwitch());
		db.insert(switchesTable, null, cv);

		db.close();
	}

	public int UpdateCls(Cls cls) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colStart, cls.getStart());
		cv.put(colEnd, cls.getEnd());
		cv.put(colWeekDayNumber, cls.getWeekDayNumber());
		cv.put(colCourse, cls.getCourse());
		int i = db.update(coursesTable, cv, colClassID + "=?",
				new String[] { String.valueOf(cls.getClassID()) });
		db.close();
		return i;
	}

	public int getCourseIDbyName(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] columns = new String[] { "CourseID", colName };
		Cursor c = db.query(coursesTable, columns, colName + "=?",
				new String[] { name }, null, null, null, null);
		c.moveToFirst();
		
		int CourseID = c.getInt(0);
		c.close();
		db.close();
		return CourseID;
	}

	public String getCourseNamebyID(int ID) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] columns = new String[] { "CourseID", colName };
		Cursor c = db.query(coursesTable, columns, String.valueOf(colCourseID)
				+ "=?", new String[] { String.valueOf(ID) }, null, null, null,
				null);
		c.moveToFirst();
		String Name = c.getString(1);
		c.close();
		db.close();
		
		return Name;
	}

	public Cursor getAllClasses() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cur = db.rawQuery("SELECT " + colEnd + ", " + colStart + ", "
				+ colCourse + ", " + colWeekDayNumber + " FROM " + clsesTable
				+ " ORDER BY " + colStart + " asc", new String[] {});
		return cur;
	}

	public void recreateDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + clsesTable);
		db.execSQL("DROP TABLE IF EXISTS " + coursesTable);
		db.execSQL("DROP TABLE IF EXISTS " + switchesTable);

		onCreate(db);
		db.close();
	}

	public Cursor getDayClasses(int WeekDayNumber) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT " + colClassID + ", " + colEnd + ", "
				+ colStart + ", " + colCourse + ", " + colWeekDayNumber
				+ " FROM " + clsesTable + " WHERE " + colWeekDayNumber + " = "
				+ WeekDayNumber + " ORDER BY " + colStart + " asc",
				new String[] {});
		return cur;
	}

	public Boolean isClassOnDay(int WeekDayNumber, int Course) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT " + colCourse + ", "
				+ colWeekDayNumber + " FROM " + clsesTable + " WHERE "
				+ colWeekDayNumber + " = " + WeekDayNumber + " AND "
				+ colCourse + " = " + Course, new String[] {});
		
		if (cur.getCount() > 0) {
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}

	public Cursor getSwitchforToday(Date curDate) {

		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cur = db.rawQuery("SELECT " + colDatetoSwitch + ", "
				+ colDayNumbertoSwitchto + " FROM " + switchesTable + " WHERE "
				+ colDatetoSwitch + " = '" + curDate.toString() + "'",
				new String[] {});
		return cur;
		
		
		
	}

	public void deleteSwitch(Date datetoDelete) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(switchesTable,
				colDatetoSwitch + "='" + datetoDelete.toString() + "'", null);
	
	}

	public Cursor getAllCourses() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cur = db.rawQuery(
				"SELECT " + colCourseID + ", " + colName + " FROM "
						+ coursesTable + " ORDER BY " + colCourseID + " asc",
				new String[] {});
		return cur;
	}

	public void deleteAllCourses() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(coursesTable, null, null);
		db.close();
	}

	public void InsertCourses(String[] courseNames) {
		for (int i = 0; i < courseNames.length; i++) {
			InsertCourse(courseNames[i]);
		}
	}

	public void DeleteClasses(Cursor cur) {
		cur.moveToFirst();
		SQLiteDatabase db = this.getWritableDatabase();
		do {
			db.delete(clsesTable, colClassID + "=?",
					new String[] { cur.getString(0) });
			cur.moveToNext();
		} while (!cur.isAfterLast());
		cur.close();
		db.close();

	}

	public Calendar getSchoolStart() {
		return (Calendar) SchoolStart.clone();
	}

	public Calendar getSchoolEnd() {
		return (Calendar) SchoolEnd.clone();
	}

	public Object[] getNextClass(int nowMin, int dayOfWeek) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cur = db.rawQuery("SELECT " + colStart + ", " + colCourse + ", "
				+ colEnd + " FROM " + clsesTable + " WHERE " + colStart + " > "
				+ nowMin + " AND " + colWeekDayNumber + " = " + dayOfWeek
				+ " ORDER BY " + colStart + " asc", new String[] {});
		
		if (cur.getCount() < 1) {
			cur.close();
			return null;
		} else {
			cur.moveToFirst();
			Object[] returns = new Object[3];
			returns[0] = cur.getInt(0) - nowMin;
			returns[1] = this.getCourseNamebyID(cur.getInt(1));
			returns[2] = cur.getInt(2);
			cur.close();
			return returns;
		}
	}

	public Object[] getLastCurrentClass(int nowMin, int dayOfWeek) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cur = db.rawQuery("SELECT " + colEnd + ", " + colCourse
				+ " FROM " + clsesTable + " WHERE " + colStart + " < " + nowMin
				+ " AND " + colWeekDayNumber + " = " + dayOfWeek + " ORDER BY "
				+ colStart + " desc", new String[] {});

		if (cur.getCount() < 1) {
			cur.close();
			return null;
		} else {
			cur.moveToFirst();
			
			Object[] returns = new Object[2];
			returns[0] = cur.getInt(0);
			returns[1] = this.getCourseNamebyID(cur.getInt(1));
			cur.close();
			return returns;
		}
		
	}

}