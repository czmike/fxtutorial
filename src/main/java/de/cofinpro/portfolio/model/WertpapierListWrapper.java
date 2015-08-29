package de.cofinpro.portfolio.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 *
 * @author Michael Czadek
 */
@XmlRootElement(name = "wertpapiere")
public class WertpapierListWrapper {

    private List<Wertpapier> wertpapiere;

    @XmlElement(name = "wertpapier")
    public List<Wertpapier> getWertpapiere() {
        return wertpapiere;
    }

    public void setWertpapiere(List<Wertpapier> wertpapiere) {
        this.wertpapiere = wertpapiere;
    }
}