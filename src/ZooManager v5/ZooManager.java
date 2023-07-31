package Assignment5;

/*
    CSC 241 Fall 2022
    Assignment 5
    Name: Declan ONUNKWO
    ID: 806075156
 *//*
   (1) I implemented the merge sort divide and conquer algorithm @ line 204-246
   (2) I did the extra work for ranged search in age and double search in the other keys.
   (3) I used linear search on the ordered array. This linear search would end after the index of the last value seen.
       This is because in a Binary search, you would still have to linearly search the left and right in case there are
       multiple occurrences of that certain value. Yes, it'll be fast if the value only appeared once(which is unlikely,
       for example while searching for males or females). Also, the problem increases if you attempt the extra credit work.
       This is because there are ranges and double values, meaning more duplicates and a slower binary search. The Linear
       search would save space and score almost the same speed as the binary search.
   (4) I noticed an error in the sample code of the Enhanced Conditional Search in number 4D. The prompt asks for
       id, AM253431, EQ000132 but fails to return the data at EQ000132. I think its meant to be zebra, but the id isn't correct.
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
        // System.out.println(System.getProperty("user.dir"));

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
        System.out.println("Choose menu [1:view 2:find 3:edit 4:add 5:remove 6:quit]?");
        System.out.print(">>> ");
       /* Changed the line below to a string since I couldn't fix
        the error that pops up when the user types something other than an integer*/
        String userNumber = menuNumber.next();
        switch (userNumber) {
            case "1" -> runViewMenu(obj);
            case "2" -> runFindMenu(obj);
            case "3" -> runEditMenu(obj);
            case "4" -> runAddMenu(obj);
            case "5" -> runRemoveMenu(obj);
            case "6" -> System.exit(0);
            default -> System.out.println("INVALID INPUT! Use the menu numbers.");
        }
        mainMenu();
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

    private static void runFindMenu(JsonObject obj) {
        System.out.print("Enter animal you want to find: ");
        iDFinder.useDelimiter("\n");
        String removeData = iDFinder.next();
        String[] str = removeData.split(", ");
        switch (str[0]) {
            case "species" -> findSpecies(str,obj);
            case "id" -> findID(str,obj);
            case "sex" -> findSex(str,obj);
            case "age" -> findAge(str,obj);
            case "date" -> findDate(str,obj);
            case "area" -> findArea(str,obj);
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

    private static void findSpecies(String[] str, JsonObject obj) {
        Boolean stringChecker = true;
        String key = "Species";
        ArrayList<String> unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj);
    }

    private static void findID(String[] str, JsonObject obj) {
        Boolean stringChecker = true;
        String key = "ID";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj);
    }

    private static void findSex(String[] str, JsonObject obj) {
        Boolean stringChecker = true;
        String key = "Sex";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj);
    }

    private static void findAge(String[] str, JsonObject obj) {
        Boolean stringChecker = false; // to show that age is not a string, and must use the "getInt" method.
        String key = "Age";
        ArrayList<Integer> unsortedArray = buildingAnArray(obj, stringChecker, key);
        ArrayList<String> indexes = new ArrayList<>();
        System.out.println(unsortedArray);
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
        System.out.println(indexes);
        printTheObjects(indexes, obj, str);
    }

    private static void findDate(String[] str, JsonObject obj) {
        Boolean stringChecker = true;
        String key = "Imported Date";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj);
    }

    private static void findArea(String[] str, JsonObject obj) {
        Boolean stringChecker = true;
        String key = "Area";
        ArrayList unsortedArray = buildingAnArray(obj, stringChecker, key);
        doMergeSort(unsortedArray);
        doLinearSearch(unsortedArray, str, obj);
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

    private static void doLinearSearch(ArrayList<String> unsortedArray, String[] str, JsonObject obj){ // Linear search
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
        printTheObjects(indexes, obj, str); // Method for printing the stored indexes
    }

    private static void printTheObjects(ArrayList<String> indexes, JsonObject obj, String[] str){ // To print all indexes found
        JsonArray jArray = obj.getJsonArray("Animals");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-18s | %-8s | %1s | %3s | %-10s | %4s %n","Species", "iD", "Sex", "Age", "Date", "Area");
        System.out.println("-----------------------------------------------------------------");
        for(int i = 0; i<indexes.size(); i++){
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Species"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("ID"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Sex"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getInt("Age"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Imported Date"),
                    jArray.get(Integer.parseInt(indexes.get(i))).asJsonObject().getString("Area"));
        }
        if(str.length<3 & str[1].contains("-")){
            System.out.println("Here is the data from age "+str[1].substring(0, str[1].indexOf("-"))+" to "
                    +(str[1].substring(str[1].indexOf("-") + 1)));
        }
        else if(str.length<3){System.out.println("Here is the data for "+str[1]);}
        else{System.out.println("Here is the data for "+str[1]+" and "+str[2]);}
        System.out.println();
        mainMenu();
    }

    private static void runEditMenu(JsonObject obj) {
        System.out.print("Enter Animal ID you want to change >>>");
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
        objBuilder.add("Number of Animals",obj.get("Number of Animals"));
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
}