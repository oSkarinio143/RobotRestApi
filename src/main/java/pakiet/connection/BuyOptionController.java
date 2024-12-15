package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pakiet.service.operate.OperationMenager;
import pakiet.service.operate.UserMenager;

@RestController
@RequestMapping("/robot/")
public class BuyOptionController {

    @GetMapping("{nick}/buy/")
    public String showBuyOperations(){
        return StringHelper.getBuyOperationChoice();
    }

    @GetMapping("{nick}/buy/{choiceOperation}")
    public ResponseEntity<Void> choiceBuyOperation(@PathVariable String nick, @PathVariable int choiceOperation, HttpSession session){
        switch (choiceOperation) {
            case 1:
                if(OperationMenager.buyInvestor())
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 2:
                if(OperationMenager.buyBooksSeller())
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 3:
                if(OperationMenager.buyBoardGamesSeller())
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 4:
                if(OperationMenager.buyComputerGamesSeller())
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 5:
                if(OperationMenager.buyHousesSeller())
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                break;
            case 6:
                if(OperationMenager.buyMachine())
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);
                break;
            default:
                return ApiHelper.getResponseLocation("/robot/" + nick + "/buy/");
        }
        return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }
}
