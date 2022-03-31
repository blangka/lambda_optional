package com.hkmc.aws;

import com.hkmc.aws.model.Dish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CollectTest {

    enum CaloricLevel { DIET, NORMAL, FAT };

    @Test
    public void 콜렉트_리듀싱() throws Exception {

        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("전어",false,10, Dish.Type.FISH));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("콩나물",true,3, Dish.Type.OTHER));
        menu.add(new Dish("불고가",false,100, Dish.Type.MEAT));

        //DISH 갯수 구하기
        long howManyDishes1 = menu.stream().collect(Collectors.counting());
        long howManyDishes2 = menu.stream().count();

        //최소값 최대 값 구하기
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));

        //요약 연산
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

        //문자열 연결
        String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());
        String commaMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));

        //범용 리듀싱 연산
        Optional<Dish> reducingMostCalorieDish = menu.stream().collect(reducing(
                (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2
        ));
    }

    @Test
    public void 콜렉트_그룹핑() throws Exception {

        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("전어",false,10, Dish.Type.FISH));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("두부",true,2, Dish.Type.OTHER));
        menu.add(new Dish("콩나물",true,3, Dish.Type.OTHER));
        menu.add(new Dish("불고가",false,100, Dish.Type.MEAT));

        // type으로 분류
        Map<Dish.Type, List<Dish>> collect1 = menu.stream().collect(groupingBy(Dish::getType));

        // Dish 의 이름을 Type 별로 그룹화
        Map<Dish.Type, List<String>> collect2 = menu.stream().collect(
                groupingBy(Dish::getType,
                        mapping(Dish::getName, toList())));

        // 2 칼로리가 넘는 것을 그룹화
        // 2개의 차이는 filter를 하는 경우에는 없는 Type의 경우에는 아예 나오지가 않는다.
        // group by를 묶은 다음에 filter를 걸은 경우에는 없는 Type 의 경우 ex) DRINK =[] 처럼 나온다 indexing때문이다.
        Map<Dish.Type, List<Dish>> collect3 = menu.stream().filter(dish -> dish.getCalories() > 500).collect(groupingBy(Dish::getType));
        Map<Dish.Type, List<Dish>> collect4 = menu.stream().collect(
                groupingBy(Dish::getType,
                        filtering(dish -> dish.getCalories() > 500, toList())));

        //다수준 그룹화 : 그룹화 된것에 라벨을 붙인다. map
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> collect5 = menu.stream().collect(
                groupingBy(Dish::getType,
                        groupingBy((Dish dish) -> {
                            if (dish.getCalories() <= 2) {
                                return CaloricLevel.DIET;
                            }
                            else if (dish.getCalories() <= 3) {
                                return CaloricLevel.NORMAL;
                            }
                            else {
                                return CaloricLevel.FAT;
                            }
                        })
                )
        );
        // {MEAT={FAT=[Dish(name=불고가, vegetarian=false, calories=100, type=MEAT)]}
        // , FISH={FAT=[Dish(name=전어, vegetarian=false, calories=10, type=FISH)]}
        // , OTHER={NORMAL=[Dish(name=콩나물, vegetarian=true, calories=3, type=OTHER)], DIET=[Dish(name=샐러드, vegetarian=true, calories=2, type=OTHER), Dish(name=두부, vegetarian=true, calories=2, type=OTHER)]}}
        System.out.println(collect5);

        //다수준 그룹화 : 그룹화 된것에 라벨을 붙인다. set
        Map<Dish.Type, Set<CaloricLevel>> collect6 = menu.stream().collect(
                groupingBy(Dish::getType, mapping(
                        dish -> {
                            if (dish.getCalories() <= 2) {
                                return CaloricLevel.DIET;
                            } else if (dish.getCalories() <= 3) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        },
                        toSet()
                ))
        );

        // 분할함수  2개의 그룹으로 분활한다.
        Map<Boolean, List<Dish>> collect7 = menu.stream().collect(partitioningBy(Dish::isVegetarian));
        List<Dish> vegetarianDishes = collect7.get(true);
    }
}
