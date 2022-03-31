package com.hkmc.aws;

import com.hkmc.aws.model.Car;
import com.hkmc.aws.model.Insurance;
import com.hkmc.aws.model.Person;
import com.hkmc.aws.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OptionalTest {

    @Test
    public void optional_생성() throws Exception {

        // 값이 비어 있는 경우
        Optional<String> optional1 = Optional.empty();
        System.out.println(optional1); // Optional.empty
        System.out.println(optional1.isPresent()); // false

        // 값이 null이 아닌 경우
        Optional<String> optional2 = Optional.of("MyName");

        // 값이 null일수도 있고 아닐수도 있는 경우
        Optional<String> optional3 = Optional.ofNullable("test");
        String name = optional3.orElse("anonymous");

    }

    @Test
    public void optional_flatmap() throws Exception {

        Person person = new Person(Optional.empty(),10);

        Optional<Person> optionalPerson = Optional.of(person);

        /*
        Optional<String> result1 = optionalPerson
                .map(Person::getCar)
                .map(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("unknown");
         */

        String result2 = optionalPerson.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("unknown");

        System.out.println(result2);
    }

    @Test
    public void optional_활용예시() throws Exception {

        // case 1 : null 검사후 새로운 객체 생성
        // Java8 이전
        List<String> names = Arrays.asList("kim","lim","lee");
        List<String> beList = names != null
                ? names
                : new ArrayList<>();

        // Optional 사용
        List<String> afList = Optional.ofNullable(names)
                .orElseGet(() -> new ArrayList<>());


        // case 2 : 다중 null check
        User user = new User();
        User.Address uesrAddress = new User.Address();
        uesrAddress.setPostCode("1111");
        user.setAddress(uesrAddress);
        // java 8이전
        String result1;
        if (user != null) {
            User.Address address = user.getAddress();
            if (address != null) {
                String postCode = address.getPostCode();
                if (postCode != null) {
                    result1 = postCode;
                } else {
                    result1 = "우편번호 없음";
                }
            }
        }
        // optional
        String result2 = Optional.of(user).map(User::getAddress)
                .map(User.Address::getPostCode)
                .orElse("우편번호 없음");

        //case 3 : exception
        // java 8이전
        String name = "test";
        String result3 = "";
        try {
            result3 = name.toUpperCase();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
        //optional
        Optional<String> nameOpt = Optional.ofNullable(name);
        String result4 = nameOpt.orElseThrow(NullPointerException::new)
                .toUpperCase();
    }

    @Test
    public void optional_orElse_orElseGet() throws Exception {

        String userEmail = "Empty";
        String result1 = Optional.ofNullable(userEmail)
                .orElse(getUserEmail());
        System.out.println(result1);

        String result2 = Optional.ofNullable(userEmail)
                .orElseGet(this::getUserEmail);
        System.out.println(result2);

    }

    public String getUserEmail() {
        System.out.println("getUserEmail() Called");
        return "mangkyu@tistory.com";
    }

    @Test
    public void optional_사용가이드() throws Exception {

        // case 1 : Optional 변수에 Null을 할당하지 말아라
        // optional에 null을 주명 변수 자체를 다시 null check 해야 되기 때문에 empty로 사용하자
        // AVOID
        Optional<Car> emptyCar1 = null;
        // PREFER
        Optional<Car> emptyCar2 = Optional.empty();

        // case 2 : 값이 없을 때 Optional.orElseX()로 기본 값을 반환하라
        Optional<String> optionalName = null;
        String result1;
        // AVOID
        if(optionalName.isPresent()) {
            result1 = optionalName.get();
        } else {
            result1 = findDefaultName();
        }
        // PREFER
        result1 = optionalName.orElseGet(this::findDefaultName);

        // case 3 : 단순히 값을 얻으려는 목적으로만 Optional을 사용하지 마라
        String result2;
        String name = "test";
        // AVOID
        result2 = Optional.ofNullable(name).orElse("Default");
        // PREFER
        result2 = name == null
                ? "Default"
                : name;

        // case 4 : 생성자, 수정자, 메소드 파라미터 등으로 Optional을 넘기지 마라
        // 파라미터로 넘기면 null check 도 해줘야 하고 optional은 반환 타입으로
        // 대체 동작을 위해 고안된 것임이여서 Serializable을 구현하지 않으르로 필드값으로 사용하지 않아야 한다.
        // AVOID
        /*
        public class User {
            private final String name;
            private final Optional<String> postcode;

            public Customer(String name, Optional<String> postcode) {
                this.name = Objects.requireNonNull(name, () -> "Cannot be null");
                this.postcode = postcode;
            }

            public Optional<String> getName() {
                return Optional.ofNullable(name);
            }

            public Optional<String> getPostcode() {
                return postcode;
            }
        }
        */

        // case 5 : Collection의 경우 Optional을 사용하지 말고 빈 Collection으로 처리하라
        List<String> users = Arrays.asList("lim","kim","lee");
        // AVOID
        Optional<List<String>> users1 = Optional.ofNullable(users);
        // PREFER
        List<String> users2 = users == null
                ? Collections.emptyList()
                : users;

        // AVOID
        Map<String, Optional<String>> resultMap1=new HashMap<>();
        Map<String, Optional<String>> items = new HashMap<>();
        items.put("I1", Optional.ofNullable("test"));

        Optional<String> item = items.get("I1");

        if (item == null) {
            resultMap1.put("I1",Optional.ofNullable("Default Name"));
        } else {
            resultMap1.put("I1",Optional.ofNullable("test"));
        }
        // PREFER
        Map<String, String> mapItems = new HashMap<>();
        mapItems.put("I1", "test");
        String resultMap2 = mapItems.getOrDefault("I1", "Default Name");


        // case 6
        // Optional은 반환 타입으로써에러가 발생할수 있는 경우에 결과 없을을 명확히 들어내려고 만든것이다.
        // 목적에 맞게 사용해야한다.

    }

    public String findDefaultName() {
        return "EMPTY";
    }

}
