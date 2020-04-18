package edu.montana.gsoc.msusel.inject.transform.source.member

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.TypeRef
import edu.isu.isuese.datamodel.TypedMember
import edu.montana.gsoc.msusel.inject.transform.source.BasicSourceTransform
import groovy.transform.builder.Builder

class ChangeMemberType extends BasicSourceTransform {

    TypeRef retType
    TypedMember member

    /**
     * Constructs a new BasicSourceTransform
     * @param file the file to be modified
     */
    @Builder(buildMethodName = "create")
    ChangeMemberType(File file, TypedMember member, TypeRef retType) {
        super(file)
        this.member = member
        this.retType = retType
    }

    @Override
    void setup() {
        ops = new java.io.File(file.getFullPath())
        lines = ops.readLines()

        start = member.getStart() - 1
        end = member.getEnd() - 1

        if (start == end)
            text = lines[start]
        else
            text = lines[start..end].join("\n")
    }

    @Override
    void buildContent() {
        def matcher = text =~ /\s+(\w+\s+)*${member.getType().getTypeName()}/
        if (matcher.find()) {
            String content = matcher[0][0]
            def newContent = content.replace(member.getType().getTypeName(), retType.getTypeName())
            text = text.replace(content, newContent)
        }
    }

    @Override
    void injectContent() {
        def textLines = text.split("\n")
        for (int i = 0; i < textLines.size(); i++)
            lines[start + i] = textLines[i]

        ops.text = lines.join("\n")
    }

    @Override
    void updateModel() {
        member.setType(retType)
    }
}
