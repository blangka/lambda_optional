package com.hkmc.aws;

import com.hkmc.aws.model.Apple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LambdaTest {

    @Test
    public void 람다_인터테이스() throws Exception {
        //given
        Runnable r1 = () -> System.out.println("hello world 1"); //람다 사용
        Runnable r2 = new Runnable() { // 익명 클래스 사용
            public void run() {
                System.out.println("hello world 2");
            }
        };
        //when

        //then
        process(r1);
        process(r2);
        process(() -> System.out.println("hello world 3"));
    }

    public static void process(Runnable r) {
        r.run();
    }

    @Test
    public void 람다_Predicate() throws Exception {
        //given
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("");
        list.add("test3");

        Predicate<String> nonEmptyStr = (String s) -> !s.isEmpty();
        List<String> nonEmpty =  list.stream().filter(nonEmptyStr).collect(Collectors.toList());
        //when

        //then
        System.out.println("list : " + list);
        System.out.println("nonEmpty : " + nonEmpty);
    }



    @Test
    public void 람다_매서드참조() throws Exception {
        //given
        Apple a1 = new Apple();
        a1.setWeight(30);

        Apple a2 = new Apple();
        a2.setWeight(10);

        List<Apple> inven = new ArrayList<>();
        inven.add(a1); inven.add(a2);

        //when
        inven.sort(Comparator.comparing(Apple::getWeight));
        //then
        System.out.println("inven : " + inven);
    }

    @Test
    public void 람다_다양한조합예제() throws Exception {
        //given
        Apple a1 = new Apple();
        a1.setWeight(30);
        a1.setContry("kor");
        a1.setColor("red");

        Apple a2 = new Apple();
        a2.setWeight(10);
        a2.setContry("usa");
        a2.setColor("green");

        Apple a3 = new Apple();
        a3.setWeight(30);
        a3.setContry("usa");
        a3.setColor("blue");

        List<Apple> inven = new ArrayList<>();
        inven.add(a1); inven.add(a2); inven.add(a3);

        //when
        //comparator 조합
        inven.sort(Comparator.comparing(Apple::getWeight)
                .reversed() //무게를 내림차순으로 조합
                .thenComparing(Apple::getContry) // 무게가 같으면 국가 별로 정렬
        );

        //function 조합
        Function<Integer, Integer> f = x-> x+1;
        Function<Integer, Integer> g = x-> x*2;
        Function<Integer, Integer> h = f.andThen(g);
        Function<Integer, Integer> z = f.compose(g);
        int result1 = h.apply(1); // f 수행되고 g 수행
        int result2 = z.apply(1); // g 수행되고 f 수행

        //then
    }
}
