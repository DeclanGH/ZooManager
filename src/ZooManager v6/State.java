package Assignment6;

/*
    CSC 241 Fall 2022
    Assignment 5
    Name: Declan ONUNKWO
    ID: 806075156
*/

// states for animal
enum State {
    HEALTHY,
    SICK,
    PREGNANT,
    DEAD,
    HUNGRY,
    ASLEEP
}

// sex of animal
enum Sex {
    MALE,
    FEMALE
}

abstract class Animal {
    String species;        // species of animal
    String food;        // category for food: meat, grass, or both
    State state;        // current state of animal
    Sex sex;            // sex of animal
    int age;            // age of animal

    Animal(String species, String food, State state, Sex sex, int age){
        this.species = species;
        this.food = food;
        this.state = state;
        this.sex = sex;
        this.age = age;
    }
    /* TODO:
        - Define constructors, variables, methods
        - Define concrete method "setState", "getState"
        - DO NOT set food in this abstract class
    */
    public String getSpecies(){
        return species;
    }
    public void setSpecies(String species){
        this.species = species;
    }
    public State getState(){
        return state;
    }
    public void setState(State state){
        this.state = state;
    }
    public Sex getSex(){
        return sex;
    }
    public void setSex(Sex sex){
        this.sex = sex;
    }
    public int getAge(){
        return age;
    }
    public void setAge(int Age){
        this.age = age;
    }
    public String getFood(){return food;}
}

abstract class Mammal extends Animal {
    String id;
    String importedDate;
    String area;        // area # in which the animal is
    String feet;

    Mammal(String species, String food, State state, Sex sex, int age) {
        super(species, food, state, sex, age);
    }
    /* TODO:
        - Define constructors, variables, methods
        - Define abstract method "feed"
        - DO NOT set feet in this abstract class
    */
    void setId(String id){
        this.id=id;
    }
    String getId(){return id;}
    void setImportedDate(String importedDate){
        this.importedDate=importedDate;
    }
    String getImportedDate(){return importedDate;}
    void setArea(String Area){this.area=area;}
    String getArea(){return area;}
    String getFeet(){return feet;}
}

class Carnivore extends Mammal {
    /* TODO:
        - Define constructors, variables, methods
        - Set food "meat"
        - Set feet "pads"
        - Implement the method "feed"
    */
    Carnivore(String species, String food, State state, Sex sex, int age) {
        super(species, food, state, sex, age);
    }
    void setFood(){this.food="meat";}
    public String getFood(){return food;}
    void setFeet(){this.feet="pads";}
    String getFeet(){return feet;}
}

class Hervibore extends Mammal {
    Hervibore(String species, String food, State state, Sex sex, int age) {
        super(species, food, state, sex, age);
    }
    /* TODO:
        - Define constructors, variables, methods
        - Set food "grass"
        - Set feet "hoof"
        - Implement the method "feed"
    */
    void setFood(){food="grass";}
    public String getFood(){return food;}
    void setFeet(){feet="hoof";}
    public String getFeet(){return feet;}

    public String feed(String food){
        return null;
    };
}

class Bird extends Animal {
    String id;
    String importedDate;
    String area;        // area # in which the animal is

    Bird(String species, String food, State state, Sex sex, int age) {
        super(species, food, state, sex, age);
    }
    /* TODO:
        - Define constructors, variables, methods
        - Define concrete method "feed"
     */
    void setId(String id){
        this.id=id;
    }
    String getId(){return id;}
    void setImportedDate(String importedDate){
        this.importedDate=importedDate;
    }
    String getImportedDate(){return importedDate;}
    void setArea(String Area){this.area=area;}
    String getArea(){return area;}
    public String feed(String food){
        return "worm";
    }
}
