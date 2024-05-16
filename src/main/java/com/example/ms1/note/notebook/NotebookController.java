package com.example.ms1.note.notebook;

import com.example.ms1.note.MainDataDto;
import com.example.ms1.note.MainService;
import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteRepository;
import com.example.ms1.note.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;
    private final MainService mainService;
    private final NotebookRepository notebookRepository;
    private final NoteService noteService;
    private final NoteRepository noteRepository;

    @PostMapping("/books/write")
    public String write() {
        mainService.saveDefaultNotebook();
        return "redirect:/";

    }

    @PostMapping("/groups/{notebookId}/books/write")
    public String groupWrite(@PathVariable("notebookId") Long notebookId) {

        mainService.saveGroupNotebook(notebookId);
        return "redirect:/";
    }

    @GetMapping("/books/{id}")
    public String detail(@PathVariable("id") Long id) {
        Notebook notebook = notebookService.getNotebook(id);
        Note note = notebook.getNoteList().get(0);

        return "redirect:/books/%d/notes/%d".formatted(id, note.getId());
    }

    @PostMapping("books/{notebookId}/delete")
    public String delete(@PathVariable("notebookId") Long notebookId) {

        notebookService.delete(notebookId);
        return "redirect:/";
    }


    @PostMapping("/books/{id}/rename")
    public String update(@PathVariable("id") Long id, Long targetNoteId, String newName) {
        notebookService.renameNotebook(id, newName);
        return "redirect:/books/%d/notes/%d".formatted(id, targetNoteId);
    }

    @GetMapping("/get-notebook-and-note-names")
    @ResponseBody
    public List<Object> getAllNotebookAndNoteNames() {
        List<Notebook> notebooks = notebookRepository.findAll();
        List<Note> notes = noteService.getAllNotes();

        List<String> notebookNames = notebooks.stream()
                .map(Notebook::getName)
                .collect(Collectors.toList());

        List<String> noteTitles = notes.stream()
                .map(Note::getTitle)
                .collect(Collectors.toList());

        List<Object> result = new ArrayList<>();
        result.add(notebookNames);
        result.add(noteTitles);

        return result;
    }

    @GetMapping("/books/{notebookId}/notes/sorted")
    public String sortNotes(Model model, @PathVariable("notebookId") Long notebookId, @RequestParam String sortBy) {
        List<Note> notes;
        if ("title".equals(sortBy)) {
            notes = noteService.getSortedListByTitle(notebookId);
        } else if ("createDate".equals(sortBy)) {
            notes = noteService.getSortedListByCreateDate(notebookId);
        } else {
            notes = noteService.getNoteListByNotebook(notebookId);
        }
        MainDataDto mainDataDto = mainService.getMainData(notebookId, notes.isEmpty() ? null : notes.get(0).getId());
        model.addAttribute("mainDataDto", mainDataDto);
        return "main";
    }




}
