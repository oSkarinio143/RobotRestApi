package pakiet.modules.robot;

import lombok.Getter;
import lombok.Setter;
import pakiet.modules.*;
import pakiet.service.Generator;
import pakiet.service.operate.InvestorMenager;

import java.io.Serializable;
import java.util.Map;

public class SellerBoardGames extends SellerGames{
    @Getter
    @Setter
    private static int sellerBoardGamesQuantity = 0;

    public SellerBoardGames(Map<Integer, Integer> map, Rarity rarity, Level level, Generator generator, InvestorMenager investorMenager){
        super(map, rarity, level, generator, investorMenager);
    }

    @Override
    public double earnMoney() {
        return super.earnMoney()*BOARD_GAMES_SELLER_EARN_RATE;
    }
}
