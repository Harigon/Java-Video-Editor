package asg.jcodec.movtool;

import static asg.jcodec.movtool.Remux.hidFile;

import java.io.File;
import java.io.IOException;

import asg.jcodec.containers.mp4.MP4Util;
import asg.jcodec.containers.mp4.boxes.MovieBox;

public class WebOptimize {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Syntax: optimize <movie>");
            System.exit(-1);
        }
        File tgt = new File(args[0]);
        File src = hidFile(tgt);
        tgt.renameTo(src);

        try {
            MovieBox movie = MP4Util.createRefMovie(src);

            new Flattern().flattern(movie, tgt);
        } catch (Throwable t) {
            t.printStackTrace();
            tgt.renameTo(new File(tgt.getParentFile(), tgt.getName() + ".error"));
            src.renameTo(tgt);
        }
    }
}
