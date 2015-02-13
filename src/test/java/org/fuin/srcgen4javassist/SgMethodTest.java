// CHECKSTYLE:OFF
package org.fuin.srcgen4javassist;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SgMethodTest extends SgBehaviorTest {

    private SgClass clasz;

    private SgMethod testee;

    @BeforeMethod
    public void setup() {
        clasz = new SgClass("org.fuin.onthefly", "DummyClass");
        testee = new SgMethod(clasz, "public", SgClass.INT, "getCount");
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
    public void testConstruction() {
        Assert.assertSame(testee.getOwner(), clasz);
        Assert.assertEquals(testee.getModifiers(), "public");
        Assert.assertEquals(testee.getArguments().size(), 0);
        Assert.assertEquals(testee.getExceptions().size(), 0);
        Assert.assertEquals(testee.getReturnType(), SgClass.INT);
        Assert.assertEquals(testee.getName(), "getCount");
        Assert.assertEquals(testee.getBody().size(), 0);
        Assert.assertEquals(testee.getAnnotations().size(), 0);
        Assert.assertEquals(testee.getSignature(), "public int getCount()");
    }

    @Test
    public void testGetSignature() {
        final SgMethod method = new SgMethod(clasz, "public", SgClass.VOID, "setCount");
        new SgArgument(method, SgClass.INT, "count");
        Assert.assertEquals(method.getSignature(), "public void setCount(int count)");
        new SgArgument(method, SgClass.BOOLEAN, "ok");
        Assert.assertEquals(method.getSignature(),
                "public void setCount(int count, boolean ok)");
        final SgClassPool pool = new SgClassPool();
        method.addException(SgClass.create(pool, IOException.class));
        Assert.assertEquals(method.getSignature(),
                "public void setCount(int count, boolean ok) throws java.io.IOException");
        method.addException(SgClass.create(pool, IllegalArgumentException.class));
        Assert.assertEquals(method.getSignature(),
                "public void setCount(int count, boolean ok) "
                        + "throws java.io.IOException,java.lang.IllegalArgumentException");
    }

    @Test
    public void testAddBodyLine() {
        final SgMethod method = new SgMethod(clasz, "public", SgClass.VOID, "setCount");
        new SgArgument(method, SgClass.INT, "count");
        final String line = "this.count = count;";
        Assert.assertEquals(method.getBody().size(), 0);
        method.addBodyLine(line);
        Assert.assertEquals(method.getBody().size(), 1);
        Assert.assertEquals(method.getBody().get(0), line);
    }

}
// CHECKSTYLE:ON
