import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

import static javax.swing.JOptionPane.showInputDialog;

public class Main {

    static final String DB_URL = "jdbc:mysql://localhost:3306/?characterEncoding=latin1";
    static final String USER = "root";
    static final String PASS = showInputDialog("SQL root password needed : ");
    static final String DRIVER = "com.mysql.jdbc.Driver";

    static boolean stateOfDatabase = true;

    public static void main(String[] args) throws ClassNotFoundException {

        Connection connex = null;
        try {
            Class.forName(DRIVER);

            System.out.println("Get connection ...");
            connex = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection created");

            //création de l'état de connexion
            Statement st = connex.createStatement();

            System.out.println("Creating the database ...");
            ManageADataBase myCompagnieDatabase = new ManageADataBase(st, "src/sql.txt");

            System.out.println("All complete");

            int choice = 0;
            do {

                System.out.println("------------------------------------");
                System.out.println("What do you want to do ? ");
                System.out.println("1 - Watch the database");
                System.out.println("2 - Enter a new value");
                System.out.println("3 - Delete a value");
                System.out.println("4 - Delete the database");
                System.out.println("5 - Quit");
                System.out.print("Your choice : ");
                choice = getSecureValueFromUser(1, 5);
                System.out.println("------------------------------------");

                if (stateOfDatabase) {
                    switch (choice) {
                        case 1 -> myCompagnieDatabase.showDatabase(st, "Employee");
                        case 2 -> myCompagnieDatabase.addValueInDataBase(st, "Employee");
                        case 3 -> myCompagnieDatabase.deleteValueInDatBase(st, "Employee");
                        case 4 -> stateOfDatabase = myCompagnieDatabase.deleteDataBase(st);
                        case 5 -> System.out.println("Bye");
                        default -> System.out.println("Error. Impossible choice.");
                    }
                }
                else if(!stateOfDatabase && choice != 5){
                    System.out.println("There is no more database.");
                }

                }while (choice != 5) ;

            connex.close();
            System.out.println("Connection closed");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static int getSecureValueFromUser(int min, int max) {
        boolean ok = false;
        Scanner scan = new Scanner(System.in);
        int value = 0;

        do {
            try {
                value = scan.nextInt();
                if ((value >= min) && (value <= max)) {
                    ok = true;
                } else {
                    System.out.println("Error. Input is not between " + min + " and " + max + ".");
                }
            } catch (Exception e) {
                System.out.println("Error. Input is not an integer.");
            }

        } while (!ok);

        return value;
    }

}
