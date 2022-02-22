package fr.mletemple;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static String root;
    public static final String TEMPLATES_FOLDER = "templates";
    public static final String WORK_FOLDER = "work";

    public static void main(String[] args){

        root = System.getProperty("user.dir");

        List<List<String>> files = new ArrayList<>();
        File workDir = new File(root);
        File listFile = new File(root + "/" + TEMPLATES_FOLDER +"/list.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(listFile));
            String line = reader.readLine();
            // index 0: path du fichier, index 1, 2, 3, ... path des stylesheets
            List<String> file = new ArrayList<>();
            file.add("");
            boolean didReadSS = false;
            while (line != null){
                if (line.charAt(0) == '#'){
                    if (didReadSS){
                        file.add(line.substring(1));
                    } else {
                        file.clear();
                        file.add("");
                        file.add(line.substring(1));
                    }
                    didReadSS = true;
                } else {
                    file.set(0, root + "/" + WORK_FOLDER + "/" + line);
                    files.add(new ArrayList<>(file));
                    didReadSS = false;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (List<String> file : files){
            WebPage currentPage = new WebPage(file, root + "/" + TEMPLATES_FOLDER + "/template.html");
            currentPage.printInFile();
        }



        //for (String path : files){
        //    currentPage = new WebPage(path, root + "/" + TEMPLATES_FOLDER + "/template.html");
        //    currentPage.printInFile();
        //}

        //Document template = getTemplate();
        //File currentFile = null;
        //for (String currentpath : files){
        //    currentFile = new File(currentpath);
        //    if (!currentFile.exists()){
        //        System.out.println(currentpath + " doesn't exists. Skipping to next file.");
        //    } else {
        //        try {
        //            template = getTemplate();
        //            Document doc = Jsoup.parse(currentFile, "utf-8", "");
        //            Element mainElement = doc.getElementsByTag("main").first();
        //            doc.empty();
        //            doc.appendChild(template);
        //            for (Element e : mainElement.children()){
        //                doc.getElementsByTag("main").first().appendChild(e);
        //            }
        //            currentFile.delete();
        //            currentFile.createNewFile();
        //            doc.outputSettings().indentAmount(3);
        //            BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile));
        //            bw.write(doc.child(0).outerHtml());
        //            bw.close();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //}
    }

}
