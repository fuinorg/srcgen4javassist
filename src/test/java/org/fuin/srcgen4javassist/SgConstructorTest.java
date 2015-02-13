// CHECKSTYLE:OFF
package org.fuin.srcgen4javassist;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SgConstructorTest extends SgBehaviorTest {

    private SgClass clasz;

    private SgConstructor testee;

    @BeforeMethod
    public void setup() {
        clasz = new SgClass("org.fuin.onthefly", "DummyClass");
        testee = new SgConstructor(clasz);
    }

    @AfterMethod
    public void teardown() {
        testee = null;
        clasz = null;
    }

    @Override
    protected SgBehavior getTestee() {
        return testee;
    }

    @Test
    public void testConctructionSgClass() {
        Assert.assertSame(testee.getOwner(), clasz);
        Assert.assertEquals(testee.getArguments().size(), 0);
        Assert.assertEquals(testee.getExceptions().size(), 0);
        Assert.assertEquals(testee.getBody().size(), 0);
        Assert.assertEquals(testee.getModifiers(), "public");
        Assert.assertEquals(testee.getCommaSeparatedArgumentNames(), "");
        Assert.assertEquals(testee.getSignature(), "public DummyClass()");
    }

    @Test
    public void testGetSignature() {
        final SgConstructor constructor = new SgConstructor(clasz, "public");
        new SgArgument(constructor, SgClass.INT, "count");
        Assert.assertEquals(constructor.getSignature(), "public DummyClass(int count)");
        new SgArgument(constructor, SgClass.BOOLEAN, "ok");
        Assert.assertEquals(constructor.getSignature(),
                "public DummyClass(int count, boolean ok)");
        final SgClassPool pool = new SgClassPool();
        constructor.addException(SgClass.create(pool, IOException.class));
        Assert.assertEquals(constructor.getSignature(),
                "public DummyClass(int count, boolean ok) throws java.io.IOException");
        constructor.addException(SgClass.create(pool, IllegalArgumentException.class));
        Assert.assertEquals(constructor.getSignature(),
                "public DummyClass(int count, boolean ok) throws java.io.IOException,"
                        + "java.lang.IllegalArgumentException");
    }

    @Test
    public void testConctructionSgClassString() {
        final SgConstructor constructor = new SgConstructor(clasz, "private");
        Assert.assertEquals(constructor.getArguments().size(), 0);
        Assert.assertEquals(constructor.getExceptions().size(), 0);
        Assert.assertEquals(constructor.getBody().size(), 0);
        Assert.assertEquals(constructor.getModifiers(), "private");
        Assert.assertEquals(constructor.getCommaSeparatedArgumentNames(), "");
        Assert.assertEquals(constructor.getSignature(), "private DummyClass()");

        new SgArgument(constructor, "final", SgClass.INT, "count");
        Assert.assertEquals(constructor.getSignature(), "private DummyClass(final int count)");

        new SgArgument(constructor, "final", SgClass.BOOLEAN, "ok");
        Assert.assertEquals(constructor.getSignature(),
                "private DummyClass(final int count, final boolean ok)");

    }

}
// CHECKSTYLE:ON
