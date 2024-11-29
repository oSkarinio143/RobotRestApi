package pakiet.modules.interfaces;

import pakiet.modules.Level;
import pakiet.service.Generator;

import java.util.Map;

public interface Robot {
    double revoltRate= 0.5;

    void setStatistics(Map<Integer, Integer> map);
    Map<Integer, Integer> getStatistics();
    Level getLevel();

    default double revolt() {
        double revoltChance = revoltRate * Generator.sumStats(getStatistics(), 0);
        revoltChance = Math.round(revoltChance * 100.0) / 100.0;
        return revoltChance;
    }

    default void specialFunction() {
        boolean isSuccessful = Generator.generateByChance(1);
        if(isSuccessful){
            setStatistics(Generator.upgradeStatsNumbers(getStatistics(), 1, getLevel()));
        }
    }
}
