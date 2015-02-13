// CHECKSTYLE:OFF
package org.fuin.srcgen4javassist;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SgArgumentTest extends SgVariableTest {

    @BeforeMethod
    public void setup() {
        super.setup();
    }

    @AfterMethod
    public void teardown() {
        super.teardown();
    }

    @Test
    public void testConstruction() {
        final String modifiers = "final";
        final SgClass type = SgClass.INT;
        final String name = "arg";
        final SgMethod method = new SgMethod(getDummyClass(), "public", SgClass.VOID, "setArg");
        final SgArgument arg = new SgArgument(method, modifiers, type, name);
        Assert.assertSame(arg.getOwner(), method);
        Assert.assertSame(arg.getType(), type);
        Assert.assertEquals(arg.getName(), name);
        Assert.assertEquals(arg.getModifiers(), modifiers);
        Assert.assertEquals(arg.getAnnotations().size(), 0);
        Assert.assertEquals(arg.toString(), "final int arg");
    }

}
// CHECKSTYLE:ON
