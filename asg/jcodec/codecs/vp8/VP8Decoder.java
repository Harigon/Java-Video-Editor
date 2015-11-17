package asg.jcodec.codecs.vp8;

import static asg.jcodec.codecs.vp8.VP8Util.MAX_MODE_LF_DELTAS;
import static asg.jcodec.codecs.vp8.VP8Util.MAX_REF_LF_DELTAS;
import static asg.jcodec.codecs.vp8.VP8Util.getBitInBytes;
import static asg.jcodec.codecs.vp8.VP8Util.getBitsInBytes;
import static asg.jcodec.codecs.vp8.VP8Util.getDefaultCoefProbs;
import static asg.jcodec.codecs.vp8.VP8Util.getMacroblockCount;
import static asg.jcodec.codecs.vp8.VP8Util.keyFrameYModeProb;
import static asg.jcodec.codecs.vp8.VP8Util.keyFrameYModeTree;
import static asg.jcodec.codecs.vp8.VP8Util.vp8CoefUpdateProbs;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;

import asg.jcodec.codecs.vp8.Macroblock.Subblock;
import asg.jcodec.codecs.vp8.VP8Util.QuantizationParams;
import asg.jcodec.codecs.vp8.VP8Util.SubblockConstants;
import asg.jcodec.common.Assert;
import asg.jcodec.common.model.ColorSpace;
import asg.jcodec.common.model.Picture;

public class VP8Decoder {

        private Macroblock[][] mbs;
        private int width;
        private int height;

