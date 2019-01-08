package sk.utils;

import java.util.Map;
import java.util.Objects;

public class MapEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    public Boolean isNullKey() {
        return Objects.isNull(key);
    }
}