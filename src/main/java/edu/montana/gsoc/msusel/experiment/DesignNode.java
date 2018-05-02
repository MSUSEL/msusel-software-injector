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
package edu.montana.gsoc.msusel.experiment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DesignNode {
    /**
     * @return the patternType
     */
    public String getPatternType()
    {
        return patternType;
    }

    /**
     * @param patternType
     *            the patternType to set
     */
    public void setPatternType(String patternType)
    {
        this.patternType = patternType;
    }

    /**
     * @return the instanceLoc
     */
    public String getInstanceLoc()
    {
        return instanceLoc;
    }

    /**
     * @param instanceLoc
     *            the instanceLoc to set
     */
    public void setInstanceLoc(String instanceLoc)
    {
        this.instanceLoc = instanceLoc;
    }

    /**
     * @return the injectType
     */
    public String getInjectType()
    {
        return injectType;
    }

    /**
     * @param injectType
     *            the injectType to set
     */
    public void setInjectType(String injectType)
    {
        this.injectType = injectType;
    }

    /**
     * @return the system
     */
    public String getSystem()
    {
        return system;
    }

    /**
     * @param system
     *            the system to set
     */
    public void setSystem(String system)
    {
        this.system = system;
    }

    public void setRep(int rep)
    {
        this.rep = rep;
    }

    public int getRep()
    {
        return rep;
    }

    private String patternType;
    private String instanceLoc;
    private String injectType;
    private String system;
    private int rep;

    public DesignNode(String type, int rep, String loc, String inject, String system) throws DesignNodeException
    {
        this.patternType = type;
        this.rep = rep;
        this.instanceLoc = loc;
        this.injectType = inject;
        this.system = system;

        verify();
    }

    public void verify() throws DesignNodeException
    {
        Path path = Paths.get(instanceLoc);
        if (!Files.exists(path) || !Files.isDirectory(path))
            throw new DesignNodeException("The location specified: " + instanceLoc
                    + " either does not exist or is not a directory.");

        if (!isAcceptableInjectType(injectType))
            throw new DesignNodeException("The injection type specified: " + injectType
                    + " is not a known acceptable injection type.");
    }

    /**
     * @param injectType
     * @return
     */
    private boolean isAcceptableInjectType(String injectType)
    {
//        switch (injectType)
//        {
//            case GrimeInjector.DEPG:
//            case GrimeInjector.DIPG:
//            case GrimeInjector.DESG:
//            case GrimeInjector.DISG:
//            case GrimeInjector.IEPG:
//            case GrimeInjector.IIPG:
//            case GrimeInjector.IESG:
//            case GrimeInjector.IISG:
//                return true;
//        }

        return false;
    }
}
