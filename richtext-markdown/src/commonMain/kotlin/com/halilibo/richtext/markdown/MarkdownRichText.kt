package com.halilibo.richtext.markdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.halilibo.richtext.markdown.node.AstBlockQuote
import com.halilibo.richtext.markdown.node.AstCode
import com.halilibo.richtext.markdown.node.AstEmphasis
import com.halilibo.richtext.markdown.node.AstFencedCodeBlock
import com.halilibo.richtext.markdown.node.AstHardLineBreak
import com.halilibo.richtext.markdown.node.AstHashtag
import com.halilibo.richtext.markdown.node.AstHeading
import com.halilibo.richtext.markdown.node.AstImage
import com.halilibo.richtext.markdown.node.AstIndentedCodeBlock
import com.halilibo.richtext.markdown.node.AstLink
import com.halilibo.richtext.markdown.node.AstLinkReferenceDefinition
import com.halilibo.richtext.markdown.node.AstListItem
import com.halilibo.richtext.markdown.node.AstNode
import com.halilibo.richtext.markdown.node.AstNostrUri
import com.halilibo.richtext.markdown.node.AstParagraph
import com.halilibo.richtext.markdown.node.AstSoftLineBreak
import com.halilibo.richtext.markdown.node.AstStrikethrough
import com.halilibo.richtext.markdown.node.AstStrongEmphasis
import com.halilibo.richtext.markdown.node.AstText
import com.halilibo.richtext.ui.BlockQuote
import com.halilibo.richtext.ui.FormattedList
import com.halilibo.richtext.ui.LocalOnUriCompose
import com.halilibo.richtext.ui.MediaRenderer
import com.halilibo.richtext.ui.RichTextScope
import com.halilibo.richtext.ui.string.RichTextString
import com.halilibo.richtext.ui.string.Text
import com.halilibo.richtext.ui.string.withFormat

/**
 * Only render the text content that exists below [astNode]. All the content blocks
 * like [AstBlockQuote] or [AstFencedCodeBlock] are ignored. This composable is
 * suited for [AstHeading] and [AstParagraph] since they are strictly text blocks.
 *
 * Some notes about commonmark and in general Markdown parsing.
 *
 * - Paragraph and Heading are the only RichTextString containers in base implementation.
 *   - RichTextString is build by traversing the children of Heading or Paragraph.
 *   - RichTextString can include;
 *     - Emphasis
 *     - StrongEmphasis
 *     - Image
 *     - Link
 *     - Code
 * - Code blocks should not have any children. Their whole content must reside in
 * [AstIndentedCodeBlock.literal] or [AstFencedCodeBlock.literal].
 * - Blocks like [BlockQuote], [FormattedList], [AstListItem] must have an [AstParagraph]
 * as a child to include any further RichText.
 * - CustomNode and CustomBlock can have their own scope, no idea about that.
 *
 * @param astNode Root node to accept as Text Content container.
 */
@Composable
internal fun RichTextScope.MarkdownRichText(astNode: AstNode, modifier: Modifier = Modifier) {
  val onUriCompose = LocalOnUriCompose.current ?: DefaultMediaRenderer

  // Assume that only RichText nodes reside below this level.
  val richText = remember(astNode, onUriCompose) {
    computeRichTextString(astNode, onUriCompose)
  }

  Text(text = richText, modifier = modifier)
}

private fun computeRichTextString(astNode: AstNode, renderer: MediaRenderer): RichTextString {
  val richTextStringBuilder = RichTextString.Builder()

  // Modified pre-order traversal with pushFormat, popFormat support.
  var iteratorStack = listOf(
    AstNodeTraversalEntry(
      astNode = astNode,
      isVisited = false,
      formatIndex = null
    )
  )

  var skipChildren: Boolean

  while (iteratorStack.isNotEmpty()) {
    val (currentNode, isVisited, formatIndex) = iteratorStack.first().copy()
    iteratorStack = iteratorStack.drop(1)

    skipChildren = false

    if (!isVisited) {
      val newFormatIndex = when (val currentNodeType = currentNode.type) {
        is AstCode -> {
          richTextStringBuilder.withFormat(RichTextString.Format.Code) {
            append(currentNodeType.literal)
          }
          null
        }
        is AstEmphasis -> richTextStringBuilder.pushFormat(RichTextString.Format.Italic)
        is AstStrikethrough -> richTextStringBuilder.pushFormat(
          RichTextString.Format.Strikethrough
        )
        is AstImage -> {
          renderer.renderImage(currentNodeType.title, currentNodeType.destination, richTextStringBuilder)
          null
        }
        is AstNostrUri -> {
          renderer.renderNostrUri(currentNodeType.destination, richTextStringBuilder)
          null
        }
        is AstHashtag -> {
          renderer.renderHashtag(currentNodeType.tag, richTextStringBuilder)
          null
        }
        is AstLink -> {
          val title = currentNodeType.title ?: currentNode.childrenSequence().joinToString {
            if (it.type is AstText) {
              it.type.literal
            } else {
              ""
            }
          }

          if (renderer.shouldRenderLinkPreview(title, currentNodeType.destination)) {
            skipChildren = true
            renderer.renderLinkPreview(title, currentNodeType.destination, richTextStringBuilder)
            null
          } else {
            richTextStringBuilder.pushFormat(
              RichTextString.Format.Link(destination = currentNodeType.destination)
            )
          }
        }
        is AstSoftLineBreak -> {
          richTextStringBuilder.append(" ")
          null
        }
        is AstHardLineBreak -> {
          richTextStringBuilder.append("\n")
          null
        }
        is AstStrongEmphasis -> richTextStringBuilder.pushFormat(RichTextString.Format.Bold)
        is AstText -> {
          val text = currentNodeType.literal
          val sanitizedText = if (renderer.shouldSanitizeUriLabel() && currentNode.links.parent?.type is AstLink) {
            renderer.sanitizeUriLabel(text)
          } else {
            text
          }
          richTextStringBuilder.append(sanitizedText)

          null
        }
        is AstLinkReferenceDefinition -> richTextStringBuilder.pushFormat(
          RichTextString.Format.Link(destination = currentNodeType.destination))
        else -> null
      }

      iteratorStack = iteratorStack.addFirst(
        AstNodeTraversalEntry(
          astNode = currentNode,
          isVisited = true,
          formatIndex = newFormatIndex
        )
      )

      // Do not visit children of terminals such as Text, Image, etc.
      if (!skipChildren && !currentNode.isRichTextTerminal()) {
        currentNode.childrenSequence(reverse = true).forEach {
          iteratorStack = iteratorStack.addFirst(
            AstNodeTraversalEntry(
              astNode = it,
              isVisited = false,
              formatIndex = null
            )
          )
        }
      }
    }

    if (formatIndex != null) {
      richTextStringBuilder.pop(formatIndex)
    }
  }

  return richTextStringBuilder.toRichTextString()
}

private data class AstNodeTraversalEntry(
  val astNode: AstNode,
  val isVisited: Boolean,
  val formatIndex: Int?
)

private inline fun <reified T> List<T>.addFirst(item: T): List<T> {
  return listOf(item) + this
}