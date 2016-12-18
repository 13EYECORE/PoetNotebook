package eyecore.com.poetnotebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import eyecore.com.poetnotebook.Verse;

public class MyVersesDataBaseHelper extends SQLiteOpenHelper implements IDataBaseHandler
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MY_VERSES_BASE";
    private static final String TABLE_NAME = "myverses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_TEXT = "versetext";
    private static final String COLUMN_DATE = "date";

    public MyVersesDataBaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {

    }

    @Override
    public void AddVerse(Verse verse)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, verse.getVerseName());
        cv.put(COLUMN_AUTHOR, verse.getVerseAuthor());
        cv.put(COLUMN_TEXT, verse.getVerseText());
        cv.put(COLUMN_DATE, verse.getVerseDate().toString());
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    @Override
    public Verse getVerse(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AUTHOR, COLUMN_TEXT, COLUMN_DATE},
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Verse myVerse = new Verse();
        myVerse.setVerseID(Integer.parseInt(cursor.getString(0)));
        myVerse.setVerseName(cursor.getString(1));
        myVerse.setVerseAuthor(cursor.getString(2));
        myVerse.setVerseText(cursor.getString(3));
        myVerse.setVerseDate(new LocalDate(cursor.getString(4)));
        cursor.close();
        db.close();
        return myVerse;
    }

    @Override
    public List<Verse> getAllVerses()
    {
        List<Verse> myVerses = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Verse myVerse = new Verse();
                myVerse.setVerseID(Integer.parseInt(cursor.getString(0)));
                myVerse.setVerseName(cursor.getString(1));
                myVerse.setVerseAuthor(cursor.getString(2));
                myVerse.setVerseText(cursor.getString(3));
                myVerse.setVerseDate(new LocalDate(cursor.getString(4)));
                myVerses.add(myVerse);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return myVerses;
    }

    @Override
    public int getVersesCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.close();
        db.close();
        return cursor.getCount();
    }

    @Override
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

    @Override
    public void deleteVerse(Verse verse)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(verse.getVerseID())});
        db.close();
    }

    @Override
    public void deleteAllVerses()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}






