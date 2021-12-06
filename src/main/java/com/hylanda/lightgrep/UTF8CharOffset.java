package com.hylanda.lightgrep;

import java.nio.charset.StandardCharsets;

public class UTF8CharOffset {
	public static float AVG_BYTE_PER_CHAR = StandardCharsets.UTF_8.newEncoder().averageBytesPerChar();

	private int[] byte2char;
	private int byteSize;
	private int[] char2byte;
	private int charSize;

	public UTF8CharOffset(CharSequence input) {
		this(input, (int) (input.length() * AVG_BYTE_PER_CHAR));
	}

	public UTF8CharOffset(CharSequence input, int utf8Len) {
		char2byte = new int[input.length()];
		charSize = input.length();
		byte2char = new int[utf8Len];
		byteSize = 0;
		int strPos = 0;
		for (int i = 0; i < input.length(); i++) {

			char c = input.charAt(i);
			int slop;
			// see sun.nio.cs.UTF_8.encodeArrayLoop
			if (c < 128)
				slop = 1;
			else if (c < 2048)
				slop = 2;
			else if (Character.isSurrogate(c)) {
				char2byte[i] = byteSize;
				i++;
				slop = 4;
			} else {
				slop = 3;
			}
			char2byte[i] = byteSize;

			if (byte2char.length - byteSize < slop) {
				int[] newbie = new int[Math.max(byte2char.length * 2, slop)];
				System.arraycopy(byte2char, 0, newbie, 0, byteSize);
				byte2char = newbie;
			}
			// int strPos = byteSize ==0 ? 0 : byte2char[byteSize -1] + 1;
			// if (wasSurrogate) strPos++;
			for (int k = 0; k < slop; k++)
				byte2char[byteSize + k] = strPos;
			byteSize += slop;
			strPos++;
			if (slop == 4)
				strPos++;
		}

	}

	public int fromByteToChar(int bytePos) {
		if (bytePos < 0)
			throw new IndexOutOfBoundsException("" + bytePos);
		if (bytePos > byteSize)
			throw new IndexOutOfBoundsException("" + bytePos);
		if (bytePos == byteSize)
			return charSize;
		else
			return byte2char[bytePos];
	}

	public int fromStringToByte(int charPos) {
		if (charPos < 0)
			throw new IndexOutOfBoundsException("" + charPos);
		if (charPos > charSize)
			throw new IndexOutOfBoundsException("" + charPos);
		if (charPos == charSize)
			return byteSize;
		else
			return char2byte[charPos];
	}
}
