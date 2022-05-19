package com.company.xml;

import com.company.currency.Currency;
import com.company.currency.PositionType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlConfiguration {

    public static final String CURRENCY_CONFIGURATION_XML = "currency-configuration.xml";

    public List<Currency> readCurrencyConfiguration() throws ParserConfigurationException {
        List<Currency> currencies = new ArrayList<>();
        initDefaultDollarsCurrency(currencies);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            File currenciesFileConfiguration = getResourceFile(CURRENCY_CONFIGURATION_XML);
            Document doc = db.parse(currenciesFileConfiguration);
            doc.normalizeDocument();

            Element rootElement = doc.getDocumentElement();
            NodeList list = rootElement.getElementsByTagName("currency");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);

                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                Element element = (Element) node;
                Currency currency = readCurrency(element);

                if (currency.getSymbol().equals("$"))
                    continue;

                currencies.add(currency);
            }
        } catch (ParserConfigurationException | URISyntaxException | SAXException | IOException e) {
            throw new ParserConfigurationException(e.getMessage());
        }

        return currencies;
    }

    private Currency readCurrency(Element element) {
        String name = element.getAttribute("name");
        String symbol = element.getAttribute("symbol");
        PositionType positionType = PositionType.valueOf(element.getAttribute("position"));
        double value = Double.parseDouble(element.getAttribute("value"));

        return new Currency(name, symbol, positionType, value);
    }

    private void initDefaultDollarsCurrency(List<Currency> currencies) {
        currencies.add(new Currency("dollars", "$", PositionType.PREFIX, 1));
    }

    private File getResourceFile(String path) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(CURRENCY_CONFIGURATION_XML);
        if (resource == null) {
            throw new IllegalArgumentException("Resource on file path " + path + " not found");
        } else {
            return new File(resource.toURI());
        }
    }
}
