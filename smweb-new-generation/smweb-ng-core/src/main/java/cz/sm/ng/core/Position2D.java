package cz.sm.ng.core;

import com.google.gson.annotations.Expose;
import cz.sm.ng.core.libs.utils.MathUtils;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embeddable;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Tato trieda ma plnit ulohu 'obycajneho record-u' - zoskupovat vektor dat pod jeden typ.
 */
@Embeddable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@MappedSuperclass
public class Position2D implements Serializable
{

    //////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;

    /**
     * X-ova suradnica - merana v metroch
     */
    @Expose
    protected double x;

    /**
     * Y-ova suradnica - merana v metroch
     */
    @Expose
    protected double y;

//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    public Position2D()
    {
        this.x = 0.0;
        this.y = 0.0;
    }

// ======================================================================================


    public Position2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    // ======================================================================================
    public double getX()
    {
        return this.x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    // ======================================================================================
    public double getY()
    {
        return this.y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

// ======================================================================================
    /**
     * Vrati euklidovsku vzdialenost od ineho 2D bodu. (meranu v metroch, ak obe pozicie splnaju specifikaciu a su
     * merane v metroch).
     *
     * Pozn.: Tato metoda je explicitne inak pomenovana, aby znizila chyby pouzitia 3D metriky, pokial sa bude chciet
     * zmerat len vzdialenost pre 2D.
     *
     * @param other 2D pozicia, od korej sa meria vzdialenost
     * @return
     */
    public double distanceFrom2D(Position2D other)
    {
        return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y));
    }

// ======================================================================================
    /**
     * Vyrata vzdialenost medzi dvomi poziciami - pocitana v 2D (len z udajov x a y). Pre 2D len proxuje distanceFrom2D
     *
     * @param other
     * @return
     */
    public double distanceFrom(Position3D other)
    {
        return this.distanceFrom2D((Position2D) other);
    }

// ======================================================================================

    /**
     * Vrati euklidovsku vzdialenost od pociatku sur. sustavy - bodu [0, 0]. (meranu v takych jednotkach, v akych su
     * merane suradnice)
     *
     * @return
     */
    public double distanceFrom()
    {
        return Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

// ======================================================================================


    /**
     * Vrati euklidovsku dlzku tohoto 2D vektora (meranu v takych jednotkach, v akych su
     * merane suradnice)
     *
     * @return
     */
    public double length()
    {
        return this.distanceFrom();
    }


// ======================================================================================


    /**
     * Vrati euklidovsku dlzku 2D PRIEMETU tohoto vektora. (meranu v takych jednotkach, v akych su
     * merane suradnice)
     *
     * @return
     */
    public double length2D()
    {
        return this.distanceFrom();
    }


//=======================================================================================
    /**
     * Vrati novu instanciu polohy, ktora vznikne scitanim polohy aktualnej instancie a instancie other.
     * Tj. vysleok operacie (this + other)
     *
     * @param other
     * @return
     */
    public Position2D plus(Position2D other)
    {
        return new Position2D(
                this.getX() + other.getX(),
                this.getY() + other.getY()
        );
    }

//==========================================================================================
    /**
     * Vrati novu instanciu polohy, ktora vznikne scitanim polohy aktualnej instancie a instancie other.
     * Tj. vysleok operacie (this - other)
     *
     * @param other
     * @return
     */
    public Position2D minus(Position2D other)
    {
        return new Position2D(
                this.getX() - other.getX(),
                this.getY() - other.getY()
        );
    }


// ======================================================================================


    @Override
    public String toString()
    {
        return "[" + this.x + ";" + this.y + "]";
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
        final Position2D other = (Position2D) obj;


		/*
		 if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
		 return false;
		 }
		 if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
		 return false;
		 }
		 */
        // -- porovnavanie cez doubleToLongBits je alternativou k porovnaniu pomocou MathUtils.doubleEquals(), ale ma problem
        //	  s generovanum hashCode() (musi sa pocitat so zaokruhlenou hodnotou).
        if (!MathUtils.doubleEquals(this.x, other.x)) {
            return false;
        }
        if (!MathUtils.doubleEquals(this.y, other.y)) {
            return false;
        }

        return true;
    }

    // ======================================================================================
    @Override
    public int hashCode()
    {
        int hash = 3;
        //hash = 71 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        //hash = 71 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));

        hash = 71 * hash + (int) (Double.doubleToLongBits(MathUtils.round(this.x, 6)) ^ (Double.doubleToLongBits(MathUtils.round(this.x, 6)) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(MathUtils.round(this.y, 6)) ^ (Double.doubleToLongBits(MathUtils.round(this.y, 6)) >>> 32));

        return hash;
    }

// ======================================================================================

}

