package ru.spbstu.icc.kspt.zhuikov.quoridor.items;


import ru.spbstu.icc.kspt.zhuikov.quoridor.Coordinates;

import java.util.ArrayList;
import java.util.List;

abstract public class ManyCellsItem extends Item {

    protected List<Coordinates> coordinates = new ArrayList<Coordinates>();

    public ManyCellsItem() { }

    public List<Coordinates> getCoordinates() {
        return coordinates;
    } //todo может return new ArrayList<Coordinates>.addAll(coordinates);

}