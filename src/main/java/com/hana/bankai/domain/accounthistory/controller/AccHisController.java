package com.hana.bankai.domain.accounthistory.controller;

import com.hana.bankai.domain.accounthistory.service.AccHisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccHisController {

    final private AccHisService accHisService;
}
