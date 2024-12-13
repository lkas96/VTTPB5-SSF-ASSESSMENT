package vttp.batch5.ssf.noticeboard.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// Use this class to write your request handlers

@Controller
@RequestMapping("")
public class NoticeController {
    @Autowired
    NoticeService ns;

    @GetMapping("")
    public String displayNoticeForm(Model model) {
        Notice n = new Notice();
        model.addAttribute("notice", n);
        return "notice";
    }

    @PostMapping("/notice")
    // @ResponseBody
    public String processForm(@Valid @ModelAttribute("notice") Notice notice, BindingResult result, Model model) {

        //Testing
        System.out.println();
        System.out.println();
        System.out.println("CONTROLLER NOTICE OBJECT HAS BEEN PASSED IN TESTING---------------------------------------");
        System.out.println(notice.toString());
        System.out.println(notice.getCategories());
        System.out.println("CONTROLLER NOTICE OBJECT HAS BEEN PASSED IN TESTING---------------------------------------");
        System.out.println();
        System.out.println();

        // Form validation check else show direct back to the form
        // to show error on whatever fields
        if (result.hasErrors()) {
            return "notice";
        }

        // Else process the form
        List<String> successfullyPosted = ns.postToNoticeServer(notice);

        System.out.println(successfullyPosted);

        if (successfullyPosted.get(0).toLowerCase() == "success") {
            // Show success view2
            model.addAttribute("noticeId", successfullyPosted.get(1));
            return "success";
        } else {
            model.addAttribute("url", "");
            return "refused";
        }
    }

    @RequestMapping(path = "", produces = "application/json")
    public String requestMethodName(@RequestParam String param) {
        return new String();
    }

}
