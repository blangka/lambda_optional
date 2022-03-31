package com.hkmc.aws;

import com.hkmc.aws.builder.LambdaOrderBuilder;
import com.hkmc.aws.model.Order;
import com.hkmc.aws.model.Stock;
import com.hkmc.aws.model.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.hkmc.aws.builder.MethodChainingOrderBuilder.*;
import static com.hkmc.aws.builder.NestedFuctionOrderBuilder.*;
import static com.hkmc.aws.builder.MixedBuilder.*;
import static com.hkmc.aws.builder.LambdaOrderBuilder.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RefactoringTest {

    @Test
    public void 리팩토링_UnaryOperator() throws Exception {

        //Type T 인자를 받아서 동일한 Type T 객체를 리턴한다.
        UnaryOperator<Integer> unaryOperator1 = n -> n * n;
        Integer result = unaryOperator1.apply(10);
        System.out.println(result);

        UnaryOperator<Boolean> unaryOperator2 = b -> !b;
        Boolean result2 = unaryOperator2.apply(true);
        System.out.println(result2);

        UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda", "lambda");
        Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
        String result4 = pipeline.apply("Aren't labdas really sexy?!!");
        System.out.println(result4);

    }

    @Test
    public void 리팩토링_DSL샘플() throws Exception {

        plain();
        methodChaining();
        nestedFunction();
        lambda();
        mixed();
    }

    public void plain() {
        Order order = new Order();
        order.setCustomer("BigBank");

        Trade trade1 = new Trade();
        trade1.setType(Trade.Type.BUY);

        Stock stock1 = new Stock();
        stock1.setSymbol("IBM");
        stock1.setMarket("NYSE");

        trade1.setStock(stock1);
        trade1.setPrice(125.00);
        trade1.setQuantity(80);
        order.addTrade(trade1);

        Trade trade2 = new Trade();
        trade2.setType(Trade.Type.BUY);

        Stock stock2 = new Stock();
        stock2.setSymbol("GOOGLE");
        stock2.setMarket("NASDAQ");

        trade2.setStock(stock2);
        trade2.setPrice(375.00);
        trade2.setQuantity(50);
        order.addTrade(trade2);

        System.out.println("Plain:");
        System.out.println(order);
    }

    public void methodChaining() {
        Order order = forCustomer("BigBank")
                .buy(80).stock("IBM").on("NYSE").at(125.00)
                .sell(50).stock("GOOGLE").on("NASDAQ").at(375.00)
                .end();

        System.out.println("Method chaining:");
        System.out.println(order);
    }

    public void nestedFunction() {
        Order order = order("BigBank",
                buy(80,
                        stock("IBM", on("NYSE")),
                        at(125.00)),
                sell(50,
                        stock("GOOGLE", on("NASDAQ")),
                        at(375.00))
        );

        System.out.println("Nested function:");
        System.out.println(order);
    }

    public void lambda() {
        Order order = LambdaOrderBuilder.order(o -> {
            o.forCustomer( "BigBank" );
            o.buy( t -> {
                t.quantity(80);
                t.price(125.00);
                t.stock(s -> {
                    s.symbol("IBM");
                    s.market("NYSE");
                });
            });
            o.sell( t -> {
                t.quantity(50);
                t.price(375.00);
                t.stock(s -> {
                    s.symbol("GOOGLE");
                    s.market("NASDAQ");
                });
            });
        });

        System.out.println("Lambda:");
        System.out.println(order);
    }

    public void mixed() {
        Order order =
                forCustomer("BigBank",
                        buy(t -> t.quantity(80)
                                .stock("IBM")
                                .on("NYSE")
                                .at(125.00)),
                        sell(t -> t.quantity(50)
                                .stock("GOOGLE")
                                .on("NASDAQ")
                                .at(375.00)));

        System.out.println("Mixed:");
        System.out.println(order);
    }
}
