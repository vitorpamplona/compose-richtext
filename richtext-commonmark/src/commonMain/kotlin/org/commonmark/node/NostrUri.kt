package org.commonmark.node

public class NostrUri public constructor(destinationSource: String) : CustomNode() {
  public var destination: String = destinationSource

  override fun accept(visitor: Visitor) {
    visitor.visit(this)
  }

  override fun toStringAttributes(): String {
    return "destination=$destination"
  }
}