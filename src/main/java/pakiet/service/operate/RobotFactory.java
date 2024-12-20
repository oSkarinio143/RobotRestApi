package pakiet.service.operate;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pakiet.modules.Level;
import pakiet.modules.Rarity;
import pakiet.modules.robot.*;
import pakiet.service.Generator;

import java.util.Map;

@Service
public class RobotFactory {
    private Generator generator;
    private InvestorMenager investorMenager;

    public RobotFactory(@Lazy Generator generator, @Lazy InvestorMenager investorMenager){
        this.generator = generator;
        this.investorMenager = investorMenager;
    }

    public AbstractRobot createSeller(String type, Map<Integer, Integer> stats, Rarity rarity, Level level){
        String typeLower = type.toLowerCase();
        switch(typeLower){
            case "books":
                return new SellerBooks(stats, rarity, level, generator, investorMenager);
            case "board_games":
                return new SellerBoardGames(stats, rarity, level, generator, investorMenager);
            case "computer_games":
                return new SellerComputerGames(stats, rarity, level, generator, investorMenager);
            case "houses":
                return new SellerHouses(stats, rarity, level, generator, investorMenager);
            case "investor":
                return new Investor(stats, rarity, level, generator, investorMenager);
            default:
                throw new RuntimeException("Incorrect Type");
        }
    }
}
