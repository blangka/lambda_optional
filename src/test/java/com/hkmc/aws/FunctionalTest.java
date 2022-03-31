package com.hkmc.aws;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FunctionalTest {

    @Test
    public void functional예제() throws Exception {
        List<List<Integer>> subs = subsets(Arrays.asList(1, 4, 9));
        subs.forEach(System.out::println);
    }

    public static <T> List<List<T>> subsets(List<T> l) {
        //입력 리스트가 비어 있다면 빈 리스트 자신이 서브 집합니다.
        if (l.isEmpty()) {
            List<List<T>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }

        //빈 리스트가 아니면 요소를 하나 ㅈ꺼내고 나머지 요소를 서브 집합으로 찾아서 전달한다.
        T first = l.get(0);
        List<T> rest = l.subList(1, l.size());
        List<List<T>> subans = subsets(rest);
        List<List<T>> subans2 = insertAll(first, subans);
        return concat(subans, subans2);
    }

    public static <T> List<List<T>> insertAll(T first, List<List<T>> lists) {
        List<List<T>> result = new ArrayList<>();
        for (List<T> l : lists) {
            List<T> copyList = new ArrayList<>();
            copyList.add(first);
            copyList.addAll(l);
            result.add(copyList);
        }
        return result;
    }

    static <T> List<List<T>> concat(List<List<T>> a, List<List<T>> b) {
        List<List<T>> r = new ArrayList<>(a);
        r.addAll(b);
        return r;
    }

    @Test
    public void functional재귀() throws Exception {
        //반복 방식
        System.out.println(factorialIterative(5));
        //재귀 방식
        System.out.println(factorialRecursive(5));
        //스트림을 사용하여 좀더 다른 방식으로 표현가능
        System.out.println(factorialStreams(5));
        //꼬리 방식으로  factorialRecursive 재귀 방식의 경우 입력 값에 따라 메모리 사용량이 증가한다.
        //stackOverflowError가 발생한다. 하지만 factorialHelper 컴파일러가 하나의 스택 프레임을 재사용하기 때문에 좋다.
        System.out.println(factorialTailRecursive(5));
    }

    public static int factorialIterative(int n) {
        int r = 1;
        for (int i = 1; i <= n; i++) {
            r *= i;
        }
        return r;
    }

    public static long factorialRecursive(long n) {
        return n == 1 ? 1 : n * factorialRecursive(n - 1);
    }

    public static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n).reduce(1, (long a, long b) -> a * b);
    }

    public static long factorialTailRecursive(long n) {
        return factorialHelper(1, n);
    }

    public static long factorialHelper(long acc, long n) {
        return n == 1 ? acc : factorialHelper(acc * n, n - 1);
    }
}
