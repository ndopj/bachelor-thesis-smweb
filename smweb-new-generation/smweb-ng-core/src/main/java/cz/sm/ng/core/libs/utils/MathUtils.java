package cz.sm.ng.core.libs.utils;

import cz.sm.ng.core.Position2D;
import cz.sm.ng.core.Position3D;
import cz.sm.ng.core.SphericalCoordinates;
import cz.sm.ng.core.libs.utils.math.AngleType;
import cz.sm.ng.core.libs.utils.math.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Staticka trieda obsahujuca pomocne metody pre vypocty.
 *
 * @author Roman Stoklasa
 */
public class MathUtils
{


    /**
     * Porovna dve double cisla ci su rovnake, pricom povolena tolerancia je 1e-6.
     *
     * @param a
     * @param b
     * @param epsilon
     * @return
     */
    public static boolean doubleEquals(double a, double b)
    {
        return doubleEquals(a, b, 1e-6);
    }

// ======================================================================================

    /**
     * Porovna dve double cisla ci su rovnake (az na povolenu toleranciu epsilon)
     *
     * @param a
     * @param b
     * @param epsilon
     * @return
     */
    public static boolean doubleEquals(double a, double b, double epsilon)
    {
        return ( Math.abs(a-b) < epsilon );
    }

// ======================================================================================



    /**
     * Prevedie stupne na radiany.
     *
     * @param deg
     * @return
     */
    public static double deg2rad(double deg)
    {
        return (deg / 360.0) * 2 * Math.PI;
    }

// ======================================================================================


    /**
     * Prevedie radiany na stupne.
     *
     * @param deg
     * @return
     */
    public static double rad2deg(double rad)
    {
        return (rad / (2 * Math.PI)) * 360.0;
    }

// ======================================================================================




// ======================================================================================


    /**
     * Prevedie uhol zadany v stupnoch v GEOGRAFICKOM ponimani (0 = sever, 90 = vychod) do matematickeho ponimania (0 = vychod, 90 = sever, ...).
     *
     * @param geographicalDeg
     * @return
     */
    public static double geographicalToMathematicalAngleDeg(double geographicalDeg)
    {
        return MathUtils.normalizeAngleDeg(90 - geographicalDeg);
    }

// ======================================================================================

    /**
     * Prevedie uhol zadany v stupnoch v MATEMATICKOM ponimani (0 = vychod, 90 = sever, ...) do geografickeho ponimania (0 = sever, 90 = vychod).
     *
     * @param mathematicalDeg
     * @return
     */
    public static double mathematicalToGeographicalAngleDeg(double mathematicalDeg)
    {
        return MathUtils.normalizeAngleDeg(90 - mathematicalDeg);
    }

// ======================================================================================


    /**
     * Prevedie uhol zadany v radianoch v GEOGRAFICKOM ponimani (0 = sever, PI/2 = vychod) do matematickeho ponimania (0 = vychod, PI/2 = sever, ...).
     *
     * @param geographicalRad
     * @return
     */
    public static double geographicalToMathematicalAngleRad(double geographicalRad)
    {
        return MathUtils.normalizeAngleRad(90 - geographicalRad);
    }

// ======================================================================================

    /**
     * Prevedie uhol zadany v radianoch v MATEMATICKOM ponimani (0 = vychod, PI/2 = sever, ...) do geografickeho ponimania (0 = sever, PI/2 = vychod).
     *
     * @param mathematicalRad
     * @return
     */
    public static double mathematicalToGeographicalAngleRad(double mathematicalRad)
    {
        return MathUtils.normalizeAngleRad(90 - mathematicalRad);
    }

// ======================================================================================


    /**
     * Vypocita uhol (smernicu) vektora v 2D, ktory vedie z bodu from do bodu to.
     * Uhol je merany v geografickom zmysle a v rozsahu 0..2*PI - uhol 0 je v smere osi Y a v smere osi X je uhol PI/2.
     *
     * @param from
     * @param to
     * @return uhol v radianoch v rozmedzi 0 .. 2*PI
     */
    public static double getAzimutInRad(Position2D from, Position2D to)
    {
        double dX = to.getX() - from.getX();
        double dY = to.getY() - from.getY();

        if (dX == 0 && dY == 0) {
            return 0;
        } else {
            return MathUtils.normalizeAngleRad(Math.PI/2 - Math.atan2(dY, dX));
        }
    }


// ======================================================================================


