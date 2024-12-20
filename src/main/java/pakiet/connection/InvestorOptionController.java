package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pakiet.service.operate.*;

@RestController
@RequestMapping("/robot/")
@Getter
@Setter
public class InvestorOptionController{
    private int chosedOperation;
    private UserMenager userMenager;
    private MachineMenager machineMenager;
    private InvestorMenager investorMenager;
    private BalanceMenager balanceMenager;
    private OperationMenager operationMenager;
    private SellerMenager sellerMenager;
    private ApiHelper apiHelper;
    private StringHelper stringHelper;

    public InvestorOptionController(UserMenager userMenager, MachineMenager machineMenager, SellerMenager sellerMenager,
                                    InvestorMenager investorMenager, BalanceMenager balanceMenager, OperationMenager operationMenager,
                                    ApiHelper apiHelper, StringHelper stringHelper){
        this.userMenager = userMenager;
        this.machineMenager = machineMenager;
        this.sellerMenager = sellerMenager;
        this.investorMenager = investorMenager;
        this.balanceMenager = balanceMenager;
        this.operationMenager = operationMenager;
        this.apiHelper = apiHelper;
        this.stringHelper = stringHelper;

    }

    @GetMapping("{nick}/investor/")
    public String showInvestorOperation(@PathVariable String nick){
        return stringHelper.getInvestorOperationChoice();
    }

    @GetMapping("{nick}/investor/{choiceOperation}")
    public ResponseEntity<Void> choseInvestorOperation(@PathVariable String nick, @PathVariable int choiceOperation){
        chosedOperation=choiceOperation;
        switch(choiceOperation) {
            case 1,3,4:
                return apiHelper.getResponseLocation("/robot/"+nick+"/investor/id/");
            case 2:
                return apiHelper.getResponseLocation("/robot/"+nick+"/investor/amount/");
            default:
                return apiHelper.getResponseLocation("/robot/"+nick+"/investor/");
        }
    }

    @GetMapping("{nick}/investor/id/")
    public String showIdCommunicate(){
        return "Podaj investor id: " + investorMenager.returnIdsList();
    }

    @GetMapping("{nick}/investor/amount/")
    public String showAmountCommmunicate(){
        return "Podaj gold amount: " + balanceMenager.returnGoldAmount();
    }

    @GetMapping("{nick}/investor/id/{choiceId}")
    public ResponseEntity<Void> enterInvestorId (@PathVariable String nick, @PathVariable int choiceId, HttpSession session){
        if(investorMenager.findInvestorById(choiceId)==null){
            return apiHelper.getResponseLocation("/robot/" + nick + "/investor/id/");
        }
        switch(chosedOperation){
            case 1:
                String message = stringHelper.getShowInvestor(choiceId);
                session.setAttribute("message", message);
                break;
            case 3:
                if(operationMenager.upgradeInvestor(choiceId))
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 4:
                operationMenager.sellInvestor(choiceId);
                apiHelper.setMessageOperationSuccessful(session);
                break;
            default:
                return apiHelper.getResponseLocation("/robot/"+nick+"/investor/id/");
        }
        return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }

    @GetMapping("{nick}/investor/amount/{amountGold}")
    public ResponseEntity<Void> enterAmountGold (@PathVariable String nick, @PathVariable int amountGold, HttpSession session){
        if (operationMenager.investGold(amountGold))
            apiHelper.setMessageOperationSuccessful(session);
        else
            apiHelper.setMessageOperationUnSuccessful(session);
        return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }
}
