package pakiet.modules;

import pakiet.exceptions.IncorrectIdRuntimeException;
import lombok.Getter;
import lombok.Setter;
import pakiet.service.Sorting;
import pakiet.service.operate.InvestorMenager;
import pakiet.service.operate.MachineService;
import pakiet.service.operate.SellerMenager;
import pakiet.modules.robot.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class User {
    private double gold;
    private String nick;
    private Machine machine;
    private List<Investor> ownedInvestors = new ArrayList<>();
    private List<AbstractSeller> ownedSellers = new ArrayList<>();
    private Sorting sorting;
    private MachineService machineService;
    private InvestorMenager investorMenager;
    private SellerMenager sellerMenager;

    public User(String nick, Sorting sorting, MachineService machineService, InvestorMenager investorMenager, SellerMenager sellerMenager) {
        this.investorMenager = investorMenager;
        this.sellerMenager = sellerMenager;
        this.machineService = machineService;
        this.sorting = sorting;
        this.nick = nick;
        gold = 100000;
    }

    @Override
    public String toString() {
        return this.nick;
    }

    public Investor giveIdInvestor(Investor investor) {
        for (int i = 0; i < ownedInvestors.size(); i++) {
            if (ownedInvestors.get(i).getInvId() != i) {
                investor.setInvId(i);
                return investor;
            }
        }
        investor.setInvId(ownedInvestors.size());
        return investor;
    }

    public AbstractSeller giveIdSeller(AbstractSeller seller) {
        int length = ownedSellers.size();
        for (int i = 0; i < length; i++) {
            if (ownedSellers.get(i).getSellerId() != i) {
                seller.setSellerId(i);
                return seller;
            }
        }
        seller.setSellerId(length);
        return seller;
    }

    public <T extends AbstractRobot> void addToList(T type) {
        if (type instanceof Investor investor) {
            addInvestorToList(investor);
        } else if (type instanceof AbstractSeller abstractSeller) {
            addSellerToList(abstractSeller);
        }
    }

    public void addInvestorToList(Investor type) {
        ownedInvestors.add(giveIdInvestor(type));
        Investor.setQuantityInv(ownedInvestors.size());
        sorting.sortListInvestor();
    }

    public void addSellerToList(AbstractSeller type) {
        ownedSellers.add(giveIdSeller(type));
        AbstractSeller.setQuantitySel(ownedSellers.size());
        sorting.sortListSeller();
        if (type instanceof SellerBooks)
            SellerBooks.setSellerBookQuantity(SellerBooks.getSellerBookQuantity() + 1);
        if (type instanceof SellerBoardGames)
            SellerBoardGames.setSellerBoardGamesQuantity(SellerBoardGames.getSellerBoardGamesQuantity() + 1);
        if (type instanceof SellerComputerGames)
            SellerComputerGames.setSellerComputerGamesQuantity(SellerComputerGames.getSellerComputerGamesQuantity() + 1);
        if (type instanceof SellerHouses)
            SellerHouses.setSellerHousesQuantity(SellerHouses.getSellerHousesQuantity() + 1);
    }

    public <T extends AbstractRobot> void removeFromList(Class<T> type, int id) {
        if (type.equals(Investor.class)) {
            removeInvestorFromList(id);
        }
        if (AbstractSeller.class.isAssignableFrom(type) && ownedSellers.contains(sellerMenager.findSellerById(id).get())) {
                removeSellerFromList(type, id);
            }

    }

    private void removeInvestorFromList(int id) {
        List<Investor> ownedInvestorsCopy = new ArrayList<>(ownedInvestors);
        if (ownedInvestors.contains(investorMenager.findInvestorById(id))) {
            ownedInvestors.forEach(k -> {
                if (k.getInvId() == id) {
                    ownedInvestorsCopy.remove(k);
                }
            });
            ownedInvestors = ownedInvestorsCopy;
            Investor.setQuantityInv(ownedInvestors.size());
        } else
            throw new IncorrectIdRuntimeException();
    }

    private <T extends AbstractRobot> void removeSellerFromList(Class<T> type, int id) {
        List<AbstractSeller> ownedSellersCopy = new ArrayList<>();
        ownedSellers.forEach(v -> {
            if (v.getSellerId() != id) {
                ownedSellersCopy.add(v);
            }
        });
        ownedSellers = ownedSellersCopy;
        AbstractSeller.setQuantitySel(ownedSellers.size());
        if (type.equals(SellerBooks.class))
            SellerBooks.setSellerBookQuantity(ownedSellers.size());
        if (type == SellerBoardGames.class)
            SellerBoardGames.setSellerBoardGamesQuantity(ownedSellers.size());
        if (type == SellerComputerGames.class)
            SellerComputerGames.setSellerComputerGamesQuantity(ownedSellers.size());
        if (type == SellerHouses.class)
            SellerHouses.setSellerHousesQuantity(ownedSellers.size());
        else
            throw new IncorrectIdRuntimeException();
    }

    public void unlockMachine() {
        machine = machineService.createMachine();
    }
}

