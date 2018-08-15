package plus.ojbk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import plus.ojbk.qqsms.HttpClientSms;

@RestController
public class IndexController {
	@GetMapping("/index")
	public ModelAndView index() {
		ModelAndView moAndView = new ModelAndView("/index/index");
		return moAndView;
	}
	
	@PostMapping("/qqsms")
	public String sendSms(@RequestParam("name")String name,@RequestParam("content")String content,@RequestParam("phone")String phone) {
		return HttpClientSms.smsSemder(name, content, phone);
	}
}
