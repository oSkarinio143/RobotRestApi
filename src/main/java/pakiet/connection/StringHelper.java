package pakiet.connection;

import lombok.Getter;
import lombok.Setter;
import pakiet.modules.OperationInvestor;
import pakiet.modules.OperationSeller;
import pakiet.modules.robot.*;
import pakiet.service.operate.InvestorMenager;
import pakiet.service.operate.OperationMenager;
import pakiet.service.operate.SellerMenager;
import java.util.Optional;
@Getter
@Setter
public class StringHelper {
    private String lastCommunicate;

    public static String getUserNick(){
        return "Enter Nick for user";
    }

    public static String getTypeOperationChoice(){
        return "Chose operation:<br>1. Investor<br>2. Seller<br>3. Buy<br>4. Other<br>5. Close connection";
    }

    public static String getInvestorOperationChoice(){
        return "Chose operation for investor:<br>1. Display investor stats<br>2. Invest gold"+
                "<br>3. Upgrade investor - cost - " + OperationInvestor.UPGRADE.getCost() +
                "<br>4. Sell investor - value - " + -1 * OperationInvestor.SELL.getCost();
    }

    public static String getSellerOperationChoice(){
        return "Operation for seller:<br>1. Display seller stats<br>2. Earn gold"+
                "<br>3. Upgrade seller (basic cost - " + OperationSeller.UPGRADE.getCost() + ", rate" + OperationMenager.getValues() + ")"+
                "<br>4. Sell seller (basic value - " + -1 * SellerMenager.countSellValue(Optional.of(SellerBooks.class)) + ", rate - " + OperationMenager.getValues() + ")";
    }

    public static String getBuyOperationChoice(){
        return "Buy something:<br>1. Buy investor - cost - " + InvestorMenager.countBuyCost() + "<br>2. Buy book seller - cost - "+ SellerMenager.countBuyCost(SellerBooks.class) +
                "<br>3. Buy board games seller - cost - " + SellerMenager.countBuyCost(SellerBoardGames.class) + "<br>4. Buy computer games seller - cost - " + SellerMenager.countBuyCost(SellerComputerGames.class) +
                "<br>5. Buy house seller - cost - " + SellerMenager.countBuyCost(SellerHouses.class) + "<br>6. Buy Machine - cost - " + Machine.getMACHINE_COST();
    }

    public static String getOtherOperationChoice(){
        return "Other operations: <br>1. Check gold" +
                "<br>2. Perform job (cost - 100)" + "<br>3. Perform investment (cost - 300)" + "<br>4. Perform job and investment (cost - 500)";
    }

    public static String getShowInvestor(int invId){
        Investor inv = InvestorMenager.findInvestorById(invId);
        return "------------------INVESTOR----------------<br>ID - " + inv.getInvId() + "<br>Rarity - " + inv.getRarity() +
            "<br>Level - " + inv.getLevel() + "<br>MIND - " + inv.getStatistics().get(0) + "<br>PREDICTION - " + inv.getStatistics().get(1) +
            "<br>RISKTENDENCY - " + inv.getStatistics().get(2) + "<br>PATIENCE - " + inv.getStatistics().get(3);
    }

    public static String getShowSeller(int sellerId){
        AbstractSeller seller = SellerMenager.findSellerById(sellerId).get();
        String sellerClass = SellerMenager.getSellerClass(seller);
        return "------------------Seller----------------<br>" + sellerClass + "<br>ID - " + sellerId + "<br>Rarity - "+seller.getRarity() +
                "<br>Level - " + seller.getLevel() + "<br>MIND - " + seller.getStatistics().get(0) + "<br>SPEED - " + seller.getStatistics().get(1) +
                "<br>NEGOTIATION - " + seller.getStatistics().get(2) + "<br>EFFICIENCY - " + seller.getStatistics().get(3);
    }
}
