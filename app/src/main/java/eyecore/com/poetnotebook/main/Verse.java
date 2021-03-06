package eyecore.com.poetnotebook.main;

import org.joda.time.LocalDate;

public class Verse
{
    private String verseName;
    private String verseAuthor;
    private String verseText;
    private String authorID;
    private LocalDate verseDate;
    private int verseID;


    public Verse()
    {
        verseDate = LocalDate.now();
    }

    public Verse(String name, String text)
    {
        verseName = name;
        verseText = text;
        verseDate = LocalDate.now();
    }

    public String getVerseName()
    {
        return verseName;
    }

    public void setVerseName(String verseName)
    {
        this.verseName = verseName;
    }

    public String getVerseAuthor()
    {
        return verseAuthor;
    }

    public void setVerseAuthor(String verseAuthor)
    {
        this.verseAuthor = verseAuthor;
    }

    public String getVerseText()
    {
        return verseText;
    }

    public void setVerseText(String verseText)
    {
        this.verseText = verseText;
    }

    public LocalDate getVerseDate() {
        return verseDate;
    }

    public void setVerseDate(LocalDate verseDate) {
        this.verseDate = verseDate;
    }

    public int getVerseID()
    {
        return verseID;
    }

    public void setVerseID(int verseID)
    {
        this.verseID = verseID;
    }

    public String getAuthorID()
    {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }
}
