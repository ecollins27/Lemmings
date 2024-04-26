package lemmings.ui;

public interface UIAction<T> {

    void perform(T object);
}
