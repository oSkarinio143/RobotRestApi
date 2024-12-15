package pakiet.modules.robot;

import lombok.Getter;
import lombok.Setter;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.modules.interfaces.Robot;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractRobot implements Robot, Serializable{
    private static final long serialVersionUID = 1L;
    private int robotId;
    private Level level;
    private Rarity rarity;
    private static int quantity=0;
    private Map<Integer, Integer> statistics;


    public AbstractRobot(Rarity rarity, Level level){
        robotId=quantity;
        quantity++;
        this.level=level;
        this.rarity=rarity;
    }
}
