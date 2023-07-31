package Assignment7;

/*
    CSC 241 Fall 2022
    Assignment 7
    Name: Declan ONUNKWO
    ID: 806075156
 *//**
   (1) The print menu was created as menu number 7.
   (2) If you choose to save, the file would be saved in the "data" package of this assignment 7.
   (3) The saved file(s) can only be viewed or seen after you quit the program.
   (4) I attempted the extra work to implementing the queue data structure. Please use 7q to access it.
 */

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.util.*;

public class ZooManager {
    private static Scanner menuNumber = new Scanner(System.in);
    private static Scanner iDFinder = new Scanner(System.in);

    public static void main (String[] args){ // main method
        System.out.println("Welcome to the ZooManager");
        System.out.println("\n -Use the menu numbers below as a guide. \n -Insert \"a\" after " +
                "menu number 1-5 to access the Area menu.\n -Insert \"q\" after menu number 7 to " +
                "print in queue format, else you get a stack print.\n");
        mainMenu();
    }

    // This method below would keep reading the file so that once an update has been made, it will change in the main menu
    private static JsonObject readFile(){
        // Properties
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read a json file
        String packageName = ZooManager.class.getPackageName();
        String dataName = "Animals";
        String filePath = prop.getProperty("filepath") + File.separator + packageName + File.separator + "data" + File.separator + dataName + ".json";
        File inputFile = new File(filePath);

        // read a json file
        InputStream input = null;
        try {
            input = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonReader read = Json.createReader(input);
        JsonObject obj = read.readObject();
        read.close();
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static void mainMenu() {
        JsonObject obj = readFile();
        System.out.println("Choose menu [1:view 2:find 3:edit 4:add 5:remove 6:validate 7:print 8:quit]?");
        System.out.print(">>> ");
        String userNumber = menuNumber.next();
        switch (userNumber) {
            case "1" -> runViewMenu(obj);
            case "1a" -> runViewMenuA(obj);
            case "2" -> runFindMenu(obj);
            case "2a" -> runFindMenuA(obj);
            case "3" -> runEditMenu(obj);
            case "3a" -> runEditMenuA(obj);
            case "4" -> runAddMenu(obj);
            case "5" -> runRemoveMenu(obj);
            case "5a" -> runRemoveMenuA(obj);
            case "6" -> runValidateMenu(obj);
            case "7" -> runPrintMenu(obj);
            case "7q" -> runPrintMenuQ(obj);
            case "8" -> {
                System.out.println("Exiting ZooManager...");
                System.exit(0);
            }
            default -> System.out.println("INVALID INPUT! Use the menu numbers.");
        }
        mainMenu();
    }

    private static JsonArrayBuilder areasArray(JsonObject obj){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        int counter = obj.getJsonArray("Areas").size();
        for(int i=0; i<counter; i++){
            int counter2 = obj.getJsonArray("Areas").getJsonObject(i).getJsonArray("Area").size();
            for(int j=0; j<counter2; j++){
                builder.add(obj.getJsonArray("Areas").getJsonObject(i).getJsonArray("Area").get(j));
            }
        }
        return builder;
    }

    private static void runViewMenuA(JsonObject obj) {
        JsonArray animalsJArray = obj.getJsonArray("Animals");
        JsonArray areasArray = areasArray(obj).build();
        HashMap<String,String> speciesAndArea = new HashMap<>();
        for (int i=0; i<animalsJArray.size(); i++){
            speciesAndArea.put(animalsJArray.get(i).asJsonObject().getString("Species"),
                    animalsJArray.get(i).asJsonObject().getString("Area"));
        }
        System.out.println("---------------------------------------------------");
        System.out.printf("%-12s | %-20s | %-18s %n","Area", "Species", "# of Animals");
        System.out.println("---------------------------------------------------");
        for(int i=0; i<areasArray.size(); i++){
            String name = areasArray.get(i).asJsonObject().getString("Species of Animal");
            String area = speciesAndArea.get(name);
            int numberOfAnimals = areasArray.get(i).asJsonObject().getInt("Number of Animals Stayed");
            System.out.printf("%-12s   %-20s   %-18s %n", area,name,numberOfAnimals);
        }
    }

    private static void runViewMenu(JsonObject obj) {
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        JsonArray zooArray = obj.getJsonArray("Animals");
        for (JsonValue value: zooArray){
            String name = value.asJsonObject().getString("Species");
            String iD = value.asJsonObject().getString("ID");
            String sex = value.asJsonObject().getString("Sex");
            int age = value.asJsonObject().getInt("Age");
            String date = value.asJsonObject().getString("Imported Date");
            String area = value.asJsonObject().getString("Area");
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                        name,iD,sex,age,date,area);
        }
    }

    private static void runFindMenuA(JsonObject obj) {
        System.out.print("Enter area you want to find: ");
        String areaToFind = iDFinder.next();
        JsonArray animalsJArray = obj.getJsonArray("Animals");
        JsonArray areasArray = areasArray(obj).build();
        HashMap<String,String> areaAndSpecies = new HashMap<>();
        HashMap<String,Integer> speciesAndNumber = new HashMap<>();
        for (int i=0; i<animalsJArray.size(); i++){
            areaAndSpecies.put(animalsJArray.get(i).asJsonObject().getString("Area"),
                    animalsJArray.get(i).asJsonObject().getString("Species"));
        }
        for (int i=0; i<areasArray.size(); i++){
            speciesAndNumber.put(areasArray.get(i).asJsonObject().getString("Species of Animal"),
                    areasArray.get(i).asJsonObject().getInt("Number of Animals Stayed"));
        }
        if (areaAndSpecies.containsKey(areaToFind)){
            String name = areaAndSpecies.get(areaToFind);
            String area = areaToFind;
            int numberOfAnimals = speciesAndNumber.get(name);
            System.out.println("---------------------------------------------------");
            System.out.printf("%-12s | %-20s | %-18s %n","Area", "Species", "# of Animals");
            System.out.println("---------------------------------------------------");
            System.out.printf("%-12s   %-20s   %-18s %n", area,name,numberOfAnimals);
        }else System.out.println(areaToFind + " does not exist in database.");
    }

    private static void runFindMenu(JsonObject obj) {
        System.out.print("Enter animal you want to find: ");
        iDFinder.useDelimiter("\n");
        String findMenu = iDFinder.next();
        String[] str = findMenu.split(", ");
        Boolean forPrintMenu = false;
        Boolean forPrintMenuQ = false;
        switch (str[0]) {
            case "species" -> findSpecies(str,obj,forPrintMenu,forPrintMenuQ);
            case "id" -> findID(str,obj,forPrintMenu,forPrintMenuQ);
            case "sex" -> findSex(str,obj,forPrintMenu,forPrintMenuQ);
            case "age" -> findAge(str,obj,forPrintMenu,forPrintMenuQ);
            case "date" -> findDate(str,obj,forPrintMenu,forPrintMenuQ);
            case "area" -> findArea(str,obj,forPrintMenu,forPrintMenuQ);
            default -> System.out.println("INVALID KEY! Valid keys are: species, id, sex, age, date and area.");
        }
        runFindMenu(obj);
    }

    private static ArrayList buildingAnArray(JsonObject obj, Boolean stringChecker, String key){
        JsonArray jArray = obj.getJsonArray("Animals");
        ArrayList buildAnArray = new ArrayList<>();
        if(stringChecker){
            for (int i = 0; i< jArray.size(); i++){
                buildAnArray.add(jArray.get(i).asJsonObject().getString(key)+" #"+i);
            }
        }else{
            for (int i = 0; i< jArray.size(); i++){
                buildAnArray.add(jArray.get(i).asJsonObject().getInt(key));
            }
        }
        return buildAnArray; // This function builds/ creates our ArrayList of objects.
    }

    private static void findSpecies(String[] str, JsonObject obj, Boolean forPrintMenu, Boolean forPrintMenuQ) {
        Boolean stringChecker = true;
        String key = "Species";
        ArrayList<String> unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj, forPrintMenu, forPrintMenuQ);
    }

