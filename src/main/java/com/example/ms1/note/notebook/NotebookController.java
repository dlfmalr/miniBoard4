package com.example.ms1.note.notebook;

import com.example.ms1.note.MainService;
import com.example.ms1.note.ParamHandler;
import com.example.ms1.note.note.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;
    private final MainService mainService;

    @PostMapping("/books/write")
    public String write(ParamHandler paramHandler) {
        mainService.saveDefaultNotebook();

        return paramHandler.getRedirectUrl("/");
    }

    @PostMapping("/groups/{notebookId}/books/write")
    public String groupWrite(@PathVariable("notebookId") Long notebookId, ParamHandler paramHandler) {
        mainService.saveGroupNotebook(notebookId);

        return paramHandler.getRedirectUrl("/");
    }


    @GetMapping("/books/{id}")
    public String detail(@PathVariable("id") Long id, ParamHandler paramHandler) {
        Notebook notebook = notebookService.getNotebook(id);
        Note note = notebook.getNoteList().get(0);

        return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(id, note.getId()));
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable("id") Long id, ParamHandler paramHandler) {
        notebookService.delete(id);
        return paramHandler.getRedirectUrl("/");
    }

    @PostMapping("/books/{id}/update")
    public String update(@PathVariable("id") Long id, String name, Long targetNoteId, ParamHandler paramHandler) {
        notebookService.update(id, name);
        return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(id, targetNoteId));
    }

    @PostMapping("/books/{id}/move")
    public String move(@PathVariable("id") Long id, Long destinationId, Long targetNoteId, ParamHandler paramHandler) {
        notebookService.move(id, destinationId);
        return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(destinationId, targetNoteId));
    }
}
