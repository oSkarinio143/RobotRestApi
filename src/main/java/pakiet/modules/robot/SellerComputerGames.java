package pakiet.modules.robot;

import lombok.Getter;
import lombok.Setter;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.service.Generator;
import pakiet.service.operate.InvestorMenager;

import java.io.Serializable;
import java.util.Map;

public class SellerComputerGames extends SellerGames{
    @Getter
    @Setter
    private static int sellerComputerGamesQuantity = 0;

    public SellerComputerGames(Map<Integer, Integer> map, Rarity rarity, Level level, Generator generator, InvestorMenager investorMenager){
        super(map, rarity, level, generator, investorMenager);
    }

    @Override
    public double earnMoney() {
        return super.earnMoney()*COMPUTER_GAMES_SELLER_EARN_RATE;
    }
}
