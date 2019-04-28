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

import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.DefaultCodeTree
import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef

class AddFieldSpec extends BaseTransformSpec {

    def testExecute() {
        deleteDir(new File("testdata"))

        given: "A File containing a Type and a new Field to add to that type"
        Project proj = new Project()
        File fn = File.builder().key('testdata/Test.java').create()
        Type tn = Class.builder().key('Test').accessibility(Accessibility.PUBLIC).create()

        new CreateFile(fn, tree).execute()
        new CreateType(fn, tree, tn).execute()
        Field fld = Field.builder().key("Test#field").type(TypeRef.getInstance("int")).create()

        when: "We create a new Transform"
        SourceTransform trans = new AddField(fn, tree, tn, fld)

        then: "We call execute on the transform"
        trans.execute()

        expect: "Something to happen"
    }
}
