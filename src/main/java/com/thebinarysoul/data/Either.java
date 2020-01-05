package com.thebinarysoul.data;

import java.util.NoSuchElementException;

public class Either<Left extends Throwable, Right> {
    private final Left left;
    private final Right right;
    final boolean isLeft;
    final boolean isRight;

    private Either(Left left) {
        this.left = left;
        this.right = null;
        this.isLeft = true;
        this.isRight = false;
    }

    private Either(Right right) {
        this.left = null;
        this.right = right;
        this.isLeft = false;
        this.isRight = true;
    }

    public static <Left extends Throwable, Right> Either<Left, Right> left(Left left) {
        return new Either<>(left);
    }

    public static <Left extends Throwable, Right> Either<Left, Right> right(Right right) {
        return new Either<>(right);
    }

    public Left getLeft() {
        if(isLeft) return left;
        else throw new NoSuchElementException();
    }

    public Right getRight() {
        if(isRight) return right;
        else throw new NoSuchElementException();
    }
}
