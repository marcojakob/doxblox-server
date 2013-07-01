part of doxblox.model;

/**
 * A block used inside a [Document].
 */
abstract class DocumentBlock {
  String id;
  
  /// The title used for the digest
  String get digestTitle;
  /// The snippet used for the digest
  String get digestSnippet;
}



