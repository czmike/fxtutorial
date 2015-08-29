package de.cofinpro.portfolio.model;

/**
 * Created by mczadek on 28.08.2015.
 */

import java.time.LocalDate;
import java.time.LocalDateTime;


import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Model class for a Person.
 *
 * @author Michael Czadek
 */
public class Wertpapier {

    private final StringProperty name;
    private final StringProperty isin;
    private final DoubleProperty preis;
    private final StringProperty ticker;
    //private final ObjectProperty<LocalDateTime> lastTime;

    /**
     * Default constructor.
     */
    public Wertpapier() {
        this(null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param name
     * @param isin
     * @param preis
     * @param ticker
      */
    public Wertpapier(String name, String isin, Double preis,String ticker ) {
        this.name = new SimpleStringProperty(name);
        this.isin = new SimpleStringProperty(isin);
        if(preis != null) {
            this.preis = new SimpleDoubleProperty(preis);
        }
        else {
            this.preis = new SimpleDoubleProperty(0.0);
        }

        this.ticker = new SimpleStringProperty(ticker);
        //this.lastTime  = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getIsin() {
        return isin.get();
    }

    public void setIsin(String isin) {
        this.isin.set(isin);
    }

    public StringProperty isinProperty() {
        return isin;
    }

    public double getPreis() {
        return this.preis.get();
    }

    public void setPreis(double preis) {
        this.preis.set(preis);
    }

    public DoubleProperty preisProperty() {
        return preis;
    }

    public String getTicker() {return ticker.get();}

    public void setTicker(String ticker){this.ticker.set(ticker);}

    public StringProperty tickerProperty() {return ticker;}

 }
