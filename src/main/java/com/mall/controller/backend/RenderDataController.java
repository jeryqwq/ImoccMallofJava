package com.mall.controller.backend;

import com.mall.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/renderData")
public class RenderDataController {
    @RequestMapping("/UserSex")
    public ServerResponse getUserSexData(){

        return  null;
    }
}
