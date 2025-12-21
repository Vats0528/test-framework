package com.test.controllers;

import com.framework.annotation.*;
import com.framework.util.UploadedFile;

@Controller
public class UploadController {

    // POST - endpoint to receive uploaded file and a description
    @PostMapping("/upload")
    public String uploadFile(UploadedFile file, @RequestParam("description") String description) {
        if (file == null) {
            return "Aucun fichier reçu";
        }
        String info = String.format("Fichier reçu: name=%s, filename=%s, size=%d bytes, desc=%s",
                file.getFieldName(), file.getFileName(), file.getContent().length, description == null ? "" : description);
        return info;
    }

    // POST - accept multiple files with same field name
    @PostMapping("/uploadMultiple")
    public String uploadMultiple(UploadedFile[] photos) {
        if (photos == null || photos.length == 0) return "Aucun fichier reçu";
        StringBuilder sb = new StringBuilder();
        sb.append("Reçu ").append(photos.length).append(" fichier(s):\n");
        for (UploadedFile f : photos) {
            sb.append(String.format("- %s (%d bytes)\n", f.getFileName(), f.getContent().length));
        }
        return sb.toString();
    }

}
