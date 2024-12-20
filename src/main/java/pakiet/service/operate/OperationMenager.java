package pakiet.service.operate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pakiet.modules.OperationInvestor;
import pakiet.modules.OperationSeller;
import pakiet.modules.User;
import pakiet.modules.interfaces.RobotSeller;
import pakiet.modules.robot.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
public class OperationMenager {
    @Setter
    private static User user;
    @Getter
    private static List<Class> classes = new ArrayList<>(List.of(SellerBooks.class, SellerBoardGames.class, SellerComputerGames.class, SellerHouses.class));
    @Getter
    private static List<Double> values = new ArrayList<>(List.of(RobotSeller.BOOK_SELLER_COST_RATE, RobotSeller.BOARD_GAMES_SELLER_COST_RATE,
            RobotSeller.COMPUTER_GAMES_SELLER_COST_RATE, RobotSeller.HOUSES_SELLER_COST_RATE));
    @Getter
    private List<Map> operationsList = new ArrayList<>();
    private UserMenager userMenager;
    private MachineMenager machineMenager;
    private SellerMenager sellerMenager;
    private InvestorMenager investorMenager;
    private BalanceMenager balanceMenager;

    public OperationMenager(UserMenager userMenager, MachineMenager machineMenager, SellerMenager sellerMenager, InvestorMenager investorMenager, BalanceMenager balanceMenager){
        this.userMenager = userMenager;
        this.machineMenager = machineMenager;
        this.sellerMenager = sellerMenager;
        this.investorMenager = investorMenager;
        this.balanceMenager = balanceMenager;
    }

    public void displayOperations() {
        user = userMenager.actualUsedUser();
        System.out.println(user.getNick() + ", Operation:" +
                "\n1. Investor" +
                "\n2. Seller" +
                "\n3. Buy" +
                "\n4. Other" +
                "\n5. Close connection");
    }

    public void displayInvestorOperations() {
        System.out.println("Operation for investor:" +
                "\n1. Display investor stats" +
                "\n2. Invest gold " +
                "\n3. Upgrade investor - cost - " + OperationInvestor.UPGRADE.getCost() +
                "\n4. Sell investor - value - " + -1 * OperationInvestor.SELL.getCost());
    }

    public void displaySellerOperations() {
        System.out.println("Operation for seller:" +
                "\n1. Display seller stats" +
                "\n2. Earn gold" +
                "\n3. Upgrade seller (basic cost - " + OperationSeller.UPGRADE.getCost() + ", rate" + values + ") - " +
                "\n4. Sell seller (baisc value - " + -1 * sellerMenager.countSellValue(Optional.of(SellerBooks.class)) + ", rate" + values + ") - ");
    }

    public void displayBuyOperations() {
        System.out.println("Buy something:" +
                "\n1. Buy investor - cost - " + investorMenager.countBuyCost() +
                "\n2. Buy book seller - cost - " + sellerMenager.countBuyCost(SellerBooks.class) +
                "\n3. Buy board games seller - cost - " + sellerMenager.countBuyCost(SellerBoardGames.class) +
                "\n4. Buy computer games seller - cost - " + sellerMenager.countBuyCost(SellerComputerGames.class) +
                "\n5. Buy house seller - cost - " + sellerMenager.countBuyCost(SellerHouses.class) +
                "\n6. Buy Machine - cost - " + Machine.getMACHINE_COST());
    }

    public void displayOtherOperations() {
        System.out.println("Other operations:" +
                "\n1. Check gold " +
                "\n2. Perform job (cost - 100)" +
                "\n3. Perform investment (cost - 300)" +
                "\n4. Perform job and investment (cost - 500)");
    }

    public void checkGold() {
        System.out.println(user.getNick() + " - Gold: " + user.getGold());
    }

    public void showInvestor(int idInv) {
        Investor thisInvestor = investorMenager.findInvestorById(idInv);
        System.out.println("------------------INVESTOR----------------");
        System.out.println("ID - " + thisInvestor.getInvId() +
                "\nRarity - " + thisInvestor.getRarity() +
                "\nLevel - " + thisInvestor.getLevel());
        investorMenager.displayInvestorStats(idInv);
        System.out.println("----------------------------------------");
    }

    public boolean investGold(int goldAmount) {
        boolean isSuccesful = balanceMenager.safeCheckBalance(goldAmount);
        if (isSuccesful) {
            investorMenager.investMoney(goldAmount);
            return true;
        }
        return false;
    }

    public boolean upgradeInvestor(int idInv) {
        boolean isSuccesful = balanceMenager.safeChangeBalance(OperationInvestor.UPGRADE.getCost());
        if (isSuccesful) {
            investorMenager.upgradeInvestor(idInv);
            return true;
        }
        return false;
    }

    public void sellInvestor(int idInv) {
        investorMenager.removeInvestor(idInv);
        balanceMenager.safeChangeBalance(OperationInvestor.SELL.getCost());
    }

