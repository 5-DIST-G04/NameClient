package com.distributed.ta;

import java.io.IOException;
import java.nio.file.*;

public class FileChangeListener extends Thread{
    WatchService watchService;
    Path path;

    public FileChangeListener(){

    }

    @Override
    public void run() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            path = Paths.get("src/main/java/com/distributed/data");
            WatchKey watchKey = path.register(
                    watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                }
                key.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void handleChange(String kind, String context){
        switch (kind){
            case "ENTRY_CREATE":{

            }break;
            case "ENTRY_DELETE":{

            }break;
            case "ENTRY_MODIFY":{

            }break;
        }
    }

}
