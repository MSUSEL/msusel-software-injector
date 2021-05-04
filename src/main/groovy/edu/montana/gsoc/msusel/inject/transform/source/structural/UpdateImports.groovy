/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.montana.gsoc.msusel.inject.transform.source.structural

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.File
import edu.montana.gsoc.msusel.inject.transform.source.BasicSourceTransform
import groovy.transform.builder.Builder

class UpdateImports extends BasicSourceTransform {

    int delta = 0

    /**
     * Constructs a new AddImport transform
     * @param file The file to be modified
     * @param imp The import to be added
     */
    @Builder(buildMethodName = "create")
    UpdateImports(File file) {
        super(file)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setup() {
        ops = new java.io.File(file.fullPath)
        lines = ops.readLines()
        start = findImportInsertionPoint(ops)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildContent() {
        List<String> imps = Lists.newArrayList()
        file.getImports().each {
            String str = "import ${it.name};"
            str = str.split(/ /)[1]
            str = str.substring(0, str.indexOf(";"))
            if (!imps.contains(str))
                imps << str
        }

        text = imps.join("\n")
        lines.add(start, text)

        println "\nImports Written:"
        println text
        println ""

        delta = imps.size() - (end - start)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void injectContent() {
        for (int i = start; i <= end; i++)
            lines.remove(i)
        lines.add(start, text)

        ops.text = lines.join("\n")
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void updateModel() {
        updateAllFollowing(start + 1, delta + 1)
    }

    /**
     * Selects the line into which the import will be injected
     * @return Line at which the import should be injected
     */
    int findImportInsertionPoint(java.io.File actual) {
        int line = 0

        start = -1
        end = -1
        actual.readLines().eachWithIndex { String str, int ndx ->
            if (str.startsWith("import ") && start == -1) {
                start = ndx
                end = ndx
            } else if (str.startsWith("import ") && start >= 0) {
                end = ndx
            }
        }
        line = start

        if (start == -1 || file.getImports().isEmpty()) {
            actual.readLines().eachWithIndex { String str, int ndx ->
                if (str.startsWith("package ")) {
                    start = end = line = ndx + 1
                }
            }
        }

        if (start == -1)
            start = line = 0

        println(file.getRelPath())
        for (int i = start; i <= end; i++)
            println actual.text.split("\n")[i]
        println()

        line
    }
}