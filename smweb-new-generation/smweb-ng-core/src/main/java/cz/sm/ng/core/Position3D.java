package cz.sm.ng.core;

import com.google.gson.annotations.Expose;
import cz.sm.ng.core.libs.utils.MathUtils;
import cz.sm.ng.core.libs.utils.transformations.Vector3D;

import javax.persistence.Embeddable;
import java.util.Locale;

/**
 * Trida udrzujici informace o pozice v trojrozmernem prostoru
 * @author Dejvino
 */
@Embeddable
public class Position3D extends Position2D
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    private static final long serialVersionUID = 2L;

    /**
     * Vyska - merana v metroch
     */
    @Expose
    protected double alt;



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    public Position3D()
    {
        this.x = 0.0;
        this.y = 0.0;
        this.alt = 0.0;
    }


// ======================================================================================


    public Position3D(double x, double y, double alt)
    {
        this.x = x;
        this.y = y;
        this.alt = alt;
    }


// ======================================================================================


    /**
     * Konstruktor na prevod z Vector3D.
     * @param vector
     */
    public Position3D(Vector3D vector)
    {
        this(vector.get(0), vector.get(1), vector.get(2));
    }


// ======================================================================================


    public double getAlt()
    {
        return this.alt;
    }


    public void setAlt(double alt)
    {
        this.alt = alt;
    }


// ======================================================================================

    /**
     * Vrati euklidovsku dlzku tohoto 3D vektore.
     * (meranu v takych jednotkach, v akych su merane suradnice)
     *
     * @return
     */
    @Override
    public double length()
    {
        return this.distanceFrom();
    }

// ======================================================================================


    /**
     * Vrati euklidovsku vzdialenost od pociatku sur. sustavy - bodu [0, 0, 0].
     * (meranu v takych jednotkach, v akych su merane suradnice)
     *
     * @return
     */
    @Override
    public double distanceFrom()
    {
        return Math.sqrt( (this.x * this.x) + (this.y * this.y ) + (this.alt * this.alt) );
    }

// ======================================================================================

    /**
     * Vrati euklidovsku vzdialenost od ineho 3D bodu (pocitanu v 3D).
     * (meranu v metroch, ak obe pozicie splnaju specifikaciu a su merane v metroch)
     *
     * @param other
     * @return
     */
    @Override
    public double distanceFrom(Position3D other)
    {
        return Math.sqrt( (this.x - other.x)*(this.x - other.x) + (this.y - other.y)*(this.y - other.y) + (this.alt - other.alt)*(this.alt - other.alt) );
    }

// ======================================================================================

    /**
     * Vrati instanciu Position3D, ktora vyjadruje RELATIVNU polohu tejto instancie polohy vzhladom na
     * pociatok zadany objektom origin.
     *
     * @param origin
     * @return
     */
    public Position3D getRelativePositionFrom(Position3D origin)
    {
        //return new Position3D(this.getX() - origin.getX(), this.getY() - origin.getY(), this.getAlt() - origin.getAlt());
        return this.minus(origin);
    }

// ======================================================================================

    /**
     * Vrati novu instanciu polohy, ktora vznikne scitanim polohy aktualnej instancie a instancie other.
     * Tj. vysleok operacie (this + other)
     *
     * @param other
     * @return
     */
    public Position3D plus(Position3D other)
    {
        return new Position3D(
                this.getX() + other.getX(),
                this.getY() + other.getY(),
                this.getAlt() + other.getAlt()
        );
    }


// ======================================================================================

    /**
     * Vrati novu instanciu polohy, ktora vznikne odcitanim polohy instancie other od polohy aktualnej instancie.
     *	Tj. vysleok operacie (this - other)
     *
     * @param other
     * @return
     */
    public Position3D minus(Position3D other)
    {
        return new Position3D(
                this.getX() - other.getX(),
                this.getY() - other.getY(),
                this.getAlt() - other.getAlt()
        );
    }

// ======================================================================================



    @Override
    public String toString()
    {
        return String.format(Locale.US, "[%.3f;%.3f;%.3f]",  this.x, this.y, this.alt);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position3D other = (Position3D) obj;

        if ( !super.equals(obj) ) {
            return false;
        }


		/*
		// -- porovnavanie cez doubleToLongBits je vypnute a nahradene porovnanim pomocou MathUtils.doubleEquals()
		if (Double.doubleToLongBits(this.alt) != Double.doubleToLongBits(other.alt)) {
			return false;
		}
		*/

        // -- porovnavanie cez doubleToLongBits je alternativou k porovnaniu pomocou MathUtils.doubleEquals(), ale ma problem
        //	  s generovanum hashCode() (musi sa v hashCode pouzivat zaokruhlovanie).
        if (!MathUtils.doubleEquals(this.alt, other.alt)) {
            return false;
        }



        return true;
    }


// ======================================================================================

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
        //hash = 71 * hash + (int) (Double.doubleToLongBits(this.alt) ^ (Double.doubleToLongBits(this.alt) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(MathUtils.round(this.alt, 6)) ^ (Double.doubleToLongBits(MathUtils.round(this.alt, 6)) >>> 32));
        return hash;
    }

// ======================================================================================

    /**
     * Prevedie tuto instanciu na instanciu Vector3D.
     * @return
     */
    public Vector3D toVector3D()
    {
        return new Vector3D(this.getX(), this.getY(), this.getAlt());
    }

// ======================================================================================


    /**
     * Factory metoda na vyrobenie instancie Position3D z instnacie Vector3D
     * @param vector
     * @return
     */
    public static Position3D createFromVector3D(Vector3D vector)
    {
        return new Position3D(vector.get(0), vector.get(1), vector.get(2));
    }

// ======================================================================================



    /**
     * Prevedie tuto instanciu do sferickych suradnic.
     * @return
     */
    public SphericalCoordinates toSphericalCoordinatesDeg()
    {
        return MathUtils.cartesian2SphericalCoordsDeg(this);
    }

// ======================================================================================



    /**
     * Vrati smer, ktory vedie z [0, 0] do tejto pozicie.
     * Pozicia sa prevedie do Sferickych suradnic, a z nich sa pouziju uhle fi a theta aby sa vytvorila SphericalAddress.
     *
     * @return
     */
    public SphericalAddress getDirectionAsSphericalAddressDeg()
    {
        return this.toSphericalCoordinatesDeg().getDirection();
    }

// ======================================================================================


    /**
     * Vrati vektor projekcie na 2D vodorovnu rovinu (XY)
     * @return
     */
    public Position2D toGroundProjection()
    {
        return new Position2D(this.x, this.y);
    }




}
