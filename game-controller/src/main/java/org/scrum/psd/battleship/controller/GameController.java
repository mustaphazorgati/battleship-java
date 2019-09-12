package org.scrum.psd.battleship.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.scrum.psd.battleship.controller.dto.Color;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

public class GameController {

    public static boolean checkIsHit(Collection<Ship> ships, Position shot) {
        if (ships == null) {
            throw new IllegalArgumentException("ships is null");
        }

        if (shot == null) {
            throw new IllegalArgumentException("shot is null");
        }

        for (Ship ship : ships) {
            for (Position position : ship.getPositions()) {
                if (position.equals(shot)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static List<Ship> initializeShips() {
        return Arrays.asList(
            new Ship("Aircraft Carrier", 5, Color.CADET_BLUE),
            new Ship("Battleship", 4, Color.RED),
            new Ship("Submarine", 3, Color.CHARTREUSE),
            new Ship("Destroyer", 3, Color.YELLOW),
            new Ship("Patrol Boat", 2, Color.ORANGE));
    }

    public static boolean isShipValid(Ship ship) {
        List<Position> positions = ship.getPositions().stream().distinct()
            .sorted((p1, p2) -> {
                int compare = p1.getColumn().compareTo(p2.getColumn());
                if (compare == 0) {
                    return Integer.compare(p1.getRow(), p2.getRow());
                }
                return compare;
            }).collect(Collectors.toList());

        boolean valid = positions.size() == ship.getSize();
        Boolean vertical = null;
        Position curr = null;
        Iterator<Position> iterator = positions.iterator();
        while (iterator.hasNext() && valid) {
            if (curr == null) {
                curr = iterator.next();
            } else {
                Position next = iterator.next();
                if (vertical == null) {
                    vertical = curr.getColumn().ordinal() + 1 == next.getColumn().ordinal();
                }
                if (vertical) {
                    valid =
                        curr.getRow() == next.getRow() && curr.getColumn().ordinal() + 1 == next.getColumn().ordinal();
                } else {
                    valid = curr.getColumn() == next.getColumn() && curr.getRow() + 1 == next.getRow();
                }
                curr = next;
            }
        }
        return valid;
    }

    public static Position getRandomPosition(int size) {
        Random random = new Random();
        Letter letter = Letter.values()[random.nextInt(size)];
        int number = random.nextInt(size);
        Position position = new Position(letter, number);
        return position;
    }
}
