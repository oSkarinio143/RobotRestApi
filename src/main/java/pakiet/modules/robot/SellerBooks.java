package pakiet.modules.robot;

import lombok.Getter;
import lombok.Setter;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.modules.interfaces.RobotSeller;
import pakiet.service.Generator;
import pakiet.service.operate.InvestorMenager;

import java.io.Serializable;
import java.util.Map;

public class SellerBooks extends AbstractSeller{
    @Getter
    @Setter
    private static int sellerBookQuantity = 0;

    @Getter
    private static final int uniqueId = 0;

    public SellerBooks(Map<Integer, Integer> map, Rarity rarity, Level level, Generator generator, InvestorMenager investorMenager){
        super(map, rarity, level, generator, investorMenager);
    }

    @Override
    public double earnMoney() {
        return super.earnMoney()* RobotSeller.BOOK_SELLER_EARN_RATE;
    }
}
