package com.example.ms1.note.note;

import com.example.ms1.note.MainDataDto;
import com.example.ms1.note.MainService;
import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookRepository;
import com.example.ms1.note.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books/{notebookId}/notes")
public class NoteController {

    private final NoteService noteService;
    private final MainService mainService;
    private final NoteRepository noteRepository;
    private final NotebookRepository notebookRepository;

    @PostMapping("/write")
    public String write(@PathVariable("notebookId") Long notebookId) {

        mainService.addToNotebook(notebookId);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable("notebookId") Long notebookId, @PathVariable("id") Long id) {

        MainDataDto mainDataDto = mainService.getMainData(notebookId, id);
        model.addAttribute("mainDataDto", mainDataDto);

        return "main";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable("notebookId") Long notebookId, @PathVariable("id") Long id, String title, String content) {
        Note note = noteService.getNote(id);

        if(title.trim().length() == 0) {
            title = "제목 없음";
        }

        note.setTitle(title);
        note.setContent(content);

        noteService.save(note);
        return "redirect:/books/%d/notes/%d".formatted(notebookId, id);
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("notebookId") Long notebookId, @PathVariable("id") Long id) {

        noteService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Object> searchNotes(@RequestParam String keyword) {
        List<Notebook> notebooks = notebookRepository.findByNameContaining(keyword);
        List<Note> notes = noteRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return List.of(notebooks, notes);
    }

    @GetMapping("/sort")
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
