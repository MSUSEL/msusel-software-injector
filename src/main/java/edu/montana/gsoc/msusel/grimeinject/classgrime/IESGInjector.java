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
/**
 * 
 */
package edu.montana.gsoc.msusel.grimeinject.classgrime;

import edu.montana.gsoc.msusel.codetree.CodeTree;
import edu.montana.gsoc.msusel.codetree.node.CodeNode;
import edu.montana.gsoc.msusel.grimeinject.GrimeInjector;
import edu.montana.gsoc.msusel.inject.transform.SourceTransform;

import java.util.List;

/**
 * ISEGInjector -
 * 
 * @author Isaac Griffith
 */
public class IESGInjector extends GrimeInjector {

    /**
     * 
     */
    public IESGInjector()
    {
        super(ClassGrimeTypes.IESG, null, null, null);
    }

    @Override
    public List<SourceTransform> createTransforms(CodeTree tree, List<CodeNode> nodes) {
        return null;
//        List<TypeNode> contents = (List<TypeNode>) Node.types();
//        for (TypeNode cie : contents)
//        {
//            FieldNode field = new FieldNode("test", "test", null, cie, Accessibility.Private, false, false, false,
//                    false, false, 0, null);
//            graph.addFieldNode(field);
//            graph.addField(field, cie);
//            cie.addField(field);
//
//            MethodNode extMethod;
//            MethodNode mutator;
//            try
//            {
//                mutator = new MethodNode("mutator", "mutator", null, null, field.getType(), null,
//                        Accessibility.Public, false, false, false, false, false, false, false, false);
//                mutator.addFieldAccess(field);
//                cie.addMethod(mutator);
//                graph.addMethod(mutator);
//                graph.addMethod(mutator, cie);
//
//                extMethod = new MethodNode("test", "testMethod", null, null, VoidTypeNode.VOID, null,
//                        Accessibility.Public, false, false, false, false, false, false, false, false);
//                extMethod.addMethodCall(mutator);
//                graph.addMethod(extMethod);
//                graph.addMethod(extMethod, cie);
//                cie.addMethod(extMethod);
//            }
//            catch (MethodNodeCreationException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
    }

}
