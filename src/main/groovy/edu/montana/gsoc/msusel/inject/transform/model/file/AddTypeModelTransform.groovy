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
package edu.montana.gsoc.msusel.inject.transform.model.file

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.model.FileModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.source.type.AddType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddTypeModelTransform extends FileModelTransform {

    String name
    Accessibility access
    String typeName
    Modifier[] mods
    Type type

    AddTypeModelTransform(File file, String name, Accessibility access, String typeName, Modifier... mods) {
        super(file)
        this.name = name
        this.access = access
        this.typeName = typeName
        this.mods = mods
    }

    @Override
    void verifyPreconditions() {
        // 1. name is not null or empty
        if (!name)
            throw new ModelTransformPreconditionsNotMetException()
        // 2. access is not null or empty
        if (!access)
            throw new ModelTransformPreconditionsNotMetException()
        // 3. type is not null or empty
        if (!typeName)
            throw new ModelTransformPreconditionsNotMetException()
        // 4. file does not already contain a type with name
        if (file.getAllTypes().find { it.name == name })
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        // Execute Transform
        switch (typeName) {
            case "class":
                type = Class.builder()
                        .name(name)
                        .compKey(file.getParentNamespace().getNsKey() + ":" + name)
                        .accessibility(access)
                        .create()
                break
            case "interface":
                type = Interface.builder()
                        .name(name)
                        .accessibility(access)
                        .compKey(file.getParentNamespace().getNsKey() + ":" + name)
                        .create()
                break
            case "enum":
                type = Enum.builder()
                        .name(name)
                        .accessibility(access)
                        .compKey(file.getParentNamespace().getNsKey() + ":" + name)
                        .create()
                break
        }
        mods.each {
            type.addModifier(it)
        }
        file.addType(type)
        file.getParentNamespace().addType(type)
        // Generate Source Transform
        new AddType(file, type).execute()
    }

    @Override
    void verifyPostconditions() {
        // 1. A new type with the given name and features exists in file
        assert (file.getAllTypes().contains(type))
        // 2. The new type's parent is file
        assert (type.getParentFile() == file)
    }
}
