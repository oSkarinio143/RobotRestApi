package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pakiet.modules.User;
import pakiet.modules.robot.SellerBooks;
import pakiet.service.operate.BalanceMenager;
import pakiet.service.operate.SellerMenager;
import pakiet.service.operate.UserMenager;
@Getter
@RestController
@RequestMapping("/robot")
public class UserController {
    private boolean isFirstConnection = true;
    private int operationAmount=0;
    @Autowired
    private UserMenager userMenager;
    @Autowired
    private BalanceMenager balanceMenager;
    @Autowired
    private ApiHelper apiHelper;
    @Autowired
    private StringHelper stringHelper;
    @Autowired
    private SellerMenager sellerMenager;

    @GetMapping
    public ResponseEntity<Void> getRobotMain(){
        return apiHelper.getResponseLocation("/robot/");
    }

    @GetMapping("/")
    public String enterUserNick(){
        if(isFirstConnection) {
            isFirstConnection = false;
            return "Witaj nowy cliencie, wprowadz swoj nick";
        }
        else
            return "Client rozlaczyl sie z serwerem.<br> Witaj nowy cliencie, wprowadz swoj nick";
        }

    @GetMapping("/{nick}")
    public ResponseEntity<Void> registerUser(@PathVariable("nick") String nick, HttpSession session) {
            if(userMenager.createIfNewUser(nick)){
                session.setAttribute("messageInitial", "Zarejestrowano nowego uzytkownika. Witaj "+nick);
            }
            else
                session.setAttribute("messageInitial", "Witaj spowrotem " + nick + "<br>");
            userMenager.setUserEverywhere();
            operationAmount=0;
            return apiHelper.getResponseLocation("/robot/"+nick+"/type/");
    }

    @GetMapping("/{nick}/type/")
    public String welcomeUser(@PathVariable String nick, HttpSession session) {
        if(operationAmount>0){
            session.setAttribute("messageInitial", "Wybierz kolejna operacje, " + nick + "<br>");
        }
        operationAmount++;
        String sessionMessageInitial = (String) session.getAttribute("messageInitial");
        String sessionMessage = (String) session.getAttribute("message");

        if (sessionMessageInitial!=null) {
            if(sessionMessage!=null)
                return sessionMessage + "<br>-------------------------------------------------<br>" + sessionMessageInitial + "<br>" +stringHelper.getTypeOperationChoice();
            else
                return sessionMessageInitial + "<br>" + stringHelper.getTypeOperationChoice();
        }
        return stringHelper.getTypeOperationChoice();
    }

    @GetMapping("/{nick}/type/{choiceType}")
    public ResponseEntity<Void> chooseTypeOperation(@PathVariable String nick, @PathVariable int choiceType){
        switch (choiceType) {
            case 1:
                if (ifInvestorOperationPossible(nick))
                    return apiHelper.getResponseLocation("/robot/" + nick + "/investor/");
                else
                    return apiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 2:
                if (ifSellerOperationPossible(nick))
                    return apiHelper.getResponseLocation("/robot/" + nick + "/seller/");
                else
                    return apiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 3:
                if (ifBuyOperationPossible())
                    return apiHelper.getResponseLocation("/robot/" + nick + "/buy/");
                else
                    return apiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 4:
                return apiHelper.getResponseLocation("/robot/" + nick + "/other/");

            case 5:
                operationAmount=0;
                return apiHelper.getResponseLocation("/robot/");

            default:
                return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
        }
    }

    public boolean ifInvestorOperationPossible(String nick){
        User user = userMenager.findUserByNick(nick);
        return !user.getOwnedInvestors().isEmpty();
    }

    public boolean ifSellerOperationPossible(String nick){
        User user = userMenager.findUserByNick(nick);
        return !user.getOwnedSellers().isEmpty();
    }

    public boolean ifBuyOperationPossible(){
        return balanceMenager.returnGoldAmount()> sellerMenager.countBuyCost(SellerBooks.class);
    }
}

