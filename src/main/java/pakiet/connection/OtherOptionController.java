package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pakiet.service.operate.BalanceMenager;
import pakiet.service.operate.InvestorMenager;
import pakiet.service.operate.OperationMenager;
import pakiet.service.operate.SellerMenager;

@RestController
@RequestMapping("/robot/")
public class OtherOptionController {
    private int chosedOperation;
    private int chosedTimes;

    @GetMapping("{nick}/other/")
    public String showOtherOperations() {
        return StringHelper.getOtherOperationChoice();
    }

    @GetMapping("{nick}/other/{choiceOperation}")
    public ResponseEntity<Void> choseFirstOperation(@PathVariable String nick, @PathVariable int choiceOperation, HttpSession session) {
        chosedOperation = choiceOperation;
        switch (choiceOperation) {
            case 1:
                String message = "Dostepny gold - "+Double.toString(BalanceMenager.returnGoldAmount());
                session.setAttribute("message", message);
                return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 2, 3, 4:
                return ApiHelper.getResponseLocation("/robot/" + nick + "/other/times/");

            default:
                return ApiHelper.getResponseLocation("/robot/" + nick + "other/");
        }
    }

    @GetMapping("{nick}/other/times/")
    public String choseTimesCommunication() {
        return "Podaj ile razy ma zostac wykonana operacja: [1-100]";
    }

    @GetMapping("{nick}/other/times/{choiceTimes}")
    public ResponseEntity<Void> setOperationTimes(@PathVariable String nick, @PathVariable int choiceTimes, HttpSession session) {
        chosedTimes = choiceTimes;
        if (choiceTimes < 1 || choiceTimes > 100) {
            return ApiHelper.getResponseLocation("/robot/" + nick + "/other/times/");
        }
        switch (chosedOperation) {
            case 2:
                if(OperationMenager.performWork(choiceTimes))
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 3,4:
                return ApiHelper.getResponseLocation("/robot/" + nick + "/other/amount/");

            default:
                return ApiHelper.getResponseLocation("/robot/" + nick + "times/");
        }
    }

    @GetMapping("{nick}/other/amount/")
    public String choseAmountCommunication(){
        return "Podaj ilosc golda, dostepne - " + BalanceMenager.returnGoldAmount();
    }


    @GetMapping("{nick}/other/amount/{choiceAmount}")
    public ResponseEntity<Void> setOperatoinAmount (@PathVariable String nick, @PathVariable int choiceAmount, HttpSession session) {
        switch (chosedOperation) {
            case 3:
                if(OperationMenager.performInvestment(chosedTimes, choiceAmount))
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 4:
                if(OperationMenager.performWorkInvestment(chosedTimes, choiceAmount))
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");

            default:
                return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");
        }
    }
}
