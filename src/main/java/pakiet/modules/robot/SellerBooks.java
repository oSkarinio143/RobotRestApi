package pakiet.modules.robot;

import lombok.Getter;
import lombok.Setter;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.modules.interfaces.RobotSeller;

import java.io.Serializable;
import java.util.Map;

public class SellerBooks extends AbstractSeller implements Serializable{
    private static final long serialVersionUID = 1L;
    private int sellerBookId;

    @Getter
    @Setter
    private static int sellerBookQuantity = 0;

    @Getter
    private static final int uniqueId = 0;

    public SellerBooks(Map<Integer, Integer> map, Rarity rarity, Level level){
        super(map, rarity, level);
        sellerBookId=sellerBookQuantity;
    }

    @Override
    public double earnMoney() {
        return super.earnMoney()* RobotSeller.BOOK_SELLER_EARN_RATE;
    }
}
