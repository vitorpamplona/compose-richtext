package com.halilibo.richtext.ui

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.halilibo.richtext.ui.string.RichTextString

public val LocalOnUriCompose: ProvidableCompositionLocal<MediaRenderer?> = compositionLocalOf<MediaRenderer?> { null }

public interface MediaRenderer {
  public fun renderImage(title: String?, uri: String, richTextStringBuilder: RichTextString.Builder)
  public fun renderNostrUri(uri: String, richTextStringBuilder: RichTextString.Builder)
  public fun renderHashtag(tag: String, richTextStringBuilder: RichTextString.Builder)
  public fun renderLinkPreview(title: String?, uri: String, richTextStringBuilder: RichTextString.Builder)
  public fun shouldRenderLinkPreview(title: String?, uri: String): Boolean
  public fun shouldSanitizeUriLabel(): Boolean
  public fun sanitizeUriLabel(label: String): String
}
