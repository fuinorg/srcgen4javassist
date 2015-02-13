/**
 * Copyright (C) 2009 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.srcgen4javassist;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Some helper for the package.
 */
public final class SgUtils {

    private static final String CLASS_ABSTRACT_AND_FINAL_ERROR = "Classes cannot be declared "
            + "abstract and final simultaneously!";

    private static final String METHOD_ACCESS_MODIFIER_ERROR = "A method declaration can contain "
            + "only one of the access modifiers public, package, protected and private!";

    private static final String METHOD_ILLEGAL_ABSTRACT_MODIFIERS_ERROR = "Abstract methods cannot "
            + "be declared private, static, final, native, strictfp or synchronized!";

    private static final String METHOD_NATIVE_STRICTFP_ERROR = "Methods cannot be declared native "
            + "and strictfp simultaneously!";

    private static final String FIELD_FINAL_VOLATILE_ERROR = "Final fields cannot be volatile!";

    // Types
    private static final int FIELD = 0;
    private static final int METHOD = 1;
    private static final int CONSTRUCTOR = 2;
    private static final int OUTER_CLASS = 3;
    private static final int INNER_CLASS = 4;
    private static final int OUTER_INTERFACE = 5;
    private static final int INNER_INTERFACE = 6;

    private static final String[] TYPE_NAMES = new String[] { "Field", "Method", "Constructor",
            "Outer Class", "Inner Class", "Outer Interface", "Inner Interface" };

    // Modifiers
    private static final int ABSTRACT = 0;
    private static final int FINAL = 1;
    private static final int NATIVE = 2;
    private static final int PRIVATE = 3;
    private static final int PROTECTED = 4;
    private static final int PUBLIC = 5;
    private static final int STATIC = 6;
    private static final int SYNCHRONIZED = 7;
    private static final int TRANSIENT = 8;
    private static final int VOLATILE = 9;
    private static final int STRICTFP = 10;

    private static final String[] MODIFIER_NAMES = new String[] { "abstract", "final", "native",
            "private", "protected", "public", "static", "synchronized", "transient", "volatile",
            "strictfp" };

    private static final int[] MODIFIER_VALUES = new int[] { Modifier.ABSTRACT, Modifier.FINAL,
            Modifier.NATIVE, Modifier.PRIVATE, Modifier.PROTECTED, Modifier.PUBLIC,
            Modifier.STATIC, Modifier.SYNCHRONIZED, Modifier.TRANSIENT, Modifier.VOLATILE,
            Modifier.STRICT };

    private static final boolean[][] MODIFIERS_MATRIX = new boolean[][] {
            { false, true, false, true, true, true, true },
            { true, true, false, true, true, false, false },
            { false, true, false, false, false, false, false },
            { true, true, true, false, true, false, true },
            { true, true, true, false, true, false, true },
            { true, true, true, true, true, true, true },
            { true, true, false, false, true, false, true },
            { false, true, false, false, false, false, false },
            { true, false, false, false, false, false, false },
            { true, false, false, false, false, false, false },
            { false, true, false, true, true, true, true } };

    private SgUtils() {
        throw new UnsupportedOperationException(
                "It's not allowed to create an instance of this class!");
    }

    private static void throwIllegalArgument(final int type, final int modifier) {
        throw new IllegalArgumentException("The modifier '" + MODIFIER_NAMES[modifier]
                + "' is not allowed for '" + TYPE_NAMES[type] + "'!");
    }