    /**
     * Vypocita uhol (smernicu) vektora v 2D, ktory vedie z POCIATKU [0,0] do bodu to.
     * Uhol je merany v geografickom zmysle a v rozsahu 0..2PI - uhol 0 je v smere osi Y a v smere osi X je uhol PI/2.
     *
     * @param to
     * @return
     */
    public static double getAzimutInRad(Position2D to)
    {
        return getAzimutInRad(new Position2D(0.0, 0.0), to);
    }

// ======================================================================================





    /**
     * Normalizuje uhol v stupnoch do intervalu 0..360 stupnov
     *
     * @param angle
     * @return
     */
    public static double normalizeAngleDeg(double angle)
    {
        double divisor = Math.floor(angle / 360);
        double remainder = angle - (divisor * 360);
        return remainder;
    }

// ======================================================================================

    /**
     * Normalizuje uhol do intervalu -180..180 stupnov
     *
     * @param angle
     * @return
     */
    public static double normalizeAngleSymmetricallyDeg(double angle)
    {
        double result = MathUtils.normalizeAngleDeg(angle);
        if (result > 180) {
            result -= 360;
        }
        return result;
    }

// ======================================================================================

    /**
     * Normalizuje uhol v radianoch do intervalu 0..2 PI
     *
     * @param angle
     * @return
     */
    public static double normalizeAngleRad(double angle)
    {
        double divisor = Math.floor(angle / (2 * Math.PI));
        double remainder = angle - (divisor * (2 * Math.PI));
        return remainder;
    }

// ======================================================================================


    /**
     * Normalizuje uhol do intervalu -PI..PI radianov
     *
     * @param angle
     * @return
     */
    public static double normalizeAngleSymmetricallyRad(double angle)
    {
        double result = MathUtils.normalizeAngleRad(angle);
        if (result > Math.PI) {
            result -= (2 * Math.PI);
        }
        return result;
    }

// ======================================================================================


    /**
     * Overi, ci zadany heading (uhol) je medzi ohranicujucimi uhlami minBound a maxBound.
     *
     * Ak je minBound a maxBound su rovnake hodnoty, povazuje sa to za ohranicenie celeho kruhu, takze
     * vrati vzdy true.
     *
     * @param heading
     * @param minBound
     * @param maxBound
     * @return
     */
    public static boolean isHeadingBetweenBoundsDeg(double heading, double minBound, double maxBound)
    {
        double min = normalizeAngleDeg(minBound);
        double max = normalizeAngleDeg(maxBound);
        double head = normalizeAngleDeg(heading);

        if (min == max) { // <-- hranice su rovnake, takze to povazuje za ohranicenie celeho kruhu.
            return true;
        }

        if (min <= max) {
            return (head >= min && head <= max);
        } else {
            return (head >= min || head <= max);
        }
    }

// ======================================================================================


    /**
     * Prevedie 3D kartezske suradnice na sfericke, pricom sfericke suradnice budu obsahuvat uhly v radianoch.
     * @param pos
     * @return
     */
    public static SphericalCoordinates cartesian2SphericalCoordsRad(Position3D pos)
    {
        double r = Math.sqrt(pos.getX() * pos.getX() + pos.getY() * pos.getY() + pos.getAlt() * pos.getAlt());

        double fi = 0;
        if (pos.getX() != 0 || pos.getY() != 0) {
            fi = Math.atan2(pos.getY(), pos.getX());
            //if (fi < 0) {
            //	fi += 2 * Math.PI;
            //}
        }

        double theta = Math.asin(pos.getAlt() / r);

        return new SphericalCoordinates(r, fi, theta, AngleType.RAD);
    }

// ======================================================================================


    /**
     * Prevedie 3D kartezske suradnice na sfericke, pricom sfericke suradnice budu obsahuvat uhly v stupnoch.
     *
     * @param pos
     * @return
     */
    public static SphericalCoordinates cartesian2SphericalCoordsDeg(Position3D pos)
    {
        SphericalCoordinates radCoords = cartesian2SphericalCoordsRad(pos);
        return new SphericalCoordinates(radCoords.getR(),
                MathUtils.rad2deg(radCoords.getFi()),
                MathUtils.rad2deg(radCoords.getTheta()),
                AngleType.DEG);
    }

// ======================================================================================

