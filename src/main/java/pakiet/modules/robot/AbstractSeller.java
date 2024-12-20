package pakiet.modules.robot;

import com.fasterxml.jackson.core.base.GeneratorBase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.modules.StatsSeller;
import pakiet.modules.interfaces.RobotSeller;
import pakiet.service.Generator;
import pakiet.service.operate.InvestorMenager;
import pakiet.service.operate.UniwersalMenager;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public abstract class AbstractSeller extends AbstractRobot implements RobotSeller {
    private int sellerId;
    private HashMap<StatsSeller, Integer> statistics = new HashMap<>();

    @Setter
    @Getter
    private static int quantitySel = 0;

    private Generator generator;
    private InvestorMenager investorMenager;

    public AbstractSeller(Map<Integer, Integer> stats, Rarity rarity, Level level, Generator generator, InvestorMenager investorMenager){
        super(rarity, level);
        sellerId = quantitySel;
        setStatistics(stats);
        this.generator = generator;
        this.investorMenager = investorMenager;
    }

    public double earnMoney(){
        double earnings = RobotSeller.EARNINGS* UniwersalMenager.sumStats(getStatistics(), 1, 2, 3);
        if(investorMenager.checkIfAboveNumber(getStatistics(), 8))
            specialFunction(generator);
        return earnings;
    }

    public double revolt(){
        double revoltChance = super.revolt();
        if(statistics.get(StatsSeller.MIND)<7){
            return revoltChance;
        }
        else{
            int mind = statistics.get(StatsSeller.MIND);
            double autodestructionChance = revoltChance*0.0001*generator.sinew(mind, 4);
            return autodestructionChance;
        }
    }

    public void setStatistics(int... statNumber){
        Map<Integer, Integer> intMap = new HashMap<>();
        int i=0;
        for (int stat : statNumber) {
            if(intMap.size()<=4){
                statistics.remove(i);
                statistics.put(StatsSeller.getById(i), stat);
                intMap.put(i, stat);
                i++;
            }
        }
    }

    public void setStatistics(Map<Integer, Integer> intMap){
        statistics.putAll((convertStatsToOrg(intMap)));
    }

    public Map<Integer, Integer> getStatistics(){
        return convertStatsToInt(statistics);
    }

    public Map<Integer, Integer> convertStatsToInt(Map<StatsSeller, Integer> statMap){
        Map<Integer, Integer> intMap = new HashMap<>();
        statMap.forEach((k, v) ->{
            intMap.put(k.getId(), v);
        });
        return intMap;
    }

    public Map<StatsSeller, Integer> convertStatsToOrg(Map<Integer, Integer> intMap){
        Map<StatsSeller, Integer> statMap = new HashMap<>();
        intMap.forEach((k, v) ->{
           statMap.put(StatsSeller.getById(k), v);
        });
        return statMap;
    }
}
