package pl.jozefniemiec.langninja.data.repository.firebase.model;

import java.util.HashMap;
import java.util.Map;

public class Likes {

    private int count;
    private Map<String, Boolean> likesMap = new HashMap<>();
    private Map<String, Boolean> dislikesMap = new HashMap<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<String, Boolean> getLikesMap() {
        return likesMap;
    }

    public void setLikesMap(Map<String, Boolean> likesMap) {
        this.likesMap = likesMap;
    }

    public Map<String, Boolean> getDislikesMap() {
        return dislikesMap;
    }

    public void setDislikesMap(Map<String, Boolean> dislikesMap) {
        this.dislikesMap = dislikesMap;
    }

    @Override
    public String toString() {
        return "Likes{" +
                "count=" + count +
                ", likesMap=" + likesMap +
                ", dislikesMap=" + dislikesMap +
                '}';
    }
}
