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
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Namespace

import java.nio.file.Files
import java.nio.file.Paths

class CreateFileSpec extends BaseTransformSpec {

    def testExecute() {
        deleteDir(new File("testdata"))

        given: "A File with only a key for a non-existant file"
        Project proj = new Project()
        File fn = File.builder().key("testdata/Test.java").create()

        when: "Create a CreateFile transform"
        SourceTransform trans = new CreateFile(fn, tree)

        then: "Execute the transform"
        trans.execute()

        expect: "File for the filenode was created and is empty"
        Files.exists(Paths.get('testdata/Test.java'))
        Files.size(Paths.get('testdata/Test.java')) > 0
    }

    def testFileWithNamespace() {
        deleteDir(new File("testdata"))

        given: 'A File with a key and a namespace for a non-existant file'
        Namespace ns = Namespace.builder().key("testdata").create()
        File fn = File.builder().key("testdata/Test.java").namespace(ns).create()

        when: 'Create an CreateFile transform'
        SourceTransform trans = new CreateFile(fn)

        then: 'Execute the transform'
        trans.execute()

        expect: 'File for the filenode was created and is not empty'
        Files.exists(Paths.get('testdata/Test.java'))
        Files.size(Paths.get('testdata/Test.java')) > 0
    }
}
