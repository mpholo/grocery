package com.mpholo.project.grocery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/09/24
 IDE IntelliJ IDEA
 *******************************************************************/

@Controller
public class SnackBar {

    @GetMapping("/snackbar")
    public String snackBar() {
        return "snackbar";
    }
}
