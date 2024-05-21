package com.example.ms1.note;

import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteService;
import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final NotebookService notebookService;
    private final NoteService noteService;

    public Notebook getNotebook(Long notebookId) {
        return notebookService.getNotebook(notebookId);
    }

    public List<Notebook> getNotebookList() {
        return notebookService.getNotebookList();
    }
    public Notebook saveDefaultNotebook() {
        Notebook notebook = new Notebook();
        notebook.setName("새노트북");

        notebookService.save(notebook);

        Note note = noteService.saveDefault(notebook);
        notebook.addNote(note);

        return notebookService.save(notebook);
    }

    public MainDataDto getDefaultMainData() {
        List<Notebook> notebookList = notebookService.getNotebookList();

        if (notebookList.isEmpty()) {
            Notebook notebook = this.saveDefaultNotebook();
            notebookList.add(notebook);
        }

        Notebook targetNotebook = notebookList.get(0);
        List<Note> noteList = noteService.getNoteListByNotebook(targetNotebook);
        Note targetNote = noteList.get(0);

        MainDataDto mainDataDto = new MainDataDto(notebookList, targetNotebook, noteList, targetNote);

        return mainDataDto;
    }

    public MainDataDto getMainData(Long notebookId, Long noteId) {
        MainDataDto mainDataDto = this.getDefaultMainData();
        Notebook targetNotebook = this.getNotebook(notebookId);
        Note targetNote = noteService.getNote(noteId);

        mainDataDto.setTargetNotebook(targetNotebook);
        mainDataDto.setTargetNote(targetNote);
        mainDataDto.setNoteList(targetNotebook.getNoteList());

        return mainDataDto;
    }

    public void saveGroupNotebook(Long notebookId) {
        Notebook parent = this.getNotebook(notebookId);
        Notebook child = this.saveDefaultNotebook();
        parent.addChild(child);

        notebookService.save(parent);
    }
}
