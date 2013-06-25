package ch.doxblox.domain.document;

import org.bson.types.ObjectId;

/**
 * A block used inside a {@link Document}.
 *
 * @author Marco Jakob
 */
public interface DocumentBlock {
  ObjectId getId();

  void setId(ObjectId id);
}
