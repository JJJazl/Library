package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.service.RequestService;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/request")
public class RequestController {
    private final RequestService requestService;
    private final UserService userService;

    @Autowired
    public RequestController(RequestService requestService,
                             UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping("/add/{bookId}")
    public String add(@PathVariable("bookId") long bookId) {
        /*BookReadUpdateDto bookDto = bookService.getBook(bookId);
        requestDto.setBookDto(bookDto);*/
        requestService.addRequest(bookId);
        return "redirect:/book/list";
    }

    @PreAuthorize("hasRole('ROLE_READER')")
    @GetMapping("/pageReader")
    public String getRequestedBooks(Authentication auth, Model model){
        Optional<User> user = userService.findUserByEmail(auth.getName());
        model.addAttribute("requests", requestService.getRequestedBooks(user.orElseThrow().getId()));
        return "reader/page-reader";
    }

    @PreAuthorize("hasRole('ROLE_READER')")
    @GetMapping("/getBook/{id}")
    public String getRequestInfo(@PathVariable("id") long id, Model model){
        model.addAttribute("request", requestService.getRequestById(id));
        return "reader/description-of-request";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/pageManager")
    public String getRequestedBooksWithStatusWaiting(Model model){
        //в модель добавить метод на получений доступных копий
        model.addAttribute("list", requestService.getBooksWithStatusWaiting());
        return "manager/page-manager";
    }

    @PreAuthorize("hasRole('ROLE_READER')")
    @PostMapping("/returnBookToLibrary/{id}")
    public String returnBookToLibrary(@PathVariable("id") long requestId){
        requestService.returnBookToLibrary(requestId);
        return "redirect:/request/pageReader";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/accept/{id}")
    public String acceptRequest(@PathVariable("id") long id) {
        requestService.acceptRequest(id);
        return "redirect:/request/pageManager";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/reject/{id}")
    public String rejectRequest(@PathVariable("id") long id) {
        requestService.rejectRequest(id);
        return "redirect:/request/pageManager";
    }
}
