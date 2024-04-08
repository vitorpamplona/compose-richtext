package com.halilibo.richtext.ui.string

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

public class UriComposableRenderer(
  private val richTextStringBuilder: RichTextString.Builder
) {
  public fun renderInline(innerComposable: @Composable () -> Unit) {
    richTextStringBuilder.appendInlineContent(
      content = InlineContent(
        initialSize = {
          IntSize(128.dp.roundToPx(), 128.dp.roundToPx())
        }
      ) {
        innerComposable()
      }
    )
  }
  public fun renderAsCompleteLink(title: String, destination: String) {
    richTextStringBuilder.pushFormat(
      RichTextString.Format.Link(destination = destination)
    )
    richTextStringBuilder.append(title)
    richTextStringBuilder.pop()
  }
}