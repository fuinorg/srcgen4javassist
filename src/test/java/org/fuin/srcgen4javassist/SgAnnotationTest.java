// CHECKSTYLE:OFF
package org.fuin.srcgen4javassist;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SgAnnotationTest {

    @Test
    public void testConstruction() {

        final String packageName = "org.fuin.onthefly";
        final String simpleName = "MyAnnotation";
        final SgAnnotation annotation = new SgAnnotation(packageName, simpleName);
        Assert.assertEquals(annotation.getPackageName(), packageName);
        Assert.assertEquals(annotation.getSimpleName(), simpleName);
        Assert.assertNotNull(annotation.getArguments());
        Assert.assertEquals(annotation.getArguments().size(), 0);

        annotation.addArgument("count", SgClass.INT);
        Assert.assertEquals(annotation.getArguments().size(), 1);

        try {
            annotation.getArguments().put("dummy", SgClass.BOOLEAN);
            Assert.fail("The map is excepected to be unmodifiable!");
        } catch (final UnsupportedOperationException ex) {
            // OK
        }

    }

}
// CHECKSTYLE:ON
