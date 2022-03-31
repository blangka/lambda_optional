package com.hkmc.aws;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static java.util.Map.entry;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StreamLambdaExtraTest {

    @Test
    public void 콜렉션API개선_생성() throws Exception {

        //List 생성
        List<String> friends1 = List.of("lim", "cha", "song");
        //friends.add("kim");

        //set 생성
        Set<String> friends2 = Set.of("lim", "cha", "song");

        //map 생성
        Map<String, Integer> ageOfFriends2 = Map.ofEntries(
                entry("Raphael", 30),
                entry("Olivia", 25),
                entry("Thibaut", 26));
    }

    @Test
    public void 콜렉션API개선_삭제및변경() throws Exception {
        // 숫자로 시작하는거 삭제
        List<String> code = new ArrayList<>();
        code.add("a12");
        code.add("14b");
        code.add("b13");
        code.removeIf(c -> Character.isDigit(c.charAt(0)));
        System.out.println(code);

        // replace 해서 변경한다.
        code.replaceAll(c -> Character.toUpperCase(c.charAt(0)) + c.substring(1));
        System.out.println(code);

        //for each
        System.out.println("--> Iterating a map with a for loop");
        Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
        for (Map.Entry<String, Integer> entry : ageOfFriends.entrySet()) {
            String friend = entry.getKey();
            Integer age = entry.getValue();
            System.out.println(friend + " is " + age + " years old");
        }

        System.out.println("--> Iterating a map with forEach()");
        ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));

    }

    @Test
    public void 콜렉션API개선_계산패턴() throws Exception {
        // 계산 패턴
        // lim에게 줄 영화 목록을 만든다.
        Map<String, List<String>> friendsToMovies = new HashMap<>();
        friendsToMovies.computeIfAbsent("lim", name -> new ArrayList<>())
                .add("Star Wars");
        friendsToMovies.computeIfPresent("lim", (s, list) -> list)
                .add("Star Wars2");

        System.out.println(friendsToMovies);

        // remove
        Map<String, String> favouriteMovies = new HashMap<>();
        favouriteMovies.put("Raphael", "Jack Reacher 2");
        favouriteMovies.put("Cristina", "Matrix");
        favouriteMovies.put("Olivia", "James Bond");
        String key = "Raphael";
        String value = "Jack Reacher 2";
        favouriteMovies.remove(key, value);

        // replace
        favouriteMovies.replaceAll((friend, movie) -> movie.toUpperCase());
    }

    @Test
    public void 콜렉션API개선_merge() throws Exception {
        // merge
        Map<String, String> family = Map.ofEntries(
                entry("Teo", "Star Wars"),
                entry("Cristina", "James Bond"));
        Map<String, String> friends = Map.ofEntries(entry("Raphael", "Star Wars"));
        Map<String, String> everyone = new HashMap<>(family);
        everyone.putAll(friends);


        // merge 에서 중복되는 이름이 있는 경우 조건을 주어서 합치는 방법
        Map<String, String> friendsOther = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Cristina", "Matrix"));
        Map<String, String> everyone2 = new HashMap<>(family);
        friendsOther.forEach((k, v) -> everyone2.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
        System.out.println(everyone2);
    }

}

