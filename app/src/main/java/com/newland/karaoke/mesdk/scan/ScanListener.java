package com.newland.karaoke.mesdk.scan;

public interface ScanListener {
    void   scanResponse(String var1);
    void   scanError();
    void   scanTimeout();
    void   scanCancel();
}
