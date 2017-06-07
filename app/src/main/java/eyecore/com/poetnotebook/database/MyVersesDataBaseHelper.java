package eyecore.com.poetnotebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import eyecore.com.poetnotebook.main.Verse;
import eyecore.com.poetnotebook.main.VerseType;

public class MyVersesDataBaseHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final int NEW_DB_VERSION = 2;
    private static final String DB_NAME = "MY_VERSES_BASE";
    private static final String TABLE_NAME = "myverses";
    private static final String TABLE_NAME_2 = "favourites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_TEXT = "versetext";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_AUTHOR_ID = "authorid";


    //new db constructor
    public MyVersesDataBaseHelper(Context context, String dbName)
    {
        super(context, DB_NAME + "_" + dbName, null, NEW_DB_VERSION);
    }

    //old db constructor (for app ver. 1.0.2)
    public  MyVersesDataBaseHelper(Context context)
    {
        super(context, DB_NAME, null, NEW_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_TEXT + " TEXT, "
                + COLUMN_DATE + " TEXT" + ");");

        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME_2 + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_TEXT + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_AUTHOR_ID + " TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        if (i == DB_VERSION & i2 == NEW_DB_VERSION)
        {
            sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME_2 + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_AUTHOR + " TEXT, "
                    + COLUMN_TEXT + " TEXT, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_AUTHOR_ID + " TEXT" + ");");
        }
    }

    public void addVerse(Verse verse, int verseType)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, verse.getVerseName());
        cv.put(COLUMN_AUTHOR, verse.getVerseAuthor());
        cv.put(COLUMN_TEXT, verse.getVerseText());
        cv.put(COLUMN_DATE, verse.getVerseDate().toString());

        switch (verseType)
        {
            case VerseType.MY_VERSE:

                db.insert(TABLE_NAME, null, cv);
                break;

            case VerseType.FAVOURITE_VERSE:

                cv.put(COLUMN_AUTHOR_ID, verse.getAuthorID());
                db.insert(TABLE_NAME_2, null, cv);
                break;
        }

        db.close();
    }

    public Verse getVerse(int id, int verseType)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Verse verse = new Verse();
        Cursor cursor;

        switch (verseType)
        {
            case VerseType.MY_VERSE:

                cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AUTHOR,
                        COLUMN_TEXT, COLUMN_DATE}, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)}, null, null, null, null);
                cursor.moveToFirst();
                verse.setVerseID(Integer.parseInt(cursor.getString(0)));
                verse.setVerseName(cursor.getString(1));
                verse.setVerseAuthor(cursor.getString(2));
                verse.setVerseText(cursor.getString(3));
                verse.setVerseDate(new LocalDate(cursor.getString(4)));
                cursor.close();
                break;

            case VerseType.FAVOURITE_VERSE:

                cursor = db.query(TABLE_NAME_2, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AUTHOR,
                        COLUMN_TEXT, COLUMN_DATE, COLUMN_AUTHOR_ID}, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)}, null, null, null, null);
                cursor.moveToFirst();
                verse.setVerseID(Integer.parseInt(cursor.getString(0)));
                verse.setVerseName(cursor.getString(1));
                verse.setVerseAuthor(cursor.getString(2));
                verse.setVerseText(cursor.getString(3));
                verse.setVerseDate(new LocalDate(cursor.getString(4)));
                verse.setAuthorID(cursor.getString(5));
                cursor.close();
                break;
        }

        db.close();
        return verse;
    }

    public List<Verse> getAllVerses(int verseType)
    {
        List<Verse> verses = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        switch (verseType)
        {
            case VerseType.MY_VERSE:
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

                if (cursor.moveToFirst())
                {
                    do
                    {
                        Verse verse = new Verse();
                        verse.setVerseID(Integer.parseInt(cursor.getString(0)));
                        verse.setVerseName(cursor.getString(1));
                        verse.setVerseAuthor(cursor.getString(2));
                        verse.setVerseText(cursor.getString(3));
                        verse.setVerseDate(new LocalDate(cursor.getString(4)));
                        verses.add(verse);
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();
                break;

            case VerseType.FAVOURITE_VERSE:
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);

                if (cursor.moveToFirst())
                {
                    do
                    {
                        Verse verse = new Verse();
                        verse.setVerseID(Integer.parseInt(cursor.getString(0)));
                        verse.setVerseName(cursor.getString(1));
                        verse.setVerseAuthor(cursor.getString(2));
                        verse.setVerseText(cursor.getString(3));
                        verse.setVerseDate(new LocalDate(cursor.getString(4)));
                        verse.setAuthorID(cursor.getString(5));
                        verses.add(verse);
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();
                break;
        }

        db.close();
        return verses;
    }

    public int getVersesCount(int verseType)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        int count = 0;

        switch (verseType)
        {
            case VerseType.MY_VERSE:
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
                cursor.close();
                count = cursor.getCount();
                break;

            case VerseType.FAVOURITE_VERSE:
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);
                cursor.close();
                count = cursor.getCount();
                break;
        }

        db.close();
        return count;
    }

    public int updateVerse(Verse verse)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, verse.getVerseName());
        cv.put(COLUMN_AUTHOR, verse.getVerseAuthor());
        cv.put(COLUMN_TEXT, verse.getVerseText());
        cv.put(COLUMN_DATE, verse.getVerseDate().toString());

        return db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(verse.getVerseID())});
    }

    public void deleteVerse(Verse verse, int verseType)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        switch (verseType)
        {
            case VerseType.MY_VERSE:
                db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(verse.getVerseID())});
                break;

            case VerseType.FAVOURITE_VERSE:
                db.delete(TABLE_NAME_2, COLUMN_ID + " = ?", new String[]{String.valueOf(verse.getVerseID())});
                break;
        }

        db.close();
    }

    public void deleteAllVerses(int verseType)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        switch (verseType)
        {
            case VerseType.MY_VERSE:
                db.delete(TABLE_NAME, null, null);
                break;

            case VerseType.FAVOURITE_VERSE:
                db.delete(TABLE_NAME_2, null, null);
                break;
        }

        db.close();
    }
}






