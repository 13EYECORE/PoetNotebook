package eyecore.com.poetnotebook.database;

import java.util.List;

import eyecore.com.poetnotebook.Verse;

public interface IDataBaseHandler
{
    public void AddVerse(Verse verse);
    public Verse getVerse(int id);
    public List<Verse> getAllVerses();
    public int getVersesCount();
    public int updateVerse(Verse verse);
    public void deleteVerse(Verse verse);
    public void deleteAllVerses();
}