    /**
     * Prevedie sfericke suradnice do kartezskych.
     *
     * @param sphereCoords
     * @return
     */
    public static Position3D spherical2cartesianCoords(SphericalCoordinates sphereCoords)
    {
        double fiRad = (sphereCoords.isRad() ? sphereCoords.getFi() : MathUtils.deg2rad(sphereCoords.getFi()));
        double thetaRad = (sphereCoords.isRad() ? sphereCoords.getTheta() : MathUtils.deg2rad(sphereCoords.getTheta()));
        double r = sphereCoords.getR();

        double distanceInTopDownProjection =  r * Math.cos(thetaRad);

        double x = distanceInTopDownProjection * Math.cos(fiRad);
        double y = distanceInTopDownProjection * Math.sin(fiRad);
        double z = sphereCoords.getR() * Math.sin(thetaRad);

        return new Position3D(x, y, z);
    }

// ======================================================================================

    /**
     * Zaokruhli na patricny pocet desatinnych miest.
     * Zaporna hodnota decimalPlaces znamena, ze sa bude zaokruhlovat na desiatky, stovky, tisicky atd..
     * @param num
     * @param decimalPlaces Kladna hodnota znaci pocet desatinnych miest, zaporna hodnota pocet 'desiatok' (desiatky, stovky, ..)
     * @return
     */
    public static double round(double num, int decimalPlaces)
    {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(num * factor) / factor;
    }


// ======================================================================================


    /**
     * Zaokruhli cislo na dele jednotky (bez desatinnej casti)
     *
     * @param num
     * @return
     */
    public static double round(double num)
    {
        return round(num, 0);
    }


// ======================================================================================


    /**
     * Vrati nahodne cislo v rozsahu [min..max]
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInt(int min, int max)
    {
        return (int)round(min + (int)(Math.random() * ((max - min) + 1)));
    }


// ======================================================================================


    /**
     * Prevedie rychlost v km/h na rychlost v m/s.
     *
     * @param speedKMH
     * @return Prevedenu hodnotu rychlosti v m/s.
     */
    public static double speedKmh2ms(double speedKMH)
    {
        return speedKMH / 3.6;
    }


// ======================================================================================


    /**
     * Prevedie rychlost v m/s na rychlost v km/h.
     *
     * @param speedKMH
     * @return Prevedenu hodnotu rychlosti v km/h.
     */
    public static double speedMs2kmh(double speedMs)
    {
        return speedMs * 3.6;
    }


// ======================================================================================


    /**
     * Vypocita dlzku strany trojuholnika podla kosinusovej vety zo zadanych dlzok stran a, b, a uhlu gamma ktory zvieraju.
     * @param sideA
     * @param sideB
     * @param gammaRad
     * @return dlzku strany C.
     */
    public static double cosineLawForSideLength(double sideA, double sideB, double gammaRad)
    {
        // kosinusova veta:
        double cSquare = sideA*sideA + sideB*sideB - 2 * sideA * sideB * Math.cos(gammaRad);
        return Math.sqrt(cSquare);
    }

// ======================================================================================

    /**
     * Vypocita velkost uhlu alpha v trojuholniku podla kosinusovej vety zo zadanych dlzok stran a, b, c.
     * Uhol alpha je oproti strane sideA!
     *
     * @param sideA
     * @param sideB
     * @param sideC
     * @return Hodnota uhlu v radianoch.
     */
    public static double cosineLawForAngleAlpha(double sideA, double sideB, double sideC)
    {
        // kosinusova veta:
        double cosAlpha = (- sideA*sideA + sideB*sideB + sideC*sideC) / (2 * sideB * sideC);
        return Math.acos(cosAlpha);
    }

// ======================================================================================


    /**
     * Metoda na vypocet determinantu 2D matice.
     *
     * @author http://wiki.answers.com/Q/Determinant_of_matrix_in_java
     *
     * @param mat	2D matica
     * @return
     */
    public static double determinant(double[][] mat)
    {
        double result = 0;

        if (mat.length == 1) {
            result = mat[0][0];
            return result;
        }

        if (mat.length == 2) {
            result = mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
            return result;
        }

        for (int i = 0; i < mat[0].length; i++) {
            double temp[][] = new double[mat.length - 1][mat[0].length - 1];

            for (int j = 1; j < mat.length; j++) {
                System.arraycopy(mat[j], 0, temp[j - 1], 0, i);
                System.arraycopy(mat[j], i + 1, temp[j - 1], i, mat[0].length - i - 1);
            }

            result += mat[0][i] * Math.pow(-1, i) * determinant(temp);
        }

        return result;

    }


// ======================================================================================




