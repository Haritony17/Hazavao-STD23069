package com.exam.hazavao.endpoint.rest.controller.health;

import com.exam.hazavao.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HazavaoController {

  private final ChatGptService chatGptService;

  @GetMapping("/hazavao")
  public String hazavao(@RequestParam String teny) throws Exception {
    return chatGptService.getMalagasyDefinition(teny);
  }
}
