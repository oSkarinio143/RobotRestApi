package pakiet.modules.robot;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.service.Generator;

import java.util.Map;

@Service
public class AbstractFactory {
    private Generator generator;

    public AbstractFactory(@Lazy Generator generator){
        this.generator = generator;
    }

    public AbstractSeller createSeller(String type, Map<Integer, Integer> stats, Rarity rarity, Level level){
        type = type.toLowerCase();
        switch(type){
            case "books":
                return new SellerBooks(stats, rarity, level, generator);
            case "board_games":
                return new SellerBoardGames(stats, rarity, level, generator);
            case "computer_games":
                return new SellerComputerGames(stats, rarity, level, generator);
            case "houses":
                return new SellerHouses(stats, rarity, level, generator);
            default:
                throw new RuntimeException("Incorrect Type");
        }
    }
}