    /**
     * Vypocita kruznicu, ktora prechadza zadanymi 3mi bodmi.
     *
     * Ak su vsetky body na jednej priamke, tak vyhadzuje IllegalStateException
     *
     * @author http://stackoverflow.com/a/4103418
     *
     * @param p1
     * @param p2
     * @param p3
     * @return
     * @throws IllegalStateException ak zadane body lezia na jednej priamke.
     */
    public static Circle circleFromPoints(final Position2D p1, final Position2D p2, final Position2D p3)
    {
        final double TOL = 0.0000001;

        final double offset = Math.pow(p2.getX(), 2) + Math.pow(p2.getY(), 2);
        final double bc = (Math.pow(p1.getX(), 2) + Math.pow(p1.getY(), 2) - offset) / 2.0;
        final double cd = (offset - Math.pow(p3.getX(), 2) - Math.pow(p3.getY(), 2)) / 2.0;
        final double det = (p1.getX() - p2.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p2.getY());

        if (Math.abs(det) < TOL) {
            throw new IllegalArgumentException("Points p1, p2 and p3 lies in one line.");
        }

        final double idet = 1 / det;

        final double centerx = (bc * (p2.getY() - p3.getY()) - cd * (p1.getY() - p2.getY())) * idet;
        final double centery = (cd * (p1.getX() - p2.getX()) - bc * (p2.getX() - p3.getX())) * idet;
        final double radius = Math.sqrt(Math.pow(p2.getX() - centerx, 2) + Math.pow(p2.getY() - centery, 2));

        return new Circle(new Position2D(centerx, centery), radius);
    }

    /**
     * prevede int na cislo ctyrkove soustavi a vrati jako string
     * @param number cislo ktere cheme prevest
     * @return retezec, reprezentujici cislo ve ctyrkove soustave (pomoci cislic)
     */
    public static String intAsQuad(int number)
    {
        if (number == 0) {
            return "0";
        }
        ArrayList<Integer> quad = new ArrayList<Integer>();
        while(number > 0) {
            quad.add(number%4);
            number = number/4;
        }
        String result = "";
        for (int i = quad.size() - 1; i >= 0; i--) {
            result += quad.get(i);
        }
        return result;
    }

    /**
     * Vypocita teziste nekolika 3D souradnic
     * @param seznam souradnic
     * @return teziste zadanych souradnic, vraci null pro 0 zadanych souradnic
     */
    public static Position3D getCenterOfGravity(List<Position3D> list)
    {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Position3D result = new Position3D();
        int i = 0;
        for (Position3D position : list) {
            if (position != null) {
                result.setX(result.getX() + position.getX());
                result.setY(result.getY() + position.getY());
                result.setAlt(result.getAlt() + position.getAlt());
                i++;
            }
        }
        result.setX(result.getX()/i);
        result.setY(result.getY()/i);
        result.setAlt(result.getAlt()/i);

        return result;
    }



    public static double generateGaussianRandomValue(double mean, double stdev)
    {
        Random random = ThreadLocalRandom.current();

        return random.nextGaussian() * stdev + mean;
    }



    /**
     * Pre zadane cislo value zadane ako 'mili-jednotka' vrati ciselne vyjadrenie v hlavnej jednotke.
     *
     * Napr.:  18 mm --> 0.018 m
     *
     * @param value
     * @return
     */
    public static double miliValue(double value)
    {
        return value * 1e-3;
    }


    /**
     * Pre zadane cislo value zadane ako 'mikro-jednotka' vrati ciselne vyjadrenie v hlavnej jednotke.
     *
     * Napr.:  2.7 us --> 0.0000000027 s
     *
     * @param value
     * @return
     */
    public static double microValue(double value)
    {
        return value * 1e-6;
    }


    /**
     * Pre zadane cislo value zadane ako 'nano-jednotka' vrati ciselne vyjadrenie v hlavnej jednotke.
     *
     * Napr.:  13 ns --> 0.000000013 s
     *
     * @param value
     * @return
     */
    public static double nanoValue(double value)
    {
        return value * 1e-9;
    }




}

