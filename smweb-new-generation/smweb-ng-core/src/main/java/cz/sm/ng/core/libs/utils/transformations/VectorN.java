package cz.sm.ng.core.libs.utils.transformations;

import cz.sm.ng.core.libs.utils.MathUtils;

import java.util.Arrays;

/**
 * Geometric vectors of size N
 *
 * @author Ken Perlin @ NYU, 1998 (http://mrl.nyu.edu/~perlin/java/Matrix3D.html)
 * @author Roman Stoklasa (upravy)
 */
public class VectorN
{

//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    private double v[];



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * create a new vector
     * @param n
     */
    public VectorN(int n)
    {
        v = new double[n];
    }


// ======================================================================================


    /**
     * return vector size
     * @return
     */
    public int size()
    {
        return v.length;
    }


// ======================================================================================


    /**
     * get one element
     * @param j
     * @return
     */
    public double get(int j)
    {
        return v[j];
    }


// ======================================================================================


    public void set(int j, double f)
    {
        v[j] = f;
    }  // set one element


// ======================================================================================


    /**
     * copy from another vector
     * @param vec
     */
    public void set(VectorN vec)
    {
        for (int j = 0; j < size(); j++) {
            set(j, vec.get(j));
        }
    }


// ======================================================================================


    /**
     * convert to string representation
     * @return
     */
    @Override
    public String toString()
    {
        String s = "{";
        for (int j = 0; j < size(); j++) {
            s += (j == 0 ? "" : ",") + get(j);
        }
        return s + "}";
    }


// ======================================================================================


    /**
     * Multiply this vector by an N × N matrix and return the new vector
     * @param mat
     * @return new transformed instance
     */
    public VectorN transformAndReturn(MatrixN mat)
    {
        VectorN tmp;
        try {
            tmp = this.getClass().getConstructor().newInstance();
        } catch (Exception ex) {
            tmp = new VectorN(this.v.length);
        }

        double f;

        for (int i = 0; i < size(); i++) {
            f = 0.;
            for (int j = 0; j < size(); j++) {
                f += mat.get(i, j) * get(j);
            }
            tmp.set(i, f);
        }
        return tmp;
    }

// ======================================================================================


    /**
     * Transform THIS instance by multiplying by an N × N matrix
     * Returns THIS instance (which is already modified).
     * @param mat
     * @return THIS instance
     */
    public VectorN transform(MatrixN mat)
    {
        set(this.transformAndReturn(mat));
        return this;
    }


// ======================================================================================


    /**
     * euclidean distance
     * @param vec
     * @return
     */
    public double distance(VectorN vec)
    {
        double x, y, d = 0;
        for (int i = 0; i < size(); i++) {
            x = vec.get(0) - get(0);
            y = vec.get(1) - get(1);
            d += x * x + y * y;
        }
        return Math.sqrt(d);
    }


// ======================================================================================


    /**
     * POZOR!!! Tato implementacia hashCode sa NEMUSI nieked zhodovat s equals()!
     *
     * Je to preto, ze equals porovnava s nejakou malou toleranciou, ale tato metoda nie!
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + Arrays.hashCode(this.v);
        return hash;
    }


// ======================================================================================


    /**
     * Dva vektory sa zhoduju, ak maju rovnaku dimenziu a zaroven rovnake suradnice.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VectorN other = (VectorN) obj;
        for (int i = 0; i < this.v.length; i++) {
            if ( ! MathUtils.doubleEquals(this.get(i), other.get(i), 1e-12) ) {
                return false;
            }
        }

        return true;
    }


// ======================================================================================




}

