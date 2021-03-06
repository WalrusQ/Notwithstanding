package ru.spbstu.icc.kspt.zhuikov.quoridor;

import ru.spbstu.icc.kspt.zhuikov.quoridor.exceptions.FieldItemException;
import ru.spbstu.icc.kspt.zhuikov.quoridor.exceptions.NoBarriersException;
import ru.spbstu.icc.kspt.zhuikov.quoridor.exceptions.NoWinnerException;
import ru.spbstu.icc.kspt.zhuikov.quoridor.items.BarrierPosition;
import ru.spbstu.icc.kspt.zhuikov.quoridor.returningClasses.Field;
import ru.spbstu.icc.kspt.zhuikov.quoridor.returningClasses.Player;
import java.util.ArrayList;
import java.util.List;


//TODO мне не хватает документации к коду
//TODO также хотелось бы, чтобы ядро с логикой было выделенно, если не в отдельный модуль, то хотябы в отдельный пакет(в отдельный относительно UI)

public class Quoridor {

    private QuoridorField field = new QuoridorField(9);
    private List<QuoridorPlayer> players = new ArrayList<QuoridorPlayer>(); //todo мб убрать этот ненужный список
    private Fox fox;

    //TODO возможно есть смысл поменять на enum, с методом nextPlayer();
    private int currentPlayer;
    private int step = 0;
    private static int foxTime = 20;
    private static int foxFrequency = 10;

    public Quoridor(int playersNumber, boolean bot) {

        if (playersNumber == 2) {
            QuoridorPlayer player = QuoridorPlayer.TOP;
            player.createPlayer(field, false);
            currentPlayer = 0;                     //TODO несколько не очевидная строчка, может логичнее  = 1;
            players.add(player);

            player = QuoridorPlayer.BOTTOM;
            player.createPlayer(field, bot);
            players.add(player);

        } else {
            throw new UnsupportedOperationException("пока рано еще думать о чем-то большем...");
        }
    }

    public Player getCurrentPlayer() {

        switch (players.get(currentPlayer)) {
            case TOP:
                return Player.TOP;
            case BOTTOM:
                return Player.BOTTOM;
//            case RIGHT:
//                return Player.RIGHT;
//            default:
//                return Player.LEFT;
        }
        throw new AssertionError("unknown player" + players.get(currentPlayer));
    }

    public static void setFoxTime(int foxTime) {

        if (foxTime < 0) {
            throw new IllegalArgumentException("Fox time must be >= 0");
        }
        Quoridor.foxTime = foxTime;
    }

    public static int getFoxTime() {
        return foxTime;
    }

    public static int getFoxFrequency() {
        return foxFrequency;
    }

    public static void setFoxFrequency(int foxTurn) {

        if (foxTurn < 1 ) {
            throw new IllegalArgumentException("Fox Frequency must be > 0");
        }
        Quoridor.foxFrequency = foxTurn;
    }

    public int getStep() {
        return step;
    }

    public Field getField() {
        return new Field(field);
    }

    //todo по рукам бы надавать наверно надо за такое...
    public Player getPlayerInformation(Player player) {

        switch (player) {
            case TOP:
                player.createPlayer(QuoridorPlayer.TOP.getBarriersNumber());
                return player;
            case BOTTOM:
                player.createPlayer(QuoridorPlayer.BOTTOM.getBarriersNumber());
                return player;

//            case RIGHT:
//                player.createPlayer(QuoridorPlayer.RIGHT.getBarriersNumber());
//                return player;
//            default:
//                player.createPlayer(QuoridorPlayer.LEFT.getBarriersNumber());
//                return player;
        }
        throw new AssertionError("unknown player" + player);
    }

    public boolean isEnd() {              //TODO возможно следует подумать об использование шаблона Наблюдатель

        if (QuoridorPlayer.TOP.getMarker().getCoordinates().getVertical() == QuoridorPlayer.TOP.getDestinationRow()) {
            return true;
        }

        if (QuoridorPlayer.BOTTOM.getMarker().getCoordinates().getVertical() == QuoridorPlayer.BOTTOM.getDestinationRow()) {
            return true;
        }

        if (fox != null && fox.getMarker().getCoordinates().equals(fox.getTarget())) {
            return true;
        }

        // todo тут еще потом других сделать
        return false;
    }

    public Player getWinner() throws NoWinnerException {   //TODO по-моему, при использование Наблюдателя метод атрофируется

        if (isEnd()) {
            if (QuoridorPlayer.TOP.getMarker().getCoordinates().getVertical() == field.getRealSize() - 1) {
                return Player.TOP;
            }
            if (QuoridorPlayer.BOTTOM.getMarker().getCoordinates().getVertical() == 0) {
                return Player.BOTTOM;
            }
            if (fox != null && fox.getMarker().getCoordinates().equals(fox.getTarget())) {
                return Player.FOX;
            }
        }

        // todo вообще ужас! за это точно надо надавать...
        throw new NoWinnerException("There is no winner");
    }

    public void moveMarker(int vertical, int horizontal)
            throws FieldItemException, NoBarriersException {  //TODO странное название у исключения, возможно есть смысл переименовать в Выход за границу поля, но это не точно

        players.get(currentPlayer).makeMove(vertical, horizontal);
        changePlayerTurn();
    }

    public void placeBarrier(int vertical, int horizontal, BarrierPosition position)
            throws FieldItemException, NoBarriersException {

        players.get(currentPlayer).makeMove(vertical, horizontal, position);
        changePlayerTurn();
    }

    public List<Coordinates> getPossibleMoves() {

        return players.get(currentPlayer).getPossibleMoves();
    }

    private void changePlayerTurn() throws FieldItemException, NoBarriersException {

        if (++currentPlayer == players.size()) {
            currentPlayer = 0;
        }

        if (isEnd()) return;

        if (fox != null && step % foxFrequency == 0) {
            fox.makeMove();
            if (isEnd()) return;
        }

        if (step == foxTime) {
            fox = new Fox(field);
        }

        step++;

        if (players.get(currentPlayer).isBot()) {
            players.get(currentPlayer).makeBotMove();
            if (isEnd()) return;
            changePlayerTurn();
        }
    }

}
