package pakiet.modules.robot;

import pakiet.modules.Level;
import pakiet.modules.Rarity;

import java.io.Serializable;
import java.util.Map;

public class SellerGames extends AbstractSeller implements Serializable{
    private static final long serialVersionUID = 1L;
    public SellerGames(Map<Integer, Integer> map, Rarity rarity, Level level){
        super(map, rarity, level);
    }
}
