package util;

import java.util.*;

public class DataWrapper<T> {
    private T data;

    public DataWrapper(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataWrapper{" + "data=" + data + '}';
    }
}
