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

import edu.isu.isuese.datamodel.Enum
import edu.isu.isuese.datamodel.Literal
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.source.member.DeleteLiteral
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DeleteLiteralModelTransform extends TypeModelTransform {

    Literal literal

    DeleteLiteralModelTransform(Type type, Literal literal) {
        super(type)
        this.literal = literal
    }

    @Override
    void verifyPreconditions() {
        // 0. type instanceof enum
        if (!(type instanceof Enum))
            throw new ModelTransformPreconditionsNotMetException()
        // 1. literal is not null
        if (!literal)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. type contains literal
        if (!type.getLiterals().contains(literal))
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        type.removeMember(literal)
        literal.thaw()
        // Generate Source Transform
        new DeleteLiteral(type.getParentFile(), (Enum) type, literal).execute()
    }

    @Override
    void verifyPostconditons() {
        // 1. type no longer contains member
        assert (!type.getLiterals().contains(literal))
    }
}
