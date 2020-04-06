package cz.sm.ng.core;


import com.google.gson.annotations.Expose;
import cz.sm.ng.core.libs.utils.MathUtils;
import cz.sm.ng.core.libs.utils.math.AngleType;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Tato trieda reprezentuje len SMER, alebo tiez adresuje bod na povrchu gule pomocou dvoch uhlov - fi a theta.
 *
 * SphericalAddress moze vyjadrovat smer polopriamky, ktora vedie zo stredu gule do bodu na povrchu jednotkovej gule,
 * ktory je popisany touto instanciou.
 *
 * @author Roman Stoklasa
 */

@Embeddable
public class SphericalAddress implements Serializable
{



//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * Horizontalny uhol ("zemepisna dlzka") - (kladny smer dolava).
     *
     * Tento atribut je read-only, aby sa nemohli "injektovat" upravene hodnoty do ziskanej instancie tejto triedy (napr. z radaru).
     */
    @Expose
    private double fi;


    /**
     * Vertikalny uhol ("zemepisna sirka") - (kladny smer dohora).
     *
     * Tento atribut je read-only, aby sa nemohli "injektovat" upravene hodnoty do ziskanej instancie tejto triedy (napr. z radaru).
     */
    @Expose
    private double theta;



    /**
     * Priznak, ci su hodnoty fi a theta uvedene v stupnoch alebo radianoch.
     */
    @Expose
    private AngleType degOrRad = AngleType.RAD;




//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * Zakladny konstruktor - vytvori instanciu (0, 0).
     */
    public SphericalAddress()
    {
        this(0, 0);
    }


    /**
     * Konsturktor, ktory vytvori instanciu Sferickych suradnic uvedenych v stupnoch!
     * @param fi
     * @param theta
     */
    public SphericalAddress(double fi, double theta)
    {
        this(fi, theta, AngleType.DEG);
    }



    /**
     * Konstruktor pre zadane uhly fi a theta.
     *
     * @param fi	Horizontalny uhol (kladny smer dolava)
     * @param theta Vertikalny uhol (kladny smer dohora)
     * @param degOrRad
     */
    public SphericalAddress(double fi, double theta, AngleType degOrRad)
    {
        this.fi = fi;
        this.theta = theta;
        this.degOrRad = degOrRad;
    }


// ======================================================================================

    public SphericalAddress(SphericalCoordinates coords)
    {
        this(coords.getFi(), coords.getTheta(), coords.getDegOrRad());
    }


// ======================================================================================


    /**
     * Factory metoda na vytvorenie instancie z geograficky zadaneho smeru -- v geografickom heading-u (0..360, 0 na sever, 90 na vychod)
     * a elevacie (kladna smerom dohora - stupanie).
     *
     *
     * @param headingDeg
     * @param elevationDeg
     * @return
     */
    public static SphericalAddress fromGeograficalOrientation(double headingDeg, double elevationDeg)
    {
        return new SphericalAddress(MathUtils.geographicalToMathematicalAngleDeg(headingDeg), elevationDeg, AngleType.DEG);
    }



// ======================================================================================


    /**
     * Vrati "horizontalny" uhol (zemepisnu dlzku).
     *
     * Tato hodnota je read-only, aby sa nemohli "injektovat" upravene hodnoty do ziskanej instancie tejto triedy (napr. z radaru)
     *
     * @return
     */
    public double getFi()
    {
        return fi;
    }


// ======================================================================================


    /**
     * Vrati "vertikalny uhol" (zemepisnu sirku).
     *
     * Tato hodnota je read-only, aby sa nemohli "injektovat" upravene hodnoty do ziskanej instancie tejto triedy (napr. z radaru)
     *
     * @return
     */
    public double getTheta()
    {
        return theta;
    }

// ======================================================================================


    public SphericalCoordinates getPointOnUnitSphere(AngleType angleType)
    {
        return new SphericalCoordinates(1, this.getFi(), this.getTheta(), angleType);
    }


// ======================================================================================


    @Override
    public String toString()
    {
        return "SphericalAddress{" + "f=" + fi + ", t=" + theta + '}';
    }


// ======================================================================================


    /**
     * Odcita other koordinaty od aktualnej instancie SphericalAddress a vrati NOVU instanciu kde je zaznamenany rozdiel
     * (this.fi - other.fi, this.theta - other.theta).
     *
     * Vysledok urcuje, o kolko sa musi natocit OTHER orientacia (adresa), aby mala rovnaky smer ako THIS!
     *
     * @param other
     * @return
     */
    public SphericalAddress minus(SphericalAddress other)
    {
        if (this.degOrRad != other.degOrRad) {
            throw new IllegalArgumentException("DEG/RAD error - tato instancia je v " + this.degOrRad + ", 'other' je v " + other.degOrRad );
        }

        return new SphericalAddress(this.getFi() - other.getFi(), this.getTheta() - other.getTheta(), this.degOrRad);
    }



}

