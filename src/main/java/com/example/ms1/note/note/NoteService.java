package com.example.ms1.note.note;

import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    public Note saveDefaultNote() {
        Note note = new Note();
        note.setTitle("new title..");
        note.setContent("");
        note.setCreateDate(LocalDateTime.now());

        return noteRepository.save(note);
    }

    public Note getNote(long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public void save(Note note) {
        noteRepository.save(note);
    }

    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }


    public List<Note> getSortedListByCreateDate(Long notebookId) {
        Notebook notebook = new Notebook();
        notebook.setId(notebookId);
        return noteRepository.findByNotebookOrderByCreateDateDesc(notebook);
    }

    public List<Note> getSortedListByTitle(Long notebookId) {
        Notebook notebook = new Notebook();
        notebook.setId(notebookId);
        return noteRepository.findByNotebookOrderByTitle(notebook);
    }

    public List<Note> getNoteListByNotebook(Long notebookId) {
        Notebook notebook = new Notebook();
        notebook.setId(notebookId);
        return noteRepository.findByNotebook(notebook);
    }

}
