package org.openmrs.module.aihdconfigs.page.controller;


import com.asprise.ocr.Ocr;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsUtil;

import java.io.File;

public class ProcessImagesPageController {
    public void get(PageModel model){
        File docDir = new File(OpenmrsUtil.getApplicationDataDirectory() + "/patient_images");
        Ocr.setUp();
        Ocr ocr = new Ocr();
        ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
        String s = ocr.recognize(new File[] {new File(docDir+"/initial.jpg")}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_XML);
        System.out.println("Result: " + s);
        ocr.stopEngine();

        model.addAttribute("images", s);
    }
}
