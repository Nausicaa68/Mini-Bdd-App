import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;

public class ManageADataBase {

    public ManageADataBase(Statement st, String path){

        File file = new File(path);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc != null && sc.hasNextLine()) {
            try {
                st.execute( sc.nextLine() );
            } catch (SQLException e) {
                System.out.println("Database not created !");
                e.printStackTrace();
            }
        }
        System.out.println("Database created successfully.");
        //connex.commit(); //my autocommit is set to 1

    }
    

    public static void fillDataBase(Statement st, String[] sql) {
        try {
            for(String i : sql){
                st.execute(i);
            }
            System.out.println("Database filled successfully.");
            //connex.commit(); //my autocommit is set to 1
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addValueInDataBase(Statement st, String table) {

        Scanner scan = new Scanner(System.in);
        String name, position, payrate;
        int idnumber = 0;

        ResultSet rs = null;
        try {
            rs = st.executeQuery("select max(PersonID) from " + table);
            if (rs.next()) {
                /*
                Basically you are positioning the cursor before the first row and then requesting data.
                You need to move the cursor to the first row.
                https://stackoverflow.com/questions/2120255/resultset-exception-before-start-of-result-set
                 */
                idnumber = rs.getInt(1) + 1; //the id of the new entry = largest id in DB + 1
            }


            System.out.println("Enter a new value : ");
            System.out.print("Name : ");
            name = scan.nextLine();
            System.out.print("Position : ");
            position = scan.nextLine();
            System.out.print("PayRate : ");
            payrate = scan.nextLine();

            String request = "INSERT INTO " + table + " VALUES " +
                    "(" + idnumber + ", \"" + name + "\", \"" + position + "\", " + payrate + ")";

            System.out.println(request);

            try {
                st.executeUpdate(request);
                System.out.println("Value succesfully adds");
                //connex.commit(); //my autocommit is set to 1
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void showDatabase(Statement st, String table) {

        ResultSet rs = null;
        try {
            rs = st.executeQuery("Select * from " + table);
            //connex.commit(); //my autocommit is set to 1
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) {
                System.out.print(rs.getString(1));
                System.out.print("   ");
                System.out.print(rs.getString(2));
                System.out.print("   ");
                System.out.print(rs.getString(3));
                System.out.print("   ");
                System.out.println(rs.getString(4));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void deleteValueInDatBase(Statement st, String table) {
        int idToDelete = 0, numberOfId = 0;

        ResultSet rs = null;
        try {
            rs = st.executeQuery("select max(PersonID) from " + table);
            //connex.commit(); //my autocommit is set to 1
            if (rs.next()) {
                /*
                Basically you are positioning the cursor before the first row and then requesting data.
                You need to move the cursor to the first row.
                https://stackoverflow.com/questions/2120255/resultset-exception-before-start-of-result-set
                 */
                numberOfId = rs.getInt(1); //we try to get the largest id to secure the input after
            }

            System.out.println("Enter the id you want to delete : ");
            idToDelete = Main.getSecureValueFromUser(1, numberOfId);

            String request = "delete from " + table + " where PersonID = " + idToDelete + "";
            System.out.println(request);

            try {
                st.executeUpdate(request);
                System.out.println("Value succesfully delete");
                //connex.commit(); //my autocommit is set to 1
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean deleteDataBase(Statement st) {

        System.out.println("Are you sure you want to delete all the DataBase (1 - yes | 2 - no) : ");
        int sure = Main.getSecureValueFromUser(1, 2);

        if (sure == 1) {
            String request = "DROP DATABASE personnel";
            System.out.println(request);

            try {
                st.executeUpdate(request);
                System.out.println("Database succesfully delete");
                //connex.commit(); //my autocommit is set to 1
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Ah ok, you scared me !");
        }
        return true;
    }

}
