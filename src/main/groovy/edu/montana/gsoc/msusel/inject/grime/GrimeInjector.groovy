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
package edu.montana.gsoc.msusel.inject.grime

import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.InjectorContext
import edu.montana.gsoc.msusel.inject.SourceInjector
import edu.montana.gsoc.msusel.inject.transform.SourceTransform

/**
 * Base class for Design Pattern Grime injectors
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class GrimeInjector implements SourceInjector {

    /**
     * The pattern into which the grime will be injected
     */
    protected Pattern pattern

    /**
     * Constructs a new GrimeInjector for the provided pattern instance
     * @param pattern Pattern instance into which grime will be injected
     */
    GrimeInjector(Pattern pattern) {
        this.pattern = pattern
        this.rbml = rbml
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void inject(InjectorContext context) {
        // 2. Create transforms
        List<SourceTransform> transforms = createTransforms(context)

        // 3. add transforms to invoker
        invoker.submitAll(transforms)
    }

    /**
     * Selects a type from the pattern instance, as the focus for the injection event
     * @return Type into which grime will be injected
     */
    Type selectPatternClass() {
        //List<Type> types = pattern.types()
        def types = []
        Random rand = new Random()
        types[rand.nextInt(types.size())]
    }
}
