package edu.montana.gsoc.msusel.inject.transform.model.member

import edu.isu.isuese.datamodel.Member
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Parameter
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.inject.transform.model.MemberModelTransform
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import edu.montana.gsoc.msusel.inject.transform.source.member.AddMethodParameter

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AddParameterUseModelTransform extends MemberModelTransform {

    Type type

    AddParameterUseModelTransform(Member member, Type type) {
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
        if (((Method) member).getParameterByName(type.getName().uncapitalize()) != null)
            throw new ModelTransformPreconditionsNotMetException()
    }

    @Override
    void transform() {
        Parameter param = Parameter.builder()
                .type(type.createTypeRef())
                .name(type.getName().uncapitalize())
                .create()

        ((Method) member).addParameter(param)

        AddMethodParameter.builder()
                .file(member.getParentFile())
                .type(member.getParentType())
                .method((Method) member)
                .param(param)
                .create()
                .execute()

        member.getParentType().useTo(type)
    }

    @Override
    void verifyPostconditons() {
        assert(member.getParentType().hasUseTo(type))
        assert(((Method) member).getParameterByName(type.getName().uncapitalize()) != null)
    }
}
