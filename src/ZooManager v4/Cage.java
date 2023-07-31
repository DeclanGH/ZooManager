package Assignment4;

enum Condition {
    CLEAN,
    DIRTY,
    CONSTRUCTION,
    EMPTY
}

interface Cage {
    int maxNumAnimal = 10;

    /* TODO:
        - Define methods: setCondition, getCondition
     */
    public void setCondition(Condition condition);
    public Condition getCondition();
}
