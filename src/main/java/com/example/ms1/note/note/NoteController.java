package com.example.ms1.note.note;

import com.example.ms1.note.MainDataDto;
import com.example.ms1.note.MainService;
import com.example.ms1.note.ParamHandler;
import com.example.ms1.note.notebook.Notebook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books/{notebookId}/notes")
public class NoteController {


    private final NoteService noteService;
    private final MainService mainService;

    @PostMapping("/write")
    public String write(@PathVariable("notebookId") Long notebookId, ParamHandler paramHandler) {
        Notebook notebook = mainService.getNotebook(notebookId);
        noteService.saveDefault(notebook);

        return paramHandler.getRedirectUrl("/");
    }

    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable("id") Long id, @PathVariable("notebookId") Long notebookId, ParamHandler paramHandler) {
        MainDataDto mainDataDto = mainService.getMainData(notebookId, id, paramHandler.getKeyword());
        model.addAttribute("mainDataDto", mainDataDto);

        return "main";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable("notebookId") Long notebookId, @PathVariable("id") Long id, String title, String content, ParamHandler paramHandler) {
        Note note = noteService.getNote(id);

        if (title.trim().length() == 0) {
            title = "제목 없음";
        }

        note.setTitle(title);
        note.setContent(content);

        noteService.save(note);
        return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(notebookId, id));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("notebookId") Long notebookId, @PathVariable("id") Long id, ParamHandler paramHandler) {
        noteService.delete(id);
        return paramHandler.getRedirectUrl("/");
    }

}
