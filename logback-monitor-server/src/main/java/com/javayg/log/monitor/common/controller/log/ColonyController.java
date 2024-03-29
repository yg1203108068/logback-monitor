package com.javayg.log.monitor.common.controller.log;

import com.javayg.common.entity.RegistrationParams;
import com.javayg.log.monitor.common.component.ModuleManager;
import com.javayg.log.monitor.common.entity.vo.RestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/colony")
public class ColonyController {
    @Autowired
    private ModuleManager moduleManager;

    @GetMapping("/list")
    public RestApi<List<RegistrationParams>> list() {
        return RestApi.success(moduleManager.listModule());
    }
}
