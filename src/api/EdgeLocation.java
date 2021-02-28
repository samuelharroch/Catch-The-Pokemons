package api;

public class EdgeLocation implements edge_location {

    edge_data edge;

    public EdgeLocation(edge_data edge) {
        this.edge = edge;
    }

    @Override
    public edge_data getEdge() {
        return this.edge;
    }

    @Override
    public double getRatio() {
        return 0;
    }
}
