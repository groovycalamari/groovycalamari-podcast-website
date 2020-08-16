package com.groovycalamari.podcast

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.DataHolder
import com.vladsch.flexmark.util.data.MutableDataSet
import groovy.transform.CompileStatic

@CompileStatic
class MarkdownUtil {
    static DataHolder OPTIONS = new MutableDataSet().toImmutable()
    static Parser PARSER = Parser.builder(OPTIONS).build()
    static HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build()

    static String htmlFromMarkdown(String markdown) {
        Node document = PARSER.parse(markdown)
        RENDERER.render(document)
    }
}
