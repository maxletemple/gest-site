package fr.mletemple;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WebPage {

    private Document template;

    private String path;
    private String title;
    private List<String> stylesheets;
    private Document page;
    private File file;
    private Element mainElement;

    /**
     *
     * @param data index 0: path du fichier, index 1, 2, 3, ... : path des stylesheets
     * @param templatePath path du template (avec root)
     */
    public WebPage(List<String> data, String templatePath){
        loadTemplate(templatePath);
        this.page = this.template.clone();
        this.path = data.get(0);
        this.stylesheets = new ArrayList<String>(data.subList(1, data.size()));
        this.file = new File(this.path);
        try {
            this.page = Jsoup.parse(this.file, "utf-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.title = this.page.getElementsByTag("title").first().val();
        this.mainElement =this.page.getElementsByTag("main").first();
        Element headContent = page.getElementsByTag("head").first();
        for(Element e : headContent.getElementsByTag("link")){
            System.out.println(e.attr("rel"));
            System.out.println(!this.stylesheets.contains(e.attr("href")));
            if (e.attr("rel").equals("stylesheet") && !this.stylesheets.contains(e.attr("href"))){
                this.stylesheets.add(e.attr("href"));
            }
        }
        buildPage();
    }


    // Clone du template, ajout du titre, des stylesheets, du main à this.page
    public void buildPage(){
        //clone
        this.page = template.clone();
        page.outputSettings().indentAmount(3);
        Element headContent = page.getElementsByTag("head").first();
        //stylesheets
        for (String stylesheet : stylesheets){
            Element element = new Element("link");
            element.attr("rel", "stylesheet");
            element.attr("href", stylesheet);
            headContent.appendChild(element);

        }
        //titre
        headContent.getElementsByTag("title").first().val(this.title);

    }

    // Modification du fichier
    public void printInFile(){
        try {
            this.file.delete();
            this.file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.file));
            bw.write(this.page.outerHtml());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStylesheet(String path){
        this.stylesheets.add(path);
    }

    public Document getPage(){
        return this.page;
    }

    //chargement du template dans template
    private void loadTemplate(String path){
        File templateFile = new File(path);
        try {
            template = Jsoup.parse(templateFile, "utf-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
