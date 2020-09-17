import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class YahtzeeStrategy {
    Yahtzee game = new Yahtzee();
    //System.out.println( game );

    // The basic structure of a turn is that you must roll the dice,
    // choose to score the hand or to reroll some/all of the dice.
    //
    // If scoring you must provide the decision making on what box to score.
    //
    // If rerolling you must provide the decision making on which dice to
    // reroll.
    //
    // You may score or reroll a second time after the first reroll.
    //
    // After the second reroll you must score or scratch (score a 0).


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
            //System.out.println( "Playing turn " + turnNum + ": ");
            boxFilled = game.getScoreState();
            keep = new boolean[5];
            roll = game.play();
            // CHANGE THIS STRAGEGY TO SUIT YOURSELF
            // THIS STRATEGY KEEPS YAHTZEES, LARGE STRAIGHTS AND THAT'S IT.
            // YOU SHOULD BE ABLE TO MODULARLIZE THIS WITH SOME THOUGHT

            //System.out.println( "Turn " + turnNum + " Roll 1: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

            //DETERMINE IF THE ROLL IS A YAHTZEE LARGE STRAIGHT OR FULL HOUSE
            if(DetermineAutoFillPatterns()){
                continue;
            }
            // Since it is easier to reason with sorted arrays, we clone the
            // roll and work off a temporary copy.
            int[] tempRoll = roll.clone();
            Arrays.sort(tempRoll);

            //DECIDE IF THERE ARE DICE WE SHOULD KEEP
            //PRIORITY IS FK > TK > PAIRS
            DetermineKeepSituation(tempRoll);
            // ====================================START ROLL 2==========================================================
            roll = game.play(keep);
            //System.out.println( "Turn " + turnNum + " Roll 2: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

//            // NOTE THIS IS THE SAME AS ABOVE, WHICH IS SILLY!!!
            if(DetermineAutoFillPatterns()){
                continue;
            }
            // DO NOT SORT THE ROLL ARRAY - the order is significant!!
            // Since it is easier to reason with sorted arrays, we clone the
            // roll and work off a temporary copy.
            tempRoll = roll.clone();
            Arrays.sort(tempRoll);
            // If we have a 3 of a kind or 4 of a kind, roll for yahtzee
            // otherwise roll all 5 dice
            DetermineKeepSituation(tempRoll);
            //============================================ START ROLL 3===================================================
            roll = game.play(keep);
            //System.out.println( "Turn " + turnNum + " Roll 3: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

            // MUST SCORE SOMETHING!!
            if(DetermineAutoFillPatterns()){
                continue;
            }
            if(thisRollHas.get(Yahtzee.Boxes.SS))
                if (game.setScore("SS")) {
                    continue;
                }
            if (HandleTkFk(tempRoll, turnNum))
                    continue;
            int sumOfRoll = SumArray(tempRoll);
            if(sumOfRoll > 16 ){
                if (!boxFilled.get(Yahtzee.Boxes.C))
                    if(game.setScore("C")){
                        //System.out.println("hit C");
                        continue;
                    }
            }
            else if(sumOfRoll < 8){
                if(!boxFilled.get(Yahtzee.Boxes.U1)){
                    if(game.setScore("U1")){
                        //System.out.println("hit U1");
                        continue;
                    }
                }
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

                    case TK:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("TK");
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
                    case FK:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore("FK");
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
                else if(!boxFilled.get(Yahtzee.Boxes.U2)){
                    scratchBox = Yahtzee.Boxes.U2;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.Y)){
                    scratchBox = Yahtzee.Boxes.Y;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.LS)){
                    scratchBox = Yahtzee.Boxes.LS;
                }

                else if(!boxFilled.get(Yahtzee.Boxes.U3)){
                    scratchBox = Yahtzee.Boxes.U3;
                }

                else if(!boxFilled.get(Yahtzee.Boxes.SS)){
                    scratchBox = Yahtzee.Boxes.SS;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.TK)){
                    scratchBox = Yahtzee.Boxes.TK;
                }
                else if(!boxFilled.get(Yahtzee.Boxes.FK)){
                    scratchBox = Yahtzee.Boxes.FK;
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
                else if(!boxFilled.get(Yahtzee.Boxes.FH)){
                    scratchBox = Yahtzee.Boxes.FH;
                }
                scratched = game.scratchBox(scratchBox);

            }
            if (!scored && !scratched)
                System.err.println("Invalid game state, can't score, can't scratch.");

            //System.out.println( game );
        }
        return game.getGameScore();
    }

    public boolean CheckPattern(Object key, String pattern){
        if ((thisRollHas.get(key))) {
            // note that set score can fail if the pattern doesn't actually match up or
            // if the box is already filled.  Not a problem with yahtzee but maybe for
            // other paterns it is a problem.
            if (game.setScore(pattern)) {
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean DetermineAutoFillPatterns(){
        if(CheckPattern(Yahtzee.Boxes.Y, "Y")){
            return true;
        }
        else if(CheckPattern(Yahtzee.Boxes.LS, "LS")){
            return true;
        }
        else if(CheckPattern(Yahtzee.Boxes.FH, "FH")){
            return true;
        }
        else{
            return false;
        }
    }

    public void DetermineKeepSituation(int[]tempRoll){
        int consecutiveDice = determineConsecutiveDice(tempRoll);
        if (thisRollHas.get(Yahtzee.Boxes.FK) || thisRollHas.get(Yahtzee.Boxes.TK)) {
            // if there is a 3 or 4 of a kind, the middle die is always
            // part of the pattern, keep any die that matches it
            for (int i = 0; i < roll.length; i++)
                if (roll[i] == tempRoll[2]) keep[i] = true;
        }
        //if the user has a SS and it is open then keep it otherwise do not
        else if(thisRollHas.get(Yahtzee.Boxes.SS) && !boxFilled.get(Yahtzee.Boxes.SS)){
            //loop through roll
            for (int i = 0; i < roll.length - 1; i++){
                //if the index + 1 is one greater than the index value then keep the value at that index
                if(tempRoll[i + 1] - tempRoll[i] == 1){
                    //loop through the roll array again to get the roll's proper index instead of the temp roll index
                    for(int j = 0; j < roll.length; j++){
                        if(roll[j] == tempRoll[i]){
                            keep[j] = true;
                        }
                    }
                    //if it reaches the end of the roll then keep the end value as well
                    if(tempRoll[i + 1] == tempRoll[roll.length - 1]){
                        //loop through the roll array again to get the roll's proper index instead of the temp roll index
                        for(int j = 0; j < roll.length; j++) {
                            if (roll[j] == tempRoll[i + 1]) {
                                keep[j] = true;
                            }
                        }
                    }
                }
            }
        }
        else{
            if(checkPairs(tempRoll)){
                //if a full house is open and TK and FK are not open then keep both of the pairs
                if(!boxFilled.get(Yahtzee.Boxes.FH) && boxFilled.get(Yahtzee.Boxes.TK) && boxFilled.get(Yahtzee.Boxes.FK)){
                    KeepAllPairs(tempRoll);
                }
                //otherwise take the highest pair of the 2
                else{
                    KeepHighestPair(tempRoll);
                }
            }
            else{
                int highestIndex = 0;
                int current = 0;
                for(int i = 0; i < roll.length; i++){
                    if(tempRoll[i] > tempRoll[highestIndex]){
                        highestIndex = i;
                    }
                }
                for(int i = 0; i < roll.length; i++){
                    if(roll[i] == tempRoll[highestIndex]){
                        keep[i] = true;
                    }
                }
            }


        }
    }
    public int determineConsecutiveDice(int[] tempRoll){
        int consecutiveCount = 0;
        for(int i =0; i < tempRoll.length - 1; i++) {
            if (tempRoll[i + 1] - tempRoll[i] == 1) {
                consecutiveCount++;
            }
        }
        return consecutiveCount;

    }
    public boolean checkPairs(int[]tempRoll){
        boolean hasPair = false;
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1]) {
                hasPair = true;
            }
        }
        return hasPair;
    }

    public void KeepHighestPair(int[]tempRoll){
        int pairIndex = 0;
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1]) {
                pairIndex = i;
            }
        }
        if(pairIndex != 0){
            for(int i = 0; i < roll.length; i++){
                if(roll[i] == tempRoll[pairIndex]){
                    keep[i] = true;
                }
            }
        }

    }
    public void KeepAllPairs(int[]tempRoll){
        for(int i = 0; i < roll.length - 1; i++) {
            if (tempRoll[i] == tempRoll[i + 1]) {
                for(int j =0; j < roll.length; j++){
                    if(roll[j] == tempRoll[i]){
                        keep[j] = true;
                    }
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
            if(!boxFilled.get(getKeyFromString(DetermineUpperBox(tempRoll[2]))) && tempRoll[2] >= 1){
                return game.setScore(DetermineUpperBox(tempRoll[2]));
            }
            else{
                if(!boxFilled.get(Yahtzee.Boxes.FK)){
//                    if(SumArray(tempRoll) < 12){
//                        if()
//                    }
                    return game.setScore("FK");
                }
                else{
                    if(!boxFilled.get(getKeyFromString(DetermineUpperBox(tempRoll[2])))){
                        return game.setScore(DetermineUpperBox(tempRoll[2]));
                    }
                    else{
                        if(!boxFilled.get(Yahtzee.Boxes.TK)){
                            return game.setScore("TK");
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
        else if (thisRollHas.get(Yahtzee.Boxes.TK)){
            if(turnNum < 11){
                if(!boxFilled.get(getKeyFromString(DetermineUpperBox(tempRoll[2])))){
                    return game.setScore(DetermineUpperBox(tempRoll[2]));
                }
                else{
                    if(!boxFilled.get(Yahtzee.Boxes.TK)){
                        return game.setScore("TK");
                    }
                    else{
                        return false;
                    }

                }
            }
            else{
                if(!boxFilled.get(getKeyFromString(DetermineUpperBox(tempRoll[2])))) {
                    return game.setScore(DetermineUpperBox(tempRoll[2]));
                }
                else{
                    if(!boxFilled.get(Yahtzee.Boxes.TK)){
                        return game.setScore("TK");
                    }
                    else{
                        return false;
                    }

                }
            }

        }
        else{
            return false;
        }
    }
//            if(!boxFilled.get(Yahtzee.Boxes.FK)){
//                return game.setScore("FK");
//            }
//            else{
//                String upperBox = DetermineUpperBox(tempRoll[2]);
//                if(!boxFilled.get(getKeyFromString(upperBox))){
//                    return game.setScore(upperBox);
//
//                }
//                else{
//                    return false;
//                }
//                //determine which upper card to choose from
//            }


    //            if(!boxFilled.get(Yahtzee.Boxes.TK)){
//                return game.setScore("TK");
//            }
//            else{
//                String upperBox = DetermineUpperBox(tempRoll[2]);
//                if(!boxFilled.get(getKeyFromString(upperBox))){
//                    return game.setScore(upperBox);
//
//                }
//                else{
//                    return false;
//                }
//            }
//        }
//        else{
//           return false;
    public String DetermineUpperBox(int upperBoxNum){ ;
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
    

}