package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


public class Book extends EntityBase{

    private static final String myTableName = "Book";

    protected Properties dependencies;

    private String updateStatusMessage = "";

    public Book(String bookId) throws exception.InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE (bookId = " + bookId + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        // You must get one book at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one book. More than that is an error
            if (size != 1)
            {
                throw new exception.InvalidPrimaryKeyException("Multiple books matching id : " + bookId + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedBookData = (Properties)allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedBookData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedBookData.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no book found for this id, throw an exception
        else
        {
            throw new exception.InvalidPrimaryKeyException("No book matching id : " + bookId + " found.");
        }
    }
    public Book(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    public void save()
    {
        updateStateInDatabase();
    }

    private void updateStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("bookId") != null)
            {
                Properties prop = new Properties();
                prop.setProperty("bookId", persistentState.getProperty("bookId"));
                updatePersistentState(mySchema, persistentState, prop);
                updateStatusMessage = "Book data for bookId number : " + persistentState.getProperty("bookId") + " updated successfully in database!";
            }
            else
            {
                Integer bookId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("bookId", "" + bookId.intValue());
                updateStatusMessage = "Book data for new book : " +  persistentState.getProperty("bookId") + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing account data in database!";
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("Update", "UpdateStatusMessage");
        dependencies.setProperty("ServiceCharge", "UpdateStatusMessage");

        myRegistry.setDependencies(dependencies);
    }

    @Override
    public String toString() {
        return "Title: " + persistentState.getProperty("bookTitle") + ", Author: " +
                persistentState.getProperty("author")  + ", Year: " +
                persistentState.getProperty("pubYear") ;
    }

    public void display() {
        System.out.println(toString());
    }

    public static int compare(Book a, Book b)
    {
        String aNum = (String)a.getState("bookId");
        String bNum = (String)b.getState("bookId");

        return aNum.compareTo(bNum);
    }

    public Object getState(String key) {
        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value) {
        if (value != null) {
            persistentState.setProperty(key, (String)value);
        }
    }
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
