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

public class SellerHouses extends AbstractSeller{
    @Getter
    @Setter
    private static int sellerHousesQuantity = 0;

    public SellerHouses(Map<Integer, Integer> map, Rarity rarity, Level level, Generator generator, InvestorMenager investorMenager){
        super(map, rarity, level, generator, investorMenager);
    }

    @Override
    public double earnMoney() {
        return super.earnMoney()* RobotSeller.HOUSES_SELLER_EARN_RATE;
    }
}
