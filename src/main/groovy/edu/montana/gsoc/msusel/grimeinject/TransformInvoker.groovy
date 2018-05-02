package edu.montana.gsoc.msusel.grimeinject

class TransformInvoker {

    private def transforms = []

    void executeTransforms() {
        transforms.each {
            it.execute()
        }
    }

    void addTransform(transform) {
        if (!transform)
            return

        transforms << transform
    }
}
