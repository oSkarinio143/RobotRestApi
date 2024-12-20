package pakiet.service.operate;

import org.springframework.stereotype.Service;
import pakiet.exceptions.IllegalOperation;
import pakiet.modules.OperationSeller;
import pakiet.modules.StatsSeller;
import pakiet.modules.User;
import pakiet.modules.interfaces.RobotSeller;
import pakiet.modules.robot.*;
import pakiet.service.Generator;
import pakiet.service.Sorting;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SellerMenager {
    private static User user;
    private static List<AbstractSeller> ownedSellers = new ArrayList<>();
    private UserMenager userMenager;
    private Generator generator;
    private Sorting sorting;

    public SellerMenager(UserMenager userMenager, Generator generator, Sorting sorting){
        this.userMenager = userMenager;
        this.generator = generator;
        this.sorting = sorting;
    }

    public void setUserSell(){
        user = userMenager.actualUsedUser();
        ownedSellers = user.getOwnedSellers();
        SellerBooks.setSellerBookQuantity(0);
        SellerBoardGames.setSellerBoardGamesQuantity(0);
        SellerComputerGames.setSellerComputerGamesQuantity(0);
        SellerHouses.setSellerHousesQuantity(0);
        AbstractSeller.setQuantitySel(0);
        for (AbstractSeller seller : ownedSellers) {
            if(seller instanceof SellerBooks)
                SellerBooks.setSellerBookQuantity(SellerBooks.getSellerBookQuantity()+1);
            if(seller instanceof SellerBoardGames)
                SellerBoardGames.setSellerBoardGamesQuantity(SellerBoardGames.getSellerBoardGamesQuantity()+1);
            if(seller instanceof SellerComputerGames)
                SellerComputerGames.setSellerComputerGamesQuantity(SellerComputerGames.getSellerComputerGamesQuantity()+1);
            if(seller instanceof SellerHouses)
                SellerHouses.setSellerHousesQuantity(SellerHouses.getSellerHousesQuantity() + 1);
        }
    }

    public Optional<AbstractSeller> findSellerById(int idSeller){
        for (AbstractSeller seller : user.getOwnedSellers()) {
            if(seller.getSellerId()==idSeller){
                return Optional.of(seller);
            }
        }
        return Optional.empty();
    }

    public void displaySellerStats(int selId){
        Optional<AbstractSeller> optionalSeller = findSellerById(selId);
        AbstractSeller seller = optionalSeller.get();
        int i=0;
        for (StatsSeller value : StatsSeller.values()) {
            System.out.println(value+" - "+seller.getStatistics().get(i));
            i++;
        }
    }

    public String getSellerStats(int selId){
        Optional<AbstractSeller> optionalSeller = findSellerById(selId);
        AbstractSeller seller = optionalSeller.get();
        int i=0;
        String part;
        String stats="";
        for (StatsSeller value : StatsSeller.values()) {
            part = (value+" - "+seller.getStatistics().get(i)+"|");
            stats+=part;
        }
        return stats;
    }

    public void displaySellerClass(AbstractSeller seller){
        if(seller instanceof SellerBooks){
            System.out.println("Specialisation - BOOKS");
        }
        else if(seller instanceof SellerBoardGames){
            System.out.println("Specialisation - BOARD GAMES");
        }
        else if(seller instanceof SellerComputerGames){
            System.out.println("Specialisation - COMPUTER GAMES");
        }
        else if(seller instanceof SellerHouses){
            System.out.println("Specialisation - HOUSES");
        }
    }

    public String getSellerClass(AbstractSeller seller){
        if(seller instanceof SellerBooks){
            return ("Specialisation - BOOKS");
        }
        else if(seller instanceof SellerBoardGames){
            return ("Specialisation - BOARD GAMES");
        }
        else if(seller instanceof SellerComputerGames){
            return ("Specialisation - COMPUTER GAMES");
        }
        else if(seller instanceof SellerHouses){
            return ("Specialisation - HOUSES");
        }
        throw new RuntimeException("Error");
    }

    public <T extends AbstractSeller> void createConcreteSeller(Class<T> type){
        int levelNumber = 1;
        ArrayList<Class> classesList = new ArrayList<>(List.of(SellerBooks.class, SellerBoardGames.class, SellerComputerGames.class, SellerHouses.class));
        for (Class classSingle : classesList){

            if(type==classSingle) {
                AbstractSeller seller = generator.generateBasicConcreteSeller(levelNumber, classSingle);
                generator.upgradeBasicSeller(seller);
                user.addToList(seller);
            }
        }
    }

    public void upgradeSeller(int selId){
        AbstractSeller seller = findSellerById(selId).get();
        if(seller.getLevel().getId()<3) {
            generator.upgradeLevelSeller(seller);
        }
        else
            throw new IllegalOperation();
    }

    public int returnQuantityForId(int idQuantity){
        List <Integer> quantiteis = new ArrayList<>(List.of(SellerBooks.getQuantitySel(), SellerBoardGames.getQuantitySel(), SellerBoardGames.getQuantitySel(), SellerHouses.getQuantitySel()));
        return quantiteis.get(idQuantity);
    }

    public <T extends AbstractSeller> int countBuyCost(Class<T> type){
        double classRate = 0;
        if (type==SellerBooks.class) {
            classRate = SellerBooks.BOOK_SELLER_COST_RATE;
            classRate *= generator.sinew(RobotSeller.BUYING_RATE, SellerBooks.getSellerBookQuantity());
        }
        if (type==SellerBoardGames.class){
            classRate =  SellerBoardGames.BOARD_GAMES_SELLER_COST_RATE;
            classRate *= generator.sinew(RobotSeller.BUYING_RATE, SellerBoardGames.getSellerBoardGamesQuantity());
        }
        if (type==SellerComputerGames.class) {
            classRate = SellerComputerGames.COMPUTER_GAMES_SELLER_COST_RATE;
            classRate *= generator.sinew(RobotSeller.BUYING_RATE, SellerComputerGames.getSellerComputerGamesQuantity());
        }
        if (type==SellerHouses.class) {
            classRate = SellerHouses.HOUSES_SELLER_COST_RATE;
            classRate *= generator.sinew(RobotSeller.BUYING_RATE, SellerHouses.getSellerHousesQuantity());
        }
        classRate=Math.round(classRate*100.0) /100.0;
        return (int) (OperationSeller.CREATE.getCost()*classRate);
    }

    public <T extends AbstractSeller> Optional<Class> removeSeller(int idSel){
        Optional<AbstractSeller> receivedSeller = findSellerById(idSel);
        if(receivedSeller.isEmpty()){
            return Optional.empty();
        }
        else{
            Class<? extends AbstractSeller> sellerClass = receivedSeller.get().getClass();
            user.removeFromList(AbstractSeller.class, idSel);

            return Optional.of(sellerClass);
        }
    }

    public int countSellValue(Optional<Class> optionalClass){
        AtomicInteger rate = new AtomicInteger(0);
        AtomicInteger amount = new AtomicInteger(0);
        optionalClass.ifPresent(v -> {
            if(v==SellerBooks.class){
                rate.compareAndSet(0, (int) SellerBooks.BOOK_SELLER_COST_RATE);
                amount.compareAndSet(0, SellerBooks.getSellerBookQuantity());
            }
            if(v==SellerBoardGames.class){
                rate.compareAndSet(0, (int) SellerBoardGames.BOARD_GAMES_SELLER_COST_RATE);
                amount.compareAndSet(0, SellerBoardGames.getSellerBoardGamesQuantity());
            }
            if(v==SellerComputerGames.class){
                rate.compareAndSet(0, (int) SellerComputerGames.COMPUTER_GAMES_SELLER_COST_RATE);
                amount.compareAndSet(0, SellerBoardGames.getSellerBoardGamesQuantity());
            }
            if(v==SellerHouses.class){
                rate.compareAndSet(0, (int) SellerHouses.HOUSES_SELLER_COST_RATE);
                amount.compareAndSet(0, SellerBoardGames.getSellerBoardGamesQuantity());
            }
        });
        int value=rate.get();
        value=(int) (value*OperationSeller.SELL.getCost()*generator.sinew(AbstractSeller.BUYING_RATE, amount.get()));
        return value;
    }

    public void upgradeRandomSellersMind(int chance){
        for (int i = 0; i < user.getOwnedSellers().size(); i++) {
            for (int i1 = 0; i1 < chance; i1++) {
                int number = (int)(Math.random()*100)+1;
                if(number==1 && user.getOwnedSellers().get(i).getStatistics().get(0)<10){
                    user.getOwnedSellers().get(i).setStatistics(user.getOwnedSellers().get(i).getStatistics().get(0)+1);
                }
            }
        }
    }

    public boolean isRevolt(){
        double revoltChance=0;
        for (AbstractSeller seller : user.getOwnedSellers()) {
            if(seller.getStatistics().get(0)<7) {
                revoltChance += seller.revolt();
            }
        }
        return generator.checkingRevolt((int) revoltChance*200);
    }

    public boolean isAutodestruction(){
        double autodestructionChance=0;
        for(AbstractSeller seller : user.getOwnedSellers()){
            if(seller.getStatistics().get(0)>=7){
                autodestructionChance += seller.revolt();
            }
        }
        System.out.println(autodestructionChance);
        return generator.checkingRevolt((int) autodestructionChance*200);
    }

    public void performWork(){
        double generalEarned=0;

        for (int i = 0; i < user.getOwnedSellers().size(); i++) {
            generalEarned += user.getOwnedSellers().get(i).earnMoney();
        }
        double earnedGold=Math.round(generalEarned*100.0)/100.0;
        double userGold=user.getGold()+earnedGold;
        user.setGold(userGold);
    }

    public void removeSellerHighestStat(List<AbstractSeller> sellerList, int stat){
        List<AbstractSeller> sortedSellers = sorting.sortSellerListByStat(sellerList, stat);
        removeSeller(sortedSellers.get(0).getSellerId());
    }

    public void earnGold(){
        if(isRevolt()){
            System.out.println("Bunt");
            upgradeRandomSellersMind(100);
        }
        if(isAutodestruction()){
            System.out.println("Destrukcja");
            removeSellerHighestStat(user.getOwnedSellers(), 0);
        }else {
            performWork();
        }
    }

    public boolean checkIfAboveNumber(Map<Integer, Integer> map, int number){
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getValue()>number){
                return true;
            }
        }
        return false;
    }

    public List<Integer> returnIdsList (){
        List<Integer> idsList = new ArrayList<>();
        user.getOwnedSellers().forEach(v -> idsList.add(v.getSellerId()));
        return idsList;
    }

    public Class getSellerClass(int idSel){
        AbstractSeller seller = findSellerById(idSel).get();
        if (seller instanceof SellerBooks){
            return SellerBooks.class;
        }if (seller instanceof SellerBoardGames){
            return SellerBoardGames.class;
        }if (seller instanceof SellerComputerGames){
            return SellerComputerGames.class;
        }if (seller instanceof SellerHouses){
            return SellerHouses.class;
        }
        throw new RuntimeException("Incorrect id");
    }

    public String getStringClass(int idSel){
        AbstractSeller seller = findSellerById(idSel).get();
        if (seller instanceof SellerBooks){
            return "SellerBooks";
        }if (seller instanceof SellerBoardGames){
            return "SellerBoardGames";
        }if (seller instanceof SellerComputerGames){
            return "SellerComputerGames";
        }if (seller instanceof SellerHouses){
            return "SellerHouses";
        }
        throw new RuntimeException("Incorrect id");
    }

}
