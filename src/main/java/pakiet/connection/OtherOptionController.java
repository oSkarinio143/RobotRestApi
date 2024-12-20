package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pakiet.service.operate.*;

@RestController
@RequestMapping("/robot/")
public class OtherOptionController {
    private int chosedOperation;
    private int chosedTimes;

    private final OperationMenager operationMenager;
    private final BalanceMenager balanceMenager;
    private final ApiHelper apiHelper;
    private final StringHelper stringHelper;
    private final MachineMenager machineMenager;

    public OtherOptionController(MachineMenager machineMenager, OperationMenager operationMenager, BalanceMenager balanceMenager, ApiHelper apiHelper, StringHelper stringHelper){
        this.operationMenager = operationMenager;
        this.balanceMenager = balanceMenager;
        this.apiHelper = apiHelper;
        this.stringHelper = stringHelper;
        this.machineMenager = machineMenager;
    }

    @GetMapping("{nick}/other/")
    public String showOtherOperations() {
        return stringHelper.getOtherOperationChoice();
    }

    @GetMapping("{nick}/other/{choiceOperation}")
    public ResponseEntity<Void> choseFirstOperation(@PathVariable String nick, @PathVariable int choiceOperation, HttpSession session) {
        chosedOperation = choiceOperation;
        switch (choiceOperation) {
            case 1:
                String message = "Dostepny gold - "+Double.toString(balanceMenager.returnGoldAmount());
                session.setAttribute("message", message);
                return apiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 2, 3, 4:
                if(machineMenager.isMachineUnlocked())
                    return apiHelper.getResponseLocation("/robot/" + nick + "/other/times/");
                else {
                    session.setAttribute("message","Maszyna niedostepna");
                    return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
                }
            default:
                return apiHelper.getResponseLocation("/robot/" + nick + "other/");
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
            return apiHelper.getResponseLocation("/robot/" + nick + "/other/times/");
        }
        switch (chosedOperation) {
            case 2:
                if(operationMenager.performWork(choiceTimes))
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                return apiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 3,4:
                return apiHelper.getResponseLocation("/robot/" + nick + "/other/amount/");

            default:
                return apiHelper.getResponseLocation("/robot/" + nick + "times/");
        }
    }

    @GetMapping("{nick}/other/amount/")
    public String choseAmountCommunication(){
        return "Podaj ilosc golda, dostepne - " + balanceMenager.returnGoldAmount();
    }


    @GetMapping("{nick}/other/amount/{choiceAmount}")
    public ResponseEntity<Void> setOperatoinAmount (@PathVariable String nick, @PathVariable int choiceAmount, HttpSession session) {
        switch (chosedOperation) {
            case 3:
                if(operationMenager.performInvestment(chosedTimes, choiceAmount))
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                return apiHelper.getResponseLocation("/robot/" + nick + "/type/");

            case 4:
                if(operationMenager.performWorkInvestment(chosedTimes, choiceAmount))
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                return apiHelper.getResponseLocation("/robot/" + nick + "/type/");

            default:
                return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
        }
    }
}
