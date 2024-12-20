package pakiet.service.operate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pakiet.modules.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMenager {
    @Getter
    @Setter
    private static List<User> userList = new ArrayList<>();
    @Getter
    private static String actualUserNick;

    private MachineMenager machineMenager;
    private BalanceMenager balanceMenager;
    private InvestorMenager investorMenager;
    private SellerMenager sellerMenager;
    private UserService userService;

    public UserMenager (@Lazy MachineMenager machineMenager, @Lazy BalanceMenager balanceMenager, @Lazy InvestorMenager investorMenager, @Lazy SellerMenager sellerMenager, @Lazy UserService userService){
        this.machineMenager = machineMenager;
        this.balanceMenager = balanceMenager;
        this.investorMenager = investorMenager;
        this.sellerMenager = sellerMenager;
        this.userService = userService;
    }

    public User findUserByNick(String nick){
        for (User userNick : userList) {
            if(userNick.getNick().equals(nick)) {
                actualUserNick = userNick.getNick();
                return userNick;
            }
        }
        return null;
    }

    public User actualUsedUser(){
        return findUserByNick(actualUserNick);
    }

    public void setUserEverywhere() {
        investorMenager.setUserInv();
        sellerMenager.setUserSell();
        machineMenager.setUserMachine();
        balanceMenager.setUserBalance();
    }

    public User createNewUser(String nick){
        User user = userService.createUserWithNick(nick);
        actualUserNick = nick;
        userList.add(user);
        return user;
    }

    public boolean createIfNewUser(String nick){
        if(findUserByNick(nick)==null) {
            createNewUser(nick);
            return true;
        }
        else{
            actualUserNick=nick;
            return false;
        }
    }
}


