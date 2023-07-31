package Assignment1;

/*
    CSC 241 Fall 2022
    Assignment 1
    Name: Declan ONUNKWO
    ID: 806075156
 */

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ZooManager {
    // Below are some local variables
    private static final int LIMIT = 1000000;
    private static Scanner menuNumber = new Scanner(System.in);
    private static Scanner iDFinder = new Scanner(System.in);
    /*I'm trying to create some sort of parallel arrays here,
    since I ran out of ideas on how to solve this.*/
    /*Also, the ArrayList isn't made final because I want to ensure when the
    file is edited using the edit command, my newly created file would be
    read again. Thus change is final in the file not the ArrayList*/
    private static ArrayList<String> nameArray = new ArrayList<>(LIMIT);
    private static ArrayList<String> iDArray = new ArrayList<>(LIMIT);
    private static ArrayList<String> sexArray = new ArrayList<>(LIMIT);
    private static ArrayList<String> ageArray = new ArrayList<>(LIMIT);
    private static ArrayList<String> dateArray = new ArrayList<>(LIMIT);
    private static ArrayList<String> areaArray = new ArrayList<>(LIMIT);
    // Since they are parallel, I can create a single counter that should work for all.
    private static int counter = 0; // This counter would be updated in the while loop in the main method.

    // Main method
    public static void main (String[] args) throws Exception{
        String dataName = "Animals";
        String filePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"),
                "src","Assignment1","data", dataName +".txt").toAbsolutePath().toString();
        File inputFile = new File(filePath);
        Scanner toScan = new Scanner (inputFile);
        System.out.println("Welcome to the ZooManager!");
        System.out.println();

        while (toScan.hasNext()){
            String data = toScan.nextLine();
            String name, iD, sex, age, date, area;
            name = data.substring(0,data.indexOf(","));
            nameArray.add(name);
            iD = data.substring(data.indexOf(",")+2, data.indexOf(",")+10);
            iDArray.add(iD);
            sex = data.substring(data.indexOf(",")+12, data.indexOf(",")+13);
            sexArray.add(sex);
            age = data.substring(data.indexOf(",")+14, data.lastIndexOf(",")-12);
            ageArray.add(age);
            date = data.substring(data.lastIndexOf(",")-10, data.lastIndexOf(","));
            dateArray.add(date);
            area = data.substring(data.lastIndexOf(" ")+1);
            areaArray.add(area);
            counter = counter +1; //Updated counter. All arrays should have an equal amount of elements.
        }
        toScan.close();



        mainMenu();  /*Display the main menu.
        This would also be present at the end of other sub methods to keep the menu running.*/
    }


    private static void mainMenu() {
        System.out.println("Choose menu [1:view 2:find 3:edit 4:quit]?");
        System.out.print(">>> ");
       /* Changed the line below to a string since I couldn't fix
        the error that pops up when the user types something other than an integer*/
        String userNumber = menuNumber.next();
        switch (userNumber) {
            case "1" -> runViewMenu();
            case "2" -> runFindMenu();
            case "3" -> runEditMenu();
            case "4" -> System.exit(0);
            default -> System.out.println("INVALID INPUT! Use the menu numbers.");
        }
        mainMenu();
    }

    private static void runViewMenu() {
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        // I do not know if there's an easier way to do this, but again, this is all I could come up with.
        for(int x=0; x < counter; x++){
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                    nameArray.get(x),iDArray.get(x),sexArray.get(x),ageArray.get(x),dateArray.get(x),areaArray.get(x));
        }
        mainMenu();
    }

    private static void runFindMenu() {
        System.out.print("Enter Animal ID you want to find >>>");
        String selectedID = iDFinder.next();
        /* The lines below check to see if the user input is contained in the array of IDs
         If it is, the index of that input is stored in a variable and printed using print format.
         else, an error would pop up*/
        if(iDArray.contains(selectedID)){
            int i = iDArray.indexOf(selectedID); // Lets use "i" for index
            System.out.println("-----------------------------------------------------------------");
            System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
            System.out.println("-----------------------------------------------------------------");
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                    nameArray.get(i),iDArray.get(i),sexArray.get(i),ageArray.get(i),dateArray.get(i),areaArray.get(i));
        }else{
            System.out.println("INVALID INPUT! Try again.\n"+"N.B This menu is case sensitive");
            runFindMenu();
        }
        mainMenu();
    }

    private static void runEditMenu() {
        System.out.print("Enter Animal ID you want to change >>>");
        String selectedID = iDFinder.next();
        /*Just a copy and paste of some code in the runFindMenu method
        Would have loved to create a seperate method that both can share, but the
        recursive call after the error message would not work well*/
        if (iDArray.contains(selectedID)) {
            int i = iDArray.indexOf(selectedID);
            System.out.println("-----------------------------------------------------------------");
            System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
            System.out.println("-----------------------------------------------------------------");
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                    nameArray.get(i), iDArray.get(i), sexArray.get(i), ageArray.get(i), dateArray.get(i), areaArray.get(i));
            runEditMenu2(i);
        } else {
            System.out.println("INVALID INPUT! Try again.\n" + "N.B This menu is case sensitive");
            runEditMenu();
        }
    }

        private static void runEditMenu2(int i) {
            int newIndex = i;  /* reassigning this again. I couldn't access it in the next sub methods
                                              probably because its not a global variable, but this works :) */
            System.out.print("What do you want to change? [1:species 2:id 3:sex 4:age 5:date 6:area] >>>");
            String choice = iDFinder.next();
            switch (choice) {
                case "1" -> editSpecies(newIndex);
                case "2" -> editID(newIndex);
                case "3" -> editSex(newIndex);
                case "4" -> editAge(newIndex);
                case "5" -> editDate(newIndex);
                case "6" -> editArea(newIndex);
                default -> System.out.println("INVALID INPUT! Try again with the options given.");
            }
            runEditMenu2(i);
        }

    private static void editArea(int newIndex){
        System.out.print("Change Area to >>>");
        String changeMade = iDFinder.next();
        areaArray.set(newIndex, changeMade);
        replacementFile();
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                nameArray.get(newIndex), iDArray.get(newIndex), sexArray.get(newIndex), ageArray.get(newIndex), dateArray.get(newIndex), areaArray.get(newIndex));
        mainMenu();
    }

    private static void editDate(int newIndex) {
        System.out.print("Change Date to >>>");
        String changeMade = iDFinder.next();
        dateArray.set(newIndex, changeMade);
        replacementFile();
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                nameArray.get(newIndex), iDArray.get(newIndex), sexArray.get(newIndex), ageArray.get(newIndex), dateArray.get(newIndex), areaArray.get(newIndex));
        mainMenu();
    }

    private static void editAge(int newIndex) {
        System.out.print("Change Age to >>>");
        String changeMade = iDFinder.next();
        ageArray.set(newIndex, changeMade);
        replacementFile();
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                nameArray.get(newIndex), iDArray.get(newIndex), sexArray.get(newIndex), ageArray.get(newIndex), dateArray.get(newIndex), areaArray.get(newIndex));
        mainMenu();
    }

    private static void editSex(int newIndex) {
        System.out.print("Change Sex to >>>");
        String changeMade = iDFinder.next();
        sexArray.set(newIndex, changeMade);
        replacementFile();
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                nameArray.get(newIndex), iDArray.get(newIndex), sexArray.get(newIndex), ageArray.get(newIndex), dateArray.get(newIndex), areaArray.get(newIndex));
        mainMenu();
    }

    private static void editID(int newIndex) {
        System.out.print("Change ID to >>>");
        String changeMade = iDFinder.next();
        iDArray.set(newIndex, changeMade);
        replacementFile();
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                nameArray.get(newIndex), iDArray.get(newIndex), sexArray.get(newIndex), ageArray.get(newIndex), dateArray.get(newIndex), areaArray.get(newIndex));
        mainMenu();
    }

    private static void editSpecies(int newIndex) {
        System.out.print("Change Species to >>>");
        String changeMade = iDFinder.next();
        nameArray.set(newIndex, changeMade);
        replacementFile();
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                nameArray.get(newIndex), iDArray.get(newIndex), sexArray.get(newIndex), ageArray.get(newIndex), dateArray.get(newIndex), areaArray.get(newIndex));
        mainMenu();
    }

    private static void replacementFile(){
        // needed this input file here so i can delete later.
        String dataName = "Animals";
        String filePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"),
                "src","Assignment1","data", dataName +".txt").toAbsolutePath().toString();
        File inputFile = new File(filePath);
        // new output file to store data temporarily
        File outputFile = new File(filePath+".tmp");

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
        BufferedWriter br = new BufferedWriter(outputWriter);

        for(int k=0; k<counter; k++) {
            try {
                br.write(nameArray.get(k) + ", " + iDArray.get(k) + ", " + sexArray.get(k) + ", " + ageArray.get(k) + ", " + dateArray.get(k) + ", " + areaArray.get(k));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                br.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFile.delete();
        outputFile.renameTo(new File(filePath));
    }
}

    /*I know this program has a terrible time and space complexity. There are probably better ways to code this.*/



