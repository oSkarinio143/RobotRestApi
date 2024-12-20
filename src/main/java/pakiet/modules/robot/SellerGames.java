package pakiet.modules.robot;

import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.service.Generator;
import pakiet.service.operate.InvestorMenager;

import java.io.Serializable;
import java.util.Map;

public class SellerGames extends AbstractSeller{
    private static final long serialVersionUID = 1L;
    public SellerGames(Map<Integer, Integer> map, Rarity rarity, Level level, Generator generator, InvestorMenager investorMenager){
        super(map, rarity, level, generator, investorMenager);
    }
}
