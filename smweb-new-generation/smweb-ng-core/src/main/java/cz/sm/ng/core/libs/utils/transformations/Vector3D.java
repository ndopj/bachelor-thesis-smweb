package cz.sm.ng.core.libs.utils.transformations;

/**
 * Homogeneous vectors in three dimensions
 *
 * @author Ken Perlin @ NYU, 1998 (http://mrl.nyu.edu/~perlin/java/Matrix3D.html)
 * @author Roman Stoklasa (upravy)
 */
public class Vector3D extends VectorN
{

    /**
     * create a new 3D homogeneous vector
     */
    public Vector3D()
    {
        super(4);
    }

// ======================================================================================


    /**
     * create a new 3D homogeneous vector which will be initialized with values [X,Y,Z]
     */
    public Vector3D(double x, double y, double z)
    {
        super(4);
        this.set(x, y, z);
    }


// ======================================================================================


    /**
     * set value of vector
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void set(double x, double y, double z, double w)
    {
        set(0, x);
        set(1, y);
        set(2, z);
        set(3, w);
    }


// ======================================================================================


    /**
     * set value of a 3D point
     * @param x
     * @param y
     * @param z
     */
    public void set(double x, double y, double z)
    {
        set(x, y, z, 1);
    }


// ======================================================================================



}

