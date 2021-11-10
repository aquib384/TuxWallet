package io.tux.wallet.testnet.utils;

public final class Base32 {
  /* lookup table used to encode() groups of 5 bits of data */
  private static final String base32Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
  /* lookup table used to decode() characters in Base32 strings */
  private static final byte[] base32Lookup = { 26, 27, 28, 29, 30, 31, -1,
      -1, -1, -1, -1, -1, -1, -1, // 23456789:;<=>?
      -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // @ABCDEFGHIJKLMNO
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, // PQRSTUVWXYZ[\]^_
      -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // `abcdefghijklmno
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 // pqrstuvwxyz
  };
  /* Messsages for Illegal Parameter Exceptions in decode() */
  private static final String errorCanonicalLength = "non canonical Base32 string length";
  private static final String errorCanonicalEnd = "non canonical bits at end of Base32 string";
  private static final String errorInvalidChar = "invalid character in Base32 string";

  /**
   * Decode a Base32 string into an array of binary bytes. May fail if the
   * parameter is a non canonical Base32 string (the only other possible
   * exception is that the returned array cannot be allocated in memory)
   */
  static public byte[] decode(final String base32)
      throws IllegalArgumentException {
    // Note that the code below detects could detect non canonical
    // Base32 length within the loop. However canonical Base32 length
    // can be tested before entering the loop.
    // A canonical Base32 length modulo 8 cannot be:
    // 1 (aborts discarding 5 bits at STEP n=0 which produces no byte),
    // 3 (aborts discarding 7 bits at STEP n=2 which produces no byte),
    // 6 (aborts discarding 6 bits at STEP n=1 which produces no byte)
    // So these tests could be avoided within the loop.
    switch (base32.length() % 8) { // test the length of last subblock
    case 1: // 5 bits in subblock: 0 useful bits but 5 discarded
    case 3: // 15 bits in subblock: 8 useful bits but 7 discarded
    case 6: // 30 bits in subblock: 24 useful bits but 6 discarded
      throw new IllegalArgumentException(errorCanonicalLength);
    }
    byte[] bytes = new byte[base32.length() * 5 / 8];
    int offset = 0, i = 0, lookup;
    byte nextByte, digit;
    // Also the code below does test that other discarded bits
    // (1 to 4 bits at end) are effectively 0.
    while (i < base32.length()) {
      // Read the 1st char in a 8-chars subblock
      // check that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 0: leave 5 bits
      nextByte = (byte) (digit << 3);
      // Assert(i < base32.length) // tested before loop
      // Read the 2nd char in a 8-chars subblock
      // Check that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 5: insert 3 bits, leave 2 bits
      bytes[offset++] = (byte) (nextByte | (digit >> 2));
      nextByte = (byte) ((digit & 3) << 6);
      if (i >= base32.length()) {
        if (nextByte != (byte) 0) {
          throw new IllegalArgumentException(errorCanonicalEnd);
        }
        break; // discard the remaining 2 bits
      }
      // Read the 3rd char in a 8-chars subblock
      // Check that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 2: leave 7 bits
      nextByte |= (byte) (digit << 1);
      // Assert(i < base32.length) // tested before loop
      // Read the 4th char in a 8-chars subblock
      // Check that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 7: insert 1 bit, leave 4 bits
      bytes[offset++] = (byte) (nextByte | (digit >> 4));
      nextByte = (byte) ((digit & 15) << 4);
      if (i >= base32.length()) {
        if (nextByte != (byte) 0) {
          throw new IllegalArgumentException(errorCanonicalEnd);
        }
        break; // discard the remaining 4 bits
      }
      // Read the 5th char in a 8-chars subblock
      // Assert that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 4: insert 4 bits, leave 1 bit
      bytes[offset++] = (byte) (nextByte | (digit >> 1));
      nextByte = (byte) ((digit & 1) << 7);
      if (i >= base32.length()) {
        if (nextByte != (byte) 0) {
          throw new IllegalArgumentException(errorCanonicalEnd);
        }
        break; // discard the remaining 1 bit
      }
      // Read the 6th char in a 8-chars subblock
      // Check that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 1: leave 6 bits
      nextByte |= (byte) (digit << 2);
      // Assert(i < base32.length) // tested before loop
      // Read the 7th char in a 8-chars subblock
      // Check that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 6: insert 2 bits, leave 3 bits
      bytes[offset++] = (byte) (nextByte | (digit >> 3));
      nextByte = (byte) ((digit & 7) << 5);
      if (i >= base32.length()) {
        if (nextByte != (byte) 0) {
          throw new IllegalArgumentException(errorCanonicalEnd);
        }
        break; // discard the remaining 3 bits
      }
      // Read the 8th char in a 8-chars subblock
      // Check that chars are not outside the lookup table and valid
      lookup = base32.charAt(i++) - '2';
      if (lookup < 0 || lookup >= base32Lookup.length) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      digit = base32Lookup[lookup];
      if (digit == -1) {
        throw new IllegalArgumentException(errorInvalidChar);
      }
      // // STEP n = 3: insert 5 bits, leave 0 bit
      bytes[offset++] = (byte) (nextByte | digit);
      // // possible end of string here with no trailing bits
    }
    // On loop exit, discard trialing n bits.
    return bytes;
  }

