import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Names extends RozetkaParsing {
    protected static ArrayList<String> namesOfRefrigerators() throws Exception {
        Document doc = connect();
        Elements divs_name = doc.getElementsByAttributeValue("class", "g-i-tile-i-title clearfix");
        ArrayList<String> names = new ArrayList<String>();

        divs_name.forEach(div_el -> {
            Element aElement = div_el.child(0);
            String nm = aElement.text();
            if (nm.contains("/")) {
                nm = nm.split("/")[0];
            } else if (nm.contains(" + ") && !nm.contains("/")) {
                nm = nm.split("\\+")[0];
            }
            names.add(nm);
        });
        return names;
    }
}
