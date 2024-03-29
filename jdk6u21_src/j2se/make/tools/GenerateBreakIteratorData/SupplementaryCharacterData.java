/*
 * @(#)SupplementaryCharacterData.java	1.4 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.util.Arrays;

final class SupplementaryCharacterData {

    /**
     * Default value
     */
    private static final byte DEFAULT_VALUE = 0;
    private byte defaultValue;

    /**
     * A array which has a unicode code point(0x010000-0x10FFFF) as the upper
     * 3 bytes and a value as the remaining one byte in each data.
     */
    private int[] dataTable;

    /*
     * Counter to keep the number of data in tempTable
     */
    private int dataCount;

    /*
     * Temporary data table used until dataTable is generated.
     */
    private long[] tempTable;

    /*
     * Initila tempTable size
     */
    private static final int INITIAL_TABLE_SIZE = 150;

    /*
     * The number of entries to be appended when tempTable becomes full
     */
    private static final int ADDITIONAL_TABLE_SIZE = 10;

    /*
     * Upper limit, used as a sentinel
     */
    private static final int UPPER_LIMIT = Character.MAX_CODE_POINT + 1;

    /*
     * Mask for supplementary code points(0x010000-0x10FFFF)
     */
    private static final int CODEPOINT_MASK = 0x1FFFFF;


    public SupplementaryCharacterData(byte value) {
        defaultValue = value;
        dataCount = 0;
    }

    /**
     * Appends the given data to tempTable
     */
    public void appendElement(int start, int end, byte value) {
        if (tempTable == null) {
            tempTable = new long[INITIAL_TABLE_SIZE];
        }

        if (dataCount == tempTable.length) {
            long[] tempTempTable = new long[dataCount + ADDITIONAL_TABLE_SIZE];
            System.arraycopy(tempTable, 0, tempTempTable, 0, dataCount);
            tempTable = tempTempTable;
        }
        tempTable[dataCount++] = ((((long)start<<24) + end)<<8) + value;
    }

    /**
     * Returns the data table
     */
    public int[] getArray() {
        return dataTable;
    }

    /**
     * Creates dataTable
     */
    public void complete() {
        dataTable = generateTable();
    }

    /**
     * Generates a table from the given data
     */
    private int[] generateTable() {
        /* If tempTable is empty, put two data */
        if (dataCount == 0) {
            int[] tmpArray = new int[2];
            tmpArray[0] = composeEntry(0, DEFAULT_VALUE);
            tmpArray[1] = composeEntry(UPPER_LIMIT, DEFAULT_VALUE);
            tempTable = null;
            return tmpArray;
        } 

        Arrays.sort(tempTable, 0, dataCount);

        int[] newTempTable = new int[dataCount*2 + 3];

        int old_index = 0;
        int new_index = 0;
        int loop_count = dataCount - 1;
        long data = tempTable[old_index];
        int start = (int)((long)(data>>32)) & CODEPOINT_MASK;
        int end   = (int)(data>>8) & CODEPOINT_MASK;

        /*
         * Add an entry if the first start code point in tempTable is not
         * the minimum supplementary code point.
         */
        if (start != Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            newTempTable[new_index++] = composeEntry(Character.MIN_SUPPLEMENTARY_CODE_POINT, defaultValue);
        }

        newTempTable[new_index++] = composeEntry(start, (int)data);
        for (int i = 0; i < loop_count; i++) {
            data = tempTable[++old_index];
            int nextStart = (int)((long)(data>>32)) & CODEPOINT_MASK;

            /*
             * If the previous end code point is not equal to the previous start
             * code point and is not consecutive with the next start code point,
             * insert a filler entry before an entry for the next start code
             * point.
             */
            if (end != start && end != nextStart-1) {
                newTempTable[new_index++] = composeEntry(end + 1, defaultValue);
            }
            newTempTable[new_index++] = composeEntry(nextStart, (int)data);
            start = nextStart;
            end = (int)(data>>8) & CODEPOINT_MASK;
        }
        newTempTable[new_index++] = composeEntry(++end, defaultValue);

        /* Add an entry for a sentinel if necessary. */
        if (end < UPPER_LIMIT) {
            newTempTable[new_index++] = composeEntry(UPPER_LIMIT, defaultValue);
        }

        /* Copy data to dataTable. */
        dataTable = new int[new_index];
        System.arraycopy(newTempTable, 0, dataTable, 0, new_index);

        tempTable = null;

        return dataTable;
    }

    /**
     * Composes an entry with the given supplementary code point
     * (0x010000-0x10FFFF) for the upper 3 bytes and the given value for the
     * remaining one byte.
     */
    private int composeEntry(int codePoint, int value) {
        return (codePoint<<8) | (value&0xFF);
    }
}
