package com.example.ms1.note.notebook;

import com.example.ms1.note.note.NoteRepository;
import com.example.ms1.note.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotebookService {
    private final NotebookRepository notebookRepository;

    public List<Notebook> getNotebookList(){
        List<Notebook> notebookList = notebookRepository.findAll();
        return notebookList;
    }

    public Notebook getNotebook(long id){
        Notebook notebook = notebookRepository.findById(id).get();
        return notebook;
    }

    public Notebook save(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public void delete(Long id){
        notebookRepository.deleteById(id);
    }

    public Notebook renameNotebook(Long notebookId, String newName) {
        Notebook notebook = getNotebook(notebookId);
        notebook.setName(newName);
        return notebookRepository.save(notebook);
    }

    public List<Notebook> searchNotebooks(String query) {
        return notebookRepository.findByNameContaining(query);
    }
}