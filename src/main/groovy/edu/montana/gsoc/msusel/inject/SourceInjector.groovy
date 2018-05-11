package edu.montana.gsoc.msusel.inject

import edu.montana.gsoc.msusel.inject.transform.SourceTransform

interface SourceInjector {

    void inject(InjectorContext context)

    List<SourceTransform> createTransforms(InjectorContext context)
}
