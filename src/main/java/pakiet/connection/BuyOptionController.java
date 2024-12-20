package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pakiet.service.operate.OperationMenager;

@RestController
@RequestMapping("/robot/")
public class BuyOptionController {
    private final OperationMenager operationMenager;
    private final ApiHelper apiHelper;
    private final StringHelper stringHelper;

    BuyOptionController(OperationMenager operationMenager, ApiHelper apiHelper, StringHelper stringHelper){
        this.operationMenager = operationMenager;
        this.apiHelper = apiHelper;
        this.stringHelper = stringHelper;
    }

    @GetMapping("{nick}/buy/")
    public String showBuyOperations(){
        return stringHelper.getBuyOperationChoice();
    }

    @GetMapping("{nick}/buy/{choiceOperation}")
    public ResponseEntity<Void> choiceBuyOperation(@PathVariable String nick, @PathVariable int choiceOperation, HttpSession session){
        switch (choiceOperation) {
            case 1:
                if(operationMenager.buyInvestor())
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 2:
                if(operationMenager.buyBooksSeller())
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 3:
                if(operationMenager.buyBoardGamesSeller())
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 4:
                if(operationMenager.buyComputerGamesSeller())
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 5:
                if(operationMenager.buyHousesSeller())
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 6:
                if(operationMenager.buyMachine())
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);
                break;
            default:
                return apiHelper.getResponseLocation("/robot/" + nick + "/buy/");
        }
        return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }
}
