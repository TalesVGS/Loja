package br.com.lojabackend.core.Controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
@RequestMapping("/images")
public class ImageController {

    @GetMapping("{pid}")
    public void downloadImage(@PathVariable("pid") String pid, HttpServletResponse response) {
        try {
            File fileToDownload = new File("D:/workspace/loja/back-end/uploads-imagens/" + pid);

            try (InputStream inputStream = new FileInputStream(fileToDownload)) {
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment; filename=" + pid);
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
            }
        } catch (Exception e) {
            throw new RuntimeException((e));
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(
            @RequestParam("pid") String pid, //photo id
            @RequestParam("file") MultipartFile file
            ) {

        if(file.isEmpty()) {
            throw new RuntimeException("File given is not valid!");
        }

        String folder = "D:/workspace/loja/back-end/uploads-imagens/";

        try {
            Path pathFolder = Paths.get(folder);
            Files.createDirectories(pathFolder);

            Path pathFile = Paths.get(folder + pid);
            Files.write(pathFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
