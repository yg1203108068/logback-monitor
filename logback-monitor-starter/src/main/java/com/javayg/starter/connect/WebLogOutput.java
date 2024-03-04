package com.javayg.starter.connect;

import java.io.IOException;
import java.io.OutputStream;

public class WebLogOutput extends OutputStream {
    OutputStream outputStream;

    protected WebLogOutput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) {
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        String str = new String(bytes);
        System.out.print("LOCAL ->" + str);
        outputStream.write(bytes);
    }
}
