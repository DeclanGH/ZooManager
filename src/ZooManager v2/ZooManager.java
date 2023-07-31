package Assignment2;

/*
    CSC 241 Fall 2022
    Assignment 2
    Name: Declan ONUNKWO
    ID: 806075156
 */

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.util.*;

public class ZooManager {

    private static Scanner menuNumber = new Scanner(System.in);
    private static Scanner iDFinder = new Scanner(System.in);

    public static void main (String[] args) throws Exception {

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
        System.out.println("Choose menu [1:view 2:find 3:edit 4:quit]?");
        System.out.print(">>> ");
       /* Changed the line below to a string since I couldn't fix
        the error that pops up when the user types something other than an integer*/
        String userNumber = menuNumber.next();
        switch (userNumber) {
            case "1" -> runViewMenu(obj);
            case "2" -> runFindMenu(obj);
            case "3" -> runEditMenu(obj);
            case "4" -> System.exit(0);
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
        System.out.print("Enter Animal ID you want to find >>>");
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
                System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                        name,i_D,sex,age,date,area);

        }else{
            System.out.println("INVALID INPUT! Try again.\n"+"N.B This menu is case sensitive");
            runFindMenu(obj);
        }
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
            System.out.printf("%-18s   %-8s   %1s    %3s    %-10s   %4s %n",
                    name,i_D,sex,age,date,area);
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

}