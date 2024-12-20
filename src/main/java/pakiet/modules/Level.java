package pakiet.modules;

import pakiet.exceptions.IncorrectIdRuntimeException;
import lombok.Getter;

@Getter
public enum Level {
    BEGINNER(1, 0, 7), INTERMEDIATE(2, 4, 9), ADVANCED(3, 5, 10);
    private final int id;
    private final int additionalStats;
    private final int constraint;

    Level(int id, int additionalStats, int constraint){
        this.id=id;
        this.additionalStats=additionalStats;
        this.constraint=constraint;
    }

    public static Level getById(int idNumber){
        for (Level value : Level.values()) {
            if (value.id==idNumber) return value;
        }
        throw new IncorrectIdRuntimeException();
    }

    public static int getAdditionalStatsForLevel(int levelNumber){
        int sumAdditionalStats=0;
        for (int i = 1; i <= levelNumber; i++) {
            sumAdditionalStats+=getById(i).additionalStats;
        }
        return sumAdditionalStats;
    }
}
