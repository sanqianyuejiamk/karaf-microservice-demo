package com.mengka.springboot.controller;

import com.mengka.springboot.dao.domain.BookDO;
import com.mengka.springboot.dao.persistence.BookDOMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * @author huangyy
 * @description
 * @data 2017/02/19.
 */
@Controller
@RequestMapping("/v1")
public class CommonController {

    static final Logger logger = LogManager.getLogger(CommonController.class);

    @Autowired
    private BookDOMapper bookDOMapper;

    @RequestMapping("/rate")
    public String product(Map<String, Object> model, Long id){
        logger.info("CommonController rate id = {}",id);
        model.put("list",null);

        //add
        BookDO bookDO = new BookDO();
        bookDO.setName("数学");
        bookDO.setPrice(100);
        bookDO.setTenantId("2001");
        bookDOMapper.insert(bookDO);
        logger.info("CommonController add a new book! id = {}",bookDO.getId());

        return "product_rate";
    }
}
