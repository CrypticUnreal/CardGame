/*
Will store a non negative integer denomination
-Return value
Not allow value to change
 */

public class Card {
    /* Stores value on card 
    "final" means can not change after*/
    private final int value;

    //Card constructor (run when make new card)
    //Card values can not be negative!
    public Card(int value) {
        if(value <0){
            throw new IllegalArgumentException(
                "Card value can not be negative!"
            );
        }

        this.value = value;

    }

    //Allows the value to be accessible from another class
    //(encapsulation)
    public int getValue() {
        return value;
    }

    /*Control how card is displayed when printed.
    When card with value 7 it prints (7)
    */
   
    public String toString() {
        return String.valueOf(value);
    }


}