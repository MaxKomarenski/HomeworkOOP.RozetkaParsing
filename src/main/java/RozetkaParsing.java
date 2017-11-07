import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RozetkaParsing {

    protected static Document connect() throws Exception {
        return Jsoup.connect("https://bt.rozetka.com.ua/refrigerators/c80125/filter/").get();
    }

    private static void writeIntoCSVfile(ArrayList<String> nameOfFile, ArrayList<String> nameOfPeople, Map<String, String> dictionary, int el) throws FileNotFoundException, UnsupportedEncodingException {
        String names_of_fiels = "data/" + nameOfFile.get(el) + ".csv";
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(names_of_fiels), "Cp1251"));
        for(int t = 0; t < nameOfPeople.size(); t++){
            pw.println( nameOfPeople.get(t) + " -> Comment:  " + dictionary.get(nameOfPeople.get(t)));
            pw.println();
        }
    }


    public static void main(String[] args) throws Exception {

        ArrayList<String> names;
        names = Names.namesOfRefrigerators();
        ArrayList<String> links;
        links = Links.linksofRefrigerators();
        File dir = new File("data");
        dir.mkdir();

        for (int el = 0; el < names.size(); el++) {
            String str_link = links.get(el);
            str_link = str_link + "comments/page=1/";//go in page with comments
            Document pages = Jsoup.connect(str_link).get();
            Elements count_of_pages = pages.getElementsByAttributeValue("class", "novisited paginator-catalog-l-link");

            ArrayList<String> digits = new ArrayList<>();

            count_of_pages.forEach(i -> {
                digits.add(i.text());
            });

            int last_pages = Integer.parseInt(digits.get(digits.size() - 1));// Now I know how many pages of reviews are there.

            ArrayList<String> reviews_lst = new ArrayList<>();
            ArrayList<String> names_lst = new ArrayList<>();
            Map<String, String> dict_names_reviews = new HashMap<>();

            for (int j = 1; j <= last_pages; j++) {
                String str_link_com = links.get(el) + "comments/page=" + Integer.toString(j) + "/";
                Document reviews_and_names = Jsoup.connect(str_link_com).get();

                Elements reviews = reviews_and_names.getElementsByAttributeValue("class", "pp-review-text-i");
                Elements name = reviews_and_names.getElementsByAttributeValue("class", "pp-review-author-name");


                reviews.forEach(rev -> {
                    if (!(rev.text().contains("Достоинства: ") || rev.text().contains("Недостатки: "))) {
                        reviews_lst.add(rev.text());
                    }
                });

                name.forEach(name_el -> {
                    names_lst.add(name_el.text());
                });

                for (int el_dict = 0; el_dict < names_lst.size(); el_dict++) {
                    dict_names_reviews.put(names_lst.get(el_dict), reviews_lst.get(el_dict));
                }
            }

            writeIntoCSVfile(names, names_lst, dict_names_reviews, el);



        }

    }
}
