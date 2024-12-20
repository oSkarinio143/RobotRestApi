package pakiet.service.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pakiet.modules.User;
import pakiet.service.Sorting;

@Service
public class UserService {
    private final Sorting sorting;
    private final MachineService machineService;
    private final InvestorMenager investorMenager;
    private final SellerMenager sellerMenager;

    @Autowired
    public UserService (Sorting sorting, MachineService machineService, InvestorMenager investorMenager, SellerMenager sellerMenager){
        this.sorting = sorting;
        this.machineService = machineService;
        this.investorMenager = investorMenager;
        this.sellerMenager = sellerMenager;
    }

    public User createUserWithNick(String nick){
        return new User (nick, sorting, machineService, investorMenager, sellerMenager);
    }
}
