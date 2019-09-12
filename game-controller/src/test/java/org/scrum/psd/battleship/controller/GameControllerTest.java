package org.scrum.psd.battleship.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

@Execution(ExecutionMode.CONCURRENT)
public class GameControllerTest {

    protected static Position parsePosition(String input) {
        Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
        int number = Integer.parseInt(input.substring(1));
        return new Position(letter, number);
    }

    private static Stream<Arguments> provideShipPositions() {
        return Stream.of(
            Arguments.of(Collections.emptyList(), 3, false),
            Arguments.of(Arrays.asList("A1", "A1", "A1"), 3, false),
            Arguments.of(Arrays.asList("A1", "A2", "A3"), 3, true),
            Arguments.of(Arrays.asList("A1", "A4", "A3"), 3, false),
            Arguments.of(Arrays.asList("B1", "A1", "C1"), 3, true),
            Arguments.of(Arrays.asList("D1", "A1", "C1"), 3, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideShipPositions")
    public void testIsShipValid(List<String> positions, int size, boolean expected) {
        Ship ship = new Ship("TestShip", size,
            positions.stream().map(GameControllerTest::parsePosition).collect(Collectors.toList()));
        boolean result = GameController.isShipValid(ship);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCheckIsHitTrue() {
        List<Ship> ships = GameController.initializeShips();
        int counter = 0;

        for (Ship ship : ships) {
            Letter letter = Letter.values()[counter];

            for (int i = 0; i < ship.getSize(); i++) {
                ship.getPositions().add(new Position(letter, i));
            }

            counter++;
        }

        boolean result = GameController.checkIsHit(ships, new Position(Letter.A, 1));

        Assertions.assertTrue(result);
    }

    @Test
    public void testCheckIsHitFalse() {
        List<Ship> ships = GameController.initializeShips();
        int counter = 0;

        for (Ship ship : ships) {
            Letter letter = Letter.values()[counter];

            for (int i = 0; i < ship.getSize(); i++) {
                ship.getPositions().add(new Position(letter, i));
            }

            counter++;
        }

        boolean result = GameController.checkIsHit(ships, new Position(Letter.H, 1));

        Assertions.assertFalse(result);
    }

    @Test
    public void testCheckIsHitPositstionIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            GameController.checkIsHit(GameController.initializeShips(), null));
    }

    @Test
    public void testCheckIsHitShipIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            GameController.checkIsHit(null, new Position(Letter.H, 1)));
    }
}
