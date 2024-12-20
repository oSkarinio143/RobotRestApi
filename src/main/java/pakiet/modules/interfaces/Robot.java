package pakiet.modules.interfaces;

import pakiet.modules.Level;
import pakiet.service.Generator;
import pakiet.service.operate.UniwersalMenager;

import java.util.Map;

public interface Robot {
    double REVOLT_RATE= 0.5;

    void setStatistics(Map<Integer, Integer> map);
    Map<Integer, Integer> getStatistics();
    Level getLevel();

    default double revolt() {
        double revoltChance = REVOLT_RATE * UniwersalMenager.sumStats(getStatistics(), 0);
        revoltChance = Math.round(revoltChance * 100.0) / 100.0;
        return revoltChance;
    }

    default void specialFunction(Generator generator) {
        boolean isSuccessful = UniwersalMenager.drawByChance(1);
        if(isSuccessful){
            setStatistics(generator.upgradeStatsNumbers(getStatistics(), 1, getLevel()));
        }
    }
}
