package api;

import java.util.Objects;

public class GeoLocation implements geo_location {
    private double x;
    private double y;
    private double z;

    public GeoLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(geo_location g) {
        double x3= Math.pow(g.x()-this.x,2);
        double y3= Math.pow(g.y()-this.y,2);
        double z3= Math.pow(g.z()-this.z,2);
        double ans= x3+y3+z3;



        return Math.pow(ans,0.5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0;
    }

}
