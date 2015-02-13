package org.fuin.srcgen4javassist;

public class TestMethodAnnotation {
    private int count = 0;

    @org.fuin.srcgen4javassist.XMethodAnnotation
    public int getCount() {
        return count;
    }
}
