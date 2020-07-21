package com.mpholo.project.grocery.util.thymeleaf;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.annotation.PostConstruct;

@Slf4j
@Component //scanned by spring container
public class DecoupledLogicSetup {

    //==fields==
    private final SpringResourceTemplateResolver springResourceTemplateResolver;


    //== constructor ==
    public DecoupledLogicSetup(SpringResourceTemplateResolver springResourceTemplateResolver) {
        this.springResourceTemplateResolver = springResourceTemplateResolver;
    }

    //== init ==
    @PostConstruct
    public void init() {
        springResourceTemplateResolver.setUseDecoupledLogic(true);
        log.info("Decoupled template logic enabled");
    }
}
