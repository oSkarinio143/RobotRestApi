package pakiet.modules;

import lombok.Getter;

@Getter
public enum OperationInvestor {
    DISPLAY(0, 0), CREATE(1, 1000), UPGRADE(2, 5000), SELL(3, -500);
    private final int id;
    private final int cost;

    OperationInvestor(int id, int cost) {
        this.id = id;
        this.cost = cost;
    }
}