        public void decode(ByteBuffer frame) throws IOException {
            byte[] firstThree = new byte[3];
            frame.get(firstThree);

            boolean keyFrame = getBitInBytes(firstThree, 0) == 0;
//            System.out.println("frame type: " + (keyFrame ? "key" : ""));
            int version = getBitsInBytes(firstThree, 1, 3);
//            System.out.println("version: " + version);
            boolean showFrame = getBitInBytes(firstThree, 4) > 0;
//            System.out.println("show frame: " + showFrame);
            int partitionSize = getBitsInBytes(firstThree, 5, 19);
//            System.out.println("partition size: " + partitionSize);
            String threeByteToken = printHexByte(frame.get()) + " " + printHexByte(frame.get()) + " " + printHexByte(frame.get());
//            System.out.println("three byte token: " +threeByteToken );

            int twoBytesWidth = (frame.get() & 0xFF) | (frame.get() & 0xFF) << 8;
            int twoBytesHeight = (frame.get() & 0xFF) | (frame.get() & 0xFF) << 8;
            width = (twoBytesWidth & 0x3fff);
            height = (twoBytesHeight & 0x3fff);
//            System.out.println("size: " + width + "x" + height);
//            System.out.println("horizontal scale: " + (twoBytesWidth >> 14));
//            System.out.println("vertical scale: " + (twoBytesHeight >> 14));

            int numberOfMBRows = getMacroblockCount(height);
            int numberOfMBCols = getMacroblockCount(width);

//            System.out.println("macroblocks: " + numberOfMBRows + "x" + numberOfMBCols);
            /** Init macroblocks and subblocks */
            mbs = new Macroblock[numberOfMBRows + 2][numberOfMBCols + 2];
            for (int row = 0; row < numberOfMBRows + 2; row++)
                for (int col = 0; col < numberOfMBCols + 2; col++)
                    mbs[row][col] = new Macroblock(row, col);

            int headerOffset = frame.position();
//            System.out.println("header offset: " + headerOffset);
            BooleanArithmeticDecoder headerDecoder = new BooleanArithmeticDecoder(frame, 0);
            boolean isYUVColorSpace = (headerDecoder.decodeBit() == 0);
//            System.out.println("color space: " + (isYUVColorSpace ? "YUV" : "UNKNOWN"));

            boolean clampingRequired = headerDecoder.decodeBit() == 0;
//            System.out.println("clamp type: " + (clampingRequired ? "clampt to match [0,255]" : "no clamping needed"));
            int segmentation = headerDecoder.decodeBit();
//            System.out.println("segmentation: " + (segmentation == 1 ? "yes" : "no"));
            Assert.assertEquals(0, segmentation);
            int simpleFilter = headerDecoder.decodeBit();
//            System.out.println("simpleFilter: " + simpleFilter);
            int filterLevel = headerDecoder.decodeInt(6);
            int filterType = (filterLevel == 0) ? 0 : (simpleFilter > 0) ? 1 : 2;
//            System.out.println("filterLevel: " + filterLevel);
            int sharpnessLevel = headerDecoder.decodeInt(3);
//            System.out.println("sharpnessLevel: " + sharpnessLevel);
            int loopFilterDeltaFlag = headerDecoder.decodeBit();
//            System.out.println("loopFilterDeltaFlag: " + (loopFilterDeltaFlag == 1 ? "yes" : "no"));
            Assert.assertEquals(1, loopFilterDeltaFlag);
            int loopFilterDeltaUpdate = headerDecoder.decodeBit();
//            System.out.println("loopFilterDeltaUpdate: " + (loopFilterDeltaUpdate == 1 ? "yes" : "no"));
            Assert.assertEquals(1, loopFilterDeltaUpdate);
            int[] refLoopFilterDeltas = new int[MAX_REF_LF_DELTAS];
            int[] modeLoopFilterDeltas = new int[MAX_MODE_LF_DELTAS];
            for (int i = 0; i < MAX_REF_LF_DELTAS; i++) {

                if (headerDecoder.decodeBit() > 0) {
                    refLoopFilterDeltas[i] = headerDecoder.decodeInt(6);;
                    if (headerDecoder.decodeBit() > 0) // Apply sign
                        refLoopFilterDeltas[i] = refLoopFilterDeltas[i] * -1;
//                    System.out.println("ref_lf_deltas[" + i + "]: " + refLoopFilterDeltas[i]);
                }
            }
            for (int i = 0; i < MAX_MODE_LF_DELTAS; i++) {

                if (headerDecoder.decodeBit() > 0) {
                    modeLoopFilterDeltas[i] = headerDecoder.decodeInt(6);
                    if (headerDecoder.decodeBit() > 0) // Apply sign
                        modeLoopFilterDeltas[i] = modeLoopFilterDeltas[i] * -1;
//                    System.out.println("mode_lf_deltas[" + i + "]: " + modeLoopFilterDeltas[i]);
                }
            }
            int log2OfPartCnt = headerDecoder.decodeInt(2);
//            System.out.println("log2OfPartitionsCount: " + log2OfPartCnt);
            Assert.assertEquals(0, log2OfPartCnt);
            int partitionsCount = 1;
            long runningSize = 0;
            long zSize = frame.limit() - (partitionSize + headerOffset);
            ByteBuffer tokenBuffer = frame.duplicate();
            tokenBuffer.position(partitionSize + headerOffset);
            BooleanArithmeticDecoder decoder = new BooleanArithmeticDecoder(tokenBuffer, 0);

            int yacIndex = headerDecoder.decodeInt(7);
//            System.out.println("yacQi: " + yacIndex);
            int ydcDelta = ((headerDecoder.decodeBit() > 0) ? VP8Util.delta(headerDecoder) : 0);
//            System.out.println("ydcDelta: " + ydcDelta);
            int y2dcDelta = ((headerDecoder.decodeBit() > 0) ? VP8Util.delta(headerDecoder) : 0);
//            System.out.println("y2dcDelta: " + y2dcDelta);
            int y2acDelta = ((headerDecoder.decodeBit() > 0) ? VP8Util.delta(headerDecoder) : 0);
//            System.out.println("y2acDelta: " + y2acDelta);
            int chromaDCDelta = ((headerDecoder.decodeBit() > 0) ? VP8Util.delta(headerDecoder) : 0);
//            System.out.println("uvdcDelta: " + chromaDCDelta);
            int chromaACDelta = ((headerDecoder.decodeBit() > 0) ? VP8Util.delta(headerDecoder) : 0);
//            System.out.println("uvacDelta: " + chromaACDelta);
            boolean refreshProbs = headerDecoder.decodeBit() == 0;
//            System.out.println("refreshEntropyProbs: " + refreshProbs);
            QuantizationParams quants = new QuantizationParams(yacIndex, ydcDelta, y2dcDelta, y2acDelta, chromaDCDelta, chromaACDelta);

            int[][][][] coefProbs = getDefaultCoefProbs();
            for (int i = 0; i < VP8Util.BLOCK_TYPES; i++)
                for (int j = 0; j < VP8Util.COEF_BANDS; j++)
                    for (int k = 0; k < VP8Util.PREV_COEF_CONTEXTS; k++)
                        for (int l = 0; l < VP8Util.MAX_ENTROPY_TOKENS - 1; l++) {

                            if (headerDecoder.decodeBool(vp8CoefUpdateProbs[i][j][k][l]) > 0) {
                                int newp = headerDecoder.decodeInt(8);
                                coefProbs[i][j][k][l] = newp;
                                // System.out.println("coefProbs["+i+"]["+j+"]["+k+"]["+l+"] = "+newp);
                            }
                        }

            // Read the mb_no_coeff_skip flag
            int macroBlockNoCoeffSkip = (int) headerDecoder.decodeBit();
//            System.out.println("macroBlockNoCoeffSkip: " + macroBlockNoCoeffSkip);
            Assert.assertEquals(1, macroBlockNoCoeffSkip);
            int probSkipFalse = headerDecoder.decodeInt(8);
//            System.out.println("probSkipFalse: " + probSkipFalse);
            for (int mbRow = 0; mbRow < numberOfMBRows; mbRow++) {
                for (int mbCol = 0; mbCol < numberOfMBCols; mbCol++) {
                    Macroblock mb = mbs[mbRow + 1][mbCol + 1];
                    // System.out.println("mbSkipCoeff: " +headerDecoder.decodeBool(probSkipFalse));
                    if ((segmentation > 0)) {
                        throw new UnsupportedOperationException("TODO: frames with multiple segments are not supported yet");
                    }

                    if (loopFilterDeltaFlag > 0) {
                        int level = filterLevel;
                        level = level + refLoopFilterDeltas[0];
                        level = (level < 0) ? 0 : (level > 63) ? 63 : level;
                        mb.filterLevel = level;
                    } else {
                        throw new UnsupportedOperationException("TODO: frames with loopFilterDeltaFlag <= 0 are not supported yet");
                        // mb.filterLevel = segmentQuants.getSegQuants()[mb.segmentId].getFilterStrength();
                        // logger.error("TODO:");
                    }

                    if (macroBlockNoCoeffSkip > 0) {
                        mb.skipCoeff = headerDecoder.decodeBool(probSkipFalse);
                    }
                    mb.lumaMode = headerDecoder.readTree(keyFrameYModeTree, keyFrameYModeProb);
                    // 1 is added to account for non-displayed framing macroblocks, which are used for prediction only.
                    if (mb.lumaMode == SubblockConstants.B_PRED) {
                        for (int sbRow = 0; sbRow < 4; sbRow++) {
                            for (int sbCol = 0; sbCol < 4; sbCol++) {

                                Subblock sb = mb.ySubblocks[sbRow][sbCol];
                                Subblock A = sb.getAbove(VP8Util.PLANE.Y1, mbs);

                                Subblock L = sb.getLeft(VP8Util.PLANE.Y1, mbs);

                                sb.mode = headerDecoder.readTree(SubblockConstants.subblockModeTree, SubblockConstants.keyFrameSubblockModeProb[A.mode][L.mode]);

                            }
                        }

                    } else {
                        int fixedMode;

                        switch (mb.lumaMode) {
                        case SubblockConstants.DC_PRED:
                            fixedMode = SubblockConstants.B_DC_PRED;
                            break;
                        case SubblockConstants.V_PRED:
                            fixedMode = SubblockConstants.B_VE_PRED;
                            break;
                        case SubblockConstants.H_PRED:
                            fixedMode = SubblockConstants.B_HE_PRED;
                            break;
                        case SubblockConstants.TM_PRED:
                            fixedMode = SubblockConstants.B_TM_PRED;
                            break;
                        default:
                            fixedMode = SubblockConstants.B_DC_PRED;
                            break;
                        }
                        for (int x = 0; x < 4; x++)
                            for (int y = 0; y < 4; y++)
                                mb.ySubblocks[y][x].mode = fixedMode;
                    }
                    mb.chromaMode = headerDecoder.readTree(VP8Util.vp8UVModeTree, VP8Util.vp8KeyFrameUVModeProb);
                }
            }
                
            for (int mbRow = 0; mbRow < numberOfMBRows; mbRow++) {
                for (int mbCol = 0; mbCol < numberOfMBCols; mbCol++) {
                    Macroblock mb = mbs[mbRow+1][mbCol+1];
                    mb.decodeMacroBlock(mbs, decoder, coefProbs);
                    mb.dequantMacroBlock(mbs, quants);
                }
            }
            
            if (filterType > 0 && filterLevel != 0){
                if (filterType == 2) {
                    FilterUtil.loopFilterUV(mbs, sharpnessLevel, keyFrame);
                    FilterUtil.loopFilterY(mbs, sharpnessLevel, keyFrame);
                } else if (filterType == 1) {
//                    loopFilterSimple(frame);
                }
            }
                
        }

