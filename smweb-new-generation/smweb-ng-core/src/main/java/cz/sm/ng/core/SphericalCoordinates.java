package cz.sm.ng.core;

import cz.sm.ng.core.libs.utils.MathUtils;
import cz.sm.ng.core.libs.utils.math.AngleType;

/**
 * Reprezentacia sferickych suradnic.
 *
 * @author Roman Stoklasa
 */
public class SphericalCoordinates
{





//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Vzdialenost od pociatku
     */
    private double r;

    /**
     * Horizontalny uhol (uhol merany v rovine XY) (azimut).
     * V akych jednotkach je udavany uhol (stupne alebo radiany) si musi urcit aplikacia.
     */
    private double fi;


    /**
     * Vertikalny uhol od vodorovnej roviny.(elevacia)
     * V akych jednotkach je udavany uhol (stupne alebo radiany) si musi urcit aplikacia.
     */
    private double theta;



    /**
     * Priznak, ci su hodnoty fi a theta uvedene v stupnoch alebo radianoch.
     */
    private AngleType degOrRad = AngleType.RAD;


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Vytvori novu instanciu s uhlami v STUPNOCH!
     *
     * @param r
     * @param fi	Horizntalny uhol (azimut)
     * @param theta	Vertikalny uhol (elevacia)
     */
    public SphericalCoordinates(double r,  double fi, double theta) //double theta,
    {
        this(r, fi, theta, AngleType.DEG);
    }

// ======================================================================================


    /**
     * Konstruktor instancie.
     *
     * @param r
     * @param theta
     * @param fi
     * @param degOrRad
     */
    public SphericalCoordinates(double r,  double fi, double theta, AngleType degOrRad) //double theta,
    {
        this.degOrRad = degOrRad;
        this.r = r;

//		this.theta = theta;
//		this.fi = fi;
        this.setTheta(theta);
        this.setFi(fi);
    }


// ======================================================================================


    /**
     * Vrati horizontalny uhol (azimut).
     *
     * @return
     */
    public double getFi()
    {
        return fi;
    }


    /**
     * Atribut Fi je Read-only, aby sa nemohli "injektovat" upravene hodnoty do ziskanej instancie tejto triedy (napr. z radaru).
     *
     * @param fi
     */
    private void setFi(double fi)
    {
        if (this.isRad()) {
            this.fi = MathUtils.normalizeAngleSymmetricallyRad(fi);
        } else {
            this.fi = MathUtils.normalizeAngleSymmetricallyDeg(fi);
        }
    }


// ======================================================================================


    /**
     * Vrati vzdialenost od pociatku.
     * @return
     */
    public double getR()
    {
        return r;
    }


// ======================================================================================


    /**
     * Vrati vertikalny uhol (od vodorovnej roviny)
     * @return
     */
    public double getTheta()
    {
        return theta;
    }


    /**
     * Atribut Theta je read-only, aby sa nemohli "injektovat" upravene hodnoty do ziskanej instancie tejto triedy (napr. z radaru).
     *
     * @param theta
     */
    private void setTheta(double theta)
    {
        if (this.isRad()) {
            this.theta = MathUtils.normalizeAngleSymmetricallyRad(theta);
        } else {
            this.theta = MathUtils.normalizeAngleSymmetricallyDeg(theta);
        }
    }


// ======================================================================================


    /**
     * Vrati priznak, ci hodnoty su zaznamenane v stupnoch alebo radianoch.
     *
     * @return
     */
    public AngleType getDegOrRad()
    {
        return degOrRad;
    }



// ======================================================================================


    /**
     * Zodpovie dotaz, ci uhly v tejto instancii su ukladane ako radiany.
     * @return
     */
    public boolean isRad()
    {
        return (this.degOrRad == AngleType.RAD);
    }

// ======================================================================================



    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final SphericalCoordinates other = (SphericalCoordinates) obj;
        final int decimalPlaces = 6;

        if (Double.doubleToLongBits(MathUtils.round(this.r, decimalPlaces)) != Double.doubleToLongBits(MathUtils.round(other.r, decimalPlaces))) {
            return false;
        }
        if (Double.doubleToLongBits(MathUtils.round(this.theta, decimalPlaces)) != Double.doubleToLongBits(MathUtils.round(other.theta, decimalPlaces))) {
            return false;
        }
        if (Double.doubleToLongBits(MathUtils.round(this.fi, decimalPlaces)) != Double.doubleToLongBits(MathUtils.round(other.fi, decimalPlaces))) {
            return false;
        }
        return true;
    }


// ======================================================================================


    @Override
    public int hashCode()
    {
        int hash = 3;
        final int decimalPlaces = 6;

        hash = 23 * hash + (int) (Double.doubleToLongBits(MathUtils.round(this.r, decimalPlaces)) ^ (Double.doubleToLongBits(MathUtils.round(this.r, decimalPlaces)) >>> 32));
        hash = 23 * hash + (int) (Double.doubleToLongBits(MathUtils.round(this.theta, decimalPlaces)) ^ (Double.doubleToLongBits(MathUtils.round(this.theta, decimalPlaces)) >>> 32));
        hash = 23 * hash + (int) (Double.doubleToLongBits(MathUtils.round(this.fi, decimalPlaces)) ^ (Double.doubleToLongBits(MathUtils.round(this.fi, decimalPlaces)) >>> 32));
        return hash;
    }


// ======================================================================================


    @Override
    public String toString()
    {
        return "SphericalCoordinates{" + r + "; f=" + fi + "; t=" + theta + " [" + (this.isRad() ? "R" : "D") + "]}";
    }

// ======================================================================================


    /**
     * Vrati suradnice tohoto bodu v kartezskych suradniciach ak dany bod je vramci tejto instancie
     * kodovany pomocou sferickych suradnic meranych v stupnoch.
     *
     * @return
     */
    public Position3D toCartesianCoordinates()
    {
        return MathUtils.spherical2cartesianCoords(this);
    }

// ======================================================================================


    /**
     * Vrati instanciu, ktora charakterizuje len smer (bez vzdialenosti bodu)
     *
     * @return
     */
    public SphericalAddress getDirection()
    {
        return new SphericalAddress(this);
    }




}

