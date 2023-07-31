package Assignment3;

import org.jetbrains.annotations.NotNull;

public class Area implements Cage {
    int areaNum;                // Area number (id)
    String animalSpecies;       // Species of animal in the area
    int numAnimals;             // Number of animals in the area
    Condition condition;

    public void setCondition(Condition condition){
        this.condition = condition;
    }
    @Override
    public Condition getCondition() {
        return condition;
    }
    /* TODO:
        - Define constructors, variables, methods
     */
    Area(int areaNum,String animalSpecies,int numAnimals){
        this.areaNum = areaNum;
        this.animalSpecies = animalSpecies;
        this.numAnimals = numAnimals;
    }
}

class Section {
    Area[] areas;
    String section;         // Section ID: A, B, or C
    String typeAnimal;      // Type of animals
    int numAreas;           // Number of areas in the given section
    int numAreasOcc;        // Number of areas in which specific animals area asssigned

    /* TODO:
        - Define constructors, variables, methods
     */
    Section(Area[] areas) {
        this.areas = areas;
    }

    void setTypeAnimal(@NotNull String typeAnimal){
        this.typeAnimal = typeAnimal;
        if (typeAnimal.equalsIgnoreCase("Herbivore")){
                section = "A";
        }else if(typeAnimal.equalsIgnoreCase("Carnivore")){
                section = "B";
        }else if(typeAnimal.equalsIgnoreCase("Bird")){
                section = "C";
        }else System.out.println("Not an order of Animal!");
    }
        String getTypeAnimal(){return section;}

        void setAreas(int numAreas){
        this.numAreas=numAreas;
        }
        int getAreas(){
            return numAreas;
        }
        void setNumAreasOcc(int numAreasOcc){
            this.numAreasOcc=numAreasOcc;
        }
        int getNumAreasOcc(){
            return numAreasOcc;
        }
    }