    // CHECKSTYLE:OFF Cyclomatic complexity is OK here
    private static void checkModifiers(final int type, final int modifiers) {
        for (int modifier = ABSTRACT; modifier <= STRICTFP; modifier++) {
            if (Modifier.isPrivate(modifiers) && !MODIFIERS_MATRIX[PRIVATE][type]) {
                throwIllegalArgument(type, PRIVATE);
            }
            if (Modifier.isProtected(modifiers) && !MODIFIERS_MATRIX[PROTECTED][type]) {
                throwIllegalArgument(type, PROTECTED);
            }
            if (Modifier.isPublic(modifiers) && !MODIFIERS_MATRIX[PUBLIC][type]) {
                throwIllegalArgument(type, PUBLIC);
            }
            if (Modifier.isStatic(modifiers) && !MODIFIERS_MATRIX[STATIC][type]) {
                throwIllegalArgument(type, STATIC);
            }
            if (Modifier.isAbstract(modifiers) && !MODIFIERS_MATRIX[ABSTRACT][type]) {
                throwIllegalArgument(type, ABSTRACT);
            }
            if (Modifier.isFinal(modifiers) && !MODIFIERS_MATRIX[FINAL][type]) {
                throwIllegalArgument(type, FINAL);
            }
            if (Modifier.isNative(modifiers) && !MODIFIERS_MATRIX[NATIVE][type]) {
                throwIllegalArgument(type, NATIVE);
            }
            if (Modifier.isSynchronized(modifiers) && !MODIFIERS_MATRIX[SYNCHRONIZED][type]) {
                throwIllegalArgument(type, SYNCHRONIZED);
            }
            if (Modifier.isTransient(modifiers) && !MODIFIERS_MATRIX[TRANSIENT][type]) {
                throwIllegalArgument(type, TRANSIENT);
            }
            if (Modifier.isVolatile(modifiers) && !MODIFIERS_MATRIX[VOLATILE][type]) {
                throwIllegalArgument(type, VOLATILE);
            }
            if (Modifier.isStrict(modifiers) && !MODIFIERS_MATRIX[STRICTFP][type]) {
                throwIllegalArgument(type, STRICTFP);
            }
        }
    }

    // CHECKSTYLE:ON

    /**
     * Checks if the modifiers are valid for a class. If any of the modifiers is
     * not valid an <code>IllegalArgumentException</code> is thrown.
     * 
     * @param modifiers
     *            Modifiers.
     * @param isInterface
     *            Are the modifiers from an interface?
     * @param isInnerClass
     *            Is it an inner class?
     */
    public static void checkClassModifiers(final int modifiers, final boolean isInterface,
            final boolean isInnerClass) {

        // Basic checks
        final int type;
        if (isInterface) {
            if (isInnerClass) {
                type = INNER_INTERFACE;
            } else {
                type = OUTER_INTERFACE;
            }
        } else {
            if (isInnerClass) {
                type = INNER_CLASS;
            } else {
                type = OUTER_CLASS;
            }
        }
        checkModifiers(type, modifiers);

        // Abstract and final check
        if (Modifier.isAbstract(modifiers) && Modifier.isFinal(modifiers)) {
            throw new IllegalArgumentException(CLASS_ABSTRACT_AND_FINAL_ERROR + " ["
                    + Modifier.toString(modifiers) + "]");
        }

    }

    /**
     * Checks if the modifiers are valid for a field. If any of the modifiers is
     * not valid an <code>IllegalArgumentException</code> is thrown.
     * 
     * @param modifiers
     *            Modifiers.
     */
    public static void checkFieldModifiers(final int modifiers) {

        // Basic checks
        checkModifiers(FIELD, modifiers);

        // Check final and volatile
        if (Modifier.isFinal(modifiers) && Modifier.isVolatile(modifiers)) {
            throw new IllegalArgumentException(FIELD_FINAL_VOLATILE_ERROR + " ["
                    + Modifier.toString(modifiers) + "]");
        }

    }

    /**
     * Checks if the modifiers are valid for a method. If any of the modifiers
     * is not valid an <code>IllegalArgumentException</code> is thrown.
     * 
     * @param modifiers
     *            Modifiers.
     */
    // CHECKSTYLE:OFF Cyclomatic complexity is OK
    public static void checkMethodModifiers(final int modifiers) {

        // Base check
        checkModifiers(METHOD, modifiers);

        // Check overlapping modifiers
        if (Modifier.isPrivate(modifiers)) {
            if (Modifier.isProtected(modifiers) || Modifier.isPublic(modifiers)) {
                throw new IllegalArgumentException(METHOD_ACCESS_MODIFIER_ERROR + " ["
                        + Modifier.toString(modifiers) + "]");
            }
        }
        if (Modifier.isProtected(modifiers)) {
            if (Modifier.isPrivate(modifiers) || Modifier.isPublic(modifiers)) {
                throw new IllegalArgumentException(METHOD_ACCESS_MODIFIER_ERROR + " ["
                        + Modifier.toString(modifiers) + "]");
            }
        }
        if (Modifier.isPublic(modifiers)) {
            if (Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers)) {
                throw new IllegalArgumentException(METHOD_ACCESS_MODIFIER_ERROR + " ["
                        + Modifier.toString(modifiers) + "]");
            }
        }

