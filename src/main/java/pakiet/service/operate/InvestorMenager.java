package pakiet.service.operate;

import org.springframework.stereotype.Service;
import pakiet.exceptions.IllegalOperation;
import pakiet.exceptions.IncorrectIdRuntimeException;
import pakiet.modules.OperationInvestor;
import pakiet.modules.StatsInvestor;
import pakiet.modules.User;
import pakiet.modules.robot.Investor;
import pakiet.service.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pakiet.service.Generator.*;

@Service
public class InvestorMenager {
    private static User user;
    private static List<Investor> ownedInvestors = new ArrayList<>();
    private UserMenager userMenager;
    private Generator generator;

    public InvestorMenager(UserMenager userMenager, Generator generator){
        this.userMenager = userMenager;
        this.generator = generator;
    }

    public void setUserInv(){
        user = userMenager.actualUsedUser();
        ownedInvestors = user.getOwnedInvestors();
        Investor.setQuantityInv(ownedInvestors.size());
    }

    public Investor findInvestorById(int idInv){
        List<Investor> listInvestors = user.getOwnedInvestors();

        for (Investor investor : listInvestors) {
            if(investor.getInvId()==idInv)
                return investor;
        }
        return null;
    }

    public void displayInvestorStats(int idInv){
        Investor investor = findInvestorById(idInv);
        Map<Integer, Integer> intMap = investor.getStatistics();
        intMap.forEach((k, v)->{
            System.out.println(StatsInvestor.getById(k)+" - "+v);
        });
    }

    public void createInvestor(){
        int levelNumber = 1;
        Investor investor = generator.generateBasicInvestor(levelNumber);
        generator.upgradeBasicInvestor(investor);
        user.addToList(investor);
    }

    public void removeInvestor(int idInv){
        user.removeFromList(Investor.class, idInv);
    }

    public void upgradeInvestor (int idInv){
        Investor investor = findInvestorById(idInv);
        if (investor.getLevel().getId()<3){
            generator.upgradeLevelInvestor(investor);
        }else{
            throw new IllegalOperation();
        }
    }

    private boolean isRevolt (){
        double revoltChance=0;
        for (Investor investor : user.getOwnedInvestors()) {
            revoltChance += investor.revolt();
        }
        return generator.checkingRevolt((int) revoltChance*10);
    }

    private void makeInvestition(int goldAmount){
        double userGold = user.getGold();

        for (Investor investor : user.getOwnedInvestors()) {
            double earnedAmount = investor.invest(goldAmount);
            userGold+=earnedAmount;
        }
        userGold=Math.round(userGold*100.0)/100.0;
        user.setGold(userGold);
    }

    public void investMoney(int goldAmount){
        if(isRevolt()){
            user.setGold(user.getGold()-goldAmount);
        }else{
            makeInvestition(goldAmount);
        }
    }

    public int countBuyCost(){
        double rate = Investor.getBuyCostMultiplier();
        for(int i = 0; i<Investor.getQuantityInv(); i++){
            rate *= Investor.getBuyCostMultiplier();
        }
        int cost = (int) (OperationInvestor.CREATE.getCost()*rate);
        return cost;
    }

    public boolean checkIfAboveNumber(Map<Integer, Integer> map, int number){
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getValue()>number){
                return true;
            }
        }
        return false;
    }

    public List<Integer> returnIdsList(){
        List<Integer> idList = new ArrayList<>();
        user.getOwnedInvestors().forEach(v -> idList.add(v.getInvId()));
        return idList;
    }
}