    public void showSeller(int idSel) {
        AbstractSeller thisSeller = sellerMenager.findSellerById(idSel).orElse(null);
        System.out.println("------------------Seller----------------");
        sellerMenager.displaySellerClass(thisSeller);
        System.out.println("ID - " + thisSeller.getSellerId() +
                "\nRarity - " + thisSeller.getRarity() +
                "\nLevel - " + thisSeller.getLevel());
        sellerMenager.displaySellerStats(idSel);
        System.out.println("----------------------------------------");
    }

    public void earnGold() {
        sellerMenager.earnGold();
    }

    public boolean upgradeSeller(int selId) {
        Class expectedClass = sellerMenager.findSellerById(selId).get().getClass();
        int basicCost = OperationSeller.UPGRADE.getCost();
        int upgradeCost = 0;
        for (int i = 0; i < classes.size(); i++) {
            if (expectedClass == classes.get(i)) {
                upgradeCost = (int) (values.get(i) * basicCost);
                break;
            }
        }
        boolean isSuccessful = balanceMenager.safeChangeBalance(upgradeCost);
        if(isSuccessful){
            sellerMenager.upgradeSeller(selId);
            return true;
        }
        return false;
    }

    public void sellSeller(int selId) {
        Optional<Class> optionalClass = sellerMenager.removeSeller(selId);
        Class<? extends AbstractSeller> sellerClass = optionalClass.get();
        int basicValue = OperationSeller.SELL.getCost();
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i) == sellerClass) {
                balanceMenager.safeChangeBalance((int) (values.get(i) * basicValue));
                break;
            }
        }
    }

    public boolean buyInvestor() {
        boolean isSuccesful = balanceMenager.safeChangeBalance(investorMenager.countBuyCost());
        if (isSuccesful) {
            investorMenager.createInvestor();
            return true;
        }
        return false;
    }

    public boolean buyBooksSeller() {
        boolean isSuccesful = balanceMenager.safeChangeBalance(sellerMenager.countBuyCost(SellerBooks.class));
        if (isSuccesful) {
            sellerMenager.createConcreteSeller(SellerBooks.class);
            return true;
        }
        return false;
    }

    public boolean buyBoardGamesSeller() {
        boolean isSuccesful = balanceMenager.safeChangeBalance(sellerMenager.countBuyCost(SellerBoardGames.class));
        if (isSuccesful) {
            sellerMenager.createConcreteSeller(SellerBoardGames.class);
            return true;
        }
        return false;
    }

    public boolean buyComputerGamesSeller() {
        boolean isSuccesful = balanceMenager.safeChangeBalance(sellerMenager.countBuyCost(SellerComputerGames.class));
        if (isSuccesful){
            sellerMenager.createConcreteSeller(SellerComputerGames.class);
            return true;
        }
        return false;
    }

    public boolean buyHousesSeller() {
        boolean isSuccesful = balanceMenager.safeChangeBalance(sellerMenager.countBuyCost(SellerHouses.class));
        if (isSuccesful) {
            sellerMenager.createConcreteSeller(SellerHouses.class);
            return true;
        }
        return false;
    }

    public boolean buyMachine() {
        boolean isSuccesful = balanceMenager.safeCheckBalance(Machine.getMACHINE_COST());
        boolean isMachineOwned = machineMenager.isMachineUnlocked();
        if (isSuccesful && !isMachineOwned) {
            balanceMenager.safeChangeBalance(Machine.getMACHINE_COST());
            machineMenager.unlockMachine();
            return true;
        }
        return false;
    }

    public boolean performWork(int howManyTimes) {
        boolean isSuccessfulCondition2 = balanceMenager.safeCheckBalance(Machine.getMACHINE_SELLER_USE());
        if (isSuccessfulCondition2) {
            balanceMenager.safeChangeBalance(Machine.getMACHINE_SELLER_USE()*howManyTimes);
            machineMenager.performWorkMultiple(howManyTimes);
            return true;
        }
        return false;
    }

    public boolean performInvestment(int howManyTimes, int goldAmount) {
        boolean isSuccessfulCondition2 = balanceMenager.safeCheckBalance(Machine.getMACHINE_INVESTER_USE()*howManyTimes);
        boolean isSuccessfulCondition3 = balanceMenager.safeCheckBalance(goldAmount+Machine.getMACHINE_INVESTER_USE()*howManyTimes);
        if (isSuccessfulCondition2 && isSuccessfulCondition3) {
            balanceMenager.safeChangeBalance(Machine.getMACHINE_INVESTER_USE()*howManyTimes);
            machineMenager.performInvestmentMultiple(howManyTimes, goldAmount);
            return true;
        }
        return false;
    }

    public boolean performWorkInvestment(int howManyTimes, int goldAmount) {
        boolean isSuccessfulCondition2 = balanceMenager.safeCheckBalance(Machine.getMACHINE_TOGETHER_USE());
        boolean isSuccessfulCondition3 = balanceMenager.safeCheckBalance(goldAmount+Machine.getMACHINE_TOGETHER_USE()*howManyTimes);
        if (isSuccessfulCondition2 && isSuccessfulCondition3) {
            balanceMenager.safeChangeBalance(Machine.getMACHINE_TOGETHER_USE()*howManyTimes);
            machineMenager.performWorkInvestmentMultiple(howManyTimes, goldAmount);
            return true;
        }
        return false;
    }
}


