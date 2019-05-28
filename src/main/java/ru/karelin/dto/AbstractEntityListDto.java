package ru.karelin.dto;

import java.util.List;

public class  AbstractEntityListDto <T extends AbstractEntity> {
    private List<T> entityList;
}
