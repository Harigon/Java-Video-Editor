package net.sourceforge.jaad.aac.syntax;

import net.sourceforge.jaad.aac.AACException;

/**
 * This class is part of JAAD ( jaadec.sourceforge.net ) that is distributed
 * under the Public Domain license. Code changes provided by the JCodec project
 * are distributed under FreeBSD license. 
 *
 * @author in-somnia
 */
class DSE extends Element {

	private byte[] dataStreamBytes;

	DSE() {
		super();
	}

	void decode(IBitStream in) throws AACException {
		final boolean byteAlign = in.readBool();
		int count = in.readBits(8);
		if(count==255) count += in.readBits(8);

		if(byteAlign) in.byteAlign();

		dataStreamBytes = new byte[count];
		for(int i = 0; i<count; i++) {
			dataStreamBytes[i] = (byte) in.readBits(8);
		}
	}
}
