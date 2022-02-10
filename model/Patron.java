package model;

import exception.InvalidPrimaryKeyException;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


public class Patron extends EntityBase {

    private static final String myTableName = "Patron";

    protected Properties dependencies;

    private String updateStatusMessage = "";

    public Patron(String patronId) throws InvalidPrimaryKeyException
    {
        super(myTableName);
        String query = "SELECT * FROM " + myTableName + " WHERE (patronId = " + patronId + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null){
            int size = allDataRetrieved.size();

            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple patrons matching patronId : " + patronId + " found.");
            }
            else
            {
                Properties retrievedPatronData = (Properties)allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedPatronData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedPatronData.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        else
        {
            throw new InvalidPrimaryKeyException("No patron matching patronId : " + patronId + " found.");
        }
    }

    public Patron(Properties props)
    {
        super(myTableName);

        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while(allKeys.hasMoreElements()==true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if(nextValue!=null)
            {
                persistentState.setProperty(nextKey,nextValue);
            }
        }
    }

    public void save(){
        updateStateInDatabase();
    }

    public void updateStateInDatabase(){
        try
        {
            if(persistentState.getProperty("patronId") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("patronId", persistentState.getProperty("patronId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Patron data for patronId : " + persistentState.getProperty("patronId") + " updated successfully in database!";
            }
            else
            {
                Integer patronId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("patronId", "" + patronId.intValue());
                updateStatusMessage = "Patron data for new patron : " +  persistentState.getProperty("patronId")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing patron data in database!";
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Patron Name: " + persistentState.getProperty("name") + ", Address: " +
                persistentState.getProperty("address")  + ", City: " +
                persistentState.getProperty("city") + ", Zip: "+
                persistentState.getProperty("zip")+ ", Email: "+ persistentState.getProperty("email")
                +", DateOfBirth: "+ persistentState.getProperty("dateOfBirth");
    }

    // compare
    public void display(){
        System.out.println(toString());
    }


    public static int compare(Patron a, Patron b)
    {
        String aNum = (String)a.getState("patronId");
        String bNum = (String)b.getState("patronId");

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
    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}