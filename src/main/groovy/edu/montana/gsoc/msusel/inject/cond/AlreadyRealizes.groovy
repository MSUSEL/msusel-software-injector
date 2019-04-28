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
package edu.montana.gsoc.msusel.inject.cond

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectorContext

/**
 * A condition used to check if a class already realizes some interface
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AlreadyRealizes extends TypeHeaderCondition {

    /**
     * Potential interface the type will realize
     */
    private final Type real

    /**
     * Construts a new AlreadyRealizes condition
     * @param context current InjectorContext
     * @param file The file containing the type
     * @param node The type in question
     * @param real The interface to realize
     */
    AlreadyRealizes(InjectorContext context, File file, Type node, Type real) {
        super(context, file, node)
        this.real = real
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean check() {
        String header = getTypeHeader()
        if (!(real instanceof Interface))
            return false
        else if (header.contains("implements")) {
            String impl = header.trim().split("implements")[1]
            impl = impl.replaceAll(/\w/, "")
            impl = impl.replaceAll(/\{/, "")
            List<String> impls = Arrays.asList(impl.split(","))
            return impls.contains(real.name())
        }
    }
}
