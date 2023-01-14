package com.example.cardspending;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;

import com.example.cardspending.Student;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NfcUtil {
    private static String TAG = "NfcUtil";

    /**
    读取NFC内容
     */
    public static String[] read(Tag tag){
        String[] s_blocks = new String[20];
        MifareClassic mif = MifareClassic.get(tag);
        int ttype = mif.getType();
        Log.d(TAG, "MifareClassic tag type: " + ttype);
        int tsize = mif.getSize();
        Log.d(TAG, "tag size: " + tsize);
        int s_len = mif.getSectorCount();
        Log.d(TAG, "tag sector count: " + s_len);
        int b_len = mif.getBlockCount();
        Log.d(TAG, "tag block count: " + b_len);
        try {
            mif.connect();
            if (mif.isConnected()){
                for(int i=0; i< s_len; i++){
                    boolean isAuthenticated = false;
                    if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
                        isAuthenticated = true;
                    } else if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                        isAuthenticated = true;
                    } else if (mif.authenticateSectorWithKeyA(i,MifareClassic.KEY_NFC_FORUM)) {
                        isAuthenticated = true;
                    } else {
                        Log.d("TAG", "Authorization denied ");
                    }

                    if(isAuthenticated) {
                        int block_index = mif.sectorToBlock(i);
                        Log.e(TAG, String.valueOf(block_index));
                        byte[] block = mif.readBlock(block_index);
                        String s_block = new String(block,"UTF-8");
                        s_blocks[i] = s_block;
                        Log.d(TAG, s_block);
                    }
                }
            }
            mif.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s_blocks;
    }

    /**
    向NFC中写入内容
     */
    public static void write(Tag tag, Student student){
        MifareClassic mif = MifareClassic.get(tag);
        int ttype = mif.getType();
        Log.d(TAG, "MifareClassic tag type: " + ttype);
        int tsize = mif.getSize();
        Log.d(TAG, "tag size: " + tsize);
        int s_len = mif.getSectorCount();
        Log.d(TAG, "tag sector count: " + s_len);
        int b_len = mif.getBlockCount();
        Log.d(TAG, "tag block count: " + b_len);
        try {
            mif.connect();
            if (mif.isConnected()){
                for(int i=0; i< 11; i++){
                    boolean isAuthenticated = false;
                    if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
                        isAuthenticated = true;
                    } else if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                        isAuthenticated = true;
                    } else if (mif.authenticateSectorWithKeyA(i,MifareClassic.KEY_NFC_FORUM)) {
                        isAuthenticated = true;
                    } else {
                        Log.d("TAG", "Authorization denied ");
                    }

                    if(isAuthenticated) {
                        int block_index = mif.sectorToBlock(i);
                        Log.e(TAG, String.valueOf(block_index));
                        switch (i){
                            case 1:
                                mif.writeBlock(block_index,checkString(student.getId()).getBytes(StandardCharsets.UTF_8));
                                break;
                            case 2:
                                mif.writeBlock(block_index,checkString(student.getPassword()).getBytes(StandardCharsets.UTF_8));
                                break;
                            case 3:
                                mif.writeBlock(block_index,checkString(student.getSurplus()).getBytes(StandardCharsets.UTF_8));
                                break;
                            case 4:
                                mif.writeBlock(block_index,checkString(student.getElectricity()).getBytes(StandardCharsets.UTF_8));
                                break;
                            case 5:
                                mif.writeBlock(block_index,checkString(student.getWater()).getBytes(StandardCharsets.UTF_8));
                                break;
                            case 6:
                                mif.writeBlock(block_index,checkString(student.getIsHelp()).getBytes(StandardCharsets.UTF_8));
                                break;
                        }
                    }
                }
            }
            mif.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 规范写入数据 */
    private static String checkString(String s){
        while (s.length()<16){
            s = s+" ";
        }
        return s;
    }




}





