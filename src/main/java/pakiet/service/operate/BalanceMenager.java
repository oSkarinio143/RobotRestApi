package pakiet.service.operate;

import org.springframework.stereotype.Service;
import pakiet.exceptions.InsufficientBalanceException;
import lombok.Getter;
import lombok.Setter;
import pakiet.modules.User;

@Getter
@Setter
@Service
public class BalanceMenager {
    private static User user;
    private UserMenager userMenager;

    public BalanceMenager(UserMenager userMenager){
        this.userMenager = userMenager;
    }

    public void setUserBalance(){
        user = userMenager.actualUsedUser();
    }

    public double returnGoldAmount(){
        return user.getGold();
    }

    public boolean checkBalance(int change) throws InsufficientBalanceException {
        double userGold = user.getGold();
        if(userGold>=change)
            return true;
        else
            throw new InsufficientBalanceException();
    }

    public void changeBalance(int change) throws InsufficientBalanceException {
        if (checkBalance(change)) {
            double userGold = user.getGold();
            userGold-=change;
            user.setGold(userGold);
        }
    }

    public boolean safeCheckBalance(int amount){
        try{
            checkBalance(amount);
            return true;
        }catch (InsufficientBalanceException e){
            System.out.println("Uzytkownik nie ma golda");
            return false;
        }
    }

    public boolean safeChangeBalance(int change){
        try{
            changeBalance(change);
            return true;
        }catch (InsufficientBalanceException e){
            System.out.println("Uzytkownik nie ma golda");
            return false;
        }
    }
}