        public BufferedImage getBufferedImage() {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            WritableRaster imRas = bi.getWritableTile(0, 0);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int c[] = new int[3];
                    int yy, u, v;
                    Macroblock mb = mbs[(y >> 4)+1][(x >> 4)+1];
                    int lumaSbRow = (y&0x0F)>>2; // (y 16) / 4;
                    int lumaSbCol = (x&0x0F)>>2; // (x % 16) / 4;
                    int lumaPRowOffset = y&0x03<<2; //y % 4;
                    int lumaPCol = x&0x03; //x % 4;
                    yy = mb.ySubblocks[lumaSbRow][lumaSbCol].val[lumaPRowOffset + lumaPCol];
                    int chromaSbRow = ((y>>1)&0x07)>>2; // ((y / 2) % 8) / 4;
                    int chromaSbCol = ((x>>1)&0x07)>>2; // ((x / 2) % 8) / 4;
                    int chromaPRowOffset = (y>>1)&0x03<<2; // (y / 2) % 4;
                    int chromaPCol = (x>>1)&0x03; // (x / 2) % 4;
                    u = mb.uSubblocks[chromaSbRow][chromaSbCol].val[chromaPRowOffset + chromaPCol];
                    v = mb.vSubblocks[chromaSbRow][chromaSbCol].val[chromaPRowOffset + chromaPCol];
                    
                    c[0] = VP8Util.QuantizationParams.clip255((int) (1.164 * (yy - 16) + 1.596 * (v - 128)));
                    c[1] = VP8Util.QuantizationParams.clip255((int) (1.164 * (yy - 16) - 0.813 * (v - 128) - 0.391 * (u - 128)));
                    c[2] = VP8Util.QuantizationParams.clip255((int) (1.164 * (yy - 16) + 2.018 * (u - 128)));

                    imRas.setPixel(x, y, c);
                }
            }
            return bi;
        }
        
        
        
        public Picture getPicture() {
            Picture p = Picture.create(width, height, ColorSpace.YUV420);

            int[] luma = p.getPlaneData(0);
            //            int strideLuma = p.getPlaneWidth(0);

            int[] cb = p.getPlaneData(1);
            int[] cr = p.getPlaneData(2);
            //            int strideChroma = p.getPlaneWidth(1);
            int mbWidth = getMacroblockCount(width);
            int mbHeight = getMacroblockCount(height);
            int strideLuma = mbWidth*16;
            int strideChroma = mbWidth*8;
            
            for (int mbRow = 0; mbRow < mbHeight; mbRow++) {
                for (int mbCol = 0; mbCol < mbWidth; mbCol++) {
                    Macroblock mb = mbs[mbRow+1][mbCol+1];

                    for (int lumaRow=0;lumaRow<4;lumaRow++)
                        for(int lumaCol=0;lumaCol<4;lumaCol++)
                            for (int lumaPRow=0;lumaPRow<4;lumaPRow++)
                                for (int lumaPCol=0;lumaPCol<4;lumaPCol++){
                                    int y = (mbRow<<4) + (lumaRow<<2) + lumaPRow;
                                    int x = (mbCol<<4) + (lumaCol<<2) + lumaPCol;
                                    if (x >= strideLuma || y >= luma.length/strideLuma)
                                        continue;
                                    
                                    int yy = mb.ySubblocks[lumaRow][lumaCol].val[lumaPRow * 4 + lumaPCol];
                                    luma[strideLuma*y + x] = yy;
                                }

                    for (int chromaRow=0;chromaRow<2;chromaRow++)
                        for(int chromaCol=0;chromaCol<2;chromaCol++)
                            for (int chromaPRow=0;chromaPRow<4;chromaPRow++)
                                for (int chromaPCol=0;chromaPCol<4;chromaPCol++){
                                    int y = (mbRow<<3) + (chromaRow<<2) + chromaPRow;
                                    int x = (mbCol<<3) + (chromaCol<<2) + chromaPCol;
                                    if (x >= strideChroma || y >= cb.length/strideChroma)
                                        continue;
                                    
                                    int u = mb.uSubblocks[chromaRow][chromaCol].val[ chromaPRow * 4 + chromaPCol];
                                    int v = mb.vSubblocks[chromaRow][chromaCol].val[ chromaPRow * 4 + chromaPCol];
                                    cb[strideChroma*y + x] = u;
                                    cr[strideChroma*y + x] = v;
                                }
                }
            }
            return p;
        }
        
        public static String printHexByte(byte b) {
            return "0x" + Integer.toHexString(b & 0xFF);
        }
    }