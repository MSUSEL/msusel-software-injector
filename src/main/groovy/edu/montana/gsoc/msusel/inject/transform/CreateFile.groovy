/**
 * The MIT License (MIT)
 *
 * MSUSEL Software Injector
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.inject.transform

import edu.isu.isuese.datamodel.File
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.cond.FileExists
import groovy.transform.builder.Builder

import java.nio.file.Paths

/**
 * Transform which constructs a new file, including any physical artifacts it requires
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CreateFile extends CreateStructure {

    /**
     * Constructs a new CreateFile transform
     * @param context current InjectorContext
     * @param file the file to be created
     */
    @Builder(buildMethodName = "create")
    private CreateFile(InjectorContext context, File file) {
        super(context, file)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        def treeBuilder = new FileTreeBuilder()
        String base = Paths.get(file.key).parent.toString()
        String f = Paths.get(file.key).fileName.toString()
        StringBuilder content = new StringBuilder()
        content << """\
        /**
         * The MIT License (MIT)
         * 
         * MSUSEL Software Injector
         * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory
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
         """.stripIndent(8)
        if (file.namespace) {
            content << "package ${file.namespace.key};\n\n"
        }

        treeBuilder.dir(base) {
            "$f"(content.toString())
        }

        context.controller.getOps(file)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initializeConditions() {
        conditions << new FileExists(file)
    }
}
