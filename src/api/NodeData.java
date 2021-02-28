package api;

import java.util.HashMap;
import java.util.Objects;

public class NodeData implements node_data {
    private int key;//will represent the key of the node
   private double weight;//will represent the weight of the node
    private  geo_location g;//will represent the geo location of the node
   private int tag;//will represent the tag of the node
    private String info;//will represent the info of the node

    //constructor
    public NodeData(int key) {
        this.key = key;
        this.g=new GeoLocation(0,0,0);
    }
//copy constructor
    public NodeData(node_data other) {
        this.key=other.getKey();
        this.weight=other.getWeight();
        this.g=other.getLocation();
        this.tag=other.getTag();
        this.info=other.getInfo();


    }
//getters,setters,to string and equal methods:
    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public geo_location getLocation() {
        return this.g;
    }

    @Override
    public void setLocation(geo_location p) {
        this.g=p;

    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight=Math.abs(w);

    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info=s;

    }

    @Override
    public String toString() {
        return "NodeData{" +
                "key=" + key +
                ", tag=" + tag +
                '}';
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return key == nodeData.key ;
    }

}
