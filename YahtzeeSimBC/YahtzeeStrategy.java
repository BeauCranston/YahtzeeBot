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
            if(determineAutoFillPatterns(turnNum)){
                continue;
            }
            //DECIDE IF THERE ARE DICE WE SHOULD KEEP OR Roll everything
            //PRIORITY IS U(X) > FK > TK > PAIRS
            determineKeepSituation(tempRoll, rollNum, turnNum);
            // ====================================START ROLL 2==========================================================
            roll = game.play(keep);
            rollNum = 2;
            //System.out.println( "Turn " + turnNum + " Roll 2: " + Arrays.toString( roll ) );
            thisRollHas = game.has();
            tempRoll = roll.clone();
            Arrays.sort(tempRoll);
            if(determineAutoFillPatterns(turnNum)){
                continue;
            }
            //DECIDE IF THERE ARE DICE WE SHOULD KEEP OR Roll everything
            //PRIORITY IS U(X) > FK > TK > PAIRS
            determineKeepSituation(tempRoll, rollNum, turnNum);
            //============================================ START ROLL 3===================================================
            roll = game.play(keep);
            rollNum = 3;
            //System.out.println( "Turn " + turnNum + " Roll 3: " + Arrays.toString( roll ) );
            thisRollHas = game.has();
            tempRoll = roll.clone();
            Arrays.sort(tempRoll);
            // MUST SCORE SOMETHING!!
            //determine if it is a FH, Y, or LS
            if(determineAutoFillPatterns(turnNum)){
                continue;
            }
            //if roll has SS score it
            if(thisRollHas.get(Yahtzee.Boxes.SS) && tryScoreValue("SS", true))
                continue;

            if (HandleTkFk(tempRoll, turnNum))
                continue;

            int sumOfRoll = SumArray(tempRoll);
            if(sumOfRoll > 20 ){
                if(tryScoreValue("C", true)) continue;
            }
            else if(sumOfRoll < 6){
                if(tryScoreValue("U1", true)) continue;
            }
