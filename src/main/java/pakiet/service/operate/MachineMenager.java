package pakiet.service.operate;

import org.springframework.stereotype.Service;
import pakiet.modules.User;

import javax.crypto.Mac;
@Service
public class MachineMenager {
    private static User user;
    private UserMenager userMenager;

    public MachineMenager(UserMenager userMenager){
        this.userMenager = userMenager;
    }

    public void setUserMachine(){
        user = userMenager.actualUsedUser();
    }

    public void unlockMachine(){
        user.unlockMachine();
    }

    public void performWorkMultiple(int howManyTimes){
        user.getMachine().performWork(howManyTimes);
    }

    public void performInvestmentMultiple(int howManyTimes, int goldAmount){
        user.getMachine().performInvestition(howManyTimes, goldAmount);
    }

    public void performWorkInvestmentMultiple(int howManyTimes, int goldAmount){
        performInvestmentMultiple(howManyTimes, goldAmount);
        performWorkMultiple(howManyTimes);
    }

    public boolean isMachineUnlocked(){
        return user.getMachine() != null;
    }
}
