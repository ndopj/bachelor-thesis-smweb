package cz.sm.ng.core.libs.utils.math;


import cz.sm.ng.core.Position2D;

/**
 * Objekt na reprezentaciu kruhu
 *
 * @author Roman Stoklasa
 */
public class Circle
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * polomer
     */
    private double radius;

    /**
     * Stredu
     */
    private Position2D center;




//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    public Circle(Position2D center, double radius)
    {
        this.center = center;
        this.radius = radius;
    }

// ======================================================================================



    public double getRadius()
    {
        return radius;
    }


    public void setRadius(double radius)
    {
        this.radius = radius;
    }


// ======================================================================================


    public Position2D getCenter()
    {
        return center;
    }


    public void setCenter(Position2D center)
    {
        this.center = center;
    }


// ======================================================================================




}

