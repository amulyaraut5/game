package server;

import game.gameObjects.cards.Card;

import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args) {
        Map<Integer, String> registerAndCards = new HashMap<>();
        registerAndCards.put(1, "Test1");
        registerAndCards.put(2, "Test2");
        registerAndCards.put(3, "Test3");

        System.out.println(registerAndCards.size());

    }
}
