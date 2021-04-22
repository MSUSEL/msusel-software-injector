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
package edu.montana.gsoc.msusel.inject

import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class Director {

    def inject(ConfigObject config) {
        Project proj = Project.findFirst("projKey = ?", (String) config.where.projectKey)

        ProjectCopier copier = new ProjectCopier()
        proj = copier.execute(proj)

        Pattern pattern = Pattern.findFirst("patternKey = ?", (String) config.where.patternKey)
        PatternInstance inst = PatternInstance.findFirst("instKey = ?", (String) config.where.patternInst)

        SourceInjector injector = selectInjector(inst, config)
        int min = config.what.min
        int max = config.what.max

        Random rand = new Random()
        println("Max: $max")
        println("Min: $min")
        if (max > min) {
            int number = rand.nextInt(max - min) + min
            number.times {
                injector.inject()
            }
        }

        return [ "Key2" : proj.projectKey,
                 "Path2" : proj.getFullPath() ]
    }

    SourceInjector selectInjector(PatternInstance inst, ConfigObject config) {
        String type = config.what.type
        String form = config.what.form

        InjectorFactory.instance.createInjector(inst, type, form)
    }
}
