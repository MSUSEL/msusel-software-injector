package edu.montana.gsoc.msusel.inject.transform.model.member


import edu.isu.isuese.datamodel.Member
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypedMember
import edu.montana.gsoc.msusel.inject.transform.model.MemberModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.source.member.AddMethod
import edu.montana.gsoc.msusel.inject.transform.source.member.ChangeMemberType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddReturnTypeUseModelTransform extends MemberModelTransform {

    Type type

    AddReturnTypeUseModelTransform(Member member, Type type) {
        super(member)
        this.type = type
    }

    @Override
    void verifyPreconditions() {
        if (!member)
            throw new ModelTransformPreconditionsNotMetException()
        if (!type)
            throw new ModelTransformPreconditionsNotMetException()
        if (!(member instanceof Method))
            throw new ModelTransformPreconditionsNotMetException()
        if (((TypedMember) member).getType().getTypeName() == type.getName())
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        if (member instanceof Method) {
            Method method = (Method) member

            if (method.getType().getTypeName() == type.getName()) {
                // need to add method
                method.setType(type.createTypeRef())

                AddMethod.builder()
                        .file(method.getParentFile())
                        .type(member.getParentType())
                        .method(method)
                        .bodyContent("    throw new OperationNotSupportedException();")
                        .create()
                        .execute()
            } else {
                ChangeMemberType.builder()
                        .file(method.getParentFile())
                        .member(method)
                        .retType(type.createTypeRef())
                        .create()
                        .execute()
            }

            member.getParentType().useTo(type)
        }
    }

    @Override
    void verifyPostconditons() {
        assert(member.getParentType().hasUseTo(type))
        assert(((Method) member).getType().getTypeName() == type.getName())
    }
}
