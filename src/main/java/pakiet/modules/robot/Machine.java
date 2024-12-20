package pakiet.modules.robot;

import lombok.Getter;
import pakiet.modules.User;
import pakiet.service.operate.MachineMenager;
import pakiet.service.operate.MachineService;
import pakiet.service.operate.OperationMenager;
import pakiet.service.operate.UserMenager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Machine implements Serializable {
    private static final long serialVersionUID = 1L;
    private User user;
    @Getter
    private static final int MACHINE_COST = 10000;
    @Getter
    private static final int MACHINE_SELLER_USE = 100;
    @Getter
    private static final int MACHINE_INVESTER_USE = 300;
    @Getter
    private static final int MACHINE_TOGETHER_USE = 500;

    private OperationMenager operationMenager;

    public Machine (UserMenager userMenager, OperationMenager operationMenager){
        this.operationMenager = operationMenager;
        user = userMenager.actualUsedUser();
    }

    public void performWork(int howManyTimes){
        for (int i = 0; i < howManyTimes; i++) {
            operationMenager.earnGold();
        }
    }

    public void performInvestition(int howManyTimes, int goldAmount){
        for (int i = 0; i < howManyTimes; i++) {
            operationMenager.investGold(goldAmount);
        }
    }





}
