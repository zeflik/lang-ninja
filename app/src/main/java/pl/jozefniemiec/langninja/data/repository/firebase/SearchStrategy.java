package pl.jozefniemiec.langninja.data.repository.firebase;

import android.util.SparseArray;

public enum SearchStrategy {
    NEWEST(0),
    POPULAR(1),
    USER(2);

    private int value;
    private static SparseArray<SearchStrategy> map = new SparseArray<>();

    SearchStrategy(int value) {
        this.value = value;
    }

    static {
        for (SearchStrategy searchStrategy : SearchStrategy.values()) {
            map.put(searchStrategy.value, searchStrategy);
        }
    }

    public static SearchStrategy valueOf(int searchStrategy) {
        return map.get(searchStrategy);
    }

    public int getValue() {
        return value;
    }
}
