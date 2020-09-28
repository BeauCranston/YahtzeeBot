import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class   YahtzeeStrategy {
    Yahtzee game = new Yahtzee();
    //System.out.println( game );
    // Used enumMap instead of boolean[] so I can use enums as indexes.
    // Keep track of which boxes I've already filled.
    Map<Yahtzee.Boxes, Boolean> boxFilled;
    boolean[] keep; // flag array of dice to keep
    int[] roll;  // current turn's dice state
    // EXAMPLE GAME PLAY
    // YOU SHOULD HEAVILY EDIT THIS LOGIC TO SUIT YOUR STRATEGY
    // Track what pattern matches are in the roll.
    Map<Yahtzee.Boxes, Boolean> thisRollHas;
    public int play() {
        for (int turnNum = 1; turnNum <= 13; turnNum++) {
            int rollNum = 0;
            //System.out.println( "Playing turn " + turnNum + ": ");
            boxFilled = game.getScoreState();
            keep = new boolean[5];
            roll = game.play();
            rollNum = 1;
            //System.out.println( "Turn " + turnNum + " Roll 1: " + Arrays.toString( roll ) );
            thisRollHas = game.has();
            int[] tempRoll = roll.clone();
            Arrays.sort(tempRoll);
            //DETERMINE IF THE ROLL IS A YAHTZEE LARGE STRAIGHT OR FULL HOUSE
            if(determineAutoFillPatterns(turnNum, rollNum, tempRoll)){
                continue;
            }
            //DECIDE IF THERE ARE DICE WE SHOULD KEEP OR Roll everything
            //PRIORITY IS U(X) > FK > TK > SS > PAIRS
            determineKeepSituation(tempRoll);
            // ====================================START ROLL 2==========================================================
            roll = game.play(keep);
            rollNum = 2;
            //System.out.println( "Turn " + turnNum + " Roll 2: " + Arrays.toString( roll ) );
            thisRollHas = game.has();
            tempRoll = roll.clone();
            Arrays.sort(tempRoll);
            if(determineAutoFillPatterns(turnNum, rollNum, tempRoll)){
                continue;
            }
            //DECIDE IF THERE ARE DICE WE SHOULD KEEP OR Roll everything
            //PRIORITY IS U(X) > FK > TK > PAIRS
            determineKeepSituation(tempRoll);
            //============================================ START ROLL 3===================================================
            roll = game.play(keep);
            rollNum = 3;
            //System.out.println( "Turn " + turnNum + " Roll 3: " + Arrays.toString( roll ) );
            thisRollHas = game.has();
            tempRoll = roll.clone();
            Arrays.sort(tempRoll);
            // MUST SCORE SOMETHING!!
            //determine if it is a FH, Y, or LS
            if(determineAutoFillPatterns(turnNum, rollNum, tempRoll)){
                continue;
            }
            //if roll has SS score it
            if(thisRollHas.get(Yahtzee.Boxes.SS) && tryScoreValue("SS", true))
                continue;

            //score a TK FK in Upper or lower card if it is available
            if (HandleTkFk(tempRoll))
                continue;

            //if the sum of the roll is > 20 then score it in Chance
            int sumOfRoll = SumArray(tempRoll);
            if(sumOfRoll > 20 ){
                if(tryScoreValue("C", true)) continue;
            }

            // score it anywhere
            boolean scored = false;
            for (Yahtzee.Boxes b : Yahtzee.Boxes.values()) {
                switch (b) {
                    // yes, at this point I wish I hadn't used strings ...
                    // but I can set priority by rearranging things
                    case FH:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("FH");
                        break;

                    case SS:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("SS");
                        break;

                    case U6:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U6");
                        break;


                    case U5:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U5");
                        break;
                    case U4:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U4");
                        break;
                    case U3:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U3");
                        break;
                    case TK:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("TK");
                        break;
                    case U2:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U2");
                        break;
                    case U1:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U1");
                        break;

                    case C:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("C");
                        break;
                }

                if (scored) {
                    break;
                }
            }
            boolean scratched = false;
            if (!scored) {
                Yahtzee.Boxes scratchBox = null;
                if(!boxFilled.get(Yahtzee.Boxes.U1)){
                    scratchBox = Yahtzee.Boxes.U1;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.Y)){
                    scratchBox = Yahtzee.Boxes.Y;
                }

                else if(!boxFilled.get(Yahtzee.Boxes.LS)){
                    scratchBox = Yahtzee.Boxes.LS;
                }

                else if(!boxFilled.get(Yahtzee.Boxes.FK)){
                    scratchBox = Yahtzee.Boxes.FK;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.U2)){
                    scratchBox = Yahtzee.Boxes.U2;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.FH)){
                    scratchBox = Yahtzee.Boxes.FH;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.SS)){
                    scratchBox = Yahtzee.Boxes.SS;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.TK)){
                    scratchBox = Yahtzee.Boxes.TK;
                }

                else if(!boxFilled.get(Yahtzee.Boxes.U3)){
                    scratchBox = Yahtzee.Boxes.U3;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.U4)){
                    scratchBox = Yahtzee.Boxes.U4;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.U5)){
                    scratchBox = Yahtzee.Boxes.U5;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.U6)){
                    scratchBox = Yahtzee.Boxes.U6;
                }

                scratched = game.scratchBox(scratchBox);

            }
            if (!scored && !scratched)
                System.err.println("Invalid game state, can't score, can't scratch.");

            //System.out.println( game );
        }
        return game.getGameScore();
    }

    /**
     * This method determines if a FH, Y or a LS can be scored.
     * @param turnNum
     * @return
     */
    public boolean determineAutoFillPatterns(int turnNum, int rollNum, int[] tempRoll){
        if(thisRollHas.get(Yahtzee.Boxes.Y)){
            //if the roll has a yahtzee and it is > 4  and the turn number is > 9 and yahtzee bonus is not available then score it in the upper card instead of the yahtzee to prioritize the upper bonus 
            if(roll[1] > 4){
                if(!boxFilled.get(getKeyFromString(getUpperBoxByValue(roll[1]))) && !boxFilled.get(Yahtzee.Boxes.Y) && turnNum > 9){
                    return tryScoreValue(getUpperBoxByValue(roll[1]), false);
                }
                //otherwise score a yahtzee
                else{
                    return tryScoreValue("Y", false);
                }
            }
            else{
                return tryScoreValue("Y", false);
            }
        }
        //if the roll is a large straight, and it is available then take the large straight
        else if(thisRollHas.get(Yahtzee.Boxes.LS) && !boxFilled.get(Yahtzee.Boxes.LS)){
            return tryScoreValue("LS", false);

        }
        //if the roll is a full house but the sum of the roll is > 25 then take the TK
        else if(thisRollHas.get(Yahtzee.Boxes.FH)) {
            if(rollNum == 1 && tempRoll[2] > 3 && !boxFilled.get(getKeyFromString(getUpperBoxByValue(tempRoll[2])))){
                return false;
            }
            else{
                return tryScoreValue("FH", true);
            }
        }
        else{
            return false;
        }
    }

    /**
     * This method determines what to keep in a specific situation. It prioritizes FK and TK's since the most boxes can be scored from a TK and FK
     * and then SS and then ALMOST SS and then pairs.
     * @param tempRoll

     */
    public void determineKeepSituation(int[]tempRoll){
        //if the roll has a tk or fk
        if (thisRollHas.get(Yahtzee.Boxes.FK) || thisRollHas.get(Yahtzee.Boxes.TK) ) {
            // if there is a 3 or 4 of a kind, the middle die is always
            // part of the pattern, keep any die that matches it
            //if the box is not filled for the upper card, TK or FK and the TK/FK is > 1 then keep the value where the TK or FK is
            if(!boxFilled.get(getKeyFromString(getUpperBoxByValue(tempRoll[2]))) || !boxFilled.get(Yahtzee.Boxes.FK) || !boxFilled.get(Yahtzee.Boxes.TK) && tempRoll[2] > 1){
                keepValue(tempRoll[2]);
            }
            //otherwise take the highest value
            else{
                keepValue(tempRoll[4]);
            }

        }
        //if the user has a SS and it is open then keep it otherwise do not
        else if(thisRollHas.get(Yahtzee.Boxes.SS) && !boxFilled.get(Yahtzee.Boxes.SS)){
            //loop through roll
            keepSmallStraight(tempRoll);
        }
        //if the user has almost an SS and it is open  and the TK and FK abd U6 are filled then keep it
        else if(almostSmallStraight(tempRoll) && !boxFilled.get(Yahtzee.Boxes.SS) && boxFilled.get(Yahtzee.Boxes.FK) && boxFilled.get(Yahtzee.Boxes.TK) && boxFilled.get(Yahtzee.Boxes.U6)){

            keepAlmostSmallStraight(tempRoll);
        }
        else{
            //check if pairs exist
            if(checkPairs(tempRoll) > 0){
                int highestOpenPair = getHighestOpenPair(tempRoll);
                 //if a full house is open and TK and FK are not open then keep the highest open pair if > 2 otherwise keep all pairs
                if(!boxFilled.get(Yahtzee.Boxes.FH)  && checkPairs(tempRoll) == 2){
                    //if the highest open pair is >= 3 then keep it otherwise, keep all of the pairs
                    if(highestOpenPair >= 3){
                        keepValue(highestOpenPair);
                    }
                    else{
                        keepAllPairs(tempRoll);
                    }

                }
                //if FH is not open
                else{
                    //if the value of the pair is < 2 keep highest singleton
                    if( highestOpenPair < 2 ){
                        keepValue(tempRoll[roll.length - 1]);
                    }

                    //if pair value is > 3 then keep the pair value
                    else{
                        keepValue(highestOpenPair);
                    }
                }

            }
            else{
                //if the roll has aboslutely nothing then keep the highest available upper card value
                int highestAvailableUpper = findHighestAvailableUpper(tempRoll);
                //loop through roll starting at largest value for performance reasons
                if(highestAvailableUpper >= 2){
                    keepValue(highestAvailableUpper);
                }
                //keep last value in the roll
                else{
                    if(tempRoll[roll.length - 1] > 3){
                        keepValue(tempRoll[roll.length - 1]);
                    }

                }
            }
        }
    }

    /**
     * keeps the small straight
     * @param tempRoll
     */
    public void keepSmallStraight(int[]tempRoll){
        //loop through roll
        for (int i = 0; i < roll.length - 1; i++){
            //if the index + 1 is one greater than the index value then keep the value at that index
            if(tempRoll[i + 1] - tempRoll[i] == 1){
                //loop through the roll array again to get the roll's proper index instead of the temp roll index
                keepValue(tempRoll[i]);
                //if it reaches the end of the roll then keep the end value as well
                if(tempRoll[i + 1] == tempRoll[roll.length - 1]){
                    //loop through the roll array again to get the roll's proper index instead of the temp roll index
                    keepValue(tempRoll[i+1]);
                }
            }
        }

    }

    /**
     * Determines if almost small straight is in the hand by looping through each value and setting a new value from 1-6 and checking for small straight
     * @param tempRoll
     * @return
     */
    public boolean almostSmallStraight(int[]tempRoll){

        //clone the temproll
        int [] tempRollClone = tempRoll.clone();
        //loop through each value in roll and the change the value from 1-6 and check for SS each time
        for(int i = 0 ; i < tempRoll.length; i++){
            for(int newValue = 1; newValue <= 6; newValue++){
                if(game.has(tempRollClone, Yahtzee.Boxes.SS)){
                    return true;
                }
                else{
                    tempRollClone[i] = newValue;
                }

            }


        }
        return false;
    }
    /**
     * keeps the almost small straight. If a value is increasing by 1 3 times in a row it constitutes as a small straight
     * @param tempRoll
     * @return
     */
    public void keepAlmostSmallStraight(int[] tempRoll){
        //loop through the roll and check 2 spaces ahead
        for(int i = 0; i < tempRoll.length - 1; i++){
            // if the index + 1 is 1 greater than the value at the current index
            if(tempRoll[i + 1] - tempRoll[i] == 1 ){
                //set keep count values to 0. We need these keep count values because we only want to keep the first value that comes up.
                // We don't want to keep any pairs in this instance
                //keepCount is for the situation where i + 1 < the end of the roll array
                //keepCount2 is for the situation where i + 1 = the end of the roll array
                int keepCount = 0;
                int keepCount2 = 0;
                for(int j = 0; j < roll.length; j++){
                    if(roll[j] == tempRoll[i] && keepCount == 0){
                        keep[j] = true;
                        keepCount++;
                    }
                    if(i + 1 == tempRoll.length - 1){
                        if(roll[j] == tempRoll[i + 1 ] && keepCount2 == 0){
                            keep[j] = true;
                            keepCount2++;
                        }
                    }
                }

            }

        }
    }
    //check if pairs exist and the return the number of pairs that do exist
    public int checkPairs(int[]tempRoll){
        int pairCount = 0;
        for(int i = 0; i < roll.length - 1; i++) {
            //if I = i + 1 it is a pair
            if (tempRoll[i] == tempRoll[i + 1]) {
                pairCount++;

            }
        }
        return pairCount;
    }

    /**
     * finds the highest open pair by looping through the roll array and checking if the box for the upper is filled and then returns that value
     * @param tempRoll roll to work with
     * @return
     */
    public int getHighestOpenPair(int[]tempRoll){
        int value = 0;
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1] && !boxFilled.get(getKeyFromString(getUpperBoxByValue(tempRoll[i])))) {
                value = tempRoll[i];
            }
        }
        return value;
    }

    /**
     * Method that checks if there are 2 pairs, if there are then those 2 pairs are kept
     * @param tempRoll roll to work with
     */
    public void keepAllPairs(int[]tempRoll){
        int pairCounter = 0;
        //loop to get pair count
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1]) {
                pairCounter++;

            }
        }
        if(pairCounter == 2){
            //if there are 2 pairs, loop to keep the values
            for(int i = 0; i < roll.length - 1; i++) {
                if (tempRoll[i] == tempRoll[i + 1]) {
                    keepValue(tempRoll[i]);

                }
            }
        }
    }

    /**
     * method that gewts the sum of the roll array
     * @param arr roll array
     * @return sum of roll array
     */
    public int SumArray(int[] arr){
        int sum = 0;
        for(int i : arr){
            sum += i;
        }
        return sum;
    }

    /**
     * determines how to score a TK and FK if the roll has one of those two
     * @param tempRoll the roll to work with
     * @return true if scored false if not
     */
    public boolean HandleTkFk(int[]tempRoll) {
        if (thisRollHas.get(Yahtzee.Boxes.FK)) {
            //try to score the upper value
            if(!tryScoreUpperValue(tempRoll[2], true)){
                //if upper value cannot be scored than try to score the FK
                if(!tryScoreValue("FK", true)){
                    //if FK cannot be scored then try to score the TK
                    return tryScoreValue("TK", true);
                }
                else{
                    return true;
                }
            }
            else{
               return true;
            }
        }

        else if (thisRollHas.get(Yahtzee.Boxes.TK)){
            //try to score the upper value
            if(!tryScoreUpperValue(tempRoll[2], true)){
                //if the sum of tyhe roll is > 15 try to score it in TK otherwise return false
                if(SumArray(tempRoll) > 15){
                    return tryScoreValue("TK", true);
                }
                else{
                    return false;
                }
            }
            else{
                return true;
            }
        }
        else{
            return false;
        }

    }

    /**
     * method to convert an integer to the upper card value equivalent
     * @param upperBoxNum
     * @return
     */
    public String getUpperBoxByValue(int upperBoxNum){ ;
        switch (upperBoxNum){
            case 1:
                return "U1";
            case 2:
                return "U2";
            case 3:
                return "U3";
            case 4:
                return "U4";
            case 5:
                return "U5";
            case 6:
                return "U6";
            default:
                System.out.println("hit default");
                return "";
        }
    }

    /**
     * gets the key object from the string pattern provided
     * @param pattern the string patttern to get key for
     * @return
     */
    public Yahtzee.Boxes getKeyFromString(String pattern){
        switch (pattern){
            case "U1":
                return Yahtzee.Boxes.U1;
            case "U2":
                return Yahtzee.Boxes.U2;
            case "U3":
                return Yahtzee.Boxes.U3;
            case "U4":
                return Yahtzee.Boxes.U4;
            case "U5":
                return Yahtzee.Boxes.U5;
            case "U6":
                return Yahtzee.Boxes.U6;
            case "TK":
                return Yahtzee.Boxes.TK;
            case "FK":
                return Yahtzee.Boxes.FK;
            case "FH":
                return Yahtzee.Boxes.FH;
            case "SS":
                return Yahtzee.Boxes.SS;
            case "LS":
                return Yahtzee.Boxes.LS;
            case "Y":
                return Yahtzee.Boxes.Y;
            case "C" :
                return Yahtzee.Boxes.C;
            default:
                return null;
        }
    }
    //keeps the value in roll
    public void keepValue(int value){
        for(int i = 0; i < roll.length; i++){
            if(roll[i] == value ){
                keep[i] = true;
            }
        }
    }


    /**
     * method that determines if the value can be scored or not. You can optionally check if the value is open or not
     * @param pattern the string pattern to check against
     * @param checkIfOpen if true the method wil check if the box has already been checked
     * @return
     */
    public boolean tryScoreValue(String pattern, boolean checkIfOpen){
        if(!checkIfOpen){
            return game.setScore(pattern);
        }
        else{
            if(!boxFilled.get(getKeyFromString(pattern))){
                return game.setScore(pattern);
            }
            else{
                return false;
            }
        }

    }

    /**
     * method that determines if the upper value can be scored or not based on the integer passed to it. You can optionally check if the value is open or not
     * @param pattern the int pattern to check against
     * @param checkIfOpen if true the method wil check if the box has already been checked
     * @return
     */

    public boolean tryScoreUpperValue(int pattern, boolean checkIfOpen){
        if(!checkIfOpen){
            return game.setScore(getUpperBoxByValue(pattern));

        }
        else{
            if(!boxFilled.get(getKeyFromString(getUpperBoxByValue(pattern)))){
                return game.setScore(getUpperBoxByValue(pattern));
            }
            else{
                return false;
            }
        }

    }

    /**
     * finds the highest available upper card value and returns it
     * @param tempRoll
     * @return
     */
    public int findHighestAvailableUpper(int[] tempRoll){
        for(int i = roll.length - 1; i > 0; i--){
            if(!boxFilled.get(getKeyFromString(getUpperBoxByValue(tempRoll[i])))){
                return tempRoll[i];
            }
        }
        return 0;
    }
}