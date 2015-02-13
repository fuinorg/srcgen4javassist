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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A annotation.
 * 
 * TODO Handle annotation arguments TODO Handle different types of annotations
 * (CLASS, RUNTIME; SOURCE)
 */
public final class SgAnnotation {

    private final String packageName;

    private final String simpleName;

    private final Map<String, Object> arguments;

    /**
     * Constructor with package and name of the class.
     * 
     * @param packageName
     *            Package - Cannot be null (but empty for default package).
     * @param simpleName
     *            Name (without package) - Cannot be null.
     */
    public SgAnnotation(final String packageName, final String simpleName) {
        super();
        if (packageName == null) {
            throw new IllegalArgumentException("The argument 'packageName' cannot be null!");
        }
        this.packageName = packageName.trim();

        if (simpleName == null) {
            throw new IllegalArgumentException("The argument 'simpleName' cannot be null!");
        }
        final String trimmed = simpleName.trim();
        if (trimmed.length() == 0) {
            throw new IllegalArgumentException(
                    "The argument 'simpleName' cannot be an empty string!");
        }
        this.simpleName = trimmed;

        this.arguments = new HashMap<String, Object>();
    }

    /**
     * Returns the name of the annotation.
     * 
     * @return Name including package - Always non-null.
     */
    public final String getName() {
        if (packageName.length() == 0) {
            return simpleName;
        }
        return packageName + "." + simpleName;
    }

    /**
     * Returns the package of the annotation.
     * 
     * @return Package name - Always non-null.
     */
    public final String getPackageName() {
        return packageName;
    }

    /**
     * Returns the name of the annotation.
     * 
     * @return Name (without package) - Always non-null.
     */
    public final String getSimpleName() {
        return simpleName;
    }

    /**
     * Returns the annotations arguments.
     * 
     * @return Arguments - Always non-null, maybe empty and is unmodifiable.
     */
    public final Map<String, Object> getArguments() {
        return Collections.unmodifiableMap(arguments);
    }

    /**
     * Adds an argument.
     * 
     * @param name
     *            Name of the argument - Cannot be null.
     * @param value
     *            Value of the argument - Cannot be null.
     */
    public final void addArgument(final String name, final Object value) {
        if (name == null) {
            throw new IllegalArgumentException("The argument 'name' cannot be null!");
        }
        if (value == null) {
            throw new IllegalArgumentException("The argument 'value' cannot be null!");
        }
        arguments.put(name.trim(), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("@");
        sb.append(getName());
        if (arguments.size() > 0) {
            sb.append("(");
            if (arguments.size() == 1) {
                final Iterator<String> it = arguments.keySet().iterator();
                while (it.hasNext()) {
                    final String name = it.next();
                    final Object value = arguments.get(name);
                    if (!name.equals("value")) {
                        sb.append(name);
                        sb.append("=");
                    }
                    sb.append(value);
                }
            } else {
                int count = 0;
                final Iterator<String> it = arguments.keySet().iterator();
                while (it.hasNext()) {
                    final String name = it.next();
                    final Object value = arguments.get(name);
                    if (count > 0) {
                        sb.append(", ");
                    }
                    sb.append(name);
                    sb.append("=");
                    sb.append(value);
                    count++;
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

}
