package pakiet.modules;

import lombok.Getter;

@Getter
public enum OperationSeller {
    DISPLAY(0, 0), CREATE(1, 10), UPGRADE(2, 30), SELL(3, -5);
    private final int id;
    private final int cost;

    OperationSeller(int id, int cost){
        this.id=id;
        this.cost=cost;
    }
}
