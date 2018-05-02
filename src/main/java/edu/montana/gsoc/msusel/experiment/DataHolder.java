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

/**
 * DataHolder -
 * 
 * @author Isaac Griffith
 */
public class DataHolder {

//    private Map<String, Map<Integer, Map<String, Map<String, Pair<Double>>>>> data;
//
//    public DataHolder()
//    {
//        data = new ConcurrentHashMap<>();
//    }
//
//    public void addBefore(int rep, String pattern, String inject, String quality, double value)
//    {
//        if (data.containsKey(pattern))
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double>>>> reps = data.get(pattern);
//            if (reps.containsKey(rep))
//            {
//                Map<String, Map<String, Pair<Double>>> injects = reps.get(rep);
//                if (injects.containsKey(inject))
//                {
//                    Map<String, Pair<Double>> qualVals = injects.get(inject);
//                    if (qualVals.containsKey(quality))
//                    {
//                        Pair<Double> pair = qualVals.get(quality);
//                        pair.setFirst(value);
//                    }
//                    else
//                    {
//                        Pair<Double> values = new Pair<>();
//                        values.setFirst(value);
//                        qualVals.put(quality, values);
//                    }
//                }
//                else
//                {
//                    Map<String, Pair<Double>> qualVals = new ConcurrentHashMap<>();
//                    Pair<Double> pair = new Pair<>();
//                    pair.setFirst(value);
//                    qualVals.put(quality, pair);
//                    injects.put(inject, qualVals);
//                }
//            }
//            else
//            {
//                Map<String, Map<String, Pair<Double>>> injects = new ConcurrentHashMap<>();
//                Map<String, Pair<Double>> qualVals = new ConcurrentHashMap<>();
//                Pair<Double> pair = new Pair<>();
//                pair.setFirst(value);
//                qualVals.put(quality, pair);
//                injects.put(inject, qualVals);
//                reps.put(rep, injects);
//            }
//        }
//        else
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double>>>> reps = new ConcurrentHashMap<>();
//            Map<String, Map<String, Pair<Double>>> injects = new ConcurrentHashMap<>();
//            Map<String, Pair<Double>> qualVals = new ConcurrentHashMap<>();
//            Pair<Double> pair = new Pair<>();
//            pair.setFirst(value);
//            qualVals.put(quality, pair);
//            injects.put(inject, qualVals);
//            reps.put(rep, injects);
//            data.put(pattern, reps);
//        }
//    }
//
//    public void addAfter(int rep, String pattern, String inject, String quality, double value)
//    {
//        if (data.containsKey(pattern))
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double>>>> reps = data.get(pattern);
//            if (reps.containsKey(rep))
//            {
//                Map<String, Map<String, Pair<Double>>> injects = reps.get(rep);
//                if (injects.containsKey(inject))
//                {
//                    Map<String, Pair<Double>> qualVals = injects.get(inject);
//                    if (qualVals.containsKey(quality))
//                    {
//                        Pair<Double> pair = qualVals.get(quality);
//                        pair.setLast(value);
//                    }
//                    else
//                    {
//                        Pair<Double> values = new Pair<>();
//                        values.setLast(value);
//                        qualVals.put(quality, values);
//                    }
//                }
//                else
//                {
//                    Map<String, Pair<Double>> qualVals = new ConcurrentHashMap<>();
//                    Pair<Double> pair = new Pair<>();
//                    pair.setLast(value);
//                    qualVals.put(quality, pair);
//                    injects.put(inject, qualVals);
//                }
//            }
//            else
//            {
//                Map<String, Map<String, Pair<Double>>> injects = new ConcurrentHashMap<>();
//                Map<String, Pair<Double>> qualVals = new ConcurrentHashMap<>();
//                Pair<Double> pair = new Pair<>();
//                pair.setLast(value);
//                qualVals.put(quality, pair);
//                injects.put(inject, qualVals);
//                reps.put(rep, injects);
//            }
//        }
//        else
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double>>>> reps = new ConcurrentHashMap<>();
//            Map<String, Map<String, Pair<Double>>> injects = new ConcurrentHashMap<>();
//            Map<String, Pair<Double>> qualVals = new ConcurrentHashMap<>();
//            Pair<Double> pair = new Pair<>();
//            pair.setLast(value);
//            qualVals.put(quality, pair);
//            injects.put(inject, qualVals);
//            reps.put(rep, injects);
//            data.put(pattern, reps);
//        }
//    }
//
//    public void mergeInto(DataHolder holder)
//    {
//        Map<String, Map<Integer, Map<String, Map<String, Pair<Double>>>>> otherData = holder.getData();
//        for (String pattern : otherData.keySet())
//        {
//            if (data.containsKey(pattern))
//            {
//                for (int rep : otherData.get(pattern).keySet())
//                {
//                    if (data.get(pattern).containsKey(rep))
//                    {
//                        for (String inject : otherData.get(pattern).get(rep).keySet())
//                        {
//                            if (data.get(pattern).get(rep).containsKey(inject))
//                            {
//                                for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
//                                {
//                                    data.get(pattern).get(rep).get(inject)
//                                            .put(quality, otherData.get(pattern).get(rep).get(inject).get(quality));
//                                }
//                            }
//                            else
//                            {
//                                Map<String, Pair<Double>> map = new ConcurrentHashMap<>();
//                                for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
//                                {
//                                    map.put(quality, otherData.get(pattern).get(rep).get(inject).get(quality));
//                                }
//                                data.get(pattern).get(rep).put(inject, map);
//                            }
//                        }
//                    }
//                    else
//                    {
//                        Map<String, Map<String, Pair<Double>>> mainMap = new ConcurrentHashMap<>();
//                        for (String inject : otherData.get(pattern).get(rep).keySet())
//                        {
//                            Map<String, Pair<Double>> map = new ConcurrentHashMap<>();
//                            for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
//                            {
//                                map.put(quality, otherData.get(pattern).get(rep).get(inject).get(quality));
//                            }
//                            mainMap.put(inject, map);
//                        }
//                        data.get(pattern).put(rep, mainMap);
//                    }
//                }
//            }
//            else
//            {
//                Map<Integer, Map<String, Map<String, Pair<Double>>>> mainMap = new ConcurrentHashMap<>();
//                for (int rep : otherData.get(pattern).keySet())
//                {
//                    Map<String, Map<String, Pair<Double>>> repMap = new ConcurrentHashMap<>();
//                    for (String inject : otherData.get(pattern).get(rep).keySet())
//                    {
//                        Map<String, Pair<Double>> map = new ConcurrentHashMap<>();
//                        for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
//                        {
//                            map.put(quality, otherData.get(pattern).get(rep).get(inject).get(quality));
//                        }
//                        repMap.put(inject, map);
//                    }
//                    mainMap.put(rep, repMap);
//                }
//                data.put(pattern, mainMap);
//            }
//        }
//    }
//
//    /**
//     * @return
//     */
//    public Map<String, Map<Integer, Map<String, Map<String, Pair<Double>>>>> getData()
//    {
//        return data;
//    }
//
//    public String getDataLine(String pattern, int rep, String inject, String quality)
//    {
//        String retVal = null;
//        if (data.containsKey(pattern))
//        {
//            System.out.println("Data Contains Pattern");
//            Map<Integer, Map<String, Map<String, Pair<Double>>>> reps = data.get(pattern);
//            if (reps.containsKey(rep))
//            {
//                System.out.println("Data contains rep");
//                Map<String, Map<String, Pair<Double>>> injects = reps.get(rep);
//                if (injects.containsKey(inject))
//                {
//                    System.out.println("Data contains Inject");
//                    Map<String, Pair<Double>> qualVals = injects.get(inject);
//                    if (qualVals.containsKey(quality))
//                    {
//                        System.out.println("Data contains quality");
//                        Pair<Double> pair = qualVals.get(quality);
//                        System.out.println(pair);
//                        double diff = pair.getLast() - pair.getFirst();
//
//                        retVal = String.format("%s,%s,%f", pattern.replaceAll(" ", "-"), inject, diff);
//                    }
//                }
//            }
//        }
//
//        return retVal;
//    }

    /**
     * @return
     */
//    public Set<String> getKeys()
//    {
//        return this.data.keySet();
//    }
}
