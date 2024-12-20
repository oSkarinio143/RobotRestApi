package pakiet.service;

import org.springframework.stereotype.Service;
import pakiet.exceptions.IncorrectIdRuntimeException;
import pakiet.exceptions.IncorrectNumberRuntimeException;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.modules.robot.*;
import pakiet.service.operate.RobotFactory;

import java.util.*;

@Service
public class Generator{
    private static final Random random = new Random();
    private static final int EPIC_CHANCE = 5;
    private static final int RARE_CHANCE = 20;
    private static final int BOOK_CHANCE = 80;
    private static final int BOARD_GAMES_CHANCE = 12;
    private static final int COMPUTER_GAMES_CHANCE = 7;
    private static final int HOUSE_CHANCE = 1;
    private RobotFactory robotFactory;
    private Sorting sorting;

    public Generator(RobotFactory robotFactory, Sorting sorting){
        this.robotFactory = robotFactory;
        this.sorting = sorting;
    }

    public Iterator<Integer> generateMyIterator(int number, int expectedNumber){
        int[] numberArray = new int[1];
        numberArray[0] = number;
        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                if (numberArray[0] >= expectedNumber) {
                    numberArray[0] = numberArray[0] - expectedNumber;
                    return true;
                } else {
                    return false;
                }
            }
            @Override
            public Integer next() {
                return expectedNumber;
            }

        };
    }

    public int returnOneRegardChance (int ...chance){
        int random = (int)(Math.random()*100)+1;
        int nextChance=0;
        for (int i : chance) {
            nextChance+=i;
            if(nextChance>=random){
                return i;
            }
        }
        throw new IncorrectNumberRuntimeException();
    }

    public Integer generateNumber(int minRange, int maxRange){
        return random.nextInt((maxRange-minRange)+1)+minRange;
    }

    private Integer generateStatsPower(){
        int minStatsRange = 6;
        int maxStatsRange = 18;
        return generateNumber(minStatsRange, maxStatsRange);
    }

    private Level generateLevel(int id){
        if(id==1) return Level.BEGINNER;
        if(id==2) return Level.INTERMEDIATE;
        if(id==3) return Level.ADVANCED;
        throw new IncorrectIdRuntimeException("Incorrect Number");
    }

    private Rarity generateRarity(int epicChance, int rareChance){
        int randomNumber = random.nextInt(101);
        if(randomNumber<=epicChance){
            return Rarity.EPIC;
        }
        else if(randomNumber<=(rareChance+epicChance)){
            return Rarity.RARE;
        }
        else if(randomNumber > (rareChance+epicChance)){
            return Rarity.COMMON;
        }
        return null;
    }

    private Map<Integer, Integer> generateEachStat(int statsPower, Level level){
        Map <Integer, Integer> statsMap = new HashMap<>();

        int loopRandomNumber;
        do {
            loopRandomNumber=statsPower;
            statsMap.put(0, random.nextInt(level.getConstraint()));
            statsMap.put(1, random.nextInt(level.getConstraint()));
            statsMap.put(2, random.nextInt(level.getConstraint()));
            statsMap.put(3, random.nextInt(level.getConstraint()));
            loopRandomNumber-=statsMap.get(0)+statsMap.get(1)+statsMap.get(2)+statsMap.get(3);
        }while(loopRandomNumber!=0);
        return statsMap;
    }

    public Map<Integer, Integer> generateUpgradesStats(int upgradeStatsPower){
            Map<Integer, Integer> upgradeStats = new LinkedHashMap<>();
            Iterator <Integer> myIterator = generateMyIterator(upgradeStatsPower%4,1);
            int thisStat;

            for (int i = 0; i < 4; i++) {
                thisStat=upgradeStatsPower/4;
                if(myIterator.hasNext())
                    thisStat+=myIterator.next();
                upgradeStats.put(i, thisStat);
            }
            return upgradeStats;
        }

    public Map<Integer, Integer> upgradeStatsNumbers(Map<Integer, Integer> statsMap, int upgradeStatsPower, Level level){
        Map<Integer, Integer> upgradeMap = generateUpgradesStats(upgradeStatsPower);
        Map<Integer, Integer>sortedStatsMap = sorting.sortMapStream(statsMap);
        int kUpg = 0;
        int nextUpg=0;
        for(Map.Entry<Integer, Integer> entryMap : sortedStatsMap.entrySet()){
            int v;
            int k = entryMap.getKey();
            v = entryMap.getValue() + upgradeMap.get(kUpg) + nextUpg;
            nextUpg=0;
            kUpg++;
            while (v>level.getConstraint()){
                v-=1;
                nextUpg++;
            }
            sortedStatsMap.put(k, v);
        }
        return sortedStatsMap;
    }

    public int countBasicStatsUpgradeInvestor(Investor investor){
        return investor.getRarity().getAdditionalStats() + Level.getAdditionalStatsForLevel(investor.getLevel().getAdditionalStats());
    }

    public int countBasicStatsUpgradeSeller(AbstractSeller seller){
        return seller.getRarity().getAdditionalStats() + Level.getAdditionalStatsForLevel(seller.getLevel().getAdditionalStats());
    }

    public int countUpgradeLevelInvestor(Investor investor){
        int newLevelId = investor.getLevel().getId()+1;
        investor.setLevel(Level.getById(newLevelId));
        return Level.getById(newLevelId).getAdditionalStats();
    }

    public int countUpgradeLevelSeller(AbstractSeller seller){
        int newlevelId = seller.getLevel().getId()+1;
        seller.setLevel(Level.getById(newlevelId));
        return Level.getById(newlevelId).getAdditionalStats();
    }

    public Investor generateBasicInvestor(int levelNumber){
        int rareChance = 20;
        int epicChance = 5;

        Level level = generateLevel(levelNumber);
        Rarity rarity = generateRarity(epicChance, rareChance);
        Map<Integer, Integer> eachStat = generateEachStat(generateStatsPower(), level);

        return (Investor) robotFactory.createSeller("investor", eachStat, rarity, level);
    }

    public <T extends AbstractSeller> AbstractSeller generateBasicConcreteSeller(int levelNumber, Class<T> type){
        Level level = generateLevel(levelNumber);
        Rarity rarity = generateRarity(EPIC_CHANCE, RARE_CHANCE);
        Map<Integer, Integer> eachStat = generateEachStat(generateStatsPower(), level);

        if(type==(SellerBooks.class)){
            return (AbstractSeller) robotFactory.createSeller("books", eachStat, rarity, level);
        }
        else if(type==SellerBoardGames.class){
            return (AbstractSeller) robotFactory.createSeller("board_games", eachStat, rarity, level);
        }
        else if(type==SellerComputerGames.class){
            return (AbstractSeller) robotFactory.createSeller("computer_games", eachStat, rarity, level);
        }
        else if(type==SellerHouses.class){
            return (AbstractSeller) robotFactory.createSeller("houses", eachStat, rarity, level);
        }
        throw new IncorrectNumberRuntimeException();
    }

    public void upgradeBasicInvestor(Investor investor){
        int statsUpgrade = countBasicStatsUpgradeInvestor(investor);
        investor.setStatistics(upgradeStatsNumbers(sorting.sortMapComparator(investor.getStatistics()), statsUpgrade, investor.getLevel()));
    }

    public void upgradeBasicSeller(AbstractSeller seller){
        int upgradeStats = countBasicStatsUpgradeSeller(seller);
        seller.setStatistics(upgradeStatsNumbers(sorting.sortMapComparator(seller.getStatistics()), upgradeStats, seller.getLevel()));
    }

    public void upgradeLevelInvestor(Investor investor){
        int statsNumber = countUpgradeLevelInvestor(investor);
        investor.setStatistics(upgradeStatsNumbers(sorting.sortMapComparator(investor.getStatistics()), statsNumber, investor.getLevel()));
    }

    public void upgradeLevelSeller(AbstractSeller seller){
        int statsNumber = countUpgradeLevelSeller(seller);
        seller.setStatistics(upgradeStatsNumbers(sorting.sortMapComparator(seller.getStatistics()), statsNumber, seller.getLevel()));
    }

    public double sinew(double basis, int index){
        double number=basis;
        if(index>0) {
            for (int i = 1; i < index; i++) {
                number = number * basis;
            }
        }
        else
            number=1;
        return number;
    }

    public boolean checkingRevolt(int revoltChance){
        for (int i = 0; i < revoltChance; i++) {
            int number = random.nextInt(1000) + 1;
            if(number==revoltChance){
                return true;
            }
        }
        return false;
    }
}
