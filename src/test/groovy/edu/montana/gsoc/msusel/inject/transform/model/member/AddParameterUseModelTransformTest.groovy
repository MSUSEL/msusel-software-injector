package edu.montana.gsoc.msusel.inject.transform.model.member

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Parameter
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.inject.transform.model.ModelTransformPreconditionsNotMetException
import org.junit.Test

class AddParameterUseModelTransformTest extends MemberModelTransformBaseTest {

    @Test
    void "test execute happy path"() {
        // given
        Type use = Class.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()

        // then
        the(method.getParentType().hasUseTo(use)).shouldBeTrue()
        the(((Method) method).getParameterByName(use.getName().uncapitalize())).shouldNotBeNull()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is null"() {
        // given
        method = null
        Type use = Class.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute type used is null"() {
        // given
        Type use = null
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute member is not a method"() {
        // given
        Type use = Class.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(field, use)

        // when
        fixture.execute()
    }

    @Test(expected = ModelTransformPreconditionsNotMetException.class)
    void "test execute method already has param with name"() {
        // given
        method.addParameter(Parameter.builder().name("typeZ").type(TypeRef.createPrimitiveTypeRef("int")).create())
        Type use = Class.findFirst("name = ?", "TypeZ")
        fixture = new AddParameterUseModelTransform(method, use)

        // when
        fixture.execute()
    }
}