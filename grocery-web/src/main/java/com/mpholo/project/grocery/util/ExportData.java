package com.mpholo.project.grocery.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/09/17
 IDE IntelliJ IDEA
 *******************************************************************/

@Slf4j
@Component
public class ExportData<T> {

    @Value("${file.destination}")
    private String destination;

    public  void writeToFile(List<T> items) {

        String d=destination;
        File destination = new File(d);
        try(FileWriter writer = new FileWriter(destination)) {

            items.stream().forEach(i-> {
                try {
                    writer.write(i.toString()+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            log.info("Total items exported {}",items.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
