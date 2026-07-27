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

public class NostrUriExtension private constructor() : ParserExtension {
  override fun extend(parserBuilder: Builder) {
    parserBuilder.postProcessor(AutolinkNostrPostProcessor())
  }

  public companion object {
    public fun create(): Extension {
      return NostrUriExtension()
    }
  }
}

public class AutolinkNostrPostProcessor : PostProcessor {
  override fun process(node: Node): Node {
    val autolinkVisitor = AutolinkVisitor()
    node.accept(autolinkVisitor)
    return node
  }

  private fun linkify(originalTextNode: Text) {
    if (!originalTextNode.literal.contains(':')) return

    val sourceSpans = originalTextNode.sourceSpans
    val sourceSpan = if (sourceSpans.size == 1) sourceSpans[0] else null
    val literal = originalTextNode.literal

    val matcher = nip19regex.matcher(originalTextNode.literal)

    var lastNode: Node = originalTextNode

    var index = 0
    while (matcher.find()) {
      if (matcher.start() > index) {
        val textNode = createTextNode(literal, index, matcher.start(), sourceSpan)
        lastNode = insertNode(textNode, lastNode)
      }

      val linkNode = createNostrUri(literal, matcher.start(), matcher.end(), sourceSpan)
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

    override fun visit(text: Text) {
      if (inLink == 0) {
        linkify(text)
      }
    }
  }

  public companion object {
    public val nip19regex: Pattern =
      Pattern.compile(
        "nostr:(nsec|npub|nevent|naddr|note|nprofile|nrelay|nembed)1([qpzry9x8gf2tvdw0s3jn54khce6mua7l]+)",
        Pattern.CASE_INSENSITIVE,
      )

    private fun createTextNode(literal: String, beginIndex: Int, endIndex: Int, sourceSpan: SourceSpan?): Text {
      val text = literal.substring(beginIndex, endIndex)
      val textNode = Text(text)
      if (sourceSpan != null) {
        val length = endIndex - beginIndex
        textNode.addSourceSpan(SourceSpan.of(sourceSpan.lineIndex, beginIndex, length))
      }
      return textNode
    }

    private fun createNostrUri(literal: String, beginIndex: Int, endIndex: Int, sourceSpan: SourceSpan?): NostrUri {
      val text = literal.substring(beginIndex, endIndex)
      val textNode = NostrUri(text)
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

public class NostrUri public constructor(destinationSource: String) : CustomNode() {
  public var destination: String = destinationSource

  override fun accept(visitor: Visitor) {
    visitor.visit(this)
  }

  override fun toStringAttributes(): String {
    return "destination=$destination"
  }
}