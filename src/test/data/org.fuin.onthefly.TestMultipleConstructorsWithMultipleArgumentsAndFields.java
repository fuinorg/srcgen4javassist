package org.fuin.onthefly;

public class TestMultipleConstructorsWithMultipleArgumentsAndFields {
    private final int count;
    private final boolean ok;

    public TestMultipleConstructorsWithMultipleArgumentsAndFields(int count,
        boolean ok) {
        super();
        this.count = count;
        this.ok = ok;
    }

    public TestMultipleConstructorsWithMultipleArgumentsAndFields() {
        super();
        this.count = 0;
        this.ok = false;
    }
}
