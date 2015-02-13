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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import org.testng.Assert;

import de.hunsicker.jalopy.Jalopy;

/**
 * Some assertion helper methods.
 */
public final class SgAssert {

    /**
     * Private constructor.
     */
    private SgAssert() {
        throw new UnsupportedOperationException(
                "It's not allowed to create an instance of this class!");
    }

    private static void writeToFile(final File file, final String src) {
        try {
            // Write to file
            final FileWriter writer = new FileWriter(file);
            try {
                writer.write(src);
            } finally {
                writer.close();
            }
            // Format with Jalopy
            final Jalopy jalopy = new Jalopy();
            jalopy.setInput(file);
            jalopy.setOutput(file);
            jalopy.format();

        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String readFromFile(final File file) {
        try {
            final LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            try {
                final StringBuffer sb = new StringBuffer();
                String line;
                while ((line = lnr.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                return sb.toString();
            } finally {
                lnr.close();
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Assert the content of the two java source files is equal.
     * 
     * @param actualFile
     *            Actual file.
     * @param expectedFile
     *            Excpected file.
     */
    public static void assertSrcFilesEqual(final File actualFile, final File expectedFile) {
        final String actualSrc = readFromFile(actualFile);
        final String expectedSrc = readFromFile(expectedFile);
        Assert.assertEquals(actualSrc, expectedSrc);
    }

    /**
     * Assert the content of <code>clasz.toString()</code> is equal to the
     * content stored in a file. The name of the expected file is the full
     * qualified class name with the extension ".java". The name of the actual
     * file is the full qualified class name with the extension ".tmp"
     * 
     * @param baseDir
     *            Base directory where the expected source files are stored.
     * @param clasz
     *            Class to test.
     */
    public static void assertEqualToFile(final File baseDir, final SgClass clasz) {
        final File expectedFile = new File(baseDir, clasz.getName() + ".java");
        final File actualFile = new File(baseDir, clasz.getName() + ".tmp");
        writeToFile(actualFile, clasz.toString());
        assertSrcFilesEqual(actualFile, expectedFile);
    }

}
