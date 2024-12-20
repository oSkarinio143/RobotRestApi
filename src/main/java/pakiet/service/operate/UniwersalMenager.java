package pakiet.service.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class UniwersalMenager {
    private static final Random random = new Random();

    public static boolean isInRange(int number, int minRange, int maxRange){
        return number <= maxRange && number >= minRange;
    }

    public static boolean isInteger(String idChoice){
        char[] charki = idChoice.toCharArray();
        List<Character> digitList
                = new ArrayList<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        for (char charek : charki) {
            if(!digitList.contains(charek)){
                return false;
            }
        }
        return true;
    }

    public static int stringToInteger(String stringToConvert){
        int convertedInteger = Integer.parseInt(stringToConvert);
        return convertedInteger;
    }

    public static boolean checkStringIntList(String stringToCheck, List<Integer> list){
        if (stringToCheck==null)
            return false;
        if(isInteger(stringToCheck)){
            int convertedInteger = stringToInteger(stringToCheck);
            return list.contains(convertedInteger);
        }
        return false;
    }

    public static boolean checkStringIntRange(String stringToCheck, int minRange, int maxRange){
        if(isInteger(stringToCheck)) {
            int convertedInteger = stringToInteger(stringToCheck);
            return isInRange(convertedInteger, minRange, maxRange);
        }
        return false;
    }

    public static int sumStats(Map<Integer, Integer> mapStats, int ...statsId){
        int sum=0;
        for (int i : statsId) {
            sum+=mapStats.get(i);
        }
        return sum;
    }

    public static boolean drawByChance(int chanceEnd){
        int minRange=1;
        int maxRange=100;
        int chanceStart=1;
        for (int i = chanceStart; i<=chanceEnd; i++){
            int chosedNumber = random.nextInt(maxRange)+minRange;
            if(chosedNumber==1)
                return true;
            maxRange--;
        }
        return false;
    }
}