  /**
   * Encode an array of binary bytes into a Base32 string. Should not fail
   * (the only possible exception is that the returned string cannot be
   * allocated in memory)
   */
  static public String encode(final byte[] bytes) {
    StringBuffer base32 = new StringBuffer((bytes.length * 8 + 4) / 5);
    int currByte, digit, i = 0;
    while (i < bytes.length) {
      // INVARIANTS FOR EACH STEP n in [0..5[; digit in [0..31[;
      // The remaining n bits are already aligned on top positions
      // of the 5 least bits of digit, the other bits are 0.
      // //// STEP n = 0; insert new 5 bits, leave 3 bits
      currByte = bytes[i++] & 255;
      base32.append(base32Chars.charAt(currByte >> 3));
      digit = (currByte & 7) << 2;
      if (i >= bytes.length) { // put the last 3 bits
        base32.append(base32Chars.charAt(digit));
        break;
      }
      // //// STEP n = 3: insert 2 new bits, then 5 bits, leave 1 bit
      currByte = bytes[i++] & 255;
      base32.append(base32Chars.charAt(digit | (currByte >> 6)));
      base32.append(base32Chars.charAt((currByte >> 1) & 31));
      digit = (currByte & 1) << 4;
      if (i >= bytes.length) { // put the last 1 bit
        base32.append(base32Chars.charAt(digit));
        break;
      }
      // //// STEP n = 1: insert 4 new bits, leave 4 bit
      currByte = bytes[i++] & 255;
      base32.append(base32Chars.charAt(digit | (currByte >> 4)));
      digit = (currByte & 15) << 1;
      if (i >= bytes.length) { // put the last 4 bits
        base32.append(base32Chars.charAt(digit));
        break;
      }
      // //// STEP n = 4: insert 1 new bit, then 5 bits, leave 2 bits
      currByte = bytes[i++] & 255;
      base32.append(base32Chars.charAt(digit | (currByte >> 7)));
      base32.append(base32Chars.charAt((currByte >> 2) & 31));
      digit = (currByte & 3) << 3;
      if (i >= bytes.length) { // put the last 2 bits
        base32.append(base32Chars.charAt(digit));
        break;
      }
      // /// STEP n = 2: insert 3 new bits, then 5 bits, leave 0 bit
      currByte = bytes[i++] & 255;
      base32.append(base32Chars.charAt(digit | (currByte >> 5)));
      base32.append(base32Chars.charAt(currByte & 31));
      // // This point is reached for bytes.length multiple of 5
    }
    return base32.toString();
  }


  public static String byteArrayToBase32(byte[] data) {
    String result = "";
    if (data.length % 5 != 0) {
      return result;
    }

    byte[] bits = new byte[data.length * 8];
    // convert to bits
    for (int i = 0; i < data.length; i++) {
      bits[i * 8] = (byte) ((data[i] & 0x80) >> 7);
      bits[i * 8 + 1] = (byte) ((data[i] & 0x40) >> 6);
      bits[i * 8 + 2] = (byte) ((data[i] & 0x20) >> 5);
      bits[i * 8 + 3] = (byte) ((data[i] & 0x10) >> 4);
      bits[i * 8 + 4] = (byte) ((data[i] & 0x08) >> 3);
      bits[i * 8 + 5] = (byte) ((data[i] & 0x04) >> 2);
      bits[i * 8 + 6] = (byte) ((data[i] & 0x02) >> 1);
      bits[i * 8 + 7] = (byte) ((data[i] & 0x01) >> 0);
    }
    // extract 5 bit values and convert to string
    for (int i = 0; i < data.length / 5 * 8; i++) {
      if (i > 0 && i % 4 == 0) {
        result += '-';
      }
      byte value = (byte) (bits[i * 5 + 0] << 4
              | bits[i * 5 + 1] << 3 | bits[i * 5 + 2] << 2
              | bits[i * 5 + 3] << 1 | bits[i * 5 + 4] << 0);

      if (value >= 0 && value < 26) {
        result = result + (char) (value + 'A');
      }

      if (value >= 26 && value < 30) {
        result = result + (char) (value - 26 + '2');
      }

      if (value == 30) {
        result = result + '7';
      }

      if (value == 31) {
        result = result + '9';
      }
    }
    return result;
  }
}

   
    