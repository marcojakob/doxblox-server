/// Some general String utility methods.
library string_utils;

/**
 * Trims the given String [str] to the specified [size]. 
 * If the specified String is longer than [size], a new String of the specified
 * [size] is returned. Otherwise, the String itself is returned.
 */
String trimToSize(String str, int size) {
  if (str.length <= size) {
    return str;
  } else {
    return str.substring(0, size - 1);
  }
}

