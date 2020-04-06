package cz.sm.ng.core.libs.utils.transformations;

/**
 * Geometric matrices of size N × N
 * @author Ken Perlin @ NYU, 1998 (http://mrl.nyu.edu/~perlin/java/Matrix3D.html)
 * @author Roman Stoklasa (upravy)
 */
public class MatrixN
{

    /**
     * Pole riadkov patice
     */
    private VectorN v[];


    public MatrixN(int n)
    {
        // make a new square matrix
        v = new VectorN[n];
        for (int i = 0; i < n; i++) {
            v[i] = new VectorN(n);
        }
    }


    public int size()
    {
        // return no. of rows
        return v.length;
    }


    /**
     * get one element
     *
     * @param i
     * @param j
     * @return
     */
    public double get(int i, int j)
    {
        return get(i).get(j);
    }


    /**
     * set one element
     * @param i
     * @param j
     * @param f
     */
    public void set(int i, int j, double f)
    {
        v[i].set(j, f);
    }


    /**
     * get one row
     *
     * @param i
     * @return
     */
    public VectorN get(int i)
    {
        return v[i];
    }


    /**
     * set one row
     * @param i
     * @param vec
     */
    public void set(int i, VectorN vec)
    {
        v[i].set(vec);
    }


    /**
     * copy from another matrix
     * @param mat
     */
    public void set(MatrixN mat)
    {
        for (int i = 0; i < size(); i++) {
            set(i, mat.get(i));
        }
    }


    /**
     * convert to string representation
     * @return
     */
    @Override
    public String toString()
    {
        String s = "{";
        for (int i = 0; i < size(); i++) {
            s += (i == 0 ? "" : ",") + get(i);
        }
        return s + "}";
    }


    /**
     * set to identity matrix
     */
    public void identity()
    {
        for (int j = 0; j < size(); j++) {
            for (int i = 0; i < size(); i++) {
                set(i, j, (i == j ? 1 : 0));
            }
        }
    }


    /**
     * mat × this
     * @param mat
     */
    public void preMultiply(MatrixN mat)
    {
        MatrixN tmp = new MatrixN(size());
        double f;

        for (int j = 0; j < size(); j++) {
            for (int i = 0; i < size(); i++) {
                f = 0.;
                for (int k = 0; k < size(); k++) {
                    f += mat.get(i, k) * get(k, j);
                }
                tmp.set(i, j, f);
            }
        }
        set(tmp);
    }


    /**
     * this × mat
     * @param mat
     */
    public void postMultiply(MatrixN mat)
    {
        MatrixN tmp = new MatrixN(size());
        double f;

        for (int j = 0; j < size(); j++) {
            for (int i = 0; i < size(); i++) {
                f = 0.;
                for (int k = 0; k < size(); k++) {
                    f += get(i, k) * mat.get(k, j);
                }
                tmp.set(i, j, f);
            }
        }
        set(tmp);
    }
}

