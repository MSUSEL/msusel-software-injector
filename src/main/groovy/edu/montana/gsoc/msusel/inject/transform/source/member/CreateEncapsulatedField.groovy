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
package edu.montana.gsoc.msusel.inject.transform.source.member

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.inject.transform.source.AbstractSourceTransform
import edu.montana.gsoc.msusel.inject.transform.source.CompositeSourceTransform
import groovy.transform.builder.Builder

/**
 * A composite transform which constructs a fully encapsulated field
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CreateEncapsulatedField extends CompositeSourceTransform {

    /**
     * name of the field to be created
     */
    String fieldName
    /**
     * Type into which the field is to be added
     */
    Type type
    /**
     * The type of the field to be added
     */
    TypeRef fieldType
    Accessibility access
    Modifier[] mods

    /**
     * Constructs a new CreateEncapsulatedField transform
     * @param file the file to be modified
     * @param type the type into which the field is to be added
     * @param fieldType the type of the field
     * @param fieldName the name of the field
     */
    @Builder(buildMethodName = "create")
    CreateEncapsulatedField(File file, Type type, TypeRef fieldType, String fieldName, Accessibility access, Modifier ... mods) {
        super(file)
        this.type = type
        this.fieldName = fieldName
        this.fieldType = fieldType
        this.access = access
        this.mods = mods
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void execute() {
        // 1. Determine if a field already exists, with either the given name or the given type, if so throw an exception.
        // 2. If no such field exists, then create the field
        Field fld = Field.builder()
                .name("${fieldName}")
                .compKey("${type.getCompKey()}:${fieldName}")
                .accessibility(access)
                .type(fieldType)
                .create()
        mods.each {
            fld.addModifier(it)
        }

        // 3. Create the AddField transform
        AddField.builder().file(file).type(type).field(fld).create().execute()
        type.refresh()
        file.refresh()

        // 4. Check if a getter called get<FieldName> exists, if so throw an exception, else create the transform
        AddFieldGetter.builder().file(file).type(type).field(fld).create().execute()
        type.refresh()
        file.refresh()

        // 5. Check if a setter called set<FieldName> exists, if so throw an exception, else create the transform
        AddFieldSetter.builder().file(file).type(type).field(fld).create().execute()
        type.refresh()
        file.refresh()
    }
}
