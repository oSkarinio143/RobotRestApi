package pakiet.modules;

import pakiet.exceptions.IncorrectIdRuntimeException;
import lombok.Getter;

@Getter
public enum StatsSeller {
    MIND(0, 0, 10), SPEED(1, 0, 10), NEGOTIATION(2, 0, 10), EFFICIENCY(3, 0, 10);
    private final int id;
    private final int minRange;
    private final int maxRange;

    StatsSeller(int id, int minRange, int maxRange){
        this.id=id;
        this.minRange=minRange;
        this.maxRange=maxRange;
    }

    public static StatsSeller getById(int id){
        for(StatsSeller stat : StatsSeller.values()){
            if(stat.id==id){
                return stat;
            }
        }
        throw new IncorrectIdRuntimeException("Incorrect Id");
    }
}
