package cw.sprboot.dpiqb.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RequestMapping("/file")
@Controller
public class FileController {
  @PostMapping("/upload")
  public ModelAndView postReceiveFile (@RequestParam(name="file", required = true) MultipartFile file){
    ModelAndView result = new ModelAndView("upload-file");
    try {
      int fileSize = file.getBytes().length;
      result.addObject("fileSize", fileSize);
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
