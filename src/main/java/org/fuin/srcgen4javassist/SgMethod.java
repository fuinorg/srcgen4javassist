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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A method.
 */
public final class SgMethod extends SgBehavior {

    private final SgClass returnType;

    private final String name;

    private final List<String> body;

    /**
     * Constructor. The method will automatically be added to the
     * <code>owner</code>.
     * 
     * @param owner
     *            Class the behavior belongs to - Cannot be null.
     * @param modifiers
     *            Modifiers for the constructor/method - Cannot be null (but
     *            empty).
     * @param returnType
     *            Return type of the method - Cannot be null (Use VOID in model
     *            class for no return value).
     * @param name
     *            Name of the method.
     */
    public SgMethod(final SgClass owner, final String modifiers, final SgClass returnType,
            final String name) {
        super(owner, modifiers);
        if (returnType == null) {
            throw new IllegalArgumentException("The argument 'returnType' cannot be NULL!");
        }
        this.returnType = returnType;

        if (name == null) {
            throw new IllegalArgumentException("The argument 'name' cannot be NULL!");
        }
        this.name = name;

        body = new ArrayList<String>();

        // TODO Check if the class not already contains a method with the same
        // name and arguments!

        owner.addMethod(this);

    }

    /**
     * Return the return type of the method.
     * 
     * @return Type - Always non-null.
     */
    public final SgClass getReturnType() {
        return returnType;
    }

    /**
     * Returns the body of the method.
     * 
     * @return Body - Always non-null, maybe empty and is unmodifiable.
     */
    public final List<String> getBody() {
        return Collections.unmodifiableList(body);
    }

    /**
     * Add a new line to the body.
     * 
     * @param line
     *            Line to add - Cannot be null (but empty).
     */
    public final void addBodyLine(final String line) {
        if (line == null) {
            throw new IllegalArgumentException("The argument 'line' cannot be NULL!");
        }
        body.add(line.trim());
    }

    /**
     * Returns the name of the method.
     * 
     * @return Name - Always non-null.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the name of the method with an "underscore" inserted before all
     * upper case characters and all characters converted to lower case.
     * 
     * @return Name usable as a package - Always non-null.
     */
    public final String getNameAsPackage() {
        return SgUtils.uppercaseToUnderscore(getName());
    }

    /**
     * Returns the "signature" of the method.
     * 
     * @return Modifiers, return type, name and arguments - Always non-null.
     */
    public final String getSignature() {
        final StringBuffer sb = new StringBuffer();
        if (getModifiers().length() > 0) {
            sb.append(getModifiers());
            sb.append(" ");
        }
        sb.append(returnType.getName());
        sb.append(" ");
        sb.append(getName());
        sb.append("(");
        for (int i = 0; i < getArguments().size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(getArguments().get(i));
        }
        sb.append(")");
        if (getExceptions().size() > 0) {
            sb.append(" throws ");
            for (int i = 0; i < getExceptions().size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(getExceptions().get(i).getName());
            }
        }
        return sb.toString();
    }

    /**
     * Returns the "call" signature of the method.
     * 
     * @return Method name and argument names (like "methodXY(a, b, c)").
     */
    public final String getCallSignature() {
        final StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append("(");
        for (int i = 0; i < getArguments().size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            final SgArgument arg = getArguments().get(i);
            sb.append(arg.getName());
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Returns the "type" signature of the method.
     * 
     * @return Method name and argument types (like
     *         "methodXY(String, int, boolean)").
     */
    public final String getTypeSignature() {
        final StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append("(");
        for (int i = 0; i < getArguments().size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            final SgArgument arg = getArguments().get(i);
            sb.append(arg.getType().getSimpleName());
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Returns the name of the method (first character upper case) and the
     * argument types divided by by an underscore.
     * 
     * @return Method name and argument types (like
     *         "MethodXY_String_int_boolean").
     */
    public final String getUnderscoredNameAndTypes() {
        final StringBuffer sb = new StringBuffer();
        sb.append(SgUtils.firstCharUpper(getName()));
        sb.append("_");
        for (int i = 0; i < getArguments().size(); i++) {
            if (i > 0) {
                sb.append("_");
            }
            final SgArgument arg = getArguments().get(i);
            final String typeName = arg.getType().getSimpleName();
            sb.append(SgUtils.replace(typeName, "[]", "ARRAY", -1));
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return toString(true);
    }

    /**
     * Creates the method's source with or without annotations.
     * 
     * @param showAnnotations
     *            To include annotations <code>true</code> else
     *            <code>true</code>.
     * 
     * @return Source code of the method.
     */
    public final String toString(final boolean showAnnotations) {
        final StringBuffer sb = new StringBuffer();
        if (showAnnotations && (getAnnotations().size() > 0)) {
            for (int i = 0; i < getAnnotations().size(); i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(getAnnotations().get(i));
            }
            sb.append("\n");
        }
        sb.append(getSignature());
        if (getOwner().isInterface()) {
            sb.append(";");
        } else {
            sb.append("{\n");
            if (body.size() == 0) {
                sb.append("// No method source available\n");
            } else {
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.get(i));
                    sb.append("\n");
                }
            }
            sb.append("}\n");
        }
        return sb.toString();
    }

}
