package ru.sbpstu.icc.kspt.Zhuikov.courseWork;
// todo пусть тесты покрывают всю логику

import org.junit.Test;
import ru.spbstu.icc.kspt.zhuikov.quoridor.QuoridorField;
import ru.spbstu.icc.kspt.zhuikov.quoridor.QuoridorPlayer;
import ru.spbstu.icc.kspt.zhuikov.quoridor.exceptions.*;
import ru.spbstu.icc.kspt.zhuikov.quoridor.items.Barrier;
import ru.spbstu.icc.kspt.zhuikov.quoridor.items.BarrierPosition;
import ru.spbstu.icc.kspt.zhuikov.quoridor.items.ItemType;

import static org.junit.Assert.assertEquals;

public class BarrierTest {

    @Test
    public void testBarrierSet() throws FieldItemException, NoBarriersException {

        QuoridorField field = new QuoridorField(9);
        QuoridorPlayer player = QuoridorPlayer.BOTTOM;
        player.createPlayer(field, false);

        player.makeMove(5, 3, BarrierPosition.VERTICAL);

        assertEquals(ItemType.BARRIER, field.getItem(5, 3).getType());
        assertEquals(ItemType.BARRIER, field.getItem(4, 3).getType());
        assertEquals(ItemType.BARRIER, field.getItem(6, 3).getType());

        player.makeMove(13, 11, BarrierPosition.HORIZONTAL);

        assertEquals(ItemType.BARRIER, field.getItem(13, 10).getType());
        assertEquals(ItemType.BARRIER, field.getItem(13, 11).getType());
        assertEquals(ItemType.BARRIER, field.getItem(13, 12).getType());
    }


    @Test(expected = ImpossibleToSetItemException.class)
    public void testBlackCellSet() throws FieldItemException, NoBarriersException {

        QuoridorField field = new QuoridorField(9);
        QuoridorPlayer player = QuoridorPlayer.BOTTOM;
        player.createPlayer(field, false);
        player.makeMove(2, 8, BarrierPosition.HORIZONTAL);
    }

    @Test(expected = ImpossibleToSetItemException.class)
    public void testSetBetweenBlackCells() throws FieldItemException, NoBarriersException {

        QuoridorField field = new QuoridorField(9);
        QuoridorPlayer player = QuoridorPlayer.BOTTOM;
        player.createPlayer(field, false);
        player.makeMove(7, 12, BarrierPosition.VERTICAL);
    }

//    @Test(expected = FieldBoundsException.class)
//    public void testWrongCoordinates() throws FieldItemException, NoBarriersException {
//
//        QuoridorField field = new QuoridorField(9);
//        QuoridorPlayer player = QuoridorPlayer.BOTTOM;
//        player.createPlayer(field, false);
//        player.makeMove(0, 1, BarrierPosition.VERTICAL);
//    }

    @Test(expected = CellIsNotEmptyException.class)
    public void testImpossibleSet() throws FieldItemException, NoBarriersException {

        QuoridorField field = new QuoridorField(9);
        QuoridorPlayer player = QuoridorPlayer.BOTTOM;
        player.createPlayer(field, false);

        player.makeMove(5, 3, BarrierPosition.VERTICAL);
        player.makeMove(3, 3, BarrierPosition.VERTICAL);
    }

    @Test (expected = NoBarriersException.class)
    public void testNoBarriers() throws FieldItemException, NoBarriersException {

        QuoridorField field = new QuoridorField(9);
        QuoridorPlayer player = QuoridorPlayer.TOP;
        player.createPlayer(field, false);

        for (int i = 1; i < 16; i+=2) {
            player.makeMove(1, i, BarrierPosition.VERTICAL); // 8 barriers
        }
        player.makeMove(5, 1, BarrierPosition.VERTICAL);
        player.makeMove(5, 3, BarrierPosition.VERTICAL);

        player.makeMove(5, 5, BarrierPosition.VERTICAL);
    }

    @Test (expected = ImpossibleToSetItemException.class)
    public void testPlayerBlock() throws FieldItemException, NoBarriersException {

        QuoridorField field = new QuoridorField(9);
        QuoridorPlayer bottom = QuoridorPlayer.BOTTOM;
        QuoridorPlayer top = QuoridorPlayer.TOP;
        bottom.createPlayer(field, false);
        top.createPlayer(field, false);

        field.setItem(new Barrier(1, 7, BarrierPosition.VERTICAL));
        field.setItem(new Barrier(1, 9, BarrierPosition.VERTICAL));

        bottom.makeMove(3, 8, BarrierPosition.HORIZONTAL);
    }
}
