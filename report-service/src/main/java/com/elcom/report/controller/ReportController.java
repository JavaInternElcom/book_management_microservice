package com.elcom.report.controller;

import com.elcom.report.repository.library.BookCustomizeRepository;
import com.elcom.report.repository.log.BookLoanCustomizeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReportController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private BookCustomizeRepository bookCustomizeRepository;

    @Autowired
    private BookLoanCustomizeRepository bookLoanCustomizeRepository;


}
