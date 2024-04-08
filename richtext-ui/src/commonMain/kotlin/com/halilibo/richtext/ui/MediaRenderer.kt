package com.halilibo.richtext.ui

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.halilibo.richtext.ui.string.UriComposableRenderer

public val LocalOnUriCompose: ProvidableCompositionLocal<MediaRenderer?> = compositionLocalOf<MediaRenderer?> { null }

public interface MediaRenderer {
  public fun renderImage(title: String?, uri: String, helper: UriComposableRenderer)
  public fun renderNostrUri(uri: String, helper: UriComposableRenderer)
  public fun renderHashtag(uri: String, helper: UriComposableRenderer)
  public fun renderLinkPreview(title: String?, uri: String, helper: UriComposableRenderer)
  public fun shouldRenderLinkPreview(uri: String): Boolean
}
