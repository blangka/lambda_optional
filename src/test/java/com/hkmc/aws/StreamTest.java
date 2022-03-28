package com.hkmc.aws;

import com.hkmc.aws.model.Dish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StreamTest {

    @Test
    public void 스트림_필터링() throws Exception {
        //given
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("전어",false,10, Dish.Type.FISH));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("콩나물",true,3, Dish.Type.OTHER));
        menu.add(new Dish("불고가",false,100, Dish.Type.MEAT));

        //when
        List<Dish> vegetarianMenu = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());

        //then
        vegetarianMenu.stream()
                .distinct()
                .forEach((Dish d) -> System.out.println(d.getName()));
    }

    @Test
    public void 스트림_슬라이싱() throws Exception {
        //given
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("전어",false,1, Dish.Type.FISH));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("샐러드",true,3, Dish.Type.OTHER));
        menu.add(new Dish("콩나물",true,4, Dish.Type.OTHER));
        menu.add(new Dish("불고가",false,5, Dish.Type.MEAT));

        //when
        List<Dish> slice1 = menu.stream()
                .takeWhile(dist -> dist.getCalories() < 3)
                .collect(Collectors.toList());

        List<Dish> slice2 = menu.stream()
                .dropWhile(dist -> dist.getCalories() < 3)
                .collect(Collectors.toList());


        //then
        slice1.stream()
                .distinct()
                .forEach((Dish d) -> System.out.println("slice 1 : " + d.getName()));

        slice2.stream()
                .distinct()
                .forEach((Dish d) -> System.out.println("slice 2 : " + d.getName()));
    }

    @Test
    public void 스트림_매핑() throws Exception {
        //mapping
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("전어",false,1, Dish.Type.FISH));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("콩나물볶음",true,4, Dish.Type.OTHER));
        menu.add(new Dish("불고기탕",false,5, Dish.Type.MEAT));

        List<Integer> dishNameLenghts = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(Collectors.toList());

        System.out.println("dishNameLenghts : " + dishNameLenghts);

        //flat
        List<String> words = Arrays.asList("Hello", "World");
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .collect(Collectors.toList());

        List<String> uniqueChar = words.stream()
                .map(word -> word.split(""))//각 단어를 개별 문자를 포함하는 배열로 변환
                .flatMap(Arrays::stream)//생성된 스트림을 하나의 스트림으로 평면화
                .distinct()
                .collect(Collectors.toList());

        System.out.println("uniqueChar : " + uniqueChar);

    }

    @Test
    public void 스트림_검색매칭() throws Exception {
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("전어",false,1, Dish.Type.FISH));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("콩나물볶음",true,3, Dish.Type.OTHER));
        menu.add(new Dish("불고기탕",false,5, Dish.Type.MEAT));

        //anyMatch : 한 요소라도 일치하는지
        boolean a1 = menu.stream().allMatch(Dish::isVegetarian);
        //allMatch : 모든 요소가 일치하는지
        boolean a2 = menu.stream().allMatch(dish -> dish.getCalories() < 10);
        //noneMatch : allmatch 반대
        boolean a3 = menu.stream().noneMatch(dish -> dish.getCalories() > 10);

        //findAny : 임의의 요소가 있는 경우 반환한다.
        Optional<Dish> a4 = menu.stream().filter(Dish::isVegetarian).findAny();
        //findfirst : 요소중 첫번쨰 있는 것을 반환
        Optional<Dish> a5 = menu.stream().filter(Dish::isVegetarian).findFirst();
    }

    @Test
    public void 스트림_리듀싱() throws Exception {

        List<Integer> numbers = Arrays.asList(1,2,3,4,5);

        // 0을 초기 값으로 모든 수의 합을 구하여라
        int sum = numbers.stream().reduce(0,(a,b) -> a + b);

        // 초기 값이 없는 경우에 stream이 비어 있을수도 있기 때문에 optional 로 반환한다.
        Optional<Integer> reduce = numbers.stream().reduce((a, b) -> a + b);

        //최소 최대값
        numbers.stream().reduce(Integer::max);
        numbers.stream().reduce(Integer::min);
    }

    @Test
    public void 스트림_숫자형스트림() throws Exception {

        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("전어",false,1, Dish.Type.FISH));
        menu.add(new Dish("샐러드",true,2, Dish.Type.OTHER));
        menu.add(new Dish("콩나물볶음",true,3, Dish.Type.OTHER));
        menu.add(new Dish("불고기탕",false,5, Dish.Type.MEAT));

        int calories = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();

        //복원하기
        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed();

        //기본값이 없는 경우에 대해서 최대값을 찾는 경우
        OptionalInt max = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();

        //1부터 100범위릐 짝수를 나타내기
        IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0);
    }

    @Test
    public void 스트림_스트림만들기() throws Exception {

        //값으로 스트림 만들기
        Stream<String> stream = Stream.of("test1", "test2", "test3");
        stream.map(String::toUpperCase).forEach(System.out::println);

        //빈값
        Stream<String> emptyStream = Stream.empty();

        //null 될수 있는 객체 만들기
        //asis
        String homeValue = System.getProperty("home");
        Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(homeValue);
        //tobe
        Stream<String> homeValueStream2 = Stream.ofNullable(System.getProperty("home"));


        //배열로 스트림 만들기
        int[] numbers = {2,3,4,5,6,7};
        int sum = Arrays.stream(numbers).sum();

        //무한 스트림 만들기
        //피보나치
        Stream.iterate(0, n -> n +2)
                .limit(10)
                .forEach(System.out::println);
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

    }
}