//            else if(SumArray(tempRoll) <){
//
//            }
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
                    case C:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("C");
                        break;
                    case TK:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("TK");
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
                    case U2:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U2");
                        break;
                    case U1:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("U1");
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

    public boolean determineAutoFillPatterns(int turnNum){
        if(thisRollHas.get(Yahtzee.Boxes.Y)){
            if(roll[1] > 4){
                if(!boxFilled.get(getKeyFromString(determineUpperBox(roll[1]))) && !boxFilled.get(Yahtzee.Boxes.Y) && turnNum > 9){
                    return tryScoreValue(determineUpperBox(roll[1]), false);
                }
                else{
                    return tryScoreValue("Y", false);
                }
            }
            else{
                return tryScoreValue("Y", false);
            }
        }
        else if(thisRollHas.get(Yahtzee.Boxes.LS) && !boxFilled.get(Yahtzee.Boxes.LS)){
            return tryScoreValue("LS", false);

        }
        else if(thisRollHas.get(Yahtzee.Boxes.LS) && !boxFilled.get(Yahtzee.Boxes.SS) && turnNum >= 11){
            return tryScoreValue("SS", false);
        }
        else if(thisRollHas.get(Yahtzee.Boxes.FH)) {

            return tryScoreValue("FH", false);

        }
        else{
            return false;
        }
    }
    public void determineKeepSituation(int[]tempRoll, int rollNum, int turnNum){
        // if hand has a full house and full house has been scored
        if(thisRollHas.get(Yahtzee.Boxes.FH)){
            //if the TK > 1 keep the TK
            if(tempRoll[2] > 1 ){
                keepValue(tempRoll[2]);
            }
            //otherwise keep the pair
        }
        else if (thisRollHas.get(Yahtzee.Boxes.FK) || thisRollHas.get(Yahtzee.Boxes.TK)) {
            // if there is a 3 or 4 of a kind, the middle die is always
            // part of the pattern, keep any die that matches it
            //THIS MAY BE SUBJECT TO CHANGE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            int highestPair = getHighestOpenPair(tempRoll);
            if( highestPair > 2){
                if(!boxFilled.get(Yahtzee.Boxes.FK) || !boxFilled.get(Yahtzee.Boxes.TK)  || !boxFilled.get(Yahtzee.Boxes.FH) || !boxFilled.get(getKeyFromString(determineUpperBox(highestPair)))){
                    keepValue(highestPair);
                }
                else{
                    if(SumArray(tempRoll) > 9) {
                        keepValue(tempRoll[2]);
                    }
                }
            }
            else{
                keepValue(tempRoll[2]);
            }
        }
        //if the user has a SS and it is open then keep it otherwise do not
        else if(thisRollHas.get(Yahtzee.Boxes.SS) && !boxFilled.get(Yahtzee.Boxes.SS)){
            //loop through roll
            keepSmallStraight(tempRoll);
        }
        else if(almostSmallStraight(tempRoll) && !boxFilled.get(Yahtzee.Boxes.SS) && boxFilled.get(Yahtzee.Boxes.FK) && boxFilled.get(Yahtzee.Boxes.TK)  && getHighestOpenPair(tempRoll) < 4){
            //System.out.println(tempRoll[0] + " " + tempRoll[1] + " " + tempRoll[2] + " " + tempRoll[3] + " " + tempRoll[4]);

            for(int i = 0; i < tempRoll.length - 2; i++){
                if(tempRoll[i + 1] - tempRoll[i] == 1 ){
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
                if(tempRoll[i + 2] - tempRoll[i] == 2){
                    int keepCount = 0;
                    int keepCount2 = 0;
                    for(int j = 0; j < roll.length; j++){
                        if(roll[j] == tempRoll[i] && keepCount == 0){
                            keep[j] = true;
                            keepCount++;
                        }
                        if(i + 2 == tempRoll.length - 1){
                            if(roll[j] == tempRoll[i + 2] && keepCount2 == 0){
                                keep[j] = true;
                                keepCount2++;
                            }
                        }
                    }
                }
            }

        }
        else{
            //check if pairs exist
            if(checkPairs(tempRoll)){
                int highestOpenPair = getHighestOpenPair(tempRoll);
                //if a full house is open and TK and FK are not open then keep all pairs that are found
                if(!boxFilled.get(Yahtzee.Boxes.FH) && boxFilled.get(Yahtzee.Boxes.FK) && boxFilled.get(Yahtzee.Boxes.TK)){
                    if(highestOpenPair >= 3){
                        keepValue(highestOpenPair);
                    }
                    else{
                        keepAllPairs(tempRoll);
                    }

                }
                else{
                    //check the value of the highest value open pair
                    int pairValue = getHighestOpenPair(tempRoll);
                    //if the value of the pair is < 2 keep highest singleton
                    if( pairValue < 2 ){
                        keepValue(tempRoll[roll.length - 1]);
                    }

                    //if pair value is > 3 then keep the pair value
                    else{
                        keepValue(pairValue);
                    }
                }
            }
            else{
                int highestAvailableUpper = findHighestAvailableUpper(tempRoll);
                //loop through roll starting at largest value for performance reasons
                if(highestAvailableUpper >= 2){
                    keepValue(highestAvailableUpper);
                }
                else{
                    if(tempRoll[roll.length - 1] > 3){
                        keepValue(tempRoll[roll.length - 1]);
                    }

                }
            }
        }
    }

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
    public boolean almostSmallStraight(int[]tempRoll){

        int [] tempRollClone = tempRoll.clone();
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
    public boolean checkPairs(int[]tempRoll){
        boolean hasPair = false;
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1]) {
                hasPair = true;
                break;
            }
        }
        return hasPair;
    }
    public int getHighestOpenPair(int[]tempRoll){
        int value = 0;
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1] && !boxFilled.get(getKeyFromString(determineUpperBox(tempRoll[i])))) {
                value = tempRoll[i];
            }
        }
        return value;
    }

    public void keepHighestPair(int[]tempRoll){
        int pairValue = 0;
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1]) {
                pairValue = tempRoll[i];

            }
        }
        if(pairValue != 0){
            keepValue(pairValue);
        }
        else{
            keepValue(tempRoll[roll.length - 1]);
        }

    }
    public void keepAllPairs(int[]tempRoll){
        int pairCounter = 0;
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1]) {
                pairCounter++;

            }
        }
        if(pairCounter == 2){
            for(int i = 0; i < roll.length - 1; i++) {
                if (tempRoll[i] == tempRoll[i + 1]) {
                    keepValue(tempRoll[i]);

                }
            }
        }
    }
    public int SumArray(int[] arr){
        int sum = 0;
        for(int i : arr){
            sum += i;
        }
        return sum;
    }

    public boolean HandleTkFk(int[]tempRoll, int turnNum) {
        if (thisRollHas.get(Yahtzee.Boxes.FK)) {
            if(!tryScoreUpperValue(tempRoll[2], true)){
                if(!tryScoreValue("FK", true)){
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
            //if(turnNum < 10){
            if(!tryScoreUpperValue(tempRoll[2], true)){
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

    public String determineUpperBox(int upperBoxNum){ ;
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
    public void keepValue(int value){
        for(int i = 0; i < roll.length; i++){
            if(roll[i] == value ){
                keep[i] = true;
            }
        }
    }



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

    public boolean tryScoreUpperValue(int pattern, boolean checkIfOpen){
        if(!checkIfOpen){
            return game.setScore(determineUpperBox(pattern));

        }
        else{
            if(!boxFilled.get(getKeyFromString(determineUpperBox(pattern)))){
                return game.setScore(determineUpperBox(pattern));
            }
            else{
                return false;
            }
        }

    }
    public int findHighestAvailableUpper(int[] tempRoll){
        for(int i = roll.length - 1; i > 0; i--){
            if(!boxFilled.get(getKeyFromString(determineUpperBox(tempRoll[i])))){
                return tempRoll[i];
            }
        }
        return 0;
    }
}