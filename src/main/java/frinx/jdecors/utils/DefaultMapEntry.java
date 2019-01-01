package frinx.jdecors.utils;

public final class DefaultMapEntry<K, V> extends MapEntry<K, V> {

    public DefaultMapEntry(V defaultValue) {
        super(null, defaultValue);
    }
}