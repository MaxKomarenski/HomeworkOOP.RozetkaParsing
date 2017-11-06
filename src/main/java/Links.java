import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Links extends RozetkaParsing {
    protected static ArrayList<String> linksofRefrigerators() throws Exception {
        Document doc = connect();
        Elements divs_name = doc.getElementsByAttributeValue("class", "g-i-tile-i-title clearfix");
        ArrayList<String> links = new ArrayList<String>();

        divs_name.forEach(div_el -> {
            Element aElement = div_el.child(0);
            links.add(aElement.attr("href"));

        });
        return links;
    }
}
