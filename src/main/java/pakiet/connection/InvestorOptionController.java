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
import pakiet.service.operate.UserMenager;

@RestController
@RequestMapping("/robot/")
public class InvestorOptionController {
    private int chosedOperation;

    @GetMapping("{nick}/investor/")
    public String showInvestorOperation(@PathVariable String nick){
        return StringHelper.getInvestorOperationChoice();
    }

    @GetMapping("{nick}/investor/{choiceOperation}")
    public ResponseEntity<Void> choseInvestorOperation(@PathVariable String nick, @PathVariable int choiceOperation){
        chosedOperation=choiceOperation;
        switch(choiceOperation) {
            case 1,3,4:
                return ApiHelper.getResponseLocation("/robot/"+nick+"/investor/id/");
            case 2:
                return ApiHelper.getResponseLocation("/robot/"+nick+"/investor/amount/");
            default:
                return ApiHelper.getResponseLocation("/robot/"+nick+"/investor/");
        }
    }

    @GetMapping("{nick}/investor/id/")
    public String showIdCommunicate(){
        return "Podaj investor id: " + InvestorMenager.returnIdsList();
    }

    @GetMapping("{nick}/investor/amount/")
    public String showAmountCommmunicate(){
        return "Podaj gold amount: " + BalanceMenager.returnGoldAmount();
    }

    @GetMapping("{nick}/investor/id/{choiceId}")
    public ResponseEntity<Void> enterInvestorId (@PathVariable String nick, @PathVariable int choiceId, HttpSession session){
        if(InvestorMenager.findInvestorById(choiceId)==null){
            return ApiHelper.getResponseLocation("/robot/" + nick + "/investor/id/");
        }
        switch(chosedOperation){
            case 1:
                String message = StringHelper.getShowInvestor(choiceId);
                session.setAttribute("message", message);
                break;
            case 3:
                if(OperationMenager.upgradeInvestor(choiceId))
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 4:
                OperationMenager.sellInvestor(choiceId);
                ApiHelper.setMessageOperationSuccessful(session);
                break;
            default:
                return ApiHelper.getResponseLocation("/robot/"+nick+"/investor/id/");
        }
        return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }

    @GetMapping("{nick}/investor/amount/{amountGold}")
    public ResponseEntity<Void> enterAmountGold (@PathVariable String nick, @PathVariable int amountGold, HttpSession session){
        if (OperationMenager.investGold(amountGold))
            ApiHelper.setMessageOperationSuccessful(session);
        else
            ApiHelper.setMessageOperationUnSuccessful(session);
        return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }
}
