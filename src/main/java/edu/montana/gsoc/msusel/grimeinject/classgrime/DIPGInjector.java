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
import edu.montana.gsoc.msusel.source.SourceTransform;

import java.util.List;

/**
 * DPIGInjector -
 * 
 * @author Isaac Griffith
 */
public class DIPGInjector extends GrimeInjector {

    /**
     * @param
     */
    public DIPGInjector()
    {
        super(ClassGrimeTypes.DIPG, null, null, null);
    }

    @Override
    public List<SourceTransform> createTransforms(CodeTree tree, List<CodeNode> nodes) {
        return null;

//        List<TypeNode> contents = (List<TypeNode>) Node.types();
//        for (TypeNode cie : contents)
//        {
//            List<MethodNode> methods = (List<MethodNode>) cie.methods();
//            if (methods.size() >= 2)
//            {
//                FieldNode field = new FieldNode("test", "test", null, cie, Accessibility.Private, false, false,
//                        false, false, false, 0, null);
//                graph.addFieldNode(field);
//                graph.addField(field, cie);
//                cie.addField(field);
//
//                Random rand = new Random();
//                int count = 0;
//                int max = rand.nextInt(methods.size() - 1) + 1;
//                for (MethodNode method : methods)
//                {
//                    if (count >= max)
//                        method.addFieldAccess(field);
//                    break;
//                }
//
//            }
//            else
//            {
//                FieldNode field = new FieldNode("test", "test", null, cie, Accessibility.Private, false, false,
//                        false, false, false, 0, null);
//                graph.addFieldNode(field);
//                graph.addField(field, cie);
//                cie.addField(field);
//                MethodNode extMethod1;
//                MethodNode extMethod2;
//                try
//                {
//                    extMethod1 = new MethodNode("test", "testMethod", null, null, VoidTypeNode.VOID, null,
//                            Accessibility.Public, false, false, false, false, false, false, false, false);
//                    extMethod1.addFieldAccess(field);
//                    graph.addMethod(extMethod1);
//                    graph.addMethod(extMethod1, cie);
//                    cie.addMethod(extMethod1);
//
//                    extMethod2 = new MethodNode("test2", "testMethod2", null, null, VoidTypeNode.VOID, null,
//                            Accessibility.Public, false, false, false, false, false, false, false, false);
//                    graph.addMethod(extMethod2);
//                    extMethod2.addFieldAccess(field);
//                    graph.addMethod(extMethod2, cie);
//                    cie.addMethod(extMethod2);
//                }
//                catch (MethodNodeCreationException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
