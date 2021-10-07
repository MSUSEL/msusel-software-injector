/*
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


import edu.isu.isuese.datamodel.Literal
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.model.TypeModelTransform
import edu.montana.gsoc.msusel.inject.transform.source.member.MoveLiteral

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MoveLiteralModelTransform extends TypeModelTransform {

    Literal literal
    Type newParent

    MoveLiteralModelTransform(Type type, Literal literal, Type newParent) {
        super(type)
        this.literal = literal
        this.newParent = newParent
    }

    @Override
    void verifyPreconditions() {
        // Pre-Conditions
        // 1. literal is not null
        if (!literal)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. newParent is not null
        if (!newParent)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type contains literal
        if (!type.getLiterals().contains(literal))
            throw new ModelTransformPreconditionsNotMetException()
        // 4. newParent does not contain a literal with same name
        if (newParent.getLiterals().find { it.getName() == literal.getName() })
            throw new ModelTransformPreconditionsNotMetException()

    }

    @Override
    void transform() {
        // Execute Transform
        type.removeMember(literal)
        literal.thaw()
        newParent.addMember(literal)
        literal.updateKey()
        // Generate Source Transform
        new MoveLiteral(type.getParentFile(), type, newParent.getParentFile(), newParent, literal).execute() // TODO Fix this
    }

    @Override
    void verifyPostconditions() {
        // 1. type no longer contains literal
        assert (!type.getLiterals().contains(literal))
        // 2. newParent now contains literal
        assert (newParent.getLiterals().contains(literal))
    }
}
