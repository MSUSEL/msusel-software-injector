package edu.montana.gsoc.msusel.inject.transform.model.member

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Parameter
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddReturnTypeUseModelTransformTest extends MemberModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Type use = Class.findFirst("name = ?", "TypeZ")
        fixture = new AddReturnTypeUseModelTransform(method, use)

        // when
        fixture.execute()

        // then
        the(method.getParentType().hasUseTo(use)).shouldBeTrue()
        the(((Method) method).getType().getTypeName()).shouldBeEqual(use.getName())
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is null"() {
        // given
        method = null
        Type use = Class.findFirst("name = ?", "TypeZ")
        fixture = new AddReturnTypeUseModelTransform(method, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type used is null"() {
        // given
        Type use = null
        fixture = new AddReturnTypeUseModelTransform(method, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is not a method"() {
        // given
        Type use = Class.findFirst("name = ?", "TypeZ")
        fixture = new AddReturnTypeUseModelTransform(field, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute method already has return type"() {
        // given
        Type use = Class.findFirst("name = ?", "TypeZ")
        method.setReturnType(use.createTypeRef())
        fixture = new AddReturnTypeUseModelTransform(method, use)

        // when
        fixture.execute()
    }
}