    private static void findID(String[] str, JsonObject obj, Boolean forPrintMenu, Boolean forPrintMenuQ) {
        Boolean stringChecker = true;
        String key = "ID";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj, forPrintMenu, forPrintMenuQ);
    }

    private static void findSex(String[] str, JsonObject obj, Boolean forPrintMenu, Boolean forPrintMenuQ) {
        Boolean stringChecker = true;
        String key = "Sex";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj, forPrintMenu, forPrintMenuQ);
    }

    private static void findAge(String[] str, JsonObject obj, Boolean forPrintMenu, Boolean forPrintMenuQ) {
        Boolean stringChecker = false; // to show that age is not a string, and must use the "getInt" method.
        String key = "Age";
        ArrayList<Integer> unsortedArray = buildingAnArray(obj, stringChecker, key);
        ArrayList<String> indexes = new ArrayList<>();
        if (str[1].contains("-")) {
            int minCondition = Integer.parseInt(str[1].substring(0, str[1].indexOf("-")));
            int maxCondition = Integer.parseInt(str[1].substring(str[1].indexOf("-") + 1));
            for(int i = 0; i<unsortedArray.size(); i++){
                if(unsortedArray.get(i)>=minCondition & unsortedArray.get(i)<=maxCondition){
                    indexes.add(String.valueOf(i));
                }
            }
        } else{
            int ageToFind = Integer.parseInt(str[1]);
            for(int i = 0; i<unsortedArray.size(); i++) {
                if (unsortedArray.get(i) == ageToFind) {
                    indexes.add(String.valueOf(i));
                }
            }
        }
        printTheObjects(indexes, obj, str, forPrintMenu, forPrintMenuQ);
    }

    private static void findDate(String[] str, JsonObject obj, Boolean forPrintMenu, Boolean forPrintMenuQ) {
        Boolean stringChecker = true;
        String key = "Imported Date";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj, forPrintMenu, forPrintMenuQ);
    }

    private static void findArea(String[] str, JsonObject obj, Boolean forPrintMenu, Boolean forPrintMenuQ) {
        Boolean stringChecker = true;
        String key = "Area";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj, forPrintMenu, forPrintMenuQ);
    }

    private static void doMergeSort(ArrayList unsortedArray){ // Divide!
        int arrayLength = unsortedArray.size();
        if(arrayLength < 2){return;} // An array less than two is one and therefore sorted.
        int midPoint = (arrayLength)/2;
        ArrayList leftHalf = new ArrayList<>(); // When split, left half would be stored here
        ArrayList rightHalf = new ArrayList<>(); // When split, right half would be stored here

        for(int i=0; i<midPoint; i++){ // Filling up the left half
            leftHalf.add(unsortedArray.get(i));
        }
        for(int i=midPoint; i<arrayLength; i++){ // Filling up the right half. This one starts from midPoint and ends in arrayLength.
            rightHalf.add(unsortedArray.get(i));
        }
        doMergeSort(leftHalf);   // Recursive call on the leftHalf                                 //
        doMergeSort(rightHalf); //  and the rightHalf until one element remains in the arrayList. //

        doMerging(unsortedArray, leftHalf, rightHalf); // the MERGING method.
    }

    private static void doMerging(ArrayList unsortedArray, ArrayList<String> leftHalf, ArrayList<String> rightHalf){ // Conquer!
        int leftArraySize = leftHalf.size();
        int rightArraySize = rightHalf.size();
        int i=0, j=0, k=0; // iterators for the left half, right half, and merge array

        while (i<leftArraySize && j<rightArraySize){
            if(leftHalf.get(i).compareTo(rightHalf.get(j))<0){
                unsortedArray.set(k,leftHalf.get(i)); // set the index k to the element at i if leftHalf is smaller
                i++;
            }else{
                unsortedArray.set(k,rightHalf.get(j)); // if leftHalf is not smaller, then set the index at k to rightHalf
                j++;
            }k++; // Always increment k.
        }
        // Below is a clean-up code for when the while loop has been satisfied but elements still remain.
        while(i < leftArraySize){
            unsortedArray.set(k,leftHalf.get(i));
            i++; k++;
        }
        while(j < rightArraySize){
            unsortedArray.set(k,rightHalf.get(j));
            j++; k++;
        }
    }

    private static void doLinearSearch(ArrayList<String> unsortedArray, String[] str, JsonObject obj, Boolean forPrintMenu,
                                       Boolean forPrintMenuQ){ // Linear search
        ArrayList<String> indexes = new ArrayList<>();
        if(str.length < 3) {
            for(int i = 0; i<unsortedArray.size(); i++){
                if((unsortedArray.get(i).substring(0,(unsortedArray.get(i).indexOf("#"))-1)).equals(str[1])){ // for single search
                    indexes.add(unsortedArray.get(i).substring(unsortedArray.get(i).indexOf("#")+1));
                }
            }
        }else{
            for(int j = 0; j< unsortedArray.size(); j++) {
                if ((unsortedArray.get(j).substring(0,(unsortedArray.get(j).indexOf("#"))-1)).equals(str[1]) |
                        (unsortedArray.get(j).substring(0,(unsortedArray.get(j).indexOf("#"))-1)).equals(str[2])){ // for double search
                    indexes.add(unsortedArray.get(j).substring(unsortedArray.get(j).indexOf("#")+1));
                }
            }
        }
        printTheObjects(indexes, obj, str, forPrintMenu, forPrintMenuQ); // Method for printing the stored indexes
    }

    private static void printTheObjects(ArrayList<String> indexes, JsonObject obj, String[] str, Boolean forPrintMenu,
                                        Boolean forPrintMenuQ) { // To print all indexes found
        JsonArray jArray = obj.getJsonArray("Animals");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n", "Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        for (int i = 0; i < indexes.size(); i++) {
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Species"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("ID"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Sex"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getInt("Age"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Imported Date"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Area"));
        }
        if (str.length < 3 & str[1].contains("-")) {
            System.out.println("Here is the data from age " + str[1].substring(0, str[1].indexOf("-")) + " to "
                    + (str[1].substring(str[1].indexOf("-") + 1)));
        } else if (str.length < 3) {
            System.out.println("Here is the data for " + str[1]);
        } else {
            System.out.println("Here is the data for " + str[1] + " and " + str[2]);
        }
        System.out.println();
        if (forPrintMenu) {
            System.out.print("Do you want to save it [Y/N]? ");
            String choice = iDFinder.next();
            if (choice.equals("y") | choice.equals("Y")) {
                System.out.print("Name your output file: ");
                String fileName = iDFinder.next();
                Stack<String> speciesS = new Stack<>();
                Stack<String> idS = new Stack<>();
                Stack<String> sexS = new Stack<>();
                Stack<String> ageS = new Stack<>();
                Stack<String> importedDateS = new Stack<>();
                Stack<String> areaS = new Stack<>();
                for (int i = 0; i < indexes.size(); i++) {
                    speciesS.push(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Species"));
                    idS.push(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("ID"));
                    sexS.push(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Sex"));
                    ageS.push(String.valueOf(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getInt("Age")));
                    importedDateS.push(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Imported Date"));
                    areaS.push(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Area"));
                }
                Properties prop = new Properties();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream("config.properties");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    prop.load(fis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String packageName = ZooManager.class.getPackageName();
                String dataName = fileName;
                String filePath = prop.getProperty("filepath") + File.separator + packageName + File.separator + "data" + File.separator + dataName;
                File outputFile = new File(filePath + ".txt");
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(outputFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
                BufferedWriter br = new BufferedWriter(outputWriter);
                while (!speciesS.isEmpty()) { // while the stack is not empty, pop.
                    try {
                        br.write(speciesS.pop() + ", " + idS.pop() + ", " + sexS.pop() + ", " + ageS.pop() + ", " +
                                importedDateS.pop() + ", " + areaS.pop());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
            }
        }else if (forPrintMenuQ) {
                System.out.print("Do you want to save it [Y/N]? ");
                String choice = iDFinder.next();
                if (choice.equals("y") | choice.equals("Y")) {
                    System.out.print("Name your output file: ");
                    String fileName = iDFinder.next();
                    Queue<String> speciesS = new LinkedList<>();
                    Queue<String> idS = new LinkedList<>();
                    Queue<String> sexS = new LinkedList<>();
                    Queue<String> ageS = new LinkedList<>();
                    Queue<String> importedDateS = new LinkedList<>();
                    Queue<String> areaS = new LinkedList<>();
                    for (int i = 0; i < indexes.size(); i++) {
                        speciesS.add(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Species"));
                        idS.add(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("ID"));
                        sexS.add(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Sex"));
                        ageS.add(String.valueOf(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getInt("Age")));
                        importedDateS.add(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Imported Date"));
                        areaS.add(jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Area"));
                    }
                    Properties prop = new Properties();
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream("config.properties");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        prop.load(fis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String packageName = ZooManager.class.getPackageName();
                    String dataName = fileName;
                    String filePath = prop.getProperty("filepath") + File.separator + packageName + File.separator + "data" + File.separator + dataName;
                    File outputFile = new File(filePath + ".txt");
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(outputFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter br = new BufferedWriter(outputWriter);
                    while (!speciesS.isEmpty()) { // while the stack is not empty, pop.
                        try {
                            br.write(speciesS.remove() + ", " + idS.remove() + ", " + sexS.remove() + ", " + ageS.remove() + ", " +
                                    importedDateS.remove() + ", " + areaS.remove());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
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
                }
            }
            mainMenu();
    }

    private static void runEditMenuA(JsonObject obj) {
        System.out.print("Enter area you want to change: ");
        iDFinder.useDelimiter("\n");
        String selectedArea = iDFinder.next();
        String[] str = selectedArea.split(", ",2);
        JsonArray animalsJArray = obj.getJsonArray("Animals");
        JsonArray areasArray = areasArray(obj).build();
        HashMap<String,String> areaAndSpecies = new HashMap<>();
        HashMap<String,Integer> speciesAndNumber = new HashMap<>();
        for (int i=0; i<animalsJArray.size(); i++){
            areaAndSpecies.put(animalsJArray.get(i).asJsonObject().getString("Area"),
                    animalsJArray.get(i).asJsonObject().getString("Species"));
        }
        for (int i=0; i<areasArray.size(); i++){
            speciesAndNumber.put(areasArray.get(i).asJsonObject().getString("Species of Animal"),
                    areasArray.get(i).asJsonObject().getInt("Number of Animals Stayed"));
        }
        if(areaAndSpecies.containsKey(str[0])){
            String name = areaAndSpecies.get(str[0]);
            int numberOfAnimals = speciesAndNumber.get(name);
            System.out.printf("%-12s | %-20s | %-18s %n","Area", "Species", "# of Animals");
            System.out.printf("%-12s   %-20s   %-18s %n", str[0],name,numberOfAnimals);
            System.out.println("edited to...");
            System.out.printf("%-12s | %-20s | %-18s %n","Area", "Species", "# of Animals");
            System.out.printf("%-12s   %-20s   %-18s %n", str[1],name,numberOfAnimals);
            stillRunningEditMenuA(obj,str,areaAndSpecies);
        }else {
            System.out.println(str[0]+" does not exist in database.");
            mainMenu();
        }
    }

    private static void runRemoveMenuA(JsonObject obj) {
        System.out.print("Enter area you want to remove: ");
        String removedArea = iDFinder.next();
        String[] str = new String[2];
        str[0] = removedArea;
        JsonArray animalsJArray = obj.getJsonArray("Animals");
        JsonArray areasArray = areasArray(obj).build();
        HashMap<String,String> areaAndSpecies = new HashMap<>();
        HashMap<String,Integer> speciesAndNumber = new HashMap<>();
        for (int i=0; i<animalsJArray.size(); i++){
            areaAndSpecies.put(animalsJArray.get(i).asJsonObject().getString("Area"),
                    animalsJArray.get(i).asJsonObject().getString("Species"));
        }
        for (int i=0; i<areasArray.size(); i++){
            speciesAndNumber.put(areasArray.get(i).asJsonObject().getString("Species of Animal"),
                    areasArray.get(i).asJsonObject().getInt("Number of Animals Stayed"));
        }
        if(areaAndSpecies.containsKey(str[0])){
            String name = areaAndSpecies.get(str[0]);
            int numberOfAnimals = speciesAndNumber.get(name);
            System.out.printf("%-12s | %-20s | %-18s %n","Area", "Species", "# of Animals");
            System.out.printf("%-12s   %-20s   %-18s %n", str[0],name,numberOfAnimals);
            System.out.print("after removing, where to? ");
            String movedToArea = iDFinder.next();
            str[1] = movedToArea;
            System.out.printf("%-12s | %-20s | %-18s %n","Area", "Species", "# of Animals");
            System.out.printf("%-12s   %-20s   %-18s %n", str[1],name,numberOfAnimals);
            stillRunningEditMenuA(obj,str,areaAndSpecies);
        }else {
            System.out.println(str[0]+" does not exist in database.");
            mainMenu();
        }
    }

    private static void stillRunningEditMenuA(JsonObject obj, String[] str, HashMap<String,String> areaAndSpecies) {
        int intToJsonValue = Integer.parseInt(str[1].substring(1));
        String test = str[0].substring(0,1);
        JsonValue intJValue = Json.createValue(intToJsonValue);
        JsonValue jValue = Json.createValue(str[1]);
        JsonArray animalsJArray = obj.getJsonArray("Animals");
        JsonArray areaJArrayA = obj.getJsonArray("Areas").get(0).asJsonObject().getJsonArray("Area");
        JsonArray areaJArrayB = obj.getJsonArray("Areas").get(1).asJsonObject().getJsonArray("Area");
        JsonArray areaJArrayC = obj.getJsonArray("Areas").get(2).asJsonObject().getJsonArray("Area");
        JsonArrayBuilder newAnimalsArray = Json.createArrayBuilder();
        JsonArrayBuilder newAreaArrayA = Json.createArrayBuilder();
        JsonArrayBuilder newAreaArrayB = Json.createArrayBuilder();
        JsonArrayBuilder newAreaArrayC = Json.createArrayBuilder();
        for (int j=0; j<animalsJArray.size(); j++) {
            if(animalsJArray.get(j).asJsonObject().getString("Area").equals(str[0])){
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("Species",animalsJArray.get(j).asJsonObject().getString("Species"));
                object.add("ID",animalsJArray.get(j).asJsonObject().getString("ID"));
                object.add("Sex",animalsJArray.get(j).asJsonObject().getString("Sex"));
                object.add("Age",animalsJArray.get(j).asJsonObject().getInt("Age"));
                object.add("Imported Date",animalsJArray.get(j).asJsonObject().getString("Imported Date"));
                object.add("Area",jValue);
                newAnimalsArray.add(j,object.build());
            }else{newAnimalsArray.add(j,animalsJArray.get(j));}
        }JsonArray finalAnimalsArray = newAnimalsArray.build();
        int counterA = obj.getJsonArray("Areas").get(0).asJsonObject().getInt("Number of Areas Occupied");
        int counterB = obj.getJsonArray("Areas").get(1).asJsonObject().getInt("Number of Areas Occupied");
        int counterC = obj.getJsonArray("Areas").get(2).asJsonObject().getInt("Number of Areas Occupied");
        if(test.equals("A")){
            for (int j=0; j<counterA; j++) {
                if(areaJArrayA.get(j).asJsonObject().getString("Species of Animal").equals(areaAndSpecies.get(str[0]))){
                    JsonObjectBuilder object = Json.createObjectBuilder();
                    object.add("Area Number",intJValue);
                    object.add("Species of Animal",areaAndSpecies.get(str[0]));
                    object.add("Number of Animals Stayed",areaJArrayA.get(j).asJsonObject().getInt("Number of Animals Stayed"));
                    newAreaArrayA.add(j,object.build());
                }else{newAreaArrayA.add(j,areaJArrayA.get(j));}
            }
            JsonArray finalAreasArrayA = newAreaArrayA.build();
            JsonArray finalAreasArrayB = areaJArrayB;
            JsonArray finalAreasArrayC = areaJArrayC;
            replaceJsonFile2(obj,finalAnimalsArray,finalAreasArrayA,finalAreasArrayB,finalAreasArrayC);
        } else if (test.equals("B")) {
            for (int j=0; j<counterB; j++) {
                if(areaJArrayB.get(j).asJsonObject().getString("Species of Animal").equals(areaAndSpecies.get(str[0]))){
                    JsonObjectBuilder object = Json.createObjectBuilder();
                    object.add("Area Number",intJValue);
                    object.add("Species of Animal",areaAndSpecies.get(str[0]));
                    object.add("Number of Animals Stayed",areaJArrayB.get(j).asJsonObject().getInt("Number of Animals Stayed"));
                    newAreaArrayB.add(j,object.build());
                }else{newAreaArrayB.add(j,areaJArrayB.get(j));}
            }
            JsonArray finalAreasArrayA = areaJArrayA;
            JsonArray finalAreasArrayB = newAreaArrayB.build();
            JsonArray finalAreasArrayC = areaJArrayC;
            replaceJsonFile2(obj,finalAnimalsArray,finalAreasArrayA,finalAreasArrayB,finalAreasArrayC);
        }else{
            for (int j=0; j<counterC; j++) {
            if(areaJArrayC.get(j).asJsonObject().getString("Species of Animal").equals(areaAndSpecies.get(str[0]))){
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("Area Number",intJValue);
                object.add("Species of Animal",areaAndSpecies.get(str[0]));
                object.add("Number of Animals Stayed",areaJArrayC.get(j).asJsonObject().getInt("Number of Animals Stayed"));
                JsonObject newObject = object.build();
                newAreaArrayC.add(j,newObject);
            }else{newAreaArrayC.add(j,areaJArrayC.get(j));}
        }
            JsonArray finalAreasArrayA = areaJArrayA;
            JsonArray finalAreasArrayB = areaJArrayB;
            JsonArray finalAreasArrayC = newAreaArrayC.build();
            replaceJsonFile2(obj,finalAnimalsArray,finalAreasArrayA,finalAreasArrayB,finalAreasArrayC);
        }
    }

    private static void replaceJsonFile2(JsonObject obj,JsonArray finalAnimalsArray,
                                         JsonArray finalAreasArrayA,JsonArray finalAreasArrayB,JsonArray finalAreasArrayC){
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packageName = ZooManager.class.getPackageName();
        String dataName = "Animals";
        String filePath = prop.getProperty("filepath") + File.separator + packageName + File.separator + "data" + File.separator + dataName + ".json";
        File inputFile = new File(filePath);
        File outputFile = new File(filePath+".tmp");

        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonWriter writer = Json.createWriter(outStream);
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("Name",obj.get("Name"));
        objBuilder.add("Location",obj.get("Location"));
        objBuilder.add("Phone",obj.get("Phone"));
        objBuilder.add("Open Hours",obj.get("Open Hours"));
        objBuilder.add("Number of Animals",finalAnimalsArray.size());
        objBuilder.add("Animals",finalAnimalsArray);
        JsonObjectBuilder builderA = Json.createObjectBuilder();
        builderA.add("Section",obj.getJsonArray("Areas").get(0).asJsonObject().getString("Section"));
        builderA.add("Animal Type",obj.getJsonArray("Areas").get(0).asJsonObject().getString("Animal Type"));
        builderA.add("Number of Areas",obj.getJsonArray("Areas").get(0).asJsonObject().getInt("Number of Areas"));
        builderA.add("Number of Areas Occupied",finalAreasArrayA.size());
        builderA.add("Area",finalAreasArrayA);
        JsonObjectBuilder builderB = Json.createObjectBuilder();
        builderB.add("Section",obj.getJsonArray("Areas").get(1).asJsonObject().getString("Section"));
        builderB.add("Animal Type",obj.getJsonArray("Areas").get(1).asJsonObject().getString("Animal Type"));
        builderB.add("Number of Areas",obj.getJsonArray("Areas").get(1).asJsonObject().getInt("Number of Areas"));
        builderB.add("Number of Areas Occupied",finalAreasArrayB.size());
        builderB.add("Area",finalAreasArrayB);
        JsonObjectBuilder builderC = Json.createObjectBuilder();
        builderC.add("Section",obj.getJsonArray("Areas").get(2).asJsonObject().getString("Section"));
        builderC.add("Animal Type",obj.getJsonArray("Areas").get(2).asJsonObject().getString("Animal Type"));
        builderC.add("Number of Areas",obj.getJsonArray("Areas").get(2).asJsonObject().getInt("Number of Areas"));
        builderC.add("Number of Areas Occupied",finalAreasArrayC.size());
        builderC.add("Area",finalAreasArrayC);
        JsonArrayBuilder buildAreasArray  = Json.createArrayBuilder();
        buildAreasArray.add(0,builderA.build());
        buildAreasArray.add(1,builderB.build());
        buildAreasArray.add(2,builderC.build());
        objBuilder.add("Areas",buildAreasArray.build());
        JsonObject finalObject = objBuilder.build();
        Map<String,Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory wFactory = Json.createWriterFactory(config);
        writer = wFactory.createWriter(outStream);
        writer.writeObject(finalObject);
        writer.close();
        inputFile.delete();
        outputFile.renameTo(new File(filePath));
    }

    private static void runEditMenu(JsonObject obj) {
        System.out.print("Enter Animal ID you want to change: ");
        String selectedID = iDFinder.next();
        JsonArray jArray = obj.getJsonArray("Animals");
        ArrayList<String> idToArray = new ArrayList<>();
        for (JsonValue value: jArray){
            String iD = value.asJsonObject().getString("ID");
            idToArray.add(iD);
        }
        if (idToArray.contains(selectedID)){
            int i = idToArray.indexOf(selectedID);
            System.out.println("-----------------------------------------------------------------");
            System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
            System.out.println("-----------------------------------------------------------------");
            JsonObject object = (JsonObject) jArray.get(i);
            String name = object.getString("Species");
            String i_D = object.getString("ID");
            String sex = object.getString("Sex");
            int age = object.getInt("Age");
            String date = object.getString("Imported Date");
            String area = object.getString("Area");
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n", name,i_D,sex,age,date,area);
             runEditMenu2(jArray, i,obj);
        }else{
            System.out.println("INVALID INPUT! Try again.\n"+"N.B This menu is case sensitive");
            runEditMenu(obj);
        }
    }

    private static void runEditMenu2(JsonArray jArray, int i,JsonObject obj) {
        int nI = i;  // nI stands for newIndex. I reassigned this because I could not access it for some reason.
        System.out.print("What do you want to change? [1:species 2:id 3:sex 4:age 5:date 6:area] >>>");
        String choice = iDFinder.next();
        switch (choice) {
            case "1" -> editSpecies(jArray,nI,obj);
            case "2" -> editID(jArray,nI,obj);
            case "3" -> editSex(jArray,nI,obj);
            case "4" -> editAge(jArray, nI,obj);
            case "5" -> editDate(jArray, nI,obj);
            case "6" -> editArea(jArray, nI,obj);
            default -> System.out.println("INVALID INPUT! Try again with the options given.");
        }
        runEditMenu2(jArray,i,obj);
    }

    private static void replaceJsonFile(JsonObject obj, JsonArray finalArray){
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packageName = ZooManager.class.getPackageName();
        String dataName = "Animals";
        String filePath = prop.getProperty("filepath") + File.separator + packageName + File.separator + "data" + File.separator + dataName + ".json";
        File inputFile = new File(filePath);
        File outputFile = new File(filePath+".tmp");

        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonWriter writer = Json.createWriter(outStream);
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("Name",obj.get("Name"));
        objBuilder.add("Location",obj.get("Location"));
        objBuilder.add("Phone",obj.get("Phone"));
        objBuilder.add("Open Hours",obj.get("Open Hours"));
        objBuilder.add("Number of Animals",finalArray.size());
        objBuilder.add("Animals",finalArray);
        objBuilder.add("Areas",obj.getJsonArray("Areas"));
        JsonObject finalObject = objBuilder.build();
        Map<String,Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory wFactory = Json.createWriterFactory(config);
        writer = wFactory.createWriter(outStream);
        writer.writeObject(finalObject);
        writer.close();
        inputFile.delete();
        outputFile.renameTo(new File(filePath));
    }

    private static void formatPrint(JsonArray finalArray, int nI){
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        JsonObject object = (JsonObject) finalArray.get(nI);
        String name = object.getString("Species");
        String i_D = object.getString("ID");
        String sex = object.getString("Sex");
        int age = object.getInt("Age");
        String date = object.getString("Imported Date");
        String area = object.getString("Area");
        System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                name,i_D,sex,age,date,area);
    }

    private static void editSpecies(JsonArray jArray, int nI,JsonObject obj) {
        System.out.print("Change Species to >>>");
        iDFinder.useDelimiter("\n");
        String changeTo = iDFinder.next();
        JsonObject jObj = obj.getJsonArray("Animals").getJsonObject(nI);
        JsonValue jValue = Json.createValue(changeTo);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Species",jValue);
        builder.add("ID",jObj.get("ID"));
        builder.add("Sex",jObj.get("Sex"));
        builder.add("Age",jObj.get("Age"));
        builder.add("Imported Date",jObj.get("Imported Date"));
        builder.add("Area",jObj.get("Area"));
        JsonObject newObj = builder.build();
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        newArray.set(nI,newObj);
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj,finalArray);
        formatPrint(finalArray,nI);
        mainMenu();
    }

    private static void editID(JsonArray jArray, int nI,JsonObject obj) {
        System.out.print("Change ID to >>>");
        String changeTo = iDFinder.next();
        JsonObject jObj = obj.getJsonArray("Animals").getJsonObject(nI);
        JsonValue jValue = Json.createValue(changeTo);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Species",jObj.get("Species"));
        builder.add("ID",jValue);
        builder.add("Sex",jObj.get("Sex"));
        builder.add("Age",jObj.get("Age"));
        builder.add("Imported Date",jObj.get("Imported Date"));
        builder.add("Area",jObj.get("Area"));
        JsonObject newObj = builder.build();
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        newArray.set(nI,newObj);
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj,finalArray);
        formatPrint(finalArray,nI);
        mainMenu();
    }

    private static void editSex(JsonArray jArray, int nI,JsonObject obj) {
        System.out.print("Change Sex to >>>");
        String changeTo = iDFinder.next();
        JsonObject jObj = obj.getJsonArray("Animals").getJsonObject(nI);
        JsonValue jValue = Json.createValue(changeTo);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Species",jObj.get("Species"));
        builder.add("ID",jObj.get("ID"));
        builder.add("Sex",jValue);
        builder.add("Age",jObj.get("Age"));
        builder.add("Imported Date",jObj.get("Imported Date"));
        builder.add("Area",jObj.get("Area"));
        JsonObject newObj = builder.build();
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        newArray.set(nI,newObj);
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj,finalArray);
        formatPrint(finalArray,nI);
        mainMenu();
    }

    private static void editAge(JsonArray jArray, int nI,JsonObject obj) {
        System.out.print("Change Age to >>>");
        String changeTo = iDFinder.next();
        JsonObject jObj = obj.getJsonArray("Animals").getJsonObject(nI);
        JsonValue jValue = Json.createValue(changeTo);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Species",jObj.get("Species"));
        builder.add("ID",jObj.get("ID"));
        builder.add("Sex",jObj.get("Sex"));
        builder.add("Age",jValue);
        builder.add("Imported Date",jObj.get("Imported Date"));
        builder.add("Area",jObj.get("Area"));
        JsonObject newObj = builder.build();
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        newArray.set(nI,newObj);
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj,finalArray);
        formatPrint(finalArray,nI);
        mainMenu();
    }

    private static void editDate(JsonArray jArray, int nI,JsonObject obj) {
        System.out.print("Change Imported Date to >>>");
        String changeTo = iDFinder.next();
        JsonObject jObj = obj.getJsonArray("Animals").getJsonObject(nI);
        JsonValue jValue = Json.createValue(changeTo);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Species",jObj.get("Species"));
        builder.add("ID",jObj.get("ID"));
        builder.add("Sex",jObj.get("Sex"));
        builder.add("Age",jObj.get("Age"));
        builder.add("Imported Date",jValue);
        builder.add("Area",jObj.get("Area"));
        JsonObject newObj = builder.build();
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        newArray.set(nI,newObj);
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj,finalArray);
        formatPrint(finalArray,nI);
        mainMenu();
    }

    private static void editArea(JsonArray jArray, int nI,JsonObject obj) {
        System.out.print("Change Area to >>>");
        String changeTo = iDFinder.next();
        JsonObject jObj = obj.getJsonArray("Animals").getJsonObject(nI);
        JsonValue jValue = Json.createValue(changeTo);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Species",jObj.get("Species"));
        builder.add("ID",jObj.get("ID"));
        builder.add("Sex",jObj.get("Sex"));
        builder.add("Age",jObj.get("Age"));
        builder.add("Imported Date",jObj.get("Imported Date"));
        builder.add("Area",jValue);
        JsonObject newObj = builder.build();
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        newArray.set(nI,newObj);
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj,finalArray);
        formatPrint(finalArray,nI);
        mainMenu();
    }

    private static void runAddMenu(JsonObject obj) {
        System.out.print("Enter a new animal: ");
        iDFinder.useDelimiter("\n");
        String newData = iDFinder.next();
        // The split() method would split the user data into six parts at every ", "
        String[] str = newData.split(", ",6);
        int intAge = Integer.parseInt(str[3]); // so that the value type would be integer
        // Converting the strings to a JsonValue
        JsonValue nameValue = Json.createValue(str[0]); JsonValue idValue = Json.createValue(str[1]);
        JsonValue sexValue = Json.createValue(str[2]); JsonValue ageValue = Json.createValue(intAge);
        JsonValue dateValue = Json.createValue(str[4]); JsonValue areaValue = Json.createValue(str[5]);
        // Creating a new JsonObject
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Species",nameValue); builder.add("ID",idValue);
        builder.add("Sex",sexValue); builder.add("Age",ageValue);
        builder.add("Imported Date",dateValue); builder.add("Area",areaValue);
        JsonObject newObj = builder.build();
        // Creating a new JsonArray
        JsonArray jArray = obj.getJsonArray("Animals");
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        newArray.add(newObj);
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj,finalArray);
        int nI = finalArray.size()-1; // Since the added data would always be at the last index
        formatPrint(finalArray,nI);
        System.out.println("has been successfully added!");
        mainMenu();
    }

    private static void runRemoveMenu(JsonObject obj) {
        System.out.print("Enter what you want to remove: ");
        iDFinder.useDelimiter("\n");
        String removeData = iDFinder.next();
        String[] str = removeData.split(", ",2);
        switch (str[0]) {
            case "species" -> removeSpecies(str,obj);
            case "id" -> removeID(str,obj);
            case "sex" -> removeSex(str,obj);
            case "age" -> removeAge(str,obj);
            case "date" -> removeDate(str,obj);
            case "area" -> removeArea(str,obj);
            default -> System.out.println("INVALID KEY! Valid keys are: species, id, sex, age, date and area.");
        }
        runRemoveMenu(obj);
    }

    private static void removalMethod(String[] str, JsonObject obj, String userKey){
        JsonArray jArray = obj.getJsonArray("Animals");
        // ArrayBuilder is mutable. I need it, so I can use the remove method.
        JsonArrayBuilder newArray = Json.createArrayBuilder();
        for (int j=0; j<jArray.size(); j++) {
            newArray.add(j,jArray.get(j));
        }
        // To remove the objects that contain the value given by the user.
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        for (int i=jArray.size()-1; i>-1; i--){ // Working backwards here since it "removes."
            if(jArray.get(i).asJsonObject().getString(userKey).equals(str[1])){
                System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                        jArray.get(i).asJsonObject().getString("Species"),
                        jArray.get(i).asJsonObject().getString("ID"),
                        jArray.get(i).asJsonObject().getString("Sex"),
                        jArray.get(i).asJsonObject().getInt("Age"),
                        jArray.get(i).asJsonObject().getString("Imported Date"),
                        jArray.get(i).asJsonObject().getString("Area"));
                newArray.remove(i);
            }
        }
        System.out.println("has been successfully removed!");
        JsonArray finalArray = newArray.build();
        replaceJsonFile(obj, finalArray);
        mainMenu();
    }

    private static Boolean valueCheck(String[]str, String userKey, JsonObject obj){
        JsonArray jArray = obj.getJsonArray("Animals");
        ArrayList<String> checkArray = new ArrayList<>();
        for (JsonValue value : jArray) {
            checkArray.add(value.asJsonObject().getString(userKey));
        }
        return checkArray.contains(str[1]);
    }

    private static void removeSpecies(String[] str, JsonObject obj) {
        String userKey = "Species"; // So that I can manipulate the string name "userKey" in other methods.
        if (valueCheck(str,userKey,obj)) {
            removalMethod(str, obj, userKey);
        }
        System.out.println("INVALID VALUE! Try Again");
        runRemoveMenu(obj);
    }

    private static void removeID(String[] str, JsonObject obj) {
        String userKey = "ID"; // So that I can manipulate the string name "userKey" in other methods.
        if (valueCheck(str,userKey,obj)) {
            removalMethod(str, obj, userKey);
        }
        System.out.println("INVALID VALUE! Try Again");
        runRemoveMenu(obj);
    }
    private static void removeSex(String[] str, JsonObject obj) {
        String userKey = "Sex"; // So that I can manipulate the string name "userKey" in other methods.
        if (valueCheck(str,userKey,obj)) {
            removalMethod(str, obj, userKey);
        }
        System.out.println("INVALID VALUE! Try Again");
        runRemoveMenu(obj);
    }

    private static void removeAge(String[] str, JsonObject obj) {
        JsonArray jArray = obj.getJsonArray("Animals");
        // Had to code the check and removal for removeAge() because age is an int Value. Had no other ideas asides coding it.
        ArrayList<Integer> checkArray = new ArrayList<>();
        for (JsonValue value : jArray) {
            checkArray.add(value.asJsonObject().getInt("Age"));
        }
        if (checkArray.contains(Integer.parseInt(str[1]))) {
            JsonArrayBuilder newArray = Json.createArrayBuilder();
            for (int j=0; j<jArray.size(); j++) {
                newArray.add(j,jArray.get(j));
            }
            // To remove the objects that contain the value given by the user.
            System.out.println("-----------------------------------------------------------------");
            System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
            System.out.println("-----------------------------------------------------------------");
            for (int i=jArray.size()-1; i>-1; i--){ // Working backwards here since it "removes."
                if(jArray.get(i).asJsonObject().getInt("Age")==Integer.parseInt(str[1])){
                    System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                            jArray.get(i).asJsonObject().getString("Species"),
                            jArray.get(i).asJsonObject().getString("ID"),
                            jArray.get(i).asJsonObject().getString("Sex"),
                            jArray.get(i).asJsonObject().getInt("Age"),
                            jArray.get(i).asJsonObject().getString("Imported Date"),
                            jArray.get(i).asJsonObject().getString("Area"));
                    newArray.remove(i);
                }
            }
            System.out.println("has been successfully removed!");
            JsonArray finalArray = newArray.build();
            replaceJsonFile(obj, finalArray);
            mainMenu();
        }
        System.out.println("INVALID VALUE! Try Again");
        runRemoveMenu(obj);
    }

    private static void removeDate(String[] str, JsonObject obj) {
        String userKey = "Imported Date"; // So that I can manipulate the string name "userKey" in other methods.
        if (valueCheck(str,userKey,obj)) {
            removalMethod(str, obj, userKey);
        }
        System.out.println("INVALID VALUE! Try Again");
        runRemoveMenu(obj);
    }

    private static void removeArea(String[] str, JsonObject obj) {
        String userKey = "Area"; // So that I can manipulate the string name "userKey" in other methods.
        if (valueCheck(str,userKey,obj)) {
            removalMethod(str, obj, userKey);
        }
        System.out.println("INVALID VALUE! Try Again");
        runRemoveMenu(obj);
    }

    private static void runValidateMenu(JsonObject obj) {
        JsonArray animalsJArray = obj.getJsonArray("Animals");
        JsonArray areaJArrayA = obj.getJsonArray("Areas").get(0).asJsonObject().getJsonArray("Area");
        JsonArray areaJArrayB = obj.getJsonArray("Areas").get(1).asJsonObject().getJsonArray("Area");
        JsonArray areaJArrayC = obj.getJsonArray("Areas").get(2).asJsonObject().getJsonArray("Area");
        ArrayList<String> Herbivore = new ArrayList<>();
        Herbivore.add("Giant Panda"); Herbivore.add("Zebra"); Herbivore.add("Giraffe"); Herbivore.add("African Elephant");
        ArrayList<String> Carnivore = new ArrayList<>();
        Carnivore.add("Leopard"); Carnivore.add("Lion");
        ArrayList<String> Birds = new ArrayList<>();
        Birds.add("Cinereous Vulture"); Birds.add("Peafowl"); Birds.add("Ostrich");
        ArrayList<String> errors = new ArrayList<>();
        int counterA = obj.getJsonArray("Areas").get(0).asJsonObject().getInt("Number of Areas Occupied");
        int counterB = obj.getJsonArray("Areas").get(1).asJsonObject().getInt("Number of Areas Occupied");
        int counterC = obj.getJsonArray("Areas").get(2).asJsonObject().getInt("Number of Areas Occupied");
        for(int i=0; i<counterA; i++){
            if(!Herbivore.contains(areaJArrayA.getJsonObject(i).getString("Species of Animal"))){
                errors.add(areaJArrayA.getJsonObject(i).getString("Species of Animal"));
            }
        }
        for(int i=0; i<counterB; i++){
            if(!Carnivore.contains(areaJArrayB.getJsonObject(i).getString("Species of Animal"))){
                errors.add(areaJArrayB.getJsonObject(i).getString("Species of Animal"));
            }
        }
        for(int i=0; i<counterC; i++){
            if(!Birds.contains(areaJArrayC.getJsonObject(i).getString("Species of Animal"))){
                errors.add(areaJArrayC.getJsonObject(i).getString("Species of Animal"));
            }
        }
        if(errors.isEmpty()){
            System.out.println("No errors");
        }else{
            System.out.println("<Errors>");
            System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
           for(int i=0; i<animalsJArray.size();i++){
               if(errors.contains(animalsJArray.getJsonObject(i).getString("Species"))){
                   System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                           animalsJArray.get(i).asJsonObject().getString("Species"),
                           animalsJArray.get(i).asJsonObject().getString("ID"),
                           animalsJArray.get(i).asJsonObject().getString("Sex"),
                           animalsJArray.get(i).asJsonObject().getInt("Age"),
                           animalsJArray.get(i).asJsonObject().getString("Imported Date"),
                           animalsJArray.get(i).asJsonObject().getString("Area"));
               }
           }
            System.out.println("<Fixing>");
           ArrayList<String> fixes = new ArrayList<>();
            for (String s:errors) {
                System.out.print("For "+s+", where to? ");
                String newAddedArea = iDFinder.next();
                if(newAddedArea.length()<3){
                    newAddedArea = newAddedArea.substring(0,1)+"0"+newAddedArea.substring(1);
                }
                fixes.add(newAddedArea);
            }
            doValidation(obj,errors,fixes);
        }
    }

    private static void doValidation(JsonObject obj,ArrayList<String> errors,ArrayList<String> fixes){
        JsonArray animalsJArray = obj.getJsonArray("Animals");
        JsonArray areasJArray = areasArray(obj).build();
        JsonArrayBuilder newAnimalsArray = Json.createArrayBuilder();
        JsonArrayBuilder newAreaArrayA = Json.createArrayBuilder();
        JsonArrayBuilder newAreaArrayB = Json.createArrayBuilder();
        JsonArrayBuilder newAreaArrayC = Json.createArrayBuilder();
        for (int j=0; j<animalsJArray.size(); j++) {
            if (errors.contains(animalsJArray.get(j).asJsonObject().getString("Species"))) {
                int errorIndex = errors.indexOf(animalsJArray.get(j).asJsonObject().getString("Species"));
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("Species", animalsJArray.get(j).asJsonObject().getString("Species"));
                object.add("ID", animalsJArray.get(j).asJsonObject().getString("ID"));
                object.add("Sex", animalsJArray.get(j).asJsonObject().getString("Sex"));
                object.add("Age", animalsJArray.get(j).asJsonObject().getInt("Age"));
                object.add("Imported Date", animalsJArray.get(j).asJsonObject().getString("Imported Date"));
                object.add("Area", fixes.get(errorIndex));
                newAnimalsArray.add(j, object.build());
            } else {
                newAnimalsArray.add(j, animalsJArray.get(j));
            }
        }JsonArray finalAnimalsArray = newAnimalsArray.build();
        HashMap<String,String> speciesAndSection = new HashMap<>();
        for (int i=0; i<animalsJArray.size(); i++){
            speciesAndSection.put(finalAnimalsArray.get(i).asJsonObject().getString("Species"),
                    finalAnimalsArray.get(i).asJsonObject().getString("Area").substring(0,1));
        }
        HashMap<String,Integer> speciesAndNumStayed = new HashMap<>();
        for (int i=0; i<areasJArray.size(); i++){
            speciesAndNumStayed.put(areasJArray.getJsonObject(i).getString("Species of Animal"),
                    areasJArray.getJsonObject(i).getInt("Number of Animals Stayed"));
        }
        HashMap<String,Integer> speciesAndAreaNum = new HashMap<>();
        for (int i=0; i<animalsJArray.size(); i++){
            speciesAndAreaNum.put(animalsJArray.getJsonObject(i).getString("Species"),
                    Integer.valueOf(animalsJArray.getJsonObject(i).getString("Area").substring(1)));
        }
        for(int i=0; i<areasJArray.size(); i++){
            Object toCheck = areasJArray.getJsonObject(i).getString("Species of Animal");
            if(speciesAndSection.get(toCheck).equals("A")){
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("Area Number",speciesAndAreaNum.get(toCheck));
                object.add("Species of Animal", (String) toCheck);
                object.add("Number of Animals Stayed",speciesAndNumStayed.get(toCheck));
                newAreaArrayA.add(object.build());
            }else if(speciesAndSection.get(toCheck).equals("B")){
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("Area Number",speciesAndAreaNum.get(toCheck));
                object.add("Species of Animal", (String) toCheck);
                object.add("Number of Animals Stayed",speciesAndNumStayed.get(toCheck));
                newAreaArrayB.add(object.build());
            }else if(speciesAndSection.get(toCheck).equals("C")){
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("Area Number",speciesAndAreaNum.get(toCheck));
                object.add("Species of Animal", (String) toCheck);
                object.add("Number of Animals Stayed",speciesAndNumStayed.get(toCheck));
                newAreaArrayC.add(object.build());
            }
        }
        JsonArray finalAreasArrayA = newAreaArrayA.build();
        JsonArray finalAreasArrayB = newAreaArrayB.build();
        JsonArray finalAreasArrayC = newAreaArrayC.build();
        replaceJsonFile2(obj,finalAnimalsArray,finalAreasArrayA,finalAreasArrayB,finalAreasArrayC);
        System.out.println("<Updated>");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
        for(int i=0; i<finalAnimalsArray.size();i++){
            if(errors.contains(finalAnimalsArray.getJsonObject(i).getString("Species"))){
                System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                        finalAnimalsArray.get(i).asJsonObject().getString("Species"),
                        finalAnimalsArray.get(i).asJsonObject().getString("ID"),
                        finalAnimalsArray.get(i).asJsonObject().getString("Sex"),
                        finalAnimalsArray.get(i).asJsonObject().getInt("Age"),
                        finalAnimalsArray.get(i).asJsonObject().getString("Imported Date"),
                        finalAnimalsArray.get(i).asJsonObject().getString("Area"));
            }
        }
    }

    private static void runPrintMenu(JsonObject obj) {
        System.out.print("Enter animal you want to print: ");
        iDFinder.useDelimiter("\n");
        String printingMenu = iDFinder.next();
        String[] str = printingMenu.split(", ");
        Boolean forPrintMenu = true;
        Boolean forPrintMenuQ = false;
        switch (str[0]) {
            case "species" -> findSpecies(str,obj,forPrintMenu,forPrintMenuQ);
            case "id" -> findID(str,obj,forPrintMenu,forPrintMenuQ);
            case "sex" -> findSex(str,obj,forPrintMenu,forPrintMenuQ);
            case "age" -> findAge(str,obj,forPrintMenu,forPrintMenuQ);
            case "date" -> findDate(str,obj,forPrintMenu,forPrintMenuQ);
            case "area" -> findArea(str,obj,forPrintMenu,forPrintMenuQ);
            default -> System.out.println("INVALID KEY! Valid keys are: species, id, sex, age, date and area.");
        }
        runFindMenu(obj);
    }

    private static void runPrintMenuQ(JsonObject obj) {
        System.out.print("Enter animal you want to print: ");
        iDFinder.useDelimiter("\n");
        String printingMenuQ = iDFinder.next();
        String[] str = printingMenuQ.split(", ");
        Boolean forPrintMenu = false;
        Boolean forPrintMenuQ = true;
        switch (str[0]) {
            case "species" -> findSpecies(str,obj,forPrintMenu,forPrintMenuQ);
            case "id" -> findID(str,obj,forPrintMenu,forPrintMenuQ);
            case "sex" -> findSex(str,obj,forPrintMenu,forPrintMenuQ);
            case "age" -> findAge(str,obj,forPrintMenu,forPrintMenuQ);
            case "date" -> findDate(str,obj,forPrintMenu,forPrintMenuQ);
            case "area" -> findArea(str,obj,forPrintMenu,forPrintMenuQ);
            default -> System.out.println("INVALID KEY! Valid keys are: species, id, sex, age, date and area.");
        }
        runFindMenu(obj);
    }
}