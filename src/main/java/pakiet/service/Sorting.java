package pakiet.service;

import org.springframework.stereotype.Component;
import pakiet.modules.User;
import pakiet.modules.robot.AbstractSeller;
import pakiet.service.operate.UserMenager;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Sorting {
    private UserMenager userMenager;

    public Sorting(UserMenager userMenager){
        this.userMenager = userMenager;
    }

    public Map<Integer, Integer> sortMapStream (Map<Integer, Integer> mapToSort){
        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        sortedMap = mapToSort.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        return sortedMap;
    }

    public Map<Integer, Integer> sortMapComparator (Map<Integer, Integer> mapToSort) {
        Comparator<Integer> comparator = (v1, v2) -> Integer.compare(v2, v1);
        return mapToSort.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public void sortListInvestor (){
        User user = userMenager.actualUsedUser();
        Collections.sort(user.getOwnedInvestors());
    }

    public void sortListSeller (){
        User user = userMenager.actualUsedUser();
        user.getOwnedSellers().sort((a, b) -> Integer.compare(a.getSellerId(), b.getSellerId()));
    }

    public List<AbstractSeller> sortSellerListByStat(List<AbstractSeller> sellerList, int stat){
        Comparator<Integer> comparator = (v1, v2) -> Integer.compare(v2, v1);
        List<Integer> listStatNumbers = new ArrayList<>();
        for (AbstractSeller seller : sellerList) {
            listStatNumbers.add(seller.getStatistics().get(stat));
        }
        listStatNumbers.sort(comparator);
        List<AbstractSeller> sellersSortedByStat = new ArrayList<>();
        for (int i = 0; i < listStatNumbers.size(); i++) {
            for (AbstractSeller seller : sellerList) {
                if(seller.getStatistics().get(stat)==listStatNumbers.get(i)){
                    sellersSortedByStat.add(seller);
                    break;
                }
            }
        }
        return sellersSortedByStat;
    }
}
