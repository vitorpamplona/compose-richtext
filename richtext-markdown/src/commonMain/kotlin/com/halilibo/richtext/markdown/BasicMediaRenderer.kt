package com.halilibo.richtext.markdown

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import com.halilibo.richtext.ui.MediaRenderer
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.ui.string.InlineContent
import com.halilibo.richtext.ui.string.RichTextString

public object DefaultMediaRenderer: BasicMediaRenderer() {

}

public open class BasicMediaRenderer: MediaRenderer {
  override fun renderImage(title: String?, uri: String, richTextStringBuilder: RichTextString.Builder) {
    renderInline(richTextStringBuilder) {
      RemoteImage(
        url = uri,
        contentDescription = title,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Inside
      )
    }
  }

  override fun renderLinkPreview(title: String?, uri: String, richTextStringBuilder: RichTextString.Builder) {
    renderAsCompleteLink(title ?: uri, uri, richTextStringBuilder)
  }

  override fun renderNostrUri(uri: String, richTextStringBuilder: RichTextString.Builder) {
    renderAsCompleteLink(uri, uri, richTextStringBuilder)
  }

  override fun renderHashtag(tag: String, richTextStringBuilder: RichTextString.Builder) {
    renderAsCompleteLink(tag, "hashtag:${tag}", richTextStringBuilder)
  }

  override fun shouldRenderLinkPreview(title: String?, uri: String): Boolean { return false }

  override fun shouldSanitizeUriLabel(): Boolean { return false }

  public fun renderInline(richTextStringBuilder: RichTextString.Builder, innerComposable: @Composable () -> Unit) {
    richTextStringBuilder.appendInlineContent(
      content = InlineContent(
        initialSize = {
          IntSize(128.dp.roundToPx(), 128.dp.roundToPx())
        },
        placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
      ) {
        innerComposable()
      }
    )
  }
  public fun renderAsCompleteLink(title: String, destination: String, richTextStringBuilder: RichTextString.Builder) {
    richTextStringBuilder.pushFormat(
      RichTextString.Format.Link(destination = destination)
    )
    richTextStringBuilder.append(title)
    richTextStringBuilder.pop()
  }
}