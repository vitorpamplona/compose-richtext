package org.commonmark.node

public class Hashtag public constructor(tagSource: String) : CustomNode() {
  public var tag: String = tagSource

  override fun accept(visitor: Visitor) {
    visitor.visit(this)
  }

  override fun toStringAttributes(): String {
    return "tag=$tag"
  }
}