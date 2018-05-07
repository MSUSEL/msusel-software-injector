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
package edu.montana.gsoc.msusel.grimeinject.modgrime;

import edu.montana.gsoc.msusel.arc.impl.pattern4.codetree.PatternNode;
import edu.montana.gsoc.msusel.codetree.CodeTree;
import edu.montana.gsoc.msusel.codetree.node.CodeNode;
import edu.montana.gsoc.msusel.codetree.node.type.ClassNode;
import edu.montana.gsoc.msusel.grimeinject.GrimeInjector;
import edu.montana.gsoc.msusel.inject.transform.InvalidTransformException;
import edu.montana.gsoc.msusel.inject.transform.SourceTransform;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author Isaac Griffith
 */
public class TIGInjector extends GrimeInjector {

    public TIGInjector() {
        super(ModularGrimeTypes.TIG, null, null, null);
    }

    @Override
    public List<SourceTransform> createTransforms(CodeTree tree, List<CodeNode> nodes) {
        return null;
    }

    public void inject(CodeTree tree, PatternNode entity) {
        Pair<ClassNode, ClassNode> pair = select2InternalClasses();

        try {
            createTemporaryRelationship(tree, pair);
        } catch (InvalidTransformException e) {

        }
    }

    private Pair<ClassNode, ClassNode> select2InternalClasses() {
        return null;
    }

    private void createTemporaryRelationship(CodeTree tree, Pair<ClassNode, ClassNode> pair) throws InvalidTransformException {
        ClassNode from = pair.getLeft();
        ClassNode to = pair.getRight();

        SourceTransform transform;

        if (hasNoMethodsWithReturnType(from, to)) {

        }
        else if (hasNoMethodsWithParamType(from, to)) {

        } else {
            throw new InvalidTransformException();
        }
    }

    private boolean hasNoMethodsWithReturnType(ClassNode from, ClassNode to) {
        return false;
    }

    private boolean hasNoMethodsWithParamType(ClassNode from, ClassNode to) {
        return false;
    }
}
