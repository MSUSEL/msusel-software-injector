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
package edu.montana.gsoc.msusel.inject

import com.google.common.collect.Queues
import edu.montana.gsoc.msusel.inject.transform.SourceTransform

/**
 * This represents the command invoker which collects a queue of Transforms and when ready will execute them in the proper order (FIFO)
 *
 * @author Isaac Griffith
 * @version 1.2.0
 */
class TransformInvoker {

    /**
     * The queue of transforms to be executed to construct software artifacts
     */
    private Queue<SourceTransform> transforms

    /**
     * Constructs a new TransformInvoker with an empty concurrent queue
     */
    TransformInvoker() {
        transforms = Queues.newConcurrentLinkedQueue()
    }

    /**
     * Executes the current queued transforms
     */
    void executeTransforms() {
        transforms.each {
            it.execute()
        }

        transforms.clear()
    }

    /**
     * Submits the provided transform to the queue for later execution
     * @param transform Transform to be executed, if null nothing is submitted
     */
    void submit(SourceTransform transform) {
        if (!transform)
            return

        transforms << transform
    }

    /**
     * Submits the list of transforms in order to the transform queue
     * @param list List of transforms
     */
    void submitAll(List<SourceTransform> list) {
        if (!list)
            return
        transforms += list
    }
}
