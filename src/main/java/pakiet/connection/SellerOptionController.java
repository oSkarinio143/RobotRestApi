package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pakiet.modules.robot.AbstractSeller;
import pakiet.modules.robot.SellerHouses;
import pakiet.service.operate.BalanceMenager;
import pakiet.service.operate.OperationMenager;
import pakiet.service.operate.SellerMenager;

@RestController
@RequestMapping("/robot/")
public class SellerOptionController {
    private int chosedOperation;
    private int userAmount;
    private int userId;

    @GetMapping("{nick}/seller/")
    public String showSellerOperation (@PathVariable String nick){
        return StringHelper.getSellerOperationChoice();
    }

    @GetMapping("{nick}/seller/{choiceOperation}")
    public ResponseEntity<Void> choseSellerOperation (@PathVariable String nick, @PathVariable int choiceOperation, HttpSession session){
        chosedOperation = choiceOperation;
        switch (choiceOperation){
            case 1,3,4:
                return ApiHelper.getResponseLocation("/robot/" + nick + "/seller/id/");
            case 2:
                OperationMenager.earnGold();
                ApiHelper.setMessageOperationSuccessful(session);
                return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");
            default:
                return ApiHelper.getResponseLocation("/robot/" + nick + "/seller/");
        }
    }

    @GetMapping("{nick}/seller/id/")
    public String showSellerId(){
        return "Podaj id sellera: " + SellerMenager.returnIdsList();
    }

    @GetMapping("{nick}/seller/id/{userId}")
    public ResponseEntity ChoseSellerIdOperation (@PathVariable String nick, @PathVariable int userId, HttpSession session){
        if(SellerMenager.findSellerById(userId).isEmpty()){
            return ApiHelper.getResponseLocation("/robot/" + nick + "/seller/");
        }
        switch (chosedOperation){
            case 1:
                String message = StringHelper.getShowSeller(userId);
                session.setAttribute("message", message);
                break;

            case 3:
                if(OperationMenager.upgradeSeller(userId))
                    ApiHelper.setMessageOperationSuccessful(session);
                else
                    ApiHelper.setMessageOperationUnSuccessful(session);

                break;

            case 4:
                OperationMenager.sellSeller(userId);
                ApiHelper.setMessageOperationSuccessful(session);
                break;
        }
        return ApiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }


}