        // Check illegal abstract modifiers
        if (Modifier.isAbstract(modifiers)) {
            if (Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers)
                    || Modifier.isFinal(modifiers) || Modifier.isNative(modifiers)
                    || Modifier.isStrict(modifiers) || Modifier.isSynchronized(modifiers)) {
                throw new IllegalArgumentException(METHOD_ILLEGAL_ABSTRACT_MODIFIERS_ERROR + " ["
                        + Modifier.toString(modifiers) + "]");
            }
        }

        // Check native and strictfp
        if (Modifier.isNative(modifiers) && Modifier.isStrict(modifiers)) {
            throw new IllegalArgumentException(METHOD_NATIVE_STRICTFP_ERROR + " ["
                    + Modifier.toString(modifiers) + "]");
        }

    }

    // CHECKSTYLE:ON

    /**
     * Checks if the modifiers are valid for a constructor. If any of the
     * modifiers is not valid an <code>IllegalArgumentException</code> is
     * thrown.
     * 
     * @param modifiers
     *            Modifiers.
     */
    public static void checkConstructorModifiers(final int modifiers) {
        checkModifiers(CONSTRUCTOR, modifiers);
    }

    /**
     * Inserts an underscore before every upper case character and returns an
     * all lower case string. If the first character is upper case an underscore
     * will not be inserted.
     * 
     * @param str
     *            String to convert.
     * 
     * @return Lower case + underscored text.
     */
    public static String uppercaseToUnderscore(final String str) {
        if (str == null) {
            return null;
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            final char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0) {
                    sb.append("_");
                }
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Converts the first character into upper case.
     * 
     * @param str
     *            String to convert - Can be null or empty string (In both cases
     *            the unchanged value will be returned).
     * 
     * @return Same string but first character upper case-
     */
    public static String firstCharUpper(final String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return "" + Character.toUpperCase(str.charAt(0));
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Merge two packages into one. If any package is null or empty no "." will
     * be added. If both packages are null an empty string will be returned.
     * 
     * @param package1
     *            First package - Can also be null or empty.
     * @param package2
     *            Second package - Can also be null or empty.
     * 
     * @return Both packages added with ".".
     */
    public static String concatPackages(final String package1, final String package2) {
        if ((package1 == null) || (package1.length() == 0)) {
            if ((package2 == null) || (package2.length() == 0)) {
                return "";
            } else {
                return package2;
            }
        } else {
            if ((package2 == null) || (package2.length() == 0)) {
                return package1;
            } else {
                return package1 + "." + package2;
            }
        }
    }

    /**
     * Creates an <code>toString()</code> method with all fields.
     * 
     * @param pool
     *            Pool to use.
     * @param clasz
     *            Class to add the new method to.
     * @param fields
     *            List of fields to output.
     */
    public static void addToStringMethod(final SgClassPool pool, final SgClass clasz,
            final List<SgField> fields) {
        final SgMethod m = new SgMethod(clasz, "public", SgClass.create(pool, String.class),
                "toString");
        m.addBodyLine("return getClass().getSimpleName() + \"{\"");
        for (int i = 0; i < fields.size(); i++) {
            final SgField field = fields.get(i);
            final String nameValue = " + \"" + field.getName() + "=\" + " + field.getName();
            if (i < fields.size() - 1) {
                m.addBodyLine(nameValue + " + \", \"");
            } else {
                m.addBodyLine(nameValue);
            }
        }
        m.addBodyLine(" + \"}\";");
        clasz.addMethod(m);
    }

    /**
     * Create a simple HTML table for the modifier matrix. This is helpful to
     * check if the matrix is valid.
     * 
     * @return Modifier matrix HTML table.
     */
    public static String modifierMatrixToHtml() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"1\">\n");

        // Header
        sb.append("<tr>");
        sb.append("<th>&nbsp;</th>");
        for (int type = FIELD; type <= INNER_INTERFACE; type++) {
            sb.append("<th>");
            sb.append(TYPE_NAMES[type]);
            sb.append("</th>");
        }
        sb.append("</tr>\n");

        // Content
        for (int modifier = ABSTRACT; modifier <= STRICTFP; modifier++) {
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(MODIFIER_NAMES[modifier]);
            sb.append("</td>");
            for (int type = FIELD; type <= INNER_INTERFACE; type++) {
                sb.append("<td>");
                sb.append(MODIFIERS_MATRIX[modifier][type]);
                sb.append("</td>");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    private static int modifierValueForName(final String name) {
        for (int i = 0; i < MODIFIER_NAMES.length; i++) {
            if (name.equals(MODIFIER_NAMES[i])) {
                return MODIFIER_VALUES[i];
            }
        }
        throw new IllegalArgumentException("Unknown modifier '" + name + "'!");
    }

    /**
     * Returns a Java "Modifier" value for a list of modifier names.
     * 
     * @param modifiers
     *            Modifier names separated by spaces.
     * 
     * @return Modifiers.
     */
    public static int toModifiers(final String modifiers) {
        if (modifiers == null) {
            return 0;
        }
        final String trimmedModifiers = modifiers.trim();
        int modifier = 0;
        final StringTokenizer tok = new StringTokenizer(trimmedModifiers, " ");
        while (tok.hasMoreTokens()) {
            final String mod = tok.nextToken();
            modifier = modifier | modifierValueForName(mod);
        }
        return modifier;
    }

    /**
     * <p>
     * Replaces a String with another String inside a larger String, for the
     * first <code>max</code> values of the search String.
     * </p>
     * 
     * <p>
     * A <code>null</code> reference passed to this method is a no-op.
     * </p>
     * 
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     * 
     * @param text
     *            text to search and replace in, may be null
     * @param searchString
     *            the String to search for, may be null
     * @param replacement
     *            the String to replace it with, may be null
     * @param max
     *            maximum number of values to replace, or <code>-1</code> if no
     *            maximum
     * @return the text with any replacements processed, <code>null</code> if
     *         null String input
     * 
     * @author See org.apache.commons.lang.StringUtils
     */
    public static String replace(final String text, final String searchString,
            final String replacement, final int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int maxx = max;
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (maxx < 0 ? 16 : (maxx > 64 ? 64 : maxx));
        final StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--maxx == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     * 
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * 
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * String. That functionality is available in isBlank().
     * </p>
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     * 
     * @author See org.apache.commons.lang.StringUtils
     */
    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Returns the "type" signature of the method.
     * 
     * @param methodName
     *            Name of the method.
     * @param paramTypes
     *            Argument types.
     * 
     * @return Method name and argument types (like
     *         "methodXY(String, int, boolean)").
     */
    public static String createTypeSignature(final String methodName, final Class<?>[] paramTypes) {
        final StringBuffer sb = new StringBuffer();
        sb.append(methodName);
        sb.append("(");
        for (int i = 0; i < paramTypes.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(paramTypes[i].getSimpleName());
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Create a list of annotations.
     * 
     * @param ann
     *            Java annotation array.
     * 
     * @return List of annotations.
     */
    public static List<SgAnnotation> createAnnotations(final Annotation[] ann) {
        final List<SgAnnotation> list = new ArrayList<SgAnnotation>();
        if ((ann != null) && (ann.length > 0)) {
            for (int i = 0; i < ann.length; i++) {
                final SgAnnotation annotation = new SgAnnotation(ann[i].annotationType()
                        .getPackage().getName(), ann[i].annotationType().getSimpleName());
                // TODO Handle annotation arguments
                list.add(annotation);
            }
        }
        return list;
    }

}
