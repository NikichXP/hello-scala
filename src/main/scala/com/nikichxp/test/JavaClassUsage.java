package com.nikichxp.test;

import java.util.Objects;

public class JavaClassUsage {

    private int id;
    private final String text;
    private int length;

    public JavaClassUsage(String text) {
        this.text = text;
        this.length = text.length();
        this.id = text.hashCode();
    }

    public JavaClassUsage(ScalaEntity entity) {
        this.text = entity.text();
        this.length = entity.text().length();
        this.id = entity.id();
        entity.id_$eq(entity.text().length());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaClassUsage that = (JavaClassUsage) o;
        return id == that.id &&
                length == that.length &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, length);
    }
}
