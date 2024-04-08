package com.halilibo.richtext.commonmark

import org.commonmark.Extension
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.CustomNode
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.node.SourceSpan
import org.commonmark.node.Text
import org.commonmark.node.Visitor
import org.commonmark.parser.Parser.Builder
import org.commonmark.parser.Parser.ParserExtension
import org.commonmark.parser.PostProcessor
import java.util.regex.Pattern

public class HashtagExtension private constructor() : ParserExtension {
  override fun extend(parserBuilder: Builder) {
    parserBuilder.postProcessor(AutolinkHashtagPostProcessor())
  }

  public companion object {
    public fun create(): Extension {
      return HashtagExtension()
    }
  }
}

public class AutolinkHashtagPostProcessor : PostProcessor {
  override fun process(node: Node): Node {
    val autolinkVisitor = AutolinkVisitor()
    node.accept(autolinkVisitor)
    return node
  }

  private fun linkify(originalTextNode: Text) {
    if (!originalTextNode.literal.contains('#')) return

    val sourceSpans = originalTextNode.sourceSpans
    val sourceSpan = if (sourceSpans.size == 1) sourceSpans[0] else null
    val literal = originalTextNode.literal

    val matcher = hashTagsPattern.matcher(originalTextNode.literal)

    var lastNode: Node = originalTextNode

    var index = 0
    while (matcher.find()) {
      if (matcher.start() > index) {
        val textNode = createTextNode(literal, index, matcher.start(), sourceSpan)
        lastNode = insertNode(textNode, lastNode)
      }

      val linkNode = createHashtag(literal, matcher.start(), matcher.end(), sourceSpan)
      lastNode = insertNode(linkNode, lastNode)

      index = matcher.end()
    }

    if (index != 0) {
      if (literal.length > index) {
        val textNode = createTextNode(literal, index, literal.length, sourceSpan)
        insertNode(textNode, lastNode)
      }

      // Original node no longer needed
      originalTextNode.unlink()
    }
  }

  private inner class AutolinkVisitor : AbstractVisitor() {
    var inLink = 0
    override fun visit(link: Link) {
      inLink++
      super.visit(link)
      inLink--
    }

    override fun visit(link: CustomNode) {
      inLink++
      super.visit(link)
      inLink--
    }

    override fun visit(text: Text) {
      if (inLink == 0) {
        linkify(text)
      }
    }
  }

  public companion object {
    public val hashTagsPattern: Pattern =
      Pattern.compile("#[^\\s!@#\$%^&*()=+./,\\[{\\]};:'\"?><]+", Pattern.CASE_INSENSITIVE)

    private fun createTextNode(literal: String, beginIndex: Int, endIndex: Int, sourceSpan: SourceSpan?): Text {
      val text = literal.substring(beginIndex, endIndex)
      val textNode = Text(text)
      if (sourceSpan != null) {
        val length = endIndex - beginIndex
        textNode.addSourceSpan(SourceSpan.of(sourceSpan.lineIndex, beginIndex, length))
      }
      return textNode
    }

    private fun createHashtag(literal: String, beginIndex: Int, endIndex: Int, sourceSpan: SourceSpan?): Hashtag {
      val text = literal.substring(beginIndex, endIndex)
      val textNode = Hashtag(text)
      if (sourceSpan != null) {
        val length = endIndex - beginIndex
        textNode.addSourceSpan(SourceSpan.of(sourceSpan.lineIndex, beginIndex, length))
      }
      return textNode
    }

    private fun insertNode(node: Node, insertAfterNode: Node): Node {
      insertAfterNode.insertAfter(node)
      return node
    }
  }
}

public class Hashtag public constructor(tagSource: String) : CustomNode() {
  public var tag: String = tagSource

  override fun accept(visitor: Visitor) {
    visitor.visit(this)
  }

  override fun toStringAttributes(): String {
    return "tag=$tag"
  }
}