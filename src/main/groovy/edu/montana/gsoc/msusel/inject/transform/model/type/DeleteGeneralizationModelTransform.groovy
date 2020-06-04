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
package edu.montana.gsoc.msusel.inject.transform.model.type

import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.source.relation.DeleteGeneralization
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteGeneralizationModelTransform extends TypeModelTransform {

    Type gen

    DeleteGeneralizationModelTransform(Type type, Type gen) {
        super(type)
        this.gen = gen
    }

    @Override
    void verifyPreconditions() {
        // 1. gen is not null
        if (!gen)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type generalized by gen
        if (!type.getGeneralizedBy().contains(gen))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        type.removeGeneralizedBy(gen)
        // Generate Source Transform
        new DeleteGeneralization(type.getParentFile(), type, gen).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. type no longer generalized by gen
        assert(!type.getGeneralizedBy().contains(gen))
        // 2. gen no longer generalizes type
        assert(!gen.getGeneralizes().contains(type))
    }
}
