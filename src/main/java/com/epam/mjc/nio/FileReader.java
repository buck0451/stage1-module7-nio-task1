package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileReader {

    public Profile getDataFromFile(File file) {
        Profile profile = new Profile();
        try (
            RandomAccessFile raFile = new RandomAccessFile(file, "r");
            FileChannel inChannel = raFile.getChannel();
            ) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                StringBuilder builder = new StringBuilder();
                Flag flag = Flag.NAME;
                for (int i = 0; i < buffer.limit(); i++) {
                    char ch = (char) buffer.get();

                    if (ch == '\n') {
                        switch (flag) {
                            case NAME:
                                profile.setName(builder.toString());
                                builder.delete(0, builder.length());
                                break;
                            case AGE:
                                profile.setAge(Integer.parseInt(builder.toString()));
                                builder.delete(0, builder.length());
                                break;
                            case EMAIL:
                                profile.setEmail(builder.toString());
                                builder.delete(0, builder.length());
                                break;
                            case PHONE:
                                profile.setPhone(Long.parseLong(builder.toString()));
                                builder.delete(0, builder.length());
                                break;
                        }
                    } else {
                        builder.append(ch);
                        switch (builder.toString()) {
                            case "Name: ":
                                builder.delete(0, builder.length());
                                flag = Flag.NAME;
                                break;
                            case "Age: ":
                                builder.delete(0, builder.length());
                                flag = Flag.AGE;
                                break;
                            case "Email: ":
                                builder.delete(0, builder.length());
                                flag = Flag.EMAIL;
                                break;
                            case "Phone: ":
                                builder.delete(0, builder.length());
                                flag = Flag.PHONE;
                                break;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return profile;
    }

    private enum Flag {
        NAME,
        AGE,
        EMAIL,
        PHONE
    }


}
