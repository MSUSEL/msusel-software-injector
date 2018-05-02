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
package edu.montana.gsoc.msusel.experiment;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * ExperimentControl -
 * 
 * @author Isaac Griffith
 */
public class ExperimentControl extends RecursiveTask<DataHolder> {

    public static void main(String args[])
    {
        ExperimentDesign design = DesignReader.read(args[0]);
        List<DesignNode> nodes = design.getNodes();
        ForkJoinPool pool = new ForkJoinPool();
        ExperimentControl control = new ExperimentControl(nodes);
        pool.invoke(control);
    }

    private List<DesignNode> nodes;
    private DataHolder holder;

    /**
     * @param nodes
     */
    public ExperimentControl(List<DesignNode> nodes)
    {
        this.nodes = nodes;
        this.holder = new DataHolder();
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.RecursiveTask#compute()
     */
    @Override
    protected DataHolder compute()
    {
//        List<ExperimentRunner> runners = new ArrayList<>();
//        for (DesignNode node : nodes)
//        {
//            runners.add(new ExperimentRunner(node));
//        }
//
//        invokeAll(runners);
//
//        for (ExperimentRunner runner : runners)
//        {
//            runner.join();
//        }
//
//        for (ExperimentRunner runner : runners)
//        {
//            try
//            {
//                holder.mergeInto(runner.get());
//            }
//            catch (InterruptedException | ExecutionException e)
//            {
//
//            }
//        }
//
//        return holder;
        return null;
    }

}
