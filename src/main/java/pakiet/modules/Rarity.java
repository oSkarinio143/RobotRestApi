package pakiet.modules;

import lombok.Getter;

@Getter
public enum Rarity {
    COMMON(0, 0), RARE(1, 2), EPIC(2, 5);
    private final int id;
    private final int additionalStats;

    Rarity(int id, int additionalStats){
        this.id=id;
        this.additionalStats=additionalStats;
    }
}
