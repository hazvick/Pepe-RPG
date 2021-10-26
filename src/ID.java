
/*
Using Enumeration to identify objects.
In the game, we don't want if the player moves that all of the boxes, AI and other stuff move as well.
Therefore we use Enumerations to identify objects.
 */

public enum ID { //used to identify, instead of using ints etc.
    Player(),
    Block(),
    Crate(),
    Spit(),
    Enemy(),
    Item();
